package com.folumo.mekanism_lasers.common.block.laser;

import com.folumo.mekanism_lasers.common.block_entity.laser.BasicLaserBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.resource.BlockResourceInfo;


public class BasicLaser extends BlockTile.BlockTileModel<BasicLaserBlockEntity, BlockTypeTile<BasicLaserBlockEntity>>{

    public BasicLaser(BlockTypeTile<BasicLaserBlockEntity> type) {
        super(type, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor()));
    }
}
