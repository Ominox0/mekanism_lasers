package com.folumo.mekanism_lasers.common.multiblock.container;

import com.folumo.mekanism_lasers.common.block_entity.EnergyStorageCellBlockEntity;
import com.folumo.mekanism_lasers.common.multiblock.data.energyStorageMultiblockData;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.math.MathUtils;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Range;

import java.util.Map;

public class energyStorageMultiblockContainer implements IEnergyContainer {
    private final Map<BlockPos, IEnergyContainer> cells = new Object2ObjectOpenHashMap<>();

    private long cachedTotal = 0L;
    private long transferCap = 0L;
    private long storageCap = 0L;

    private final energyStorageMultiblockData multiblock;

    public energyStorageMultiblockContainer(energyStorageMultiblockData multiblock) {
        this.multiblock = multiblock;
    }

    public void addCell(BlockPos pos, EnergyStorageCellBlockEntity cell) {
        //As we already have the two different variables just pass them instead of accessing world to get tile again
        MachineEnergyContainer<EnergyStorageCellBlockEntity> energyContainer = cell.getEnergyContainer();
        cells.put(pos, energyContainer);

        storageCap = MathUtils.addClamped(storageCap, energyContainer.getMaxEnergy());
        cachedTotal = MathUtils.addClamped(cachedTotal, energyContainer.getEnergy());
    }

    @Override
    public @Range(from = 0L, to = 9223372036854775807L) long getEnergy() {
        return 0;
    }

    @Override
    public void setEnergy(@Range(from = 0L, to = 9223372036854775807L) long energy) {

    }

    @Override
    public long getMaxEnergy() {
        return 0;
    }

    @Override
    public void onContentsChanged() {

    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {

    }
}
