package lordmonoxide.gradient;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.PushReaction;

public final class GradientMaterials {
  private GradientMaterials() { }

  public static final Material MATERIAL_CLAY_MACHINE   = new Material(MaterialColor.BROWN, false, true, true, true, false, false, false, PushReaction.BLOCK);
  public static final Material MATERIAL_BRONZE_MACHINE = new Material(MaterialColor.GOLD, false, true, true, true, false, false, false, PushReaction.BLOCK);
}
