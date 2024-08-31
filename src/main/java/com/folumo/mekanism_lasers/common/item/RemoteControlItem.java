package com.folumo.mekanism_lasers.common.item;

import com.folumo.mekanism_lasers.common.block_entity.ToggleableLaserBlockEntity;
import mekanism.api.text.EnumColor;
import mekanism.api.text.TextComponentUtil;
import mekanism.common.registries.MekanismDataComponents;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.folumo.mekanism_lasers.common.registry.ComponentRegistry.RC_BLOCKPOS;
import static com.folumo.mekanism_lasers.common.registry.ComponentRegistry.RC_ACTIVITY;

public class RemoteControlItem extends Item {

    public RemoteControlItem(Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON).stacksTo(1)
                .component(MekanismDataComponents.CONFIGURATOR_MODE, mekanism.common.item.ItemConfigurator.ConfiguratorMode.CONFIGURATE_ITEMS)
                .component(RC_BLOCKPOS, new ArrayList<>())
                .component(RC_ACTIVITY, false)
        );
    }

    @NotNull
    @Override
    public Component getName(@NotNull ItemStack stack) {
        return TextComponentUtil.build(EnumColor.AQUA, super.getName(stack));
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level world, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!world.isClientSide) {
            player.sendSystemMessage(Component.literal("Switching laser mode"));
            switchLaserMode(stack, world, player);

        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level world = context.getLevel();
        ItemStack stack = context.getItemInHand();


        if (!world.isClientSide && player != null) {
            BlockPos pos = context.getClickedPos();

            BlockEntity tile = WorldUtils.getTileEntity(world, pos);

            if (tile instanceof ToggleableLaserBlockEntity && ((ToggleableLaserBlockEntity) tile).ownerMatches(player)) {
                List<BlockPos> linkedLasers = getBlockPos(stack);
                if (linkedLasers.contains(pos)) {
                    linkedLasers.remove(pos);
                    player.sendSystemMessage(Component.literal("removed"));
                } else {
                    linkedLasers.add(pos);
                    player.sendSystemMessage(Component.literal("added"));
                }
                saveLinkedLasers(stack, linkedLasers);
                return InteractionResult.PASS;

            }
            player.sendSystemMessage(Component.literal("Switching laser mode"));
            switchLaserMode(stack, world, player);

        }
        return InteractionResult.PASS;
    }

    public void switchLaserMode(ItemStack stack, Level world, Player player){
        stack.set(RC_ACTIVITY, !getActive(stack));

        List<BlockPos> linkedLasers = getBlockPos(stack);
        boolean finalLasersStatus = getActive(stack);

        linkedLasers.forEach(pos2 -> {
            BlockEntity tile2 = WorldUtils.getTileEntity(world, pos2);
            if (tile2 instanceof ToggleableLaserBlockEntity) {
                ((ToggleableLaserBlockEntity) tile2).setLaserActivity(finalLasersStatus, player);
            }
        });
    }

    @Override
    public boolean doesSneakBypassUse(@NotNull ItemStack stack, @NotNull LevelReader world, @NotNull BlockPos pos, @NotNull Player player) {
        return false;
    }

    private void saveLinkedLasers(ItemStack stack, List<BlockPos> linkedLasers) {
        stack.set(RC_BLOCKPOS, linkedLasers);
    }

    private List<BlockPos> getBlockPos(ItemStack stack){
        return new ArrayList<>(Objects.requireNonNull(stack.getComponents().get(RC_BLOCKPOS)));
    }

    private boolean getActive(ItemStack stack){
        return Boolean.TRUE.equals(stack.getComponents().get(RC_ACTIVITY));
    }

}
