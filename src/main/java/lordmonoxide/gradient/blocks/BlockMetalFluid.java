package lordmonoxide.gradient.blocks;

import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockMetalFluid extends BlockFluidClassic {
  public BlockMetalFluid(final Fluid fluid) {
    super(fluid, GradientBlocks.MATERIAL_LIQUID_METAL);
    this.setRegistryName(fluid.getUnlocalizedName());
    this.setUnlocalizedName(fluid.getUnlocalizedName());
    fluid.setBlock(this);
  }
}
