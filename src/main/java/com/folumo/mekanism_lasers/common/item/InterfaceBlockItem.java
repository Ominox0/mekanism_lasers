package com.folumo.mekanism_lasers.common.item;

import com.folumo.mekanism_lasers.common.block_entity.InterfaceBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.item.block.ItemBlockTooltip;


public class InterfaceBlockItem extends ItemBlockTooltip<BlockTile.BlockTileModel<InterfaceBlockEntity, BlockTypeTile<InterfaceBlockEntity>>> {
    public InterfaceBlockItem(BlockTile.BlockTileModel<InterfaceBlockEntity, BlockTypeTile<InterfaceBlockEntity>> block, Properties properties) {
        super(block, properties);
    }

}