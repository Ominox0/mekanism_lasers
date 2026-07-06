package com.folumo.mekanism_lasers.common.item;


import com.folumo.mekanism_lasers.common.block_entity.LaserBlockEntity;
import com.folumo.mekanism_lasers.common.block_entity.ToggleableLaserBlockEntity;
import com.folumo.mekanism_lasers.common.registry.ContainerTypeRegistry;
import mekanism.common.item.interfaces.IGuiItem;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.common.util.WorldUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class LaserConfiguratorItem extends Item implements IGuiItem {

    public LaserConfiguratorItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Player player = context.getPlayer();
        Level world = context.getLevel();
        if (player == null) {
            return InteractionResult.PASS;
        }

        BlockEntity tile = WorldUtils.getTileEntity(world, context.getClickedPos());
        // Open the configurator GUI for any of this mod's lasers (but not mekanism:laser,
        // whose tile is not one of ours).
        if (tile instanceof LaserBlockEntity || tile instanceof ToggleableLaserBlockEntity) {
            if (!world.isClientSide && player instanceof ServerPlayer serverPlayer) {
                ContainerTypeRegistry.LASER_CONFIGURATOR.tryOpenGui(serverPlayer, context.getHand(), context.getItemInHand());
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Override
    public ContainerTypeRegistryObject<?> getContainerType() {
        return ContainerTypeRegistry.LASER_CONFIGURATOR;
    }
}
