package lordmonoxide.gradient.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;

public class FluidHandlerIngredient extends Ingredient {
  private final FluidStack valid;

  protected FluidHandlerIngredient(final FluidStack valid) {
    super(FluidUtil.getFilledBucket(valid));
    this.valid = valid;
  }

  @Override
  public boolean apply(@Nullable final ItemStack input) {
    if(input == null) {
      return false;
    }

    final FluidStack fluid = FluidUtil.getFluidContained(input);

    return fluid != null && fluid.containsFluid(this.valid);
  }

  @Override
  public boolean isSimple() {
    return false;
  }
}
