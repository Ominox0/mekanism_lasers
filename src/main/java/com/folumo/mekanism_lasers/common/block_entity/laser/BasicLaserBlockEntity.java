package com.folumo.mekanism_lasers.common.block_entity.laser;

import com.folumo.mekanism_lasers.common.registry.BlockRegistry;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.lasers.ILaserReceptor;
import mekanism.api.math.FloatingLong;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.energy.LaserEnergyContainer;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.tile.laser.TileEntityBasicLaser;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BasicLaserBlockEntity extends TileEntityBasicLaser implements ILaserReceptor {

    public static FloatingLong energyCap = FloatingLong.createConst(4000000L);
    public static FloatingLong usage = FloatingLong.createConst(15000L);


    public BasicLaserBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.BASIC_LASER, pos, state);

    }

    protected void addInitialEnergyContainers(EnergyContainerHelper builder, IContentsListener listener) {
        builder.addContainer(this.energyContainer = LaserEnergyContainer.create(BasicEnergyContainer.notExternal, BasicEnergyContainer.alwaysTrue, this, listener), new RelativeSide[]{RelativeSide.BACK});
    }

    @Override
    protected FloatingLong toFire(){
        return usage;
    }

    @Override
    public @NotNull Component getName() {
        return Component.literal("Basic Laser");
    }

    public static FloatingLong getUsage() {
        return usage;
    }

    public static FloatingLong getEnergyCap() {
        return energyCap;
    }

    @Override
    public void receiveLaserEnergy(@NotNull FloatingLong floatingLong) {

    }

    @Override
    public boolean canLasersDig() {
        return false;
    }
}
