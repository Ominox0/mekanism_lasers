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

import java.util.HashSet;
import java.util.Set;


public class RemoteControlItem extends Item {
    private final Set<ToggleableLaserBlockEntity> linkedLasers = new HashSet<>();
    private boolean lasersStatus = true;
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

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level world = context.getLevel();
        if (!world.isClientSide && player != null) {
            BlockPos pos = context.getClickedPos();

            BlockEntity tile = WorldUtils.getTileEntity(world, pos);

            if (tile instanceof ToggleableLaserBlockEntity){
                if (linkedLasers.contains(tile)){
                    linkedLasers.remove(tile);
                    player.sendSystemMessage(Component.literal("removed"));
                }else {
                    linkedLasers.add((ToggleableLaserBlockEntity) tile);
                    player.sendSystemMessage(Component.literal("added"));
                }
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
        lasersStatus = !lasersStatus;

        linkedLasers.forEach(toggleableLaserBlockEntity -> toggleableLaserBlockEntity.setLaserActivity(lasersStatus));

        player.sendSystemMessage(Component.literal("Laser activity set. (" + lasersStatus +")"));

        return super.use(level, player, interactionHand);
    }
}