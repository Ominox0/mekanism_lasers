package com.folumo.mekanism_lasers.common.item;

import com.folumo.mekanism_lasers.common.block_entity.EnergyTransformerBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.item.block.ItemBlockTooltip;


public class EnergyTransformerBlockItem extends ItemBlockTooltip<BlockTile.BlockTileModel<EnergyTransformerBlockEntity, BlockTypeTile<EnergyTransformerBlockEntity>>> {
    public EnergyTransformerBlockItem(BlockTile.BlockTileModel<EnergyTransformerBlockEntity, BlockTypeTile<EnergyTransformerBlockEntity>> block, Properties properties) {
        super(block, properties);
    }

}
