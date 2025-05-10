package com.folumo.mekanism_lasers.common.block;

import com.folumo.mekanism_lasers.common.block_entity.EnergyTransformerBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.resource.BlockResourceInfo;

public class EnergyTransformer extends BlockTile.BlockTileModel<EnergyTransformerBlockEntity, BlockTypeTile<EnergyTransformerBlockEntity>>{

    public EnergyTransformer(BlockTypeTile<EnergyTransformerBlockEntity> type) {
        super(type, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor()));
    }
}