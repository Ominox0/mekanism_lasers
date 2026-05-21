package com.folumo.mekanism_lasers.common.block;

import com.folumo.mekanism_lasers.Config;
import com.folumo.mekanism_lasers.common.block_entity.ODECM_BlockEntity;
import com.folumo.mekanism_lasers.common.multiblock.OreGeneratorBase;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.resource.BlockResourceInfo;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ODECM_Block extends BlockTile.BlockTileModel<ODECM_BlockEntity, BlockTypeTile<ODECM_BlockEntity>>{
    public static final TagKey<Block> ORES_TAG = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath("c", "ores")
    );

    public static List<String> pattern = List.of("");
    public ODECM_Block(BlockTypeTile<ODECM_BlockEntity> type) {
        super(type, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor()));
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (!world.isClientSide) {
            TileEntityMekanism tile = WorldUtils.getTileEntity(TileEntityMekanism.class, world, pos);
            if (tile instanceof ODECM_BlockEntity blockEntity) {
                if (stack.getItem() instanceof BlockItem blockItem){
                    Block block = blockItem.getBlock();

                    boolean isOre = block.builtInRegistryHolder().is(ORES_TAG);

                    if (isOre){
                        List<? extends String> blacklist = Config.blacklistedOres.get();
                        boolean contains = blacklist.contains(BuiltInRegistries.BLOCK.getKey(block).toString());
                        if (!contains){
                            blockEntity.setItem(block);
                        } else {
                            player.sendSystemMessage(Component.literal("This ore has been blacklisted!"));
                        }
                    }

                    return ItemInteractionResult.SUCCESS;
                }
            }
        }
        return super.useItemOn(stack, state, world, pos, player, hand, hit);
    }

    @NotNull
    @Override
    protected InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (!world.isClientSide){
            TileEntityMekanism tile = WorldUtils.getTileEntity(TileEntityMekanism.class, world, pos);
            if (tile instanceof ODECM_BlockEntity blockEntity){
                OreGeneratorBase typeV = blockEntity.getVType();
                float amount = Config.ODECM_Generator.get().get(typeV.getIndex());

                OreGeneratorBase.BuildCheckResult canBuild = blockEntity.getVType().canBuild(blockEntity.getBlockPos(), world);

                if (canBuild.success()){
                    player.sendSystemMessage(Component.literal("Progress: " + blockEntity.getProgress() + "/" + Math.round(Config.ODECM_Progress.getAsInt() / amount) + " (of " + blockEntity.getItem() + ")"));

                } else {
                    player.sendSystemMessage(Component.literal("Cant build!  " + " (" + canBuild.pos().toShortString() + ", " + canBuild.expected().toString() + ")"));
                }

                return InteractionResult.PASS;
            }
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }
}
