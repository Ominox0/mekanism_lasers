package com.folumo.mekanism_lasers.common.block.laser_toggleable;

import com.folumo.mekanism_lasers.common.block_entity.laser_toggleable.UltimateToggleableLaserBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.resource.BlockResourceInfo;


public class UltimateToggleableLaser extends BlockTile.BlockTileModel<UltimateToggleableLaserBlockEntity, BlockTypeTile<UltimateToggleableLaserBlockEntity>>{

    public UltimateToggleableLaser(BlockTypeTile<UltimateToggleableLaserBlockEntity> type) {
        super(type, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor()));
    }
}