package com.folumo.mekanism_lasers.client.renderer;

import com.folumo.mekanism_lasers.common.block_entity.LaserStopperBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class LaserStopperRenderer implements BlockEntityRenderer<LaserStopperBlockEntity> {
    public LaserStopperRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(LaserStopperBlockEntity blockEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Item currentItem = blockEntity.getCurrentItem();
        if (currentItem == null) {
            return;
        }

        Block block = Block.byItem(currentItem);
        BlockState blockState = block.defaultBlockState();
        ResourceLocation texture = getTexturePathFromBlock(blockState);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entitySolid(texture));

        // Render the quad
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0.5, 0.5); // Center the quad
        renderQuad(matrixStack, vertexConsumer, combinedLight, combinedOverlay);
        matrixStack.popPose();
    }

    private ResourceLocation getTexturePathFromBlock(BlockState blockState) {
        return Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState).getParticleIcon().atlasLocation();
    }

    private void renderQuad(PoseStack matrixStack, VertexConsumer vertexConsumer, int combinedLight, int combinedOverlay) {
        Vec3[] vertices = new Vec3[]{
                new Vec3(-0.5, -0.5, 0),
                new Vec3(0.5, -0.5, 0),
                new Vec3(0.5, 0.5, 0),
                new Vec3(-0.5, 0.5, 0)
        };

        float[][] uvs = new float[][]{
                {0.0f, 0.0f},
                {1.0f, 0.0f},
                {1.0f, 1.0f},
                {0.0f, 1.0f}
        };
    }
}
