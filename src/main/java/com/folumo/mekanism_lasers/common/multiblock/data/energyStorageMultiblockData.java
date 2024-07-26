package com.folumo.mekanism_lasers.common.multiblock.data;

import com.folumo.mekanism_lasers.common.block_entity.EnergyStorageCellBlockEntity;
import com.folumo.mekanism_lasers.common.multiblock.container.energyStorageMultiblockContainer;
import mekanism.common.lib.multiblock.MultiblockData;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class energyStorageMultiblockData extends MultiblockData {

    @NotNull
    private final energyStorageMultiblockContainer energyContainer;
    public energyStorageMultiblockData(BlockEntity tile) {
        super(tile);
        energyContainers.add(energyContainer = new energyStorageMultiblockContainer(this));
    }

    public void addCell(EnergyStorageCellBlockEntity cell){
        energyContainer.addCell(cell.getBlockPos(), cell);
    }
}
