package com.folumo.mekanism_lasers.common.block.laser;

import com.folumo.mekanism_lasers.common.block_entity.laser.UltimateLaserBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.resource.BlockResourceInfo;


public class UltimateLaser extends BlockTile.BlockTileModel<UltimateLaserBlockEntity, BlockTypeTile<UltimateLaserBlockEntity>>{

    public UltimateLaser(BlockTypeTile<UltimateLaserBlockEntity> type) {
        super(type, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor()));
    }
}