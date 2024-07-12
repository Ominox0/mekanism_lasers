package com.folumo.mekanism_lasers.common.block_entity;

import com.folumo.mekanism_lasers.common.registry.BlockRegistry;
import mekanism.api.IConfigurable;
import mekanism.api.lasers.ILaserReceptor;
import mekanism.api.math.FloatingLong;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.capabilities.resolver.BasicCapabilityResolver;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;


public class LaserStopperBlockEntity extends TileEntityMekanism implements ILaserReceptor, IConfigurable {
    private Item currentItem = Items.AIR;

    public LaserStopperBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.LASER_STOPPER, pos, state);
        addCapabilityResolver(BasicCapabilityResolver.constant(Capabilities.LASER_RECEPTOR, this));

    }

    @Override
    public @NotNull Component getName() {
        return Component.literal("Laser Stopper");
    }

    @Override
    public void receiveLaserEnergy(@NotNull FloatingLong energy) {
    }

    @Override
    public boolean canLasersDig() {
        return false;
    }

    @Override
    public InteractionResult onSneakRightClick(Player player) {
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult onRightClick(Player player) {
        ItemStack mainHandItem = player.getMainHandItem();
        ItemStack offHandItem = player.getOffhandItem();

        if (!mainHandItem.isEmpty()) {
            changeTexture(mainHandItem.getItem());
        } else if (!offHandItem.isEmpty()) {
            changeTexture(offHandItem.getItem());
        }

        return InteractionResult.PASS;
    }

    private void changeTexture(Item item) {
        this.currentItem = item;
        // Update the block state to notify the renderer
        assert this.level != null;
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
    }

    public Item getCurrentItem() {
        return currentItem;
    }


}
