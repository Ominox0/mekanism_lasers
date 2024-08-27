package com.folumo.mekanism_lasers.common.multiblock.container;

import com.folumo.mekanism_lasers.common.block_entity.EnergyStorageCellBlockEntity;
import com.folumo.mekanism_lasers.common.multiblock.data.energyStorageMultiblockData;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.math.MathUtils;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Range;
import mekanism.api.annotations.NothingNullByDefault;
import java.util.Map;

@NothingNullByDefault
public class energyStorageMultiblockContainer implements IEnergyContainer {
    private final Map<BlockPos, IEnergyContainer> cells = new Object2ObjectOpenHashMap<>();

    private long queuedOutput = 0L;
    private long queuedInput = 0L;
    private long lastOutput = 0L;
    private long lastInput = 0L;
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
        return cachedTotal + queuedInput - queuedOutput;
    }

    @Override
    public void setEnergy(@Range(from = 0L, to = 9223372036854775807L) long energy) {

    }

    @Override
    public long getMaxEnergy() {
        return storageCap;
    }
    private long getRemainingInput() {
        return transferCap - queuedInput;
    }

    private long getRemainingOutput() {
        return transferCap - queuedOutput;
    }
    @Override
    public void onContentsChanged() {

    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {

    }

    @Override
    public long insert(long amount, Action action, AutomationType automationType) {
        if (amount <= 0L || !multiblock.isFormed()) {
            return amount;
        }
        long toAdd = Math.min(Math.min(amount, getRemainingInput()), getNeeded());
        if (toAdd == 0L) {
            //Exit if we don't actually have anything to add, either due to how much we need
            // or due to the remaining rate limit
            return amount;
        }
        if (action.execute()) {
            //Increase how much we are inputting
            queuedInput += toAdd;
        }
        return amount - toAdd;
    }

    @Override
    public long extract(long amount, Action action, AutomationType automationType) {
        if (isEmpty() || amount <= 0L || !multiblock.isFormed()) {
            return 0L;
        }
        //We limit it overall by the amount we can extract plus how much energy we have
        // as we want to be as accurate as possible with the values we return
        // It is possible that the energy we have stored is a lot less than the amount we
        // can output at once such as if the matrix is almost empty.
        amount = Math.min(Math.min(amount, getRemainingOutput()), getEnergy());
        if (amount > 0L && action.execute()) {
            //Increase how much we are outputting by the amount we accepted
            queuedOutput += amount;
        }
        return amount;
    }

    public void tick() {
        //if (!invalidPositions.isEmpty()) {
        //    for (BlockPos invalidPosition : invalidPositions) {
        //        cells.remove(invalidPosition);
        //        providers.remove(invalidPosition);
        //    }
        //    invalidPositions.clear();
        //}
        if (queuedInput < queuedOutput) {
            //queuedInput is smaller - we are removing energy
            removeEnergy(queuedOutput - queuedInput);
        } else if (queuedInput > queuedOutput) {
            //queuedInput is larger - we are adding energy
            addEnergy(queuedInput - queuedOutput);
        }
        lastInput = queuedInput;
        lastOutput = queuedOutput;
        queuedInput = 0L;
        queuedOutput = 0L;
    }

    private void removeEnergy(long energy) {
        cachedTotal -= energy;
        for (IEnergyContainer container : cells.values()) {
            //Note: extracting from the cell's energy container handles marking the cell for saving if it changes
            long extracted = container.extract(energy, Action.EXECUTE, AutomationType.INTERNAL);
            if (extracted > 0L) {
                energy -= extracted;
                if (energy == 0L) {
                    //Check less than equal rather than just equal in case something went wrong
                    // and break if we don't need to remove any more energy
                    break;
                }
            }
        }
    }

    private void addEnergy(long energy) {
        cachedTotal += energy;
        for (IEnergyContainer container : cells.values()) {
            //Note: inserting into the cell's energy container handles marking the cell for saving if it changes
            long remainder = container.insert(energy, Action.EXECUTE, AutomationType.INTERNAL);
            if (remainder < energy) {
                //Our cell accepted at least some energy
                if (remainder == 0L) {
                    //Check less than equal rather than just equal in case something went wrong
                    // and break if we don't have any energy left to add
                    break;
                }
                energy = remainder;
            }
        }
    }
}
