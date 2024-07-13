package com.folumo.mekanism_lasers.common.item;

import com.folumo.mekanism_lasers.common.block_entity.OreGeneratorBlockEntity;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.item.block.ItemBlockTooltip;
import net.minecraft.world.item.Rarity;


public class OreGeneratorBlockItem extends ItemBlockTooltip<BlockTile.BlockTileModel<OreGeneratorBlockEntity, BlockTypeTile<OreGeneratorBlockEntity>>> {

    public OreGeneratorBlockItem(BlockTile.BlockTileModel<OreGeneratorBlockEntity, BlockTypeTile<OreGeneratorBlockEntity>> block, Properties properties) {
        super(block, properties.rarity(Rarity.RARE));
    }
}
