package com.folumo.mekanism_lasers.common.block.laser;

import com.folumo.mekanism_lasers.common.block_entity.laser.AdvancedLaserBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.resource.BlockResourceInfo;

public class AdvancedLaser extends BlockTile.BlockTileModel<AdvancedLaserBlockEntity, BlockTypeTile<AdvancedLaserBlockEntity>>{

    public AdvancedLaser(BlockTypeTile<AdvancedLaserBlockEntity> type) {
        super(type, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor()));
    }
}