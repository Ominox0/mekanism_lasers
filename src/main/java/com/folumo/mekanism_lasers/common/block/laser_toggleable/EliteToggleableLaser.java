package com.folumo.mekanism_lasers.common.block.laser_toggleable;

import com.folumo.mekanism_lasers.common.block_entity.laser_toggleable.EliteToggleableLaserBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.resource.BlockResourceInfo;


public class EliteToggleableLaser extends BlockTile.BlockTileModel<EliteToggleableLaserBlockEntity, BlockTypeTile<EliteToggleableLaserBlockEntity>>{

    public EliteToggleableLaser(BlockTypeTile<EliteToggleableLaserBlockEntity> type) {
        super(type, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor()));
    }
}