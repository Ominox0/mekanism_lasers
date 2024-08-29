package com.folumo.mekanism_lasers.common.block_entity;

import com.folumo.mekanism_lasers.common.item.RemoteControlItem;
import com.folumo.mekanism_lasers.common.registry.BlockRegistry;
import mekanism.api.IContentsListener;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;


public class InterfaceBlockEntity extends TileEntityMekanism {
    private static boolean lastRedstoneLevel;
    public InterfaceBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.INTERFACE_BLOCK, pos, state);
        lastRedstoneLevel = false;
    }

    @Override
    public @NotNull Component getName() {
        return Component.literal("Interface Block");
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);

        InputInventorySlot slot = InputInventorySlot.at(listener, 8, 16);
        builder.addSlot(slot);
        slot.setSlotType(ContainerSlotType.NORMAL);

        return builder.build();
    }

    protected boolean hasNeighborSignal(Level level, BlockPos blockPos) {
        return level.hasSignal(blockPos.north(), Direction.NORTH) ||
                level.hasSignal(blockPos.east(), Direction.EAST) ||
                level.hasSignal(blockPos.south(), Direction.SOUTH) ||
                level.hasSignal(blockPos.west(), Direction.WEST) ||
                level.hasSignal(blockPos.above(), Direction.UP) ||
                level.hasSignal(blockPos.below(), Direction.DOWN);

    }

    @Override
    protected boolean onUpdateServer() {
        boolean ret = super.onUpdateServer();

        boolean thisRedstoneLevel = hasNeighborSignal(getLevel(), getBlockPos());

        if (thisRedstoneLevel != lastRedstoneLevel){
            lastRedstoneLevel = thisRedstoneLevel;
            if (thisRedstoneLevel){
                IInventorySlot slot = getInventorySlot(0, null);
                ItemStack stack = slot.getStack();

                if (!stack.isEmpty()){
                    if (stack.getItem() instanceof RemoteControlItem){
                        RemoteControlItem item = (RemoteControlItem) stack.getItem();

                        ServerLevel serverLevel = (ServerLevel) getLevel();
                        MinecraftServer server = serverLevel.getServer();
                        ServerPlayer player = server.getPlayerList().getPlayer(getOwnerUUID());

                        item.switchLaserMode(stack, getLevel(), player);
                    }
                }
            }
        }

        return ret;
    }

}
