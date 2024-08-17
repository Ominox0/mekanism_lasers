package com.folumo.mekanism_lasers.common.multiblock.validator;

import com.folumo.mekanism_lasers.common.block_entity.EnergyStorageCellBlockEntity;
import com.folumo.mekanism_lasers.common.multiblock.data.energyStorageMultiblockData;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import mekanism.common.content.blocktype.BlockType;
import mekanism.common.content.matrix.MatrixMultiblockData;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.lib.multiblock.FormationProtocol;
import mekanism.common.lib.multiblock.StructureHelper;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import com.folumo.mekanism_lasers.common.registry.BlockTypeRegistry;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;


public class energyStorageMultiblockValidator extends CuboidStructureValidator<energyStorageMultiblockData> {

    private final List<EnergyStorageCellBlockEntity> cells = new ArrayList<>();
    private static final VoxelCuboid MIN_CUBOID = new VoxelCuboid(3, 4, 3);
    private static final VoxelCuboid MAX_CUBOID = new VoxelCuboid(3, 4, 3);

    @Override
    public boolean precheck() {
        cuboid = StructureHelper.fetchCuboid(structure, MIN_CUBOID, MAX_CUBOID, EnumSet.complementOf(EnumSet.of(VoxelCuboid.CuboidSide.TOP)), 8);
        return cuboid != null;
    }

    @Override
    protected FormationProtocol.CasingType getCasingType(BlockState state) {
        Block block = state.getBlock();
        if (BlockType.is(block, BlockTypeRegistry.ENERGY_STORAGE_CASING)) {
            return FormationProtocol.CasingType.FRAME;
        } else if (BlockType.is(block, BlockTypeRegistry.ENERGY_STORAGE_PORT)) {
            return FormationProtocol.CasingType.VALVE;
        }
        return FormationProtocol.CasingType.INVALID;
    }

    @Override
    protected boolean validateInner(BlockState state, Long2ObjectMap<ChunkAccess> chunkMap, BlockPos pos) {
        if (super.validateInner(state, chunkMap, pos)) {
            return true;
        }

        //return BlockType.is(state.getBlock(), BlockType.get((Blocks.AIR)));

        if (BlockType.is(state.getBlock(), BlockTypeRegistry.ENERGY_STORAGE_CELL)) {
            //Compare blocks against the type before bothering to look up the tile
            BlockEntity tile = WorldUtils.getTileEntity(world, chunkMap, pos);
            if (tile instanceof EnergyStorageCellBlockEntity cell) {
                cells.add(cell);
                return true;
            }
            //Else something went wrong
        }
        return false;
    }

    @Override
    public FormationProtocol.FormationResult postcheck(energyStorageMultiblockData structure, Long2ObjectMap<ChunkAccess> chunkMap) {
        for (EnergyStorageCellBlockEntity cell : cells) {
            structure.addCell(cell);
        }

        return FormationProtocol.FormationResult.SUCCESS;
    }
}
