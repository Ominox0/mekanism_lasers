package com.folumo.mekanism_lasers.common.block_entity;

import com.folumo.mekanism_lasers.common.registry.BlockRegistry;
import mekanism.api.lasers.ILaserReceptor;
import mekanism.api.math.FloatingLong;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;


public class LaserStopperBlockEntity extends TileEntityMekanism implements ILaserReceptor {
    private BlockState currentBlockState;

    public LaserStopperBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.LASER_STOPPER, pos, state);
        currentBlockState = state;
    }

    @Override
    public @NotNull Component getName() {
        return Component.literal("Laser Stopper");
    }

    @Override
    public void receiveLaserEnergy(@NotNull FloatingLong energy) {
    }

    @Override
    public boolean canLasersDig() {
        return false;
    }


    public void changeTexture(BlockState state) {
        this.currentBlockState = state;
        if (this.level != null) {
            //this.level.setBlock(this.worldPosition, state, 3); // Update the block in the world
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3); // Notify the level of the change
        }
    }

    public BlockState getCurrentBlockState() {
        return currentBlockState;
    }


}
