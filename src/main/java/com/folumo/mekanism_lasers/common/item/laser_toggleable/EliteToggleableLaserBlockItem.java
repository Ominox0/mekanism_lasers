package com.folumo.mekanism_lasers.common.item.laser_toggleable;

import com.folumo.mekanism_lasers.common.block_entity.laser_toggleable.EliteToggleableLaserBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.item.block.ItemBlockTooltip;


public class EliteToggleableLaserBlockItem extends ItemBlockTooltip<BlockTile.BlockTileModel<EliteToggleableLaserBlockEntity, BlockTypeTile<EliteToggleableLaserBlockEntity>>> {

    public EliteToggleableLaserBlockItem(BlockTile.BlockTileModel<EliteToggleableLaserBlockEntity, BlockTypeTile<EliteToggleableLaserBlockEntity>> block, Properties properties) {
        super(block, properties);
    }
}
