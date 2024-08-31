package com.folumo.mekanism_lasers.common.block_entity;

import com.folumo.mekanism_lasers.common.tier.LaserTier;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.energy.LaserEnergyContainer;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.tile.laser.TileEntityLaserReceptor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class LaserBlockEntity extends TileEntityLaserReceptor {
    private final LaserTier tier;

    public LaserBlockEntity(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        tier = Attribute.getTier(blockProvider, LaserTier.class);

        if (tier == LaserTier.CREATIVE){
            this.energyContainer.insert(Long.MAX_VALUE, Action.EXECUTE, AutomationType.INTERNAL);
        }
    }

    @Override
    public void receiveLaserEnergy(long energy) {

    }

    protected void addInitialEnergyContainers(EnergyContainerHelper builder, IContentsListener listener) {
        builder.addContainer(this.energyContainer = LaserEnergyContainer.create(BasicEnergyContainer.notExternal, BasicEnergyContainer.alwaysTrue, this, listener), RelativeSide.BACK);
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

