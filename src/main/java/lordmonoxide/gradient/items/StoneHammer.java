package lordmonoxide.gradient.items;

import lordmonoxide.gradient.blocks.GradientBlocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class StoneHammer extends GradientItemTool implements GradientItemCraftable {
  public StoneHammer() {
    super("stone_hammer", 0.5f, -2.4f, 2);
    this.setHarvestLevel("pickaxe", 0);
    this.setHarvestLevel("hammer", 0);
    this.setMaxDamage(19);
  }
  
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(
      new ItemStack(this),
      "P",
      "F",
      "S",
      Character.valueOf('P'), GradientBlocks.PEBBLE,
      Character.valueOf('F'), GradientItems.FIBRE,
      Character.valueOf('S'), Items.STICK
    );
  }
}
