package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class GradientItem extends Item {
  protected String name;
  
  public GradientItem(String name) {
    this.name = name;
    setUnlocalizedName(name);
    setRegistryName(name);
  }
  
  public void registerItemModel() {
    ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(GradientMod.MODID + ":" + name, "inventory"));
  }
  
  @Override
  public GradientItem setCreativeTab(CreativeTabs tab) {
    super.setCreativeTab(tab);
    return this;
  }
}
