package com.folumo.mekanism_lasers.common.block;

import com.folumo.mekanism_lasers.common.block_entity.ToggleableLaserBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.resource.BlockResourceInfo;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class ToggleableLaser extends BlockTile.BlockTileModel<ToggleableLaserBlockEntity, BlockTypeTile<ToggleableLaserBlockEntity>>{

    public ToggleableLaser(BlockTypeTile<ToggleableLaserBlockEntity> type) {
        super(type, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor()));
    }

    @NotNull
    @Override
    protected InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (!world.isClientSide){
            TileEntityMekanism tile = WorldUtils.getTileEntity(TileEntityMekanism.class, world, pos);
            if (tile instanceof ToggleableLaserBlockEntity toggleableLaser){
                boolean activity = toggleableLaser.getLaserActivity();

                toggleableLaser.setLaserActivity(!activity, player);
            }
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }
}