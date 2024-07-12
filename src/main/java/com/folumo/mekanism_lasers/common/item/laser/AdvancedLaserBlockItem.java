package com.folumo.mekanism_lasers.common.item.laser;

import com.folumo.mekanism_lasers.common.block_entity.laser.AdvancedLaserBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.item.block.ItemBlockTooltip;

public class AdvancedLaserBlockItem extends ItemBlockTooltip<BlockTile.BlockTileModel<AdvancedLaserBlockEntity, BlockTypeTile<AdvancedLaserBlockEntity>>> {

    public AdvancedLaserBlockItem(BlockTile.BlockTileModel<AdvancedLaserBlockEntity, BlockTypeTile<AdvancedLaserBlockEntity>> block, Properties properties) {
        super(block, properties);
    }
}
