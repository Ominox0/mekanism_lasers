package com.folumo.mekanism_lasers.client.renderer;

import com.folumo.mekanism_lasers.common.block_entity.ODECM_BlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;


public class ODECM_Renderer implements BlockEntityRenderer<ODECM_BlockEntity> {

    private final BlockRenderDispatcher blockRenderer;

    public ODECM_Renderer(BlockEntityRendererProvider.Context context) {
        blockRenderer = context.getBlockRenderDispatcher();
    }


    @Override
    public void render(@NotNull ODECM_BlockEntity blockEntity, float partialTicks,
                       @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource,
                       int packedLight, int combinedOverlay) {

        BlockState state = blockEntity.getItem();
        poseStack.pushPose();

        Level level = blockEntity.getLevel();

        if (level == null){
            return;
        }

        if(state != null) {
            state.getRenderShape();
            blockRenderer.getModelRenderer().tesselateBlock(level, blockRenderer.getBlockModel(state), state, blockEntity.getBlockPos(), poseStack, bufferSource.getBuffer(RenderType.cutoutMipped()), false, RandomSource.create(), state.getSeed(blockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.cutoutMipped());
        } else {
           // blockRenderer.getModelRenderer().tesselateBlock(level, blockRenderer.getBlockModel(blockEntity.getBlockState()), state, blockEntity.getBlockPos(), poseStack, bufferSource.getBuffer(RenderType.cutoutMipped()), false, RandomSource.create(), blockEntity.getBlockState().getSeed(blockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.cutoutMipped());
        }
        poseStack.popPose();
    }

}
