package com.folumo.mekanism_lasers.common.item.laser;


import com.folumo.mekanism_lasers.common.block_entity.laser.UltimateLaserBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.item.block.ItemBlockTooltip;


public class UltimateLaserBlockItem extends ItemBlockTooltip<BlockTile.BlockTileModel<UltimateLaserBlockEntity, BlockTypeTile<UltimateLaserBlockEntity>>> {


    public UltimateLaserBlockItem(BlockTile.BlockTileModel<UltimateLaserBlockEntity, BlockTypeTile<UltimateLaserBlockEntity>> block, Properties properties) {
        super(block, properties);
    }
}
