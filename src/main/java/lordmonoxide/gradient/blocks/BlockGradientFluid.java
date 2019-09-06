package lordmonoxide.gradient.blocks;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockGradientFluid extends BlockFluidClassic {
  public BlockGradientFluid(final Fluid fluid) {
    super(fluid, Material.WATER);
    this.setRegistryName(fluid.getUnlocalizedName());
    this.setTranslationKey(fluid.getUnlocalizedName());
  }
}
