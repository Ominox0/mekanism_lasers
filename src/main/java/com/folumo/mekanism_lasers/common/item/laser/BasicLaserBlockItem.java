package com.folumo.mekanism_lasers.common.item.laser;

import com.folumo.mekanism_lasers.common.block_entity.laser.BasicLaserBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.item.block.ItemBlockTooltip;


public class BasicLaserBlockItem extends ItemBlockTooltip<BlockTile.BlockTileModel<BasicLaserBlockEntity, BlockTypeTile<BasicLaserBlockEntity>>> {

    public BasicLaserBlockItem(BlockTile.BlockTileModel<BasicLaserBlockEntity, BlockTypeTile<BasicLaserBlockEntity>> block, Properties properties) {
        super(block, properties);
    }
}
