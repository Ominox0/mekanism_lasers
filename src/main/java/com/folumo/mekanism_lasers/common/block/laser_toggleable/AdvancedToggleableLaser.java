package com.folumo.mekanism_lasers.common.block.laser_toggleable;

import com.folumo.mekanism_lasers.common.block_entity.laser_toggleable.AdvancedToggleableLaserBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.resource.BlockResourceInfo;


public class AdvancedToggleableLaser extends BlockTile.BlockTileModel<AdvancedToggleableLaserBlockEntity, BlockTypeTile<AdvancedToggleableLaserBlockEntity>>{

    public AdvancedToggleableLaser(BlockTypeTile<AdvancedToggleableLaserBlockEntity> type) {
        super(type, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor()));
    }
}