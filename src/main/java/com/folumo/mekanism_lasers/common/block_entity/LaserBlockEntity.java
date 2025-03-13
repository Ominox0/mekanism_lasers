package com.folumo.mekanism_lasers.common.block_entity;

import com.folumo.mekanism_lasers.common.tier.LaserTier;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.functions.ConstantPredicates;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.energy.LaserEnergyContainer;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.tile.laser.TileEntityLaserReceptor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class LaserBlockEntity extends TileEntityLaserReceptor {
    private final LaserTier tier;

    public LaserBlockEntity(Holder<Block> blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);

        tier = Attribute.getTier(blockProvider, LaserTier.class);

        if (tier == LaserTier.CREATIVE){
            this.energyContainer.insert(Long.MAX_VALUE, Action.EXECUTE, AutomationType.INTERNAL);
        }
    }

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

        //


        //builder.addSlot(energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 143, 35), RelativeSide.BACK);
        return builder.build();
    }

    @Override
    public void receiveLaserEnergy(long energy) {

    }

    protected void addInitialEnergyContainers(EnergyContainerHelper builder, IContentsListener listener) {
        builder.addContainer(this.energyContainer = LaserEnergyContainer.create(BasicEnergyContainer.notExternal, ConstantPredicates.alwaysTrue(), this, listener), RelativeSide.BACK);
    }

    @Override
    protected long toFire(){
        return tier.getEnergyUsage();
    }
    @Override
    public @NotNull Component getName() {
        return Component.literal("Laser");
    }

    @Override
    protected boolean onUpdateServer() {
        boolean ret = super.onUpdateServer();
        if (tier == LaserTier.CREATIVE){
            this.energyContainer.insert(Long.MAX_VALUE, Action.EXECUTE, AutomationType.INTERNAL);
        }

        return ret;
    }
}

