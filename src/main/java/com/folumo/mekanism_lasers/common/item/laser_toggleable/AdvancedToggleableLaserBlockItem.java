package com.folumo.mekanism_lasers.common.item.laser_toggleable;

import com.folumo.mekanism_lasers.common.block_entity.laser_toggleable.AdvancedToggleableLaserBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.item.block.ItemBlockTooltip;


public class AdvancedToggleableLaserBlockItem extends ItemBlockTooltip<BlockTile.BlockTileModel<AdvancedToggleableLaserBlockEntity, BlockTypeTile<AdvancedToggleableLaserBlockEntity>>> {

    public AdvancedToggleableLaserBlockItem(BlockTile.BlockTileModel<AdvancedToggleableLaserBlockEntity, BlockTypeTile<AdvancedToggleableLaserBlockEntity>> block, Properties properties) {
        super(block, properties);
    }
}
