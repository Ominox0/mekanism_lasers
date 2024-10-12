package com.folumo.mekanism_lasers.common.item;

import com.folumo.mekanism_lasers.common.block_entity.LaserBlockEntity;
import com.folumo.mekanism_lasers.common.block_entity.LaserSplitterBlockEntity;
import com.folumo.mekanism_lasers.common.tier.LaserTier;
import mekanism.api.text.TextComponentUtil;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.item.block.ItemBlockTooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;


public class LaserSplitterBlockItem extends ItemBlockTooltip<BlockTile.BlockTileModel<LaserSplitterBlockEntity, BlockTypeTile<LaserSplitterBlockEntity>>> {
    public LaserSplitterBlockItem(BlockTile.BlockTileModel<LaserSplitterBlockEntity, BlockTypeTile<LaserSplitterBlockEntity>> block, Properties properties) {
        super(block, properties);
    }

    //@Override
    //public @NotNull Component getName(@NotNull ItemStack stack) {
    //    return TextComponentUtil.build(tier.getBaseTier().getColor(), super.getName(stack));
    //}
}
