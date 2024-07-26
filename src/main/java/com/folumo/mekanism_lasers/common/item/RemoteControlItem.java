package com.folumo.mekanism_lasers.common.item;

import com.folumo.mekanism_lasers.common.block_entity.ToggleableLaserBlockEntity;
import mekanism.api.text.EnumColor;
import mekanism.api.text.TextComponentUtil;
import mekanism.common.registries.MekanismDataComponents;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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

import java.util.HashSet;
import java.util.Set;

public class RemoteControlItem extends Item {

    public RemoteControlItem(Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON).stacksTo(1)
                .component(MekanismDataComponents.CONFIGURATOR_MODE, mekanism.common.item.ItemConfigurator.ConfiguratorMode.CONFIGURATE_ITEMS)
        );
    }

    @NotNull
    @Override
    public Component getName(@NotNull ItemStack stack) {
        return TextComponentUtil.build(EnumColor.AQUA, super.getName(stack));
    }

    //@NotNull
    //@Override
    public InteractionResult useOn1(UseOnContext context) {
        Player player = context.getPlayer();
        Level world = context.getLevel();
        ItemStack stack = context.getItemInHand();
        if (!world.isClientSide && player != null) {
            BlockPos pos = context.getClickedPos();

            BlockEntity tile = WorldUtils.getTileEntity(world, pos);

            if (tile instanceof ToggleableLaserBlockEntity) {
                Set<BlockPos> linkedLasers = getLinkedLasers(stack);
                if (linkedLasers.contains(pos)) {
                    linkedLasers.remove(pos);
                    player.sendSystemMessage(Component.literal("removed"));
                } else {
                    linkedLasers.add(pos);
                    player.sendSystemMessage(Component.literal("added"));
                }
                saveLinkedLasers(stack, linkedLasers);
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean doesSneakBypassUse(@NotNull ItemStack stack, @NotNull LevelReader world, @NotNull BlockPos pos, @NotNull Player player) {
        return false;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        if (!level.isClientSide) {
            ItemStack stack = player.getItemInHand(interactionHand);
            boolean lasersStatus = getLaserStatus(stack);
            lasersStatus = !lasersStatus;
            setLaserStatus(stack, lasersStatus);

            Set<BlockPos> linkedLasers = getLinkedLasers(stack);
            boolean finalLasersStatus = lasersStatus;
            linkedLasers.forEach(pos -> {
                BlockEntity tile = WorldUtils.getTileEntity(level, pos);
                if (tile instanceof ToggleableLaserBlockEntity) {
                    ((ToggleableLaserBlockEntity) tile).setLaserActivity(finalLasersStatus, player);
                }
            });

            player.sendSystemMessage(Component.literal("Laser activity set. (" + lasersStatus + ")"));
        }
        return super.use(level, player, interactionHand);
    }

    private Set<BlockPos> getLinkedLasers(ItemStack stack) {
        CompoundTag tag = getTag(stack);
        Set<BlockPos> linkedLasers = new HashSet<>();
        if (tag.contains("LinkedLasers")) {
            ListTag listTag = tag.getList("LinkedLasers", Tag.TAG_COMPOUND);
            listTag.forEach(tag1 -> linkedLasers.add(BlockPos.of(((CompoundTag) tag1).getLong("Pos"))));
        }
        return linkedLasers;
    }

    private void saveLinkedLasers(ItemStack stack, Set<BlockPos> linkedLasers) {
        ListTag listTag = new ListTag();
        linkedLasers.forEach(pos -> {
            CompoundTag tag = new CompoundTag();
            tag.putLong("Pos", pos.asLong());
            listTag.add(tag);
        });
        getTag(stack).put("LinkedLasers", listTag);
    }

    private boolean getLaserStatus(ItemStack stack) {
        CompoundTag tag = getTag(stack);
        if (tag.contains("LaserStatus")) {
            return tag.getBoolean("LaserStatus");
        }
        return true;
    }

    private void setLaserStatus(ItemStack stack, boolean status) {
        getTag(stack).putBoolean("LaserStatus", status);
    }

    private CompoundTag getTag(ItemStack stack) {
        return new CompoundTag();
    }
}
