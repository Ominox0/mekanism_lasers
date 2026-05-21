package com.folumo.mekanism_lasers.common.block_entity;

import com.folumo.mekanism_lasers.Config;
import com.folumo.mekanism_lasers.common.multiblock.OreGeneratorBase;
import com.folumo.mekanism_lasers.common.registry.BlockRegistry;
import com.folumo.mekanism_lasers.common.registry.DamageTypeReg;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IConfigurable;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalTank;
import mekanism.common.registries.MekanismChemicals;
import mekanism.common.tile.TileEntityChemicalTank;
import mekanism.common.tile.TileEntityEnergyCube;
import mekanism.common.tile.TileEntityFluidTank;
import mekanism.common.tile.TileEntityPersonalBarrel;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ODECM_BlockEntity extends TileEntityMekanism implements IConfigurable {

    public ODECM_BlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegistry.ODECM, pos, state);
        maxProgress = Config.ODECM_Progress.getAsInt();
    }

    protected OreGeneratorBase typeV = OreGeneratorBase.GENERATOR_V1;
    protected Block item;

    private int structureCooldown = 0;
    private OreGeneratorBase.BuildCheckResult cachedStructureValid = null;

    private int progress = 0;
    private final int maxProgress;

    private BlockPos chestOut = null, powerIn = null, fluidIn = null, gasOut = null;

    private boolean doExplode = false;

    public void nextVe(){
        int id = typeV.getIndex();
        id++;

        if (OreGeneratorBase.MAPPED.containsKey(id)){
            typeV = OreGeneratorBase.MAPPED.get(id);
        } else {
            typeV = OreGeneratorBase.MAPPED.get(0);
        }
    }

    public OreGeneratorBase getVType(){
        if (typeV == null){
            return OreGeneratorBase.GENERATOR_V1;
        }

        return typeV;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.saveAdditional(nbt, provider);
        nbt.putInt("targeted_generator_id", typeV.getIndex());

        if (item != null) {
            nbt.putString("targeted_mining_block",
                    BuiltInRegistries.BLOCK.getKey(item).toString());
        }

        nbt.putInt("targeted_progress", progress);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag nbt, HolderLookup.@NotNull Provider provider) {
        super.loadAdditional(nbt, provider);

        int id = nbt.getInt("targeted_generator_id");
        OreGeneratorBase loaded = OreGeneratorBase.MAPPED.get(id);

        if (loaded == null) {
            loaded = OreGeneratorBase.GENERATOR_V1;
        }

        this.typeV = loaded;

        if (nbt.contains("targeted_mining_block")) {
            Block loadedBlock = BuiltInRegistries.BLOCK.get(
                    ResourceLocation.parse(nbt.getString("targeted_mining_block"))
            );
            this.item = (loadedBlock == Blocks.AIR) ? null : loadedBlock;
        }

        this.progress = nbt.getInt("targeted_progress");
    }

    public int getProgress() {
        return progress;
    }

    public void setItem(Block item) {
        this.item = item;
    }

    public BlockState getItem() {
        return item == null ? null : item.defaultBlockState();
    }

    protected boolean hasItem() {
        return item != null;
    }

    private long getEnergyCost(Block block) {
        // will change it to varie
        return Config.ODECM_TickCost.get();
    }

    protected boolean isStillBuilt() {
        if (typeV == null) {
            typeV = OreGeneratorBase.GENERATOR_V1;
            return false;
        }

        if (structureCooldown-- <= 0 || cachedStructureValid == null) {
            cachedStructureValid = typeV.canBuild(getBlockPos(), getLevel());
            structureCooldown = 20;

            if (cachedStructureValid.success()) {
                this.chestOut = cachedStructureValid.outLocation();
                this.powerIn = cachedStructureValid.inPower();

                this.fluidIn = cachedStructureValid.inFluid();
                this.gasOut = cachedStructureValid.outGas();
            } else {
                this.chestOut = null;
                this.powerIn = null;

                this.fluidIn = null;
                this.gasOut = null;
            }
        }

        return cachedStructureValid.success();
    }

    protected boolean simulateAndReduce(TileEntityEnergyCube energy, long costPerTick) {
        if (energy.getEnergyContainer().extract(costPerTick, Action.SIMULATE, AutomationType.INTERNAL) >= costPerTick) {
            energy.getEnergyContainer().extract(costPerTick, Action.EXECUTE, AutomationType.INTERNAL);

            return true;
        }
        return false;
    }

    protected boolean simulateAndReduce(EnergyTransformerBlockEntity energy, long costPerTick) {
        if (energy.getEnergyContainer().extract(costPerTick, Action.SIMULATE, AutomationType.INTERNAL) >= costPerTick) {
            energy.getEnergyContainer().extract(costPerTick, Action.EXECUTE, AutomationType.INTERNAL);

            return true;
        }
        return false;
    }

    protected boolean tickPower(OreGeneratorBase.GeneratorAbility ability, long costPerTick){
        boolean canGo = false;

        if (ability.needsPower){
            TileEntityMekanism be = WorldUtils.getTileEntity(TileEntityMekanism.class, level, powerIn);

            switch (be) {
                case null -> {
                    return false;
                }
                case TileEntityEnergyCube energy -> canGo = simulateAndReduce(energy, costPerTick);
                case EnergyTransformerBlockEntity energy -> canGo = simulateAndReduce(energy, costPerTick);
                default -> {
                }
            }

        } else {
            canGo = true;
        }

        return canGo;
    }

    protected boolean tickFluid(OreGeneratorBase.GeneratorAbility ability, int costPerTick, boolean pastResult){
        boolean canGo = false;

        if (ability.needsWater){
            TileEntityFluidTank tank = WorldUtils.getTileEntity(TileEntityFluidTank.class, level, fluidIn);
            if (tank == null){return false;}

            if (tank.fluidTank.extract(costPerTick, Action.SIMULATE, AutomationType.INTERNAL).getAmount() >= costPerTick){
                tank.fluidTank.extract(costPerTick, Action.EXECUTE, AutomationType.INTERNAL);
                canGo = true;
            } else {
                if (pastResult){
                    doExplode = true;
                }
            }

        } else {
            canGo = true;
        }

        return canGo;
    }

    protected boolean tickGas(OreGeneratorBase.GeneratorAbility ability, int costPerTick){
        boolean canGo = false;

        if (ability.outputsGas){
            TileEntityChemicalTank tank = WorldUtils.getTileEntity(TileEntityChemicalTank.class, level, gasOut);

            if (tank == null){return false;}

           IChemicalTank chemicalTank = tank.getChemicalTank();
           ChemicalStack stack = MekanismChemicals.STEAM.asStack(costPerTick);

           if (chemicalTank.insert(stack, Action.SIMULATE, AutomationType.INTERNAL).getAmount() <= 0){
               chemicalTank.insert(stack, Action.EXECUTE, AutomationType.INTERNAL);
               canGo = true;

           } else {
               doExplode = true;
           }

        } else {
            canGo = true;
        }

        return canGo;
    }

    protected void tickExplode(){
        if (Config.ODECM_CanExplode.get() && doExplode){
            BlockPos pos = getBlockPos();

            Level world = getLevel();

            if (world != null){
                level.explode(null,
                        null, //DamageTypeReg.ODECM.source(world),
                        null,
                        pos.getX(),
                        pos.getY(),
                        pos.getZ(),
                        20.0F,
                        false,
                        Level.ExplosionInteraction.BLOCK
                );
            }
        }
    }

    protected void tickGenerated() {
        if (item == null) return;
        OreGeneratorBase.GeneratorAbility ability = typeV.getAbility();

        float amount = Config.ODECM_Generator.get().get(typeV.getIndex());
        long costPerTick = (long) (getEnergyCost(item) * amount);

        boolean doPower = tickPower(ability, costPerTick);
        boolean doFluid = tickFluid(ability, (int) (costPerTick / 20), doPower);

        boolean doOutputGas = tickGas(ability, (int) (costPerTick / 20));

        tickExplode();

        if (doPower && doFluid && doOutputGas) {

            progress += Config.ODECM_ProgressPerTick.getAsInt();

            if (progress >= maxProgress / amount) {
                if (ability.outputsItems){
                    generateOutput();
                }
                progress = 0;
            }
        }
    }

    private void generateOutput() {
        if (chestOut == null || item == null) return;

        TileEntityPersonalBarrel barrel = WorldUtils.getTileEntity(TileEntityPersonalBarrel.class, level, chestOut);

        if (barrel == null) return;

        var inventory = barrel.getInventorySlots(Direction.UP);

        ItemStack stack = new ItemStack(item.asItem(), Config.ODECM_OutputAmount.getAsInt());

        for (mekanism.api.inventory.IInventorySlot slot : inventory) {
            if (slot.isEmpty()) {
                slot.setStack(stack.copy());
                return;
            } else if (slot.getStack().getItem() == stack.getItem()
                    && slot.getStack().getCount() < slot.getStack().getMaxStackSize()) {

                slot.getStack().grow(stack.getCount());
                return;
            }
        }
        // full → do nothing
    }

    @Override
    protected boolean onUpdateServer() {
        int oldProgress = progress;

        if (isStillBuilt() && hasItem()) {
            tickGenerated();
        } else {
            progress = 0;
        }

        return oldProgress != progress;
    }

    @Override
    public InteractionResult onSneakRightClick(Player player) {
        this.nextVe();

        String type = this.getVType().getName();
        player.sendSystemMessage(Component.literal("ODECM now uses " + type));

        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult onRightClick(Player player) {
        return InteractionResult.PASS;
    }
}