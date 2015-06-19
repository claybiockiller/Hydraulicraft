package k4unl.minecraft.Hydraulicraft.api.recipes;

import k4unl.minecraft.Hydraulicraft.lib.recipes.IFluidInventory;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.List;

public class FluidShapelessOreRecipe extends ShapelessOreRecipe implements IFluidRecipe {
    List<FluidStack> inputFluids;
    List<FluidStack> outputFluids;
    int   craftingTime = 1;
    float pressure     = 0.1f;

    public FluidShapelessOreRecipe(Block result, Object... recipe) {
        super(result, recipe);
    }

    public FluidShapelessOreRecipe(Item result, Object... recipe) {
        super(result, recipe);
    }

    public FluidShapelessOreRecipe(ItemStack result, Object... recipe) {
        super(result, recipe);
    }

    @Override
    public FluidShapelessOreRecipe addFluidInput(FluidStack fluidStack) {
        if (inputFluids == null)
            inputFluids = new ArrayList<FluidStack>();

        inputFluids.add(fluidStack);

        return this;
    }

    @Override
    public FluidShapelessOreRecipe addFluidOutput(FluidStack fluidStack) {
        if (outputFluids == null)
            outputFluids = new ArrayList<FluidStack>();

        outputFluids.add(fluidStack);
        return this;
    }

    @Override
    public List<FluidStack> getInputFluids() {
        if (inputFluids == null)
            inputFluids = new ArrayList<FluidStack>();

        return inputFluids;
    }

    @Override
    public List<FluidStack> getOutputFluids() {
        if (outputFluids == null)
            outputFluids = new ArrayList<FluidStack>();

        return outputFluids;
    }

    @Override
    public boolean matches(IFluidInventory inventory) {
        if (!super.matches(inventory.getInventoryCrafting(), null))
            return false;

        int fluidsMatched = 0;
        if (inputFluids != null) {
            for (FluidStack fluid : getInputFluids()) {
                FluidStack drained = inventory.craftingDrain(fluid, false);
                if (drained != null && drained.amount == fluid.amount)
                    fluidsMatched++;
            }

            if (fluidsMatched != getInputFluids().size())
                return false;
        }

        fluidsMatched = 0;
        if (outputFluids != null) {
            for (FluidStack fluidStack : getOutputFluids()) {
                if (inventory.craftingFill(fluidStack, false) == fluidStack.amount)
                    fluidsMatched++;
            }

            return fluidsMatched == getOutputFluids().size();
        }

        return true;
    }

    @Override
    public int getCraftingTime() {
        return craftingTime;
    }

    public FluidShapelessOreRecipe setCraftingTime(int craftingTime) {
        this.craftingTime = craftingTime;
        return this;
    }

    @Override
    public float getPressure() {
        return pressure;
    }

    public FluidShapelessOreRecipe setPressure(float pressure) {
        this.pressure = pressure;
        return this;
    }

    @Override
    public Object[] getInputItems() {
        return super.getInput().toArray(new Object[super.getInput().size()]);
    }
}