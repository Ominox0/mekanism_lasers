package com.folumo.mekanism_lasers.common.block_entity;

import mekanism.api.IContentsListener;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.tile.prefab.TileEntityInternalMultiblock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnergyStorageCellBlockEntity extends TileEntityInternalMultiblock {
    private MachineEnergyContainer<EnergyStorageCellBlockEntity> energyContainer;

    public EnergyStorageCellBlockEntity(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected @Nullable IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSide(this::getDirection);
        builder.addContainer(energyContainer = MachineEnergyContainer.internal(this, listener));
        return builder.build();
    }

    public MachineEnergyContainer<EnergyStorageCellBlockEntity> getEnergyContainer() {
        return energyContainer;
    }

    public static long getMaxEnergy(){
        return 16_000_000L;
    }
}
