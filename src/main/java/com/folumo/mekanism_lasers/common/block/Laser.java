package com.folumo.mekanism_lasers.common.block;

import com.folumo.mekanism_lasers.common.block_entity.LaserBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.resource.BlockResourceInfo;

public class Laser extends BlockTile.BlockTileModel<LaserBlockEntity, BlockTypeTile<LaserBlockEntity>>{

    public Laser(BlockTypeTile<LaserBlockEntity> type) {
        super(type, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor()));
    }
}