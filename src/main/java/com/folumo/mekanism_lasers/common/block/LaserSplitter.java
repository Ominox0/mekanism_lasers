package com.folumo.mekanism_lasers.common.block;

import com.folumo.mekanism_lasers.common.block_entity.LaserBlockEntity;
import com.folumo.mekanism_lasers.common.block_entity.LaserSplitterBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.resource.BlockResourceInfo;

public class LaserSplitter extends BlockTile.BlockTileModel<LaserSplitterBlockEntity, BlockTypeTile<LaserSplitterBlockEntity>>{

    public LaserSplitter(BlockTypeTile<LaserSplitterBlockEntity> type) {
        super(type, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor()));
    }
}