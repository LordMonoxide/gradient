package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientMaterials;
import net.minecraft.block.Block;

public class BlockBronzeMachineHull extends Block {
  public BlockBronzeMachineHull() {
    super(Properties.create(GradientMaterials.MATERIAL_BRONZE_MACHINE).hardnessAndResistance(0.1f, 0.5f));
  }
}
