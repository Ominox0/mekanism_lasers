package com.folumo.mekanism_lasers.client.renderer;

import com.folumo.mekanism_lasers.common.block_entity.ODECM_BlockEntity;
import com.folumo.mekanism_lasers.common.multiblock.OreGeneratorBase;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


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
        // Render the selected ore block. The pose stack MUST be left balanced even if
        // tesselateBlock throws, otherwise LevelRenderer.checkPoseStack crashes the game
        // with "Pose stack not empty" on the next frame (see crash-2026-07-05).
        BlockState state = blockEntity.getItem();
        Level level = blockEntity.getLevel();
        if (level != null && state != null) {
            poseStack.pushPose();
            try {
                poseStack.translate(0.5, 1.2, 0.5);
                poseStack.scale(0.5F, 0.5F, 0.5F);
                poseStack.translate(-0.5, -0.5, -0.5);

                blockRenderer.getModelRenderer().tesselateBlock(
                    level,
                    blockRenderer.getBlockModel(state),
                    state,
                    blockEntity.getBlockPos(),
                    poseStack,
                    bufferSource.getBuffer(RenderType.cutoutMipped()),
                    false,
                    RandomSource.create(),
                    state.getSeed(blockEntity.getBlockPos()),
                    OverlayTexture.NO_OVERLAY,
                    ModelData.EMPTY,
                    RenderType.cutoutMipped()
                );
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                poseStack.popPose();
            }
        }
    }

    private boolean isHoldingMekanismConfigurator(ItemStack stack) {
        if (stack.isEmpty()) return false;
        // Check if item is Mekanism Configurator by registry name
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        return id != null && id.getNamespace().equals("mekanism") && id.getPath().equals("configurator");
    }

    private void renderMultiblockHolograms(OreGeneratorBase generator, BlockPos centerPos, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks, Level level) {
        List<BlockPosAndState> allBlocks = new ArrayList<>();
        Set<BlockPos> addedPositions = new HashSet<>();
        Set<BlockPos> missingPositions = new HashSet<>();

        // Get all positions from the generator structure (only rotation 0 to avoid duplicates)
        OreGeneratorBase.Layer[] layers = generator.layers;
        Map<String, Block> keys = generator.keys;

        for (int y = 0; y < layers.length; y++) {
            OreGeneratorBase.Layer layer = layers[y];
            for (int z = 0; z < layer.layers.length; z++) {
                String row = layer.layers[z];
                for (int x = 0; x < row.length(); x++) {
                    char keyChar = row.charAt(x);
                    if (keyChar == ' ') continue;

                    Block expectedBlock = keys.get(String.valueOf(keyChar));
                    if (expectedBlock == null) continue;

                    int center = row.length() / 2;
                    int localX = x - center;
                    int localZ = z - center;
                    int localY = y - generator.indexCore;

                    int[] rotated = rotate(localX, localZ, 0);
                    BlockPos checkPos = centerPos.offset(rotated[0], localY, rotated[1]);

                    if (addedPositions.contains(checkPos)) continue;
                    addedPositions.add(checkPos);

                    BlockState expectedState = expectedBlock.defaultBlockState();
                    allBlocks.add(new BlockPosAndState(checkPos, expectedState));

                    if (!level.getBlockState(checkPos).is(expectedBlock)) {
                        missingPositions.add(checkPos);
                    }
                }
            }
        }

        // Render all missing blocks as holograms
        Vec3 camera = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        VertexConsumer builder = bufferSource.getBuffer(RenderType.lines());

        for (BlockPosAndState entry : allBlocks) {
            BlockPos pos = entry.pos;
            BlockState blockState = entry.state;

            poseStack.pushPose();
            poseStack.translate(pos.getX() - camera.x, pos.getY() - camera.y, pos.getZ() - camera.z);

            // Draw outline
            AABB aabb = new AABB(0, 0, 0, 1, 1, 1);
            LevelRenderer.renderLineBox(poseStack, builder, aabb, 0.0F, 1.0F, 0.0F, 0.4F);

            // Render semi-transparent block
            if (missingPositions.contains(pos)) {
                renderTransparentBlock(poseStack, bufferSource, blockState, 0.3F);
            }

            poseStack.popPose();
        }
    }
    
    private void renderTransparentBlock(PoseStack poseStack, MultiBufferSource bufferSource, BlockState state, float alpha) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        
        blockRenderer.getModelRenderer().tesselateBlock(
            level,
            blockRenderer.getBlockModel(state),
            state,
            BlockPos.ZERO,
            poseStack,
            bufferSource.getBuffer(RenderType.translucent()),
            false,
            RandomSource.create(),
            0L,
            OverlayTexture.NO_OVERLAY,
            ModelData.EMPTY,
            RenderType.translucent()
        );
    }

    private int[] rotate(int x, int z, int rotation) {
        return switch (rotation) {
            case 0 -> new int[]{x, z};
            case 1 -> new int[]{-z, x};
            case 2 -> new int[]{-x, -z};
            case 3 -> new int[]{z, -x};
            default -> throw new IllegalStateException("Unexpected rotation: " + rotation);
        };
    }
    
    private record BlockPosAndState(BlockPos pos, BlockState state) {}
}
