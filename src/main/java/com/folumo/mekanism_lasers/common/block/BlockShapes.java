package com.folumo.mekanism_lasers.common.block;

import mekanism.common.util.EnumUtils;
import mekanism.common.util.VoxelShapeUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockShapes {
    private static VoxelShape box(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return Block.box(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static VoxelShape[] LASER_STOPPER_SHAPE = new VoxelShape[EnumUtils.DIRECTIONS.length];

    public static VoxelShape[] ORE_GENERATOR_SHAPE = new VoxelShape[EnumUtils.DIRECTIONS.length];


    static {
        VoxelShapeUtils.setShape(VoxelShapeUtils.combine(
                box(0, 0, 0, 1, 1, 16),
                box(1, 15, 0, 15, 16, 1),
                box(15, 15, 0, 16, 16, 16),
                box(1, 15, 15, 15, 16, 16),
                box(0, 15, 0, 1, 16, 16),
                box(15, 0, 0, 16, 1, 16),
                box(1, 0, 0, 15, 1, 1),
                box(1, 0, 15, 15, 1, 16),
                box(0, 1, 15, 1, 15, 16),
                box(15, 1, 15, 16, 15, 16),
                box(15, 1, 0, 16, 15, 1),
                box(0, 1, 0, 1, 15, 1),
                box(5, 5, 5, 11, 11, 11)

        ), LASER_STOPPER_SHAPE, true, true);

        VoxelShapeUtils.setShape(VoxelShapeUtils.combine(
            box(0, 0, 0, 16, 2, 16),
                box(0, 2, 0, 2, 14, 14),
                box(14, 2, 0, 16, 14, 14),
                box(2, 2, 0, 14, 16, 2),
                box(6, 6, 4, 10, 10, 8),
                box(2, 2, 12, 14, 3, 13),
                box(2, 12, 6, 14, 13, 10),
                box(2, 13, 2, 14, 14, 6),
                box(2, 8, 10, 14, 12, 11),
                box(2, 3, 11, 14, 8, 12),
                box(14, 14, 0, 16, 16, 8),
                box(0, 14, 0, 2, 16, 8)

        ), ORE_GENERATOR_SHAPE, true, true);
    }
}




