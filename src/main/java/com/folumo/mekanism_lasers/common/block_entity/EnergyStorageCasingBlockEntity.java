package com.folumo.mekanism_lasers.common.block_entity;

import com.folumo.mekanism_lasers.Mekanism_lasers;
import com.folumo.mekanism_lasers.common.multiblock.data.energyStorageMultiblockData;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.lib.multiblock.MultiblockManager;
import mekanism.common.tile.prefab.TileEntityMultiblock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class EnergyStorageCasingBlockEntity extends TileEntityMultiblock<energyStorageMultiblockData> {
    public EnergyStorageCasingBlockEntity(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    public energyStorageMultiblockData createMultiblock() {
        return new energyStorageMultiblockData(this);
    }

    @Override
    public MultiblockManager<energyStorageMultiblockData> getManager() {
        return Mekanism_lasers.energyStorage;
    }
}
