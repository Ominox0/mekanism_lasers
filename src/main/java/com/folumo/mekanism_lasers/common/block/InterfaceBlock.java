package com.folumo.mekanism_lasers.common.block;

import com.folumo.mekanism_lasers.common.block_entity.InterfaceBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.resource.BlockResourceInfo;


public class InterfaceBlock extends BlockTile.BlockTileModel<InterfaceBlockEntity, BlockTypeTile<InterfaceBlockEntity>>{
    public InterfaceBlock(BlockTypeTile<InterfaceBlockEntity> type) {
        super(type, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor()));
    }
}