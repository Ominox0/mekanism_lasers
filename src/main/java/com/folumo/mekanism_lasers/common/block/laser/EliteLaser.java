package com.folumo.mekanism_lasers.common.block.laser;

import com.folumo.mekanism_lasers.common.block_entity.laser.EliteLaserBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.resource.BlockResourceInfo;



public class EliteLaser extends BlockTile.BlockTileModel<EliteLaserBlockEntity, BlockTypeTile<EliteLaserBlockEntity>>{

    public EliteLaser(BlockTypeTile<EliteLaserBlockEntity> type) {
        super(type, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor()));
    }
}