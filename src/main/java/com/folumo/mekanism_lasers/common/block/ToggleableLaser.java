package com.folumo.mekanism_lasers.common.block;

import com.folumo.mekanism_lasers.common.block_entity.ToggleableLaserBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.resource.BlockResourceInfo;

public class ToggleableLaser extends BlockTile.BlockTileModel<ToggleableLaserBlockEntity, BlockTypeTile<ToggleableLaserBlockEntity>>{

    public ToggleableLaser(BlockTypeTile<ToggleableLaserBlockEntity> type) {
        super(type, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor()));
    }
}