package com.folumo.mekanism_lasers.common.item;

import com.folumo.mekanism_lasers.common.block_entity.LaserStopperBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.item.block.ItemBlockTooltip;

public class LaserStopperBlockItem extends ItemBlockTooltip<BlockTile.BlockTileModel<LaserStopperBlockEntity, BlockTypeTile<LaserStopperBlockEntity>>>{

    public LaserStopperBlockItem(BlockTile.BlockTileModel<LaserStopperBlockEntity, BlockTypeTile<LaserStopperBlockEntity>> block, Properties properties) {
        super(block, properties);
    }
}
