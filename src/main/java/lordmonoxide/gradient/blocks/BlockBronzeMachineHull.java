package lordmonoxide.gradient.blocks;

import net.minecraft.creativetab.CreativeTabs;

public class BlockBronzeMachineHull extends GradientBlock {
  public BlockBronzeMachineHull() {
    super("bronze_machine_hull", CreativeTabs.MATERIALS, GradientBlocks.MATERIAL_BRONZE_MACHINE);
    this.setResistance(5.0f);
    this.setHardness(1.0f);
  }
}
