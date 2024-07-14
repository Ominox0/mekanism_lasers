package com.folumo.mekanism_lasers.client.renderer;

import com.folumo.mekanism_lasers.common.block_entity.LaserStopperBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LaserStopperRenderer implements BlockEntityRenderer<LaserStopperBlockEntity> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LaserStopperRenderer.class);
    public LaserStopperRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(LaserStopperBlockEntity blockEntity, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int combinedOverlay) {
        BlockState state = blockEntity.getCurrentBlockState();
        LOGGER.info("CHANGING STATE OF BLOCK !!!");
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        poseStack.pushPose();
        //float rot = (float) Math.toRadians(180 - getFacing(animatable).toYRot());

        //poseStack.translate(0.5, 0, 0.5);
        //poseStack.mulPose(Axis.YP.rotation(45)); // 45 -> rot
        //poseStack.translate(-0.5, 0, -0.5);

        if(!state.isAir()) {
            poseStack.pushPose();
            poseStack.translate(0.1F, 0.1F, 0.1F);
            blockRenderer.renderSingleBlock(state, poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.SOLID);
            poseStack.popPose();
        }
        poseStack.popPose();
    }

}
