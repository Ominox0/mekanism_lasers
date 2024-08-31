package com.folumo.mekanism_lasers.common.item;


import com.folumo.mekanism_lasers.common.block_entity.LaserBlockEntity;
import mekanism.common.item.interfaces.IGuiItem;
import mekanism.common.lib.security.ItemSecurityUtils;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
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

    //@NotNull
    //@Override
    //protected IInventorySlotHolder addS(IContentsListener listener) {
    //    InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);

    //    InputInventorySlot slot = InputInventorySlot.at(listener, 8, 16);
    //    builder.addSlot(slot);
    //    slot.setSlotType(ContainerSlotType.NORMAL);

    //    return builder.build();
    //}

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Player player = context.getPlayer();
        Level world = context.getLevel();


        if (!world.isClientSide && player != null) {
            BlockPos pos = context.getClickedPos();

            BlockEntity tile = WorldUtils.getTileEntity(world, pos);

            if (tile instanceof LaserBlockEntity){
                return ItemSecurityUtils.get().claimOrOpenGui(world, player, context.getHand(), getContainerType()::tryOpenGui).getResult();
            }

        }
        return InteractionResult.PASS;
    }

    @Override
    public ContainerTypeRegistryObject<?> getContainerType() {
        return null;
    }
}
