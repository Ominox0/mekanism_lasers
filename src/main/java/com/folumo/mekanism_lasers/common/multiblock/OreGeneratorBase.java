package com.folumo.mekanism_lasers.common.multiblock;

import com.folumo.mekanism_lasers.common.registry.BlockRegistry;
import mekanism.common.registries.MekanismBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;



public class OreGeneratorBase {
    protected OreGeneratorBase.Layer[] layers;
    protected String name;
    protected Map<String, Block> keys;
    private final int indexCore;
    private final int index;
    private final GeneratorAbility ability;

    public OreGeneratorBase(String name, OreGeneratorBase.Layer[] layers, Map<String, Block> keys, int indexOfCore, int index, GeneratorAbility ability){
        this.layers = layers;
        this.name = name;
        this.keys = keys;
        this.indexCore = indexOfCore;
        this.index = index;
        this.ability = ability;
    }

    public String getName(){
        return name;
    }

    public GeneratorAbility getAbility(){return ability;}

    private int[] rotate(int x, int z, int rotation) {
        return switch (rotation) {
            case 0 -> new int[]{x, z};
            case 1 -> new int[]{-z, x};
            case 2 -> new int[]{-x, -z};
            case 3 -> new int[]{z, -x};
            default -> throw new IllegalStateException("Unexpected rotation: " + rotation);
        };
    }

    public BuildCheckResult canBuild(BlockPos pos, Level level){
        BuildCheckResult firstFailure = null;
        BlockPos inPower = null, outItem = null;
        BlockPos inFluid = null, outGas = null;

        for (int rotation = 0; rotation < 4; rotation++) {

            boolean valid = true;
            BuildCheckResult failure = null;

            for (int y = 0; y < layers.length; y++) {
                Layer layer = layers[y];

                for (int z = 0; z < layer.layers.length; z++) {
                    String row = layer.layers[z];

                    for (int x = 0; x < row.length(); x++) {
                        char keyChar = row.charAt(x);

                        if (keyChar == ' ') continue;

                        Block expected = keys.get(String.valueOf(keyChar));
                        if (expected == null) continue;

                        int center = row.length() / 2;

                        int localX = x - center;
                        int localZ = z - center;
                        int localY = y - indexCore;

                        int[] rotated = rotate(localX, localZ, rotation);

                        BlockPos checkPos = pos.offset(rotated[0], localY, rotated[1]);
                        Block actual = level.getBlockState(checkPos).getBlock();

                        if (!level.getBlockState(checkPos).is(expected)) {
                            valid = false;

                            if (failure == null) {
                                failure = BuildCheckResult.fail(checkPos, expected, actual);
                            }
                        } else {
                            if (keyChar == 'O'){
                                outItem = checkPos;

                            } else if (keyChar == 'I') {
                                inPower = checkPos;
                            } else if (keyChar == 'F') {
                                inFluid = checkPos;
                            } else if (keyChar == 'G') {
                                outGas = checkPos;
                            }
                        }
                    }
                }
            }

            if (valid) {
                return BuildCheckResult.success(outItem, inPower, inFluid, outGas);
            }

            if (firstFailure == null && failure != null) {
                firstFailure = failure;
            }
        }

        return firstFailure != null ? firstFailure : BuildCheckResult.success(outItem, inPower, inFluid, outGas);
    }

    public int getIndex() {
        return this.index;
    }

    public static class Layer{
        protected int size;
        protected String[] layers;

        public Layer(int size, String[] layers){
            this.size = size;
            this.layers = layers;
        }

    }

    public static class GeneratorAbility{
        public boolean needsPower = false;
        public boolean needsWater = false;
        public boolean outputsItems = false;
        public boolean outputsGas = false;

        public GeneratorAbility withPowerNeed(){needsPower = true; return this;}
        public GeneratorAbility withoutPowerNeed(){needsPower = false; return this;}

        public GeneratorAbility withWaterNeed(){needsWater = true; return this;}
        public GeneratorAbility withoutWaterNeed(){needsWater = false; return this;}

        public GeneratorAbility withItemOutput(){outputsItems = true; return this;}
        public GeneratorAbility withoutItemOutput(){outputsItems = false; return this;}

        public GeneratorAbility withGasOutput(){outputsGas = true; return this;}
        public GeneratorAbility withoutGasOutput(){outputsGas = false; return this;}

    }

    public record BuildCheckResult(boolean success, BlockPos pos, Block expected, Block actual, BlockPos outLocation, BlockPos inPower, BlockPos inFluid, BlockPos outGas) {

        public static BuildCheckResult success(BlockPos outLocation, BlockPos inPower, BlockPos inFluid, BlockPos outGas) {
                return new BuildCheckResult(true, null, null, null, outLocation, inPower, inFluid, outGas);
            }

            public static BuildCheckResult fail(BlockPos pos, Block expected, Block actual) {
                return new BuildCheckResult(false, pos, expected, actual, null, null, null, null);
            }
        }

        // I O F G (Input power, Out item, Fluid input, Gas output)

    public static final OreGeneratorBase GENERATOR_V1 = new OreGeneratorBase(
            "v1",
            new Layer[]{
                    new Layer(3, new String[]{" B ", "ITO", " B "}),
                    new Layer(3, new String[]{"   ", " L ", "   "}),
                    new Layer(3, new String[]{"   ", " C ", "   "}),
            },
            new HashMap<>() {{
                put("B", Blocks.SMOOTH_STONE_SLAB);
                put("I", MekanismBlocks.ULTIMATE_ENERGY_CUBE.get());
                put("T", MekanismBlocks.STEEL_CASING.get());
                put("O", MekanismBlocks.PERSONAL_BARREL.get());
                put("L", MekanismBlocks.SUPERCHARGED_COIL.get());
                put("C", BlockRegistry.ODECM.get());
            }},
            2, 0,
            new GeneratorAbility().withPowerNeed().withItemOutput()
            );

    public static final OreGeneratorBase GENERATOR_V2 = new OreGeneratorBase(
            "v2",
            new Layer[]{
                    new Layer(3, new String[]{"TBT", "B#B", "IBO"}),
                    new Layer(3, new String[]{"V V", " L ", "V V"}),
                    new Layer(3, new String[]{"V V", " C ", "V V"}),
                    new Layer(3, new String[]{"V V", " L ", "V V"}),
                    new Layer(3, new String[]{"TBT", "B#B", "TBT"}),
                    new Layer(3, new String[]{" B ", "BBB", " B "}),
            },

            new HashMap<>(){{
                put("T", MekanismBlocks.STEEL_CASING.get());
                put("B", Blocks.SMOOTH_STONE_SLAB);
                put("V", BlockRegistry.ENERGIZED_FENCE.get());
                put("I", MekanismBlocks.ULTIMATE_ENERGY_CUBE.get());
                put("O", MekanismBlocks.PERSONAL_BARREL.get());
                put("#", Blocks.GOLD_BLOCK);
                put("L", MekanismBlocks.SUPERCHARGED_COIL.get());
                put("C", BlockRegistry.ODECM.get());
            }},
            2, 1,
            new GeneratorAbility().withPowerNeed().withItemOutput()
    );

    public static final OreGeneratorBase GENERATOR_V3 = new OreGeneratorBase(
            "v3",
            new Layer[]{
                    new Layer(5, new String[]{"T B T", " BDB ", "BD#DB", " BDB ", "I B O"}),
                    new Layer(5, new String[]{"V   V", "     ", "  L  ", "     ", "V   V"}),
                    new Layer(5, new String[]{"V   V", "     ", "     ", "     ", "V   V"}),
                    new Layer(5, new String[]{"V   V", "     ", "  C  ", "     ", "V   V"}),
                    new Layer(5, new String[]{"V   V", "     ", "     ", "     ", "V   V"}),
                    new Layer(5, new String[]{"V   V", "     ", "  L  ", "     ", "V   V"}),
                    new Layer(5, new String[]{"T B T", " BDB ", "BD#DB", " BDB ", "T B T"}),
                    new Layer(5, new String[]{"     ", "  B  ", " BBB ", "  B  ", "     "}),
            },
            new HashMap<>(){{
                put("V", BlockRegistry.ENERGIZED_FENCE.get());
                put("T", MekanismBlocks.STEEL_CASING.get());
                put("I", BlockRegistry.ENERGY_TRANSFORMER.get());
                put("O", MekanismBlocks.PERSONAL_BARREL.get());
                put("B", Blocks.SMOOTH_STONE_SLAB);
                put("L", MekanismBlocks.SUPERCHARGED_COIL.get());
                put("C", BlockRegistry.ODECM.get());
                put("D", Blocks.DIAMOND_BLOCK);
                put("#", Blocks.GOLD_BLOCK);
            }},
            3, 2,
            new GeneratorAbility().withPowerNeed().withItemOutput()
    );

    public static final OreGeneratorBase GENERATOR_V4 = new OreGeneratorBase(
            "v4",
            new Layer[]{
                    new Layer(7, new String[]{" BBBBB ", "B11111B", "F1TDT1G", "B1D#D1B", "I1TDT1O", "B11111B", " BBBBB "}),
                    new Layer(7, new String[]{"       ", " SUUUS ", " U   U ", " U S U ", " U   U ", " SUUUS ", "       "}),
                    new Layer(7, new String[]{"       ", " PUUUP ", " U   U ", " U L U ", " U   U ", " PUUUP ", "       "}),
                    new Layer(7, new String[]{"       ", " PUUUP ", " U   U ", " U   U ", " U   U ", " PUUUP ", "       "}),
                    new Layer(7, new String[]{"       ", " PUUUP ", " U   U ", " U C U ", " U   U ", " PUUUP ", "       "}),
                    new Layer(7, new String[]{"       ", " PUUUP ", " U   U ", " U   U ", " U   U ", " PUUUP ", "       "}),
                    new Layer(7, new String[]{"       ", " PUUUP ", " U   U ", " U L U ", " U   U ", " PUUUP ", "       "}),
                    new Layer(7, new String[]{"       ", " SUUUS ", " U   U ", " U S U ", " U   U ", " SUUUS ", "       "}),
                    new Layer(7, new String[]{" BBBBB ", "B11111B", "B1TDT1B", "B1D#D1B", "B1TDT1B", "B11111B", " BBBBB "}),
                    new Layer(7, new String[]{"       ", " B   B ", "  YBY  ", "  BBB  ", "  YBY  ", " B   B ", "       "}),
            },
            new HashMap<>(){{
                put("C", BlockRegistry.ODECM.get());
                put("B", Blocks.SMOOTH_STONE_SLAB);
                put("P", BlockRegistry.ENERGIZED_FENCE.get());
                put("1", Blocks.IRON_BLOCK);
                put("S", MekanismBlocks.REFINED_OBSIDIAN_BLOCK.get());
                put("T", MekanismBlocks.STEEL_CASING.get());
                put("D", Blocks.DIAMOND_BLOCK);
                put("#", Blocks.GOLD_BLOCK);
                put("U", MekanismBlocks.STRUCTURAL_GLASS.get());
                put("L", MekanismBlocks.SUPERCHARGED_COIL.get());

                put("I", BlockRegistry.ENERGY_TRANSFORMER.get());
                put("F", MekanismBlocks.ULTIMATE_FLUID_TANK.get());

                put("O", MekanismBlocks.PERSONAL_BARREL.get());
                put("G", MekanismBlocks.ULTIMATE_CHEMICAL_TANK.get());

            }},
            4, 3,
            new GeneratorAbility().withPowerNeed().withItemOutput().withWaterNeed().withGasOutput()
    );

    public static final Map<Integer, OreGeneratorBase> MAPPED = Map.of(
            0, GENERATOR_V1,
            1, GENERATOR_V2,
            2, GENERATOR_V3,
            3, GENERATOR_V4
    );
}
