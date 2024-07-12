package com.folumo.mekanism_lasers.common.block_entity;

import com.folumo.mekanism_lasers.common.registry.BlockRegistry;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.math.FloatingLong;
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
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class OreGeneratorBlockEntity extends TileEntityLaserReceptor {
    private int coolDown = 0;
    public static FloatingLong energyCap = FloatingLong.createConst(64_000_000L);
    public static FloatingLong usage = FloatingLong.createConst(16_000_000L);
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


    public static FloatingLong getUsage() {
        return usage;
    }
    public static FloatingLong getEnergyCap() {
        return energyCap;
    }

    @Override
    public void receiveLaserEnergy(@NotNull FloatingLong energy) {
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
            coolDown = 20 * 5;
            if(energyContainer.getEnergy().greaterOrEqual(getUsage())) {
                generateOre();
            }
        } else {
            coolDown--;
        }
        return false;
    }

    private void generateOre() {
        List<IInventorySlot> inventorySlots = getInventorySlots(null);
        ItemStack drop = InventoryUtils.insertItem(inventorySlots, getRandomDrop(), Action.EXECUTE, AutomationType.INTERNAL);
        if (drop.isEmpty()) {
            energyContainer.extract(getUsage(), Action.EXECUTE, AutomationType.INTERNAL);
        }
    }

    private static ItemStack getRandomDrop() {
        TagKey<Block> blockTag = BlockTags.create(ResourceLocation.fromNamespaceAndPath("forge", "ore"));
        List<HolderSet.Named<Block>> blocksInTag = BuiltInRegistries.BLOCK.getTag(blockTag).stream().toList();

        Random rand = new Random();

        // Get a random index
        int randomIndex = rand.nextInt(blocksInTag.size());


        HolderSet.Named<Block> block = blocksInTag.get(randomIndex);

        return new ItemStack(block.get(0).value().asItem());
    }
}
