package com.folumo.mekanism_lasers.common.block_entity;

import com.folumo.mekanism_lasers.common.registry.BlockRegistry;
import com.mojang.serialization.Dynamic;
import mekanism.api.lasers.ILaserReceptor;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class LaserStopperBlockEntity extends TileEntityMekanism implements ILaserReceptor {

    @Nullable
    private BlockState currentBlockState;

    public LaserStopperBlockEntity(BlockPos pos, @Nullable BlockState state) {
        super(BlockRegistry.LASER_STOPPER, pos, state);
        currentBlockState = state;
    }

    @Override
    public @NotNull Component getName() {
        return Component.literal("Laser Stopper");
    }

    @Override
    public void receiveLaserEnergy(long energy) {
    }


    @Override
    public boolean canLasersDig() {
        return false;
    }


    public void changeTexture(BlockState state) {
        this.currentBlockState = state;

        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3); // Notify the level of the change
        }
    }


    public @Nullable BlockState getCurrentBlockState() {
        return currentBlockState;
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        this.currentBlockState = BlockState.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, nbt.getCompound("currentBlockState"))).result().orElse(null);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbtTags, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(nbtTags, provider);
        nbtTags.put("currentBlockState", BlockState.CODEC.encodeStart(NbtOps.INSTANCE, this.currentBlockState).result().orElseGet(CompoundTag::new));
    }
}
