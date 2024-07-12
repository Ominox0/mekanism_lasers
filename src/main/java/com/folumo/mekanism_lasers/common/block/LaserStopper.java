package com.folumo.mekanism_lasers.common.block;

import com.folumo.mekanism_lasers.common.block_entity.LaserStopperBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.resource.BlockResourceInfo;

public class LaserStopper extends BlockTile.BlockTileModel<LaserStopperBlockEntity, BlockTypeTile<LaserStopperBlockEntity>> {
    public LaserStopper(BlockTypeTile<LaserStopperBlockEntity> type) {
        super(type, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor()));
    }

}
