package com.folumo.mekanism_lasers.common.block_entity;

import com.folumo.mekanism_lasers.Config;
import com.folumo.mekanism_lasers.common.multiblock.OreGeneratorBase;
import com.folumo.mekanism_lasers.common.registry.BlockRegistry;
import com.folumo.mekanism_lasers.common.registry.DamageTypeReg;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IConfigurable;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalTank;
import mekanism.common.registries.MekanismChemicals;
import mekanism.common.tile.TileEntityChemicalTank;
import mekanism.common.tile.TileEntityEnergyCube;
import mekanism.common.tile.TileEntityFluidTank;
import mekanism.common.tile.TileEntityPersonalBarrel;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ODECM_BlockEntity extends TileEntityMekanism implements IConfigurable {

    public ODECM_BlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.ODECM, pos, state);
        maxProgress = Config.ODECM_Progress.getAsInt();
    }

    protected OreGeneratorBase typeV = OreGeneratorBase.GENERATOR_V1;
    protected Block item;

    private int structureCooldown = 0;
    private OreGeneratorBase.BuildCheckResult cachedStructureValid = null;

    private int progress = 0;
    private final int maxProgress;

    private BlockPos chestOut = null, powerIn = null, fluidIn = null, gasOut = null;

    private boolean doExplode = false;

    // Implosion effect (replaces the old explosion): pull surrounding blocks toward the core,
    // then blast them outward, destroying some in the process.
    private static final int IMPLODE_SUCK_TICKS = 16;   // ticks spent pulling blocks inward
    private static final int IMPLODE_MAX_BLOCKS = 200;  // cap on blocks turned into projectiles
    private static final int IMPLODE_COOLDOWN = 200;    // ticks before it can trigger again
    private static final float IMPLODE_DESTROY_CHANCE = 0.35F; // fraction destroyed on launch

    private int implodeTimer = 0;
    private int implodeCooldown = 0;
    private final List<Integer> flyingBlockIds = new ArrayList<>();

    public void nextVe(){
        int id = typeV.getIndex();
        id++;

        if (OreGeneratorBase.MAPPED.containsKey(id)){
            typeV = OreGeneratorBase.MAPPED.get(id);
        } else {
            typeV = OreGeneratorBase.MAPPED.get(0);
        }
    }

    public OreGeneratorBase getVType(){
        if (typeV == null){
            return OreGeneratorBase.GENERATOR_V1;
        }

        return typeV;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(nbt, provider);
        nbt.putInt("targeted_generator_id", typeV.getIndex());

        if (item != null) {
            nbt.putString("targeted_mining_block",
                    BuiltInRegistries.BLOCK.getKey(item).toString());
        }

        nbt.putInt("targeted_progress", progress);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(nbt, provider);

        int id = nbt.getInt("targeted_generator_id");
        OreGeneratorBase loaded = OreGeneratorBase.MAPPED.get(id);

        if (loaded == null) {
            loaded = OreGeneratorBase.GENERATOR_V1;
        }

        this.typeV = loaded;

        if (nbt.contains("targeted_mining_block")) {
            Block loadedBlock = BuiltInRegistries.BLOCK.get(
                    ResourceLocation.parse(nbt.getString("targeted_mining_block"))
            );
            this.item = (loadedBlock == Blocks.AIR) ? null : loadedBlock;
        }

        this.progress = nbt.getInt("targeted_progress");
    }

    public int getProgress() {
        return progress;
    }

    public void setItem(Block item) {
        this.item = item;
    }

    public BlockState getItem() {
        return item == null ? null : item.defaultBlockState();
    }

    protected boolean hasItem() {
        return item != null;
    }

    private long getEnergyCost(Block block) {
        // will change it to varie
        return Config.ODECM_TickCost.get();
    }

    protected boolean isStillBuilt() {
        if (typeV == null) {
            typeV = OreGeneratorBase.GENERATOR_V1;
            return false;
        }

        if (structureCooldown-- <= 0 || cachedStructureValid == null) {
            cachedStructureValid = typeV.canBuild(getBlockPos(), getLevel());
            structureCooldown = 20;

            if (cachedStructureValid.success()) {
                this.chestOut = cachedStructureValid.outLocation();
                this.powerIn = cachedStructureValid.inPower();

                this.fluidIn = cachedStructureValid.inFluid();
                this.gasOut = cachedStructureValid.outGas();
            } else {
                this.chestOut = null;
                this.powerIn = null;

                this.fluidIn = null;
                this.gasOut = null;
            }
        }

        return cachedStructureValid.success();
    }

    protected boolean simulateAndReduce(TileEntityEnergyCube energy, long costPerTick) {
        if (energy.getEnergyContainer().extract(costPerTick, Action.SIMULATE, AutomationType.INTERNAL) >= costPerTick) {
            energy.getEnergyContainer().extract(costPerTick, Action.EXECUTE, AutomationType.INTERNAL);

            return true;
        }
        return false;
    }

    protected boolean simulateAndReduce(EnergyTransformerBlockEntity energy, long costPerTick) {
        if (energy.getEnergyContainer().extract(costPerTick, Action.SIMULATE, AutomationType.INTERNAL) >= costPerTick) {
            energy.getEnergyContainer().extract(costPerTick, Action.EXECUTE, AutomationType.INTERNAL);

            return true;
        }
        return false;
    }

    protected boolean tickPower(OreGeneratorBase.GeneratorAbility ability, long costPerTick){
        boolean canGo = false;

        if (ability.needsPower){
            TileEntityMekanism be = WorldUtils.getTileEntity(TileEntityMekanism.class, level, powerIn);

            switch (be) {
                case null -> {
                    return false;
                }
                case TileEntityEnergyCube energy -> canGo = simulateAndReduce(energy, costPerTick);
                case EnergyTransformerBlockEntity energy -> canGo = simulateAndReduce(energy, costPerTick);
                default -> {
                }
            }

        } else {
            canGo = true;
        }

        return canGo;
    }

    protected boolean tickFluid(OreGeneratorBase.GeneratorAbility ability, int costPerTick, boolean energyResult){
        boolean canGo = false;

        if (ability.needsWater){
            TileEntityFluidTank tank = WorldUtils.getTileEntity(TileEntityFluidTank.class, level, fluidIn);
            if (tank == null){return false;}

            if (tank.fluidTank.extract(costPerTick, Action.SIMULATE, AutomationType.INTERNAL).getAmount() >= costPerTick){
                tank.fluidTank.extract(costPerTick, Action.EXECUTE, AutomationType.INTERNAL);
                canGo = true;
            } else {
                if (energyResult){
                    doExplode = true;
                }
            }

        } else {
            canGo = true;
        }

        return canGo;
    }

    protected boolean tickGas(OreGeneratorBase.GeneratorAbility ability, int costPerTick, boolean energyResult){
        boolean canGo = false;

        if (ability.outputsGas){
            TileEntityChemicalTank tank = WorldUtils.getTileEntity(TileEntityChemicalTank.class, level, gasOut);

            if (tank == null){return false;}

           IChemicalTank chemicalTank = tank.getChemicalTank();
           ChemicalStack stack = MekanismChemicals.STEAM.asStack(costPerTick);

           if (chemicalTank.insert(stack, Action.SIMULATE, AutomationType.INTERNAL).getAmount() <= 0){
               chemicalTank.insert(stack, Action.EXECUTE, AutomationType.INTERNAL);
               canGo = true;

           } else {
               if (energyResult){
                   doExplode = true;
               }
           }

        } else {
            canGo = true;
        }

        return canGo;
    }

    /**
     * Kicks off the implosion: rips loose blocks around the core out of the world and turns them
     * into projectiles that get sucked toward the core (see {@link #tickImplosion()}), which then
     * launches them back outward.
     */
    private void startImplosion() {
        Level world = getLevel();
        if (!(world instanceof ServerLevel level)) {
            return;
        }

        BlockPos core = getBlockPos();
        int radius = Math.max(1, (int) (long) Config.ODECM_Explosion.get());
        double radiusSq = (double) radius * radius;
        RandomSource rand = level.getRandom();

        Set<String> blacklist = new HashSet<>(Config.ODECM_ImplodeBlacklist.get());
        Set<String> whitelist = new HashSet<>(Config.ODECM_ImplodeWhitelist.get());

        List<BlockPos> candidates = new ArrayList<>();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (dx * dx + dy * dy + dz * dz > radiusSq) {
                        continue;
                    }
                    BlockPos p = core.offset(dx, dy, dz);
                    if (p.equals(core)) {
                        continue;
                    }
                    BlockState st = level.getBlockState(p);
                    // Skip air, fluids and unbreakable blocks (bedrock, ...).
                    if (st.isAir() || !st.getFluidState().isEmpty() || st.getDestroySpeed(level, p) < 0) {
                        continue;
                    }
                    String id = BuiltInRegistries.BLOCK.getKey(st.getBlock()).toString();
                    // Blacklist always wins.
                    if (blacklist.contains(id)) {
                        continue;
                    }
                    // Block entities (machines, our own barrel, ...) are excluded unless the block
                    // is explicitly whitelisted (e.g. structural glass).
                    if (level.getBlockEntity(p) != null && !whitelist.contains(id)) {
                        continue;
                    }
                    candidates.add(p);
                }
            }
        }

        Collections.shuffle(candidates, new java.util.Random(rand.nextLong()));
        int count = Math.min(candidates.size(), IMPLODE_MAX_BLOCKS);

        flyingBlockIds.clear();
        Vec3 center = Vec3.atCenterOf(core);
        for (int i = 0; i < count; i++) {
            BlockPos p = candidates.get(i);
            BlockState st = level.getBlockState(p);
            // FallingBlockEntity.fall removes the source block and spawns the flying entity.
            FallingBlockEntity fb = FallingBlockEntity.fall(level, p, st);
            fb.setHurtsEntities(2.0F, 20);
            fb.time = 1; // don't let it self-remove on its first tick
            Vec3 toCenter = center.subtract(fb.position());
            if (toCenter.lengthSqr() > 1.0E-4) {
                fb.setDeltaMovement(toCenter.normalize().scale(0.4));
                fb.hasImpulse = true;
            }
            flyingBlockIds.add(fb.getId());
        }

        if (!flyingBlockIds.isEmpty()) {
            level.playSound(null, core, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 3.0F, 0.5F);
            implodeTimer = IMPLODE_SUCK_TICKS;
        }
    }

    /**
     * Drives the implosion each tick: pulls the flying blocks toward the core, then on the final
     * tick launches the survivors outward and destroys the rest.
     */
    private void tickImplosion() {
        Level world = getLevel();
        if (!(world instanceof ServerLevel level)) {
            endImplosion();
            return;
        }

        Vec3 center = Vec3.atCenterOf(getBlockPos());
        RandomSource rand = level.getRandom();
        implodeTimer--;
        boolean launch = implodeTimer <= 0;

        Iterator<Integer> it = flyingBlockIds.iterator();
        while (it.hasNext()) {
            Entity entity = level.getEntity(it.next());
            if (!(entity instanceof FallingBlockEntity fb) || fb.isRemoved()) {
                it.remove();
                continue;
            }

            if (!launch) {
                // Suck phase: keep steering the block toward the core.
                Vec3 toCenter = center.subtract(fb.position());
                double dist = toCenter.length();
                if (dist > 0.1) {
                    double speed = Math.min(0.9, 0.25 + dist * 0.08);
                    fb.setDeltaMovement(toCenter.normalize().scale(speed));
                    fb.hasImpulse = true;
                }
            } else if (rand.nextFloat() < IMPLODE_DESTROY_CHANCE) {
                // Launch phase: some blocks are destroyed outright.
                level.levelEvent(2001, fb.blockPosition(), Block.getId(fb.getBlockState()));
                fb.discard();
            } else {
                // The rest are flung back out from the core.
                Vec3 outward = fb.position().subtract(center);
                if (outward.lengthSqr() < 1.0E-4) {
                    outward = new Vec3(rand.nextDouble() - 0.5, rand.nextDouble(), rand.nextDouble() - 0.5);
                }
                Vec3 velocity = outward.normalize().scale(1.0 + rand.nextDouble()).add(0.0, 0.6, 0.0);
                fb.setDeltaMovement(velocity);
                fb.hasImpulse = true;
            }
        }

        if (launch) {
            endImplosion();
        }
    }

    private void endImplosion() {
        flyingBlockIds.clear();
        implodeTimer = 0;
        implodeCooldown = IMPLODE_COOLDOWN;
    }

    /**
     * Speed/cost multiplier for the current generator version. Guards against the config
     * list being shorter than the number of generator versions (which would otherwise throw
     * IndexOutOfBounds) and against non-positive values (which would make generation instant).
     */
    private float getGeneratorMultiplier() {
        java.util.List<? extends Integer> list = Config.ODECM_Generator.get();
        int idx = typeV.getIndex();
        if (idx < 0 || idx >= list.size()) return 1f;
        Integer value = list.get(idx);
        return (value == null || value <= 0) ? 1f : value.floatValue();
    }

    protected void tickGenerated() {
        if (item == null) return;
        OreGeneratorBase.GeneratorAbility ability = typeV.getAbility();

        float amount = getGeneratorMultiplier();
        long costPerTick = (long) (getEnergyCost(item) * amount);

        boolean doPower = tickPower(ability, costPerTick);
        long divisor = Config.ODECM_LiquidGasIO.get();
        int fluidCost = divisor > 0 ? (int) (costPerTick / divisor) : 0;
        int gasCost = divisor > 0 ? (int) (costPerTick / divisor) : 0;
        boolean doFluid = tickFluid(ability, fluidCost, doPower);
        boolean doOutputGas = tickGas(ability, gasCost, doPower);

        if (Config.ODECM_CanExplode.get() && doExplode && implodeTimer <= 0 && implodeCooldown <= 0) {
            startImplosion();
        }
        doExplode = false;

        if (doPower && doFluid && doOutputGas) {

            progress += Config.ODECM_ProgressPerTick.getAsInt();

            float threshold = Math.max(1f, maxProgress / amount);
            if (progress >= threshold) {
                if (ability.outputsItems){
                    generateOutput();
                }
                progress = 0;
            }
        }
    }

    private void generateOutput() {
        if (chestOut == null || item == null) return;

        TileEntityPersonalBarrel barrel = WorldUtils.getTileEntity(TileEntityPersonalBarrel.class, level, chestOut);

        if (barrel == null) return;

        var inventory = barrel.getInventorySlots(Direction.UP);

        ItemStack stack = new ItemStack(item.asItem(), Config.ODECM_OutputAmount.getAsInt());

        int remaining = stack.getCount();
        for (mekanism.api.inventory.IInventorySlot slot : inventory) {
            if (remaining <= 0) return;

            if (slot.isEmpty()) {
                ItemStack toInsert = stack.copy();
                toInsert.setCount(Math.min(remaining, slot.getLimit(toInsert)));
                slot.setStack(toInsert);
                remaining -= toInsert.getCount();
            } else if (ItemStack.isSameItemSameComponents(slot.getStack(), stack)) {
                int room = slot.getLimit(slot.getStack()) - slot.getStack().getCount();
                if (room > 0) {
                    int added = Math.min(room, remaining);
                    slot.growStack(added, mekanism.api.Action.EXECUTE);
                    remaining -= added;
                }
            }
        }
        // Anything left over (barrel full) is discarded.
    }

    @Override
    protected boolean onUpdateServer() {
        int oldProgress = progress;

        if (implodeCooldown > 0) {
            implodeCooldown--;
        }

        if (implodeTimer > 0) {
            // An implosion is in progress; drive it and pause normal generation.
            tickImplosion();
        } else if (isStillBuilt() && hasItem()) {
            tickGenerated();
        } else {
            progress = 0;
        }

        return oldProgress != progress || implodeTimer > 0;
    }

    @Override
    public InteractionResult onSneakRightClick(Player player) {
        this.nextVe();

        String type = this.getVType().getName();
        player.sendSystemMessage(Component.literal("ODECM now uses " + type));

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult onRightClick(Player player) {
        if (player.isCreative()){
            OreGeneratorBase type = this.getVType();

            player.sendSystemMessage(Component.literal("Now building " + type.getName()));
            type.forceBuild(getBlockPos(), (ServerLevel) getLevel(), player);
            setChanged();

        }

        return InteractionResult.PASS;
    }
}