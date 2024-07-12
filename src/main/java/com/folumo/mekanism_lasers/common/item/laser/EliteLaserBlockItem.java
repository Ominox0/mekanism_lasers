package com.folumo.mekanism_lasers.common.item.laser;


import com.folumo.mekanism_lasers.common.block_entity.laser.EliteLaserBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.item.block.ItemBlockTooltip;

public class EliteLaserBlockItem extends ItemBlockTooltip<BlockTile.BlockTileModel<EliteLaserBlockEntity, BlockTypeTile<EliteLaserBlockEntity>>> {

    public EliteLaserBlockItem(BlockTile.BlockTileModel<EliteLaserBlockEntity, BlockTypeTile<EliteLaserBlockEntity>> block, Properties properties) {
        super(block, properties);
    }
}
