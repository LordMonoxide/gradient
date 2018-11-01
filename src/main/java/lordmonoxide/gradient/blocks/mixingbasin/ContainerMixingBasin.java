package lordmonoxide.gradient.blocks.mixingbasin;

import lordmonoxide.gradient.progress.Age;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class ContainerMixingBasin extends Container {
  private Age playerAge = Age.AGE1;

  @Nullable
  private FluidStack fluid;

  public void setPlayerAge(final Age age) {
    this.playerAge = age;
  }

  public Age getPlayerAge() {
    return this.playerAge;
  }

  public void setFluid(@Nullable final FluidStack fluid) {
    this.fluid = fluid;
  }

  @Nullable
  public FluidStack getFluid() {
    return this.fluid;
  }

  @Override
  public boolean canInteractWith(final EntityPlayer player) {
    return true;
  }
}
