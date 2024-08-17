package com.folumo.mekanism_lasers.common.multiblock.data;

import com.folumo.mekanism_lasers.common.block_entity.EnergyStorageCellBlockEntity;
import com.folumo.mekanism_lasers.common.block_entity.EnergyStoragePortBlockEntity;
import com.folumo.mekanism_lasers.common.multiblock.container.energyStorageMultiblockContainer;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.common.lib.multiblock.IValveHandler;
import mekanism.common.lib.multiblock.MultiblockData;
import mekanism.common.util.WorldUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class energyStorageMultiblockData extends MultiblockData {
    private final List<EnergyOutputTarget> energyOutputTargets = new ArrayList<>();
    @NotNull
    private final energyStorageMultiblockContainer energyContainer;
    public energyStorageMultiblockData(BlockEntity tile) {
        super(tile);
        energyContainers.add(energyContainer = new energyStorageMultiblockContainer(this));
    }

    public void addEnergy(long energy){
        energyContainer.insert(energy, Action.EXECUTE, AutomationType.INTERNAL);
    }

    public void addCell(EnergyStorageCellBlockEntity cell){
        energyContainer.addCell(cell.getBlockPos(), cell);
    }
    @Override
    protected void updateEjectors(Level world) {
        energyOutputTargets.clear();
        for (IValveHandler.ValveData valve : valves) {
            EnergyStoragePortBlockEntity tile = WorldUtils.getTileEntity(EnergyStoragePortBlockEntity.class, world, valve.location);
            if (tile != null) {
                tile.addEnergyTargetCapability(energyOutputTargets, valve.side);
            }
        }
    }

}
