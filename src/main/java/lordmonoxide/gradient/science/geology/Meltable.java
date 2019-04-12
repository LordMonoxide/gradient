package lordmonoxide.gradient.science.geology;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class Meltable {
  public final float meltTime;
  public final float meltTemp;
  public final String fluid;
  public final int amount;

  public Meltable(final float meltTime, final float meltTemp, final String fluid, final int amount) {
    this.meltTime = meltTime;
    this.meltTemp = meltTemp;
    this.fluid    = fluid;
    this.amount   = amount;
  }

  public Fluid getFluid() {
    return FluidRegistry.getFluid(this.fluid);
  }
}
