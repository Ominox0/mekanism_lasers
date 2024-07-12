package com.folumo.mekanism_lasers.common.item.laser_toggleable;

import com.folumo.mekanism_lasers.common.block_entity.laser.BasicLaserBlockEntity;
import com.folumo.mekanism_lasers.common.block_entity.laser_toggleable.BasicToggleableLaserBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.item.block.ItemBlockTooltip;


public class BasicToggleableLaserBlockItem extends ItemBlockTooltip<BlockTile.BlockTileModel<BasicToggleableLaserBlockEntity, BlockTypeTile<BasicToggleableLaserBlockEntity>>> {

    public BasicToggleableLaserBlockItem(BlockTile.BlockTileModel<BasicToggleableLaserBlockEntity, BlockTypeTile<BasicToggleableLaserBlockEntity>> block, Properties properties) {
        super(block, properties);
    }
}
