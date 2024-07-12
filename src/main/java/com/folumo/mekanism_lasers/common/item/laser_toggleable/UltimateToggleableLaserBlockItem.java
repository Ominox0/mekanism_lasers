package com.folumo.mekanism_lasers.common.item.laser_toggleable;

import com.folumo.mekanism_lasers.common.block_entity.laser_toggleable.UltimateToggleableLaserBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.item.block.ItemBlockTooltip;


public class UltimateToggleableLaserBlockItem extends ItemBlockTooltip<BlockTile.BlockTileModel<UltimateToggleableLaserBlockEntity, BlockTypeTile<UltimateToggleableLaserBlockEntity>>> {

    public UltimateToggleableLaserBlockItem(BlockTile.BlockTileModel<UltimateToggleableLaserBlockEntity, BlockTypeTile<UltimateToggleableLaserBlockEntity>> block, Properties properties) {
        super(block, properties);
    }
}
