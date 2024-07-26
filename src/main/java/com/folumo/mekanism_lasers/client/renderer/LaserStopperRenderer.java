package com.folumo.mekanism_lasers.client.renderer;

import com.folumo.mekanism_lasers.common.block_entity.LaserStopperBlockEntity;
import com.folumo.mekanism_lasers.common.registry.BlockRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LaserStopperRenderer implements BlockEntityRenderer<LaserStopperBlockEntity> {
    public LaserStopperRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public boolean shouldRender(LaserStopperBlockEntity p_173568_, Vec3 p_173569_) {
        return BlockEntityRenderer.super.shouldRender(p_173568_, p_173569_);
    }

    protected void renderDefault(boolean hasTexture, LaserStopperBlockEntity blockEntity, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int combinedOverlay) {
        poseStack.pushPose();
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        BlockState state = BlockRegistry.LASER_STOPPER.defaultState();
        //poseStack.translate(0.5, 0, 0.5);
        //poseStack.mulPose(Axis.YP.rotation((float) Math.toRadians(180 - getFacing(animatable).toYRot())));
        //poseStack.translate(-0.5, 0, -0.5);
        if (hasTexture){
            blockRenderer.getModelRenderer().tesselateBlock(Objects.requireNonNull(blockEntity.getLevel()), blockRenderer.getBlockModel(state), state, blockEntity.getBlockPos(), poseStack, bufferSource.getBuffer(RenderType.cutoutMipped()), false, RandomSource.create(), state.getSeed(blockEntity.getBlockPos()), combinedOverlay, ModelData.EMPTY, RenderType.SOLID);
        }
        poseStack.popPose();
    }

    protected void renderNew(LaserStopperBlockEntity blockEntity, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int combinedOverlay){
        BlockState state = blockEntity.getCurrentBlockState();
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        poseStack.pushPose();

        if(!state.isAir()) {
            blockRenderer.renderSingleBlock(state, poseStack, bufferSource, packedLight, combinedOverlay, ModelData.EMPTY, RenderType.SOLID);
        }
        poseStack.popPose();
    }

    @Override
    public void render(LaserStopperBlockEntity blockEntity, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int combinedOverlay) {
        boolean hasTexture = blockEntity.getHasTexture();
        renderDefault(hasTexture, blockEntity, partialTicks, poseStack, bufferSource, packedLight, combinedOverlay);
        renderNew(blockEntity, partialTicks, poseStack, bufferSource, packedLight, combinedOverlay);



    }

}
