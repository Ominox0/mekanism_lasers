package com.folumo.mekanism_lasers.common.item;

import com.folumo.mekanism_lasers.common.block_entity.ToggleableLaserBlockEntity;
import mekanism.api.text.TextComponentUtil;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.item.block.ItemBlockTooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;


public class ToggleableLaserBlockItem extends ItemBlockTooltip<BlockTile.BlockTileModel<ToggleableLaserBlockEntity, BlockTypeTile<ToggleableLaserBlockEntity>>> {

    public ToggleableLaserBlockItem(BlockTile.BlockTileModel<ToggleableLaserBlockEntity, BlockTypeTile<ToggleableLaserBlockEntity>> block, Properties properties) {
        super(block, properties);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return TextComponentUtil.build(TextColor.fromRgb(FastColor.ARGB32.color(95, 255, 184)), super.getName(stack));
    }
}
