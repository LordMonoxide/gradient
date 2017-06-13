package lordmonoxide.gradient.items;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class BoneNeedle extends GradientItemTool implements GradientItemCraftable {
  public BoneNeedle() {
    super("bone_needle", 0, 0, 0);
    this.setMaxDamage(19);
  }
  
  @Override
  public void addRecipe() {
    GameRegistry.addShapelessRecipe(
      new ItemStack(this, 4),
      Items.BONE,
      new ItemStack(GradientItems.STONE_HAMMER, 1, OreDictionary.WILDCARD_VALUE)
    );
  }
}
