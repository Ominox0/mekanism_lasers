package com.folumo.mekanism_lasers.common.block;

import com.folumo.mekanism_lasers.common.block_entity.OreGeneratorBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.resource.BlockResourceInfo;

public class OreGenerator extends BlockTile.BlockTileModel<OreGeneratorBlockEntity, BlockTypeTile<OreGeneratorBlockEntity>> {
    public OreGenerator(BlockTypeTile<OreGeneratorBlockEntity> type) {
        super(type, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor()));
    }

}
