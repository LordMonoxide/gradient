package lordmonoxide.gradient;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public final class GradientMaterials {
  private GradientMaterials() { }

  public static final Material MATERIAL_CLAY_MACHINE   = new Material(MaterialColor.BROWN, false, true, true, true, false, false, false, EnumPushReaction.BLOCK);
  public static final Material MATERIAL_BRONZE_MACHINE = new Material(MaterialColor.GOLD, false, true, true, true, false, false, false, EnumPushReaction.BLOCK);
}
