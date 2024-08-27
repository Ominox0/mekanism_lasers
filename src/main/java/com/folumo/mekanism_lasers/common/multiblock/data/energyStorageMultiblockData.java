package com.folumo.mekanism_lasers.common.multiblock.data;

import com.folumo.mekanism_lasers.common.block_entity.EnergyStorageCellBlockEntity;
import com.folumo.mekanism_lasers.common.block_entity.EnergyStoragePortBlockEntity;
import com.folumo.mekanism_lasers.common.multiblock.container.energyStorageMultiblockContainer;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.common.lib.multiblock.IValveHandler;
import mekanism.common.lib.multiblock.MultiblockData;
import mekanism.common.util.CableUtils;
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

    @Override
    public boolean tick(Level world) {
        boolean ret = super.tick(world);
        energyContainer.tick();
        // We tick the main energy container before adding/draining from the slots, so that we make sure
        // they get first "pickings" at attempting to get or give power, without having to worry about the
        // rate limit of the structure being used up by the ports
        if (!energyOutputTargets.isEmpty() && !energyContainer.isEmpty()) {
            CableUtils.emit(getActiveOutputs(energyOutputTargets), energyContainer, Long.MAX_VALUE);
        }
        return ret;
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
