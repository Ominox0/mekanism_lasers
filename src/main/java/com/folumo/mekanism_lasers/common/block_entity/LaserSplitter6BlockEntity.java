package com.folumo.mekanism_lasers.common.block_entity;

import com.folumo.mekanism_lasers.common.registry.BlockRegistry;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.energy.LaserEnergyContainer;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.tile.laser.TileEntityLaserReceptor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class LaserSplitter6BlockEntity extends TileEntityLaserReceptor {

    private long fireNext;
    private long toFireRest;

    public LaserSplitter6BlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.LASER_SPLITTER, pos, state);
    }

    protected void addInitialEnergyContainers(EnergyContainerHelper builder, IContentsListener listener) {
        builder.addContainer(this.energyContainer = LaserEnergyContainer.create(BasicEnergyContainer.notExternal, BasicEnergyContainer.alwaysTrue, this, listener), RelativeSide.BACK);
    }

    @Override
    public @NotNull Component getName() {
        return Component.literal("Laser Splitter");
    }

    public static long getMaxEnergy() {
        return 10_000_000_000L;
    }

    protected long toFire() {
        if (toFireRest <= 0L) {
            fireNext = energyContainer.getEnergy() / 6;
            toFireRest = energyContainer.getEnergy() - fireNext;
        } else {
            toFireRest -= fireNext;

        }
        return fireNext;
    }

    @Override
    protected boolean onUpdateServer() {
        boolean sendUpdatePacket1 = super.onUpdateServer();



        setFacing(getDirection().getOpposite());

        boolean sendUpdatePacket2 = super.onUpdateServer();

        return sendUpdatePacket1 || sendUpdatePacket2;
    }
}

