package com.folumo.mekanism_lasers.common.block_entity;

import com.folumo.mekanism_lasers.common.tier.ToggleableLaserTier;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.lasers.ILaserReceptor;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.energy.LaserEnergyContainer;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.tile.laser.TileEntityBasicLaser;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ToggleableLaserBlockEntity extends TileEntityBasicLaser implements ILaserReceptor {
    private static boolean active = true;
    private final ToggleableLaserTier tier;

    public ToggleableLaserBlockEntity(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        tier = Attribute.getTier(blockProvider, ToggleableLaserTier.class);
    }

    @Override
    public void receiveLaserEnergy(@NotNull FloatingLong floatingLong) {

    }

    @Override
    public boolean canLasersDig() {
        return false;
    }

    protected void addInitialEnergyContainers(EnergyContainerHelper builder, IContentsListener listener) {
        builder.addContainer(this.energyContainer = LaserEnergyContainer.create(BasicEnergyContainer.notExternal, BasicEnergyContainer.alwaysTrue, this, listener), new RelativeSide[]{RelativeSide.BACK});
    }

    public void setLaserActivity(boolean isActive){
        active = isActive;
    }

    @Override
    protected FloatingLong toFire(){
        if (!active){
            return FloatingLong.ZERO;
        }else {
            return tier.getEnergyUsage();

        }
    }
    @Override
    public @NotNull Component getName() {
        return Component.literal("Basic Toggleable Laser");
    }
}
