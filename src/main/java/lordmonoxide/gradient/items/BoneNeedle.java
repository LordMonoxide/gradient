package lordmonoxide.gradient.items;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class BoneNeedle extends GradientItemTool implements GradientItemCraftable {
  public BoneNeedle() {
    super("bone_needle", 0, 0, 0);
    this.setMaxDamage(19);
  }
  
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ShapelessOreRecipe(
      this.getItemStack(4),
      "bone",
      "toolHammer"
    ));
  }
}
