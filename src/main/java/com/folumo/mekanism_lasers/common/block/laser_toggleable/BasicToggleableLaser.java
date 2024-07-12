package com.folumo.mekanism_lasers.common.block.laser_toggleable;

import com.folumo.mekanism_lasers.common.block_entity.laser_toggleable.BasicToggleableLaserBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.resource.BlockResourceInfo;


public class BasicToggleableLaser extends BlockTile.BlockTileModel<BasicToggleableLaserBlockEntity, BlockTypeTile<BasicToggleableLaserBlockEntity>>{

    public BasicToggleableLaser(BlockTypeTile<BasicToggleableLaserBlockEntity> type) {
        super(type, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor()));
    }
}