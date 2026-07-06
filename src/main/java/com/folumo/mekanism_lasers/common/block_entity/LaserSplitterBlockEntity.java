package com.folumo.mekanism_lasers.common.block_entity;

import com.folumo.mekanism_lasers.common.registry.BlockRegistry;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IConfigurable;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.lasers.ILaserReceptor;
import mekanism.api.math.MathUtils;
import mekanism.common.base.MekFakePlayer;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.energy.LaserEnergyContainer;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.item.gear.ItemAtomicDisassembler;
import mekanism.common.lib.math.Pos3D;
import mekanism.common.particle.LaserParticleData;
import mekanism.common.registries.MekanismDamageTypes;
import mekanism.common.tile.laser.TileEntityLaserReceptor;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A laser receptor that re-emits the energy it receives as several beams pointing out of the
 * sides perpendicular to its facing axis. The number of output beams is configurable in-world
 * with a Mekanism Configurator (sneak + right-click cycles the mode).
 */
public class LaserSplitterBlockEntity extends TileEntityLaserReceptor implements IConfigurable {

    /** Number of perpendicular output beams. 1 = corner, up to 4 = all perpendicular sides. */
    private static final int MODE_COUNT = 4;
    private int mode = 1; // default: 2-way split ("Splits laser in two")

    // Per-direction digging state (mirrors the single-beam state on TileEntityBasicLaser).
    private final Map<Direction, BlockPos> digging = new EnumMap<>(Direction.class);
    private final Map<Direction, Long> diggingProgress = new EnumMap<>(Direction.class);

    public LaserSplitterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.LASER_SPLITTER, pos, state);
    }

    protected void addInitialEnergyContainers(EnergyContainerHelper builder, IContentsListener listener) {
        builder.addContainer(this.energyContainer = LaserEnergyContainer.create(BasicEnergyContainer.notExternal, BasicEnergyContainer.internalOnly, this, listener), RelativeSide.BACK);
    }

    @Override
    public @NotNull Component getName() {
        return Component.literal("Laser Splitter");
    }

    public static long getMaxEnergy() {
        return 10_000_000_000L;
    }

    /**
     * Prevent the base single-beam firing logic from running; we do our own multi-beam firing
     * in {@link #onUpdateServer()}.
     */
    @Override
    protected long toFire() {
        return 0L;
    }

    @Override
    protected boolean onUpdateServer() {
        boolean sendUpdatePacket = super.onUpdateServer();

        long stored = energyContainer.getEnergy();
        List<Direction> outputs = outputDirections();
        if (stored > 0L && !outputs.isEmpty()) {
            long perBeam = stored / outputs.size();
            if (perBeam > 0L) {
                for (Direction direction : outputs) {
                    fireBeam(direction, perBeam);
                }
                energyContainer.extract(perBeam * outputs.size(), Action.EXECUTE, AutomationType.INTERNAL);
            }
        }
        return sendUpdatePacket;
    }

    /**
     * The output directions for the current mode: always perpendicular to the facing axis so a
     * beam never fires straight back at the source feeding it.
     */
    private List<Direction> outputDirections() {
        Direction facing = getDirection();
        Direction.Axis axis = facing.getAxis();
        List<Direction> perpendicular = new ArrayList<>(4);
        for (Direction d : Direction.values()) {
            if (d.getAxis() != axis) {
                perpendicular.add(d);
            }
        }
        // perpendicular has exactly 4 entries; pick a "horizontal-ish" pair first so a
        // horizontally-placed splitter splits sideways.
        Direction a = perpendicular.get(2);
        Direction b = a.getOpposite();
        Direction c = perpendicular.get(0);
        Direction d = c.getOpposite();
        return switch (mode) {
            case 0 -> List.of(a);             // corner (single 90-degree turn)
            case 1 -> List.of(a, b);          // 2-way
            case 2 -> List.of(a, b, c);       // 3-way
            default -> List.of(a, b, c, d);   // 4-way
        };
    }

    private void fireBeam(Direction direction, long energy) {
        Level level = getWorldNN();
        Pos3D from = Pos3D.create(this).centre().translate(direction, 0.501);
        Pos3D to = from.translate(direction, MekanismConfig.general.laserRange.get() - 0.002);
        BlockHitResult result = level.clip(new ClipContext(from, to, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, CollisionContext.empty()));
        if (result.getType() != HitResult.Type.MISS) {
            to = new Pos3D(result.getLocation());
        }

        float energyScale = getEnergyScale(energy);
        long remainingEnergy = energy;

        // Damage entities that are in the path of the beam.
        List<Entity> hitEntities = level.getEntitiesOfClass(Entity.class, getLaserBox(direction, from, to, energyScale));
        if (!hitEntities.isEmpty()) {
            Pos3D finalFrom = from;
            hitEntities.sort(Comparator.comparingDouble(entity -> entity.distanceToSqr(finalFrom)));
            long energyPerDamage = MekanismConfig.general.laserEnergyPerDamage.get();
            for (Entity entity : hitEntities) {
                if (remainingEnergy <= 0L) {
                    break;
                }
                if (entity.isInvulnerableTo(MekanismDamageTypes.LASER.source(level))) {
                    remainingEnergy = 0L;
                    to = from.adjustPosition(direction, entity);
                    break;
                }
                float damage = (float) ((double) remainingEnergy / energyPerDamage);
                if (damage <= 0) {
                    break;
                }
                float health = entity instanceof LivingEntity livingEntity ? livingEntity.getHealth() : 0;
                if (!entity.fireImmune()) {
                    entity.igniteForTicks(MathUtils.clampToInt(damage));
                }
                if (entity.hurt(MekanismDamageTypes.LASER.source(level), damage)) {
                    if (entity instanceof LivingEntity livingEntity) {
                        damage = Math.min(damage, Math.max(0, health - livingEntity.getHealth()));
                    }
                    remainingEnergy -= MathUtils.clampToLong(energyPerDamage * damage);
                    if (remainingEnergy <= 0L) {
                        remainingEnergy = 0L;
                        to = from.adjustPosition(direction, entity);
                        break;
                    }
                }
            }
        }

        // Tell clients to render the beam between from and to.
        sendLaserDataToPlayers(new LaserParticleData(direction, to.distance(from), energyScale), from);

        if (remainingEnergy <= 0L || result.getType() == HitResult.Type.MISS) {
            resetDigging(direction);
            return;
        }

        BlockPos hitPos = result.getBlockPos();
        ILaserReceptor laserReceptor = WorldUtils.getCapability(level, Capabilities.LASER_RECEPTOR, hitPos, result.getDirection());
        if (laserReceptor != null && !laserReceptor.canLasersDig()) {
            // Hand the energy over to the receptor (another splitter, energy transformer, machine, ...).
            laserReceptor.receiveLaserEnergy(remainingEnergy);
            resetDigging(direction);
        } else if (level instanceof ServerLevel serverLevel) {
            digBlock(serverLevel, direction, hitPos, result.getDirection(), to, remainingEnergy);
        }
    }

    private void digBlock(ServerLevel level, Direction beam, BlockPos hitPos, Direction hitSide, Pos3D to, long energy) {
        if (!hitPos.equals(digging.get(beam))) {
            digging.put(beam, hitPos);
            diggingProgress.put(beam, 0L);
        }
        BlockState hitState = level.getBlockState(hitPos);
        float hardness = hitState.getDestroySpeed(level, hitPos);
        if (hardness < 0) {
            return;
        }
        long progress = diggingProgress.getOrDefault(beam, 0L) + energy;
        if (progress >= hardness * MekanismConfig.general.laserEnergyPerHardness.get()) {
            if (MekanismConfig.general.aestheticWorldDamage.get()) {
                MekFakePlayer dummy = MekFakePlayer.setupFakePlayer(level, to.x(), to.y(), to.z());
                UUID owner = getOwnerUUID();
                if (owner != null) {
                    dummy.setEmulatingUUID(owner);
                }
                BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, hitPos, hitState, dummy);
                if (!NeoForge.EVENT_BUS.post(event).isCanceled()) {
                    handleBreakBlock(hitState, level, hitPos, dummy, ItemAtomicDisassembler.fullyChargedStack());
                }
                dummy.cleanupFakePlayer(level);
            }
            diggingProgress.put(beam, 0L);
        } else {
            diggingProgress.put(beam, progress);
        }
    }

    private void resetDigging(Direction beam) {
        digging.remove(beam);
        diggingProgress.remove(beam);
    }

    private float getEnergyScale(long energy) {
        return (float) Math.min(((double) energy / MekanismConfig.usage.laser.get()) / 10D, 0.6D);
    }

    private AABB getLaserBox(Direction direction, Vec3 from, Vec3 to, float energyScale) {
        AABB aabb = new AABB(from, to);
        double halfDiameter = energyScale / 2;
        return switch (direction) {
            case DOWN, UP -> aabb.inflate(halfDiameter, 0, halfDiameter);
            case NORTH, SOUTH -> aabb.inflate(halfDiameter, halfDiameter, 0);
            case WEST, EAST -> aabb.inflate(0, halfDiameter, halfDiameter);
        };
    }

    private void sendLaserDataToPlayers(LaserParticleData data, Vec3 from) {
        if (!isRemote() && level instanceof ServerLevel serverWorld) {
            for (ServerPlayer player : serverWorld.players()) {
                serverWorld.sendParticles(player, data, true, from.x, from.y, from.z, 1, 0, 0, 0, 0);
            }
        }
    }

    // --- Configurator support ---------------------------------------------------------------

    private String modeName() {
        return switch (mode) {
            case 0 -> "corner (1 beam)";
            case 1 -> "2-way split";
            case 2 -> "3-way split";
            default -> "4-way split";
        };
    }

    @Override
    public InteractionResult onSneakRightClick(Player player) {
        mode = (mode + 1) % MODE_COUNT;
        setChanged();
        player.sendSystemMessage(Component.literal("Laser Splitter mode: " + modeName()));
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult onRightClick(Player player) {
        player.sendSystemMessage(Component.literal("Laser Splitter mode: " + modeName()));
        return InteractionResult.PASS;
    }

    // --- Persistence ------------------------------------------------------------------------

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(nbt, provider);
        nbt.putInt("splitter_mode", mode);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(nbt, provider);
        if (nbt.contains("splitter_mode")) {
            mode = Math.floorMod(nbt.getInt("splitter_mode"), MODE_COUNT);
        }
    }
}
