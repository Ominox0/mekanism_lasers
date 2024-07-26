package com.folumo.mekanism_lasers.common.multiblock;


import mekanism.api.IContentsListener;
import mekanism.api.energy.IEnergyContainer;
import mekanism.common.capabilities.energy.BasicEnergyContainer;
import mekanism.common.capabilities.energy.LaserEnergyContainer;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.inventory.container.sync.dynamic.ContainerSync;
import mekanism.common.lib.multiblock.MultiblockData;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

import mekanism.common.tile.multiblock.TileEntityInductionCasing;

public class energyStorage extends BasicEnergyContainer {
    private final List<MultiblockData.EnergyOutputTarget> energyOutputTargets = new ArrayList<>();
    //@NotNull
    //private final LaserEnergyContainer energyContainer;

    private Long energy;

    @ContainerSync(getter = "getEnergy")
    private Long clientEnergy = 0L;

    public energyStorage(TileEntityInductionCasing tile, IContentsListener listener) {
        super(
                16_000_000_000L,
                BasicEnergyContainer.alwaysTrue,
                BasicEnergyContainer.alwaysTrue,
                listener
        );
    }


    @NotNull
    public energyStorage getEnergyContainer() {
        return this;
    }

    @Override
    public void onContentsChanged() {

    }
}