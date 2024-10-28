package com.folumo.mekanism_lasers.common.block_entity;

import com.folumo.mekanism_lasers.Config;
import com.folumo.mekanism_lasers.Mekanism_lasers;
import com.folumo.mekanism_lasers.common.registry.BlockRegistry;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.energy.LaserEnergyContainer;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.tile.laser.TileEntityLaserReceptor;
import mekanism.common.util.InventoryUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class OreGeneratorBlockEntity extends TileEntityLaserReceptor {
    private int coolDown = 0;
    public static long energyCap = 64_000_000L;
    public static long usage = Config.oreGeneratorFabricationCost.getAsLong();
    public static int cooldown = Config.oreGeneratorCooldown.getAsInt();
    public static int numberOfOres = Config.oreGeneratorNumberOfOres.getAsInt();
    private LaserEnergyContainer energyContainer;


    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        for (int slotX = 0; slotX < 5; slotX++) {
            for (int slotY = 0; slotY < 3; slotY++) {
                OutputInventorySlot slot = OutputInventorySlot.at(listener, 8 + slotX * 18, 16 + slotY * 18);
                builder.addSlot(slot);
                slot.setSlotType(ContainerSlotType.NORMAL);
            }
        }
        //builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 143, 35), RelativeSide.BACK);
        return builder.build();
    }

    public OreGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.ORE_GENERATOR, pos, state);

    }


    public static long getUsage() {
        return usage;
    }
    public static long getEnergyCap() {
        return energyCap;
    }

    @Override
    public void receiveLaserEnergy(long energy) {
        energyContainer.insert(energy, Action.EXECUTE, AutomationType.INTERNAL);
    }


    @Override
    public @NotNull Component getName() {
        return Component.literal("Ore Generator");
    }


    @Override
    protected void addInitialEnergyContainers(EnergyContainerHelper builder, IContentsListener listener) {
        builder.addContainer(energyContainer = LaserEnergyContainer.create(BasicEnergyContainer.notExternal, BasicEnergyContainer.internalOnly, this, listener));
    }

    public LaserEnergyContainer getEnergyContainer() {
        return energyContainer;
    }

    @Override
    protected boolean onUpdateServer() {
        if(coolDown == 0) {
            coolDown = 20 * cooldown;

            if(energyContainer.getEnergy() >= getUsage()) {
                generateOres();
            }
        } else {
            coolDown--;
        }
        return false;
    }

    private void generateOres() {
        List<IInventorySlot> inventorySlots = getInventorySlots(null);
        long energyPerOre = getUsage() / numberOfOres;


        for (int i = 0; i < numberOfOres; i++) {
            ItemStack drop = InventoryUtils.insertItem(inventorySlots, getRandomDrop(), Action.EXECUTE, AutomationType.INTERNAL);
            if (drop.isEmpty()) {
                energyContainer.extract(energyPerOre, Action.EXECUTE, AutomationType.INTERNAL);
            } else {
                break;
            }
        }
    }

    private static ItemStack getRandomDrop() {
        HolderSet.Named<Block> oreTag = BuiltInRegistries.BLOCK.getTag(Tags.Blocks.ORES).orElseThrow();
        List<? extends String> blacklist = Config.blacklistedOres.get();
        List<Holder<Block>> allowedOres = oreTag.stream()
                .filter(ore -> ore.getKey() != null && !blacklist.contains(ore.getKey().location().toString()))
                .filter(ore -> !ore.is(Mekanism_lasers.ORE_BLACKLIST))
                .toList();
        if (allowedOres.isEmpty()) {
            throw new IllegalStateException("No ores available to generate!");
        }

        RandomSource randomSource = RandomSource.create();
        Holder<Block> randomBlockHolder = allowedOres.get(randomSource.nextInt(allowedOres.size()));
        Block randomBlock = randomBlockHolder.value();

        return new ItemStack(randomBlock.asItem());
    }




}
