package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientTools;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ToolHead extends GradientItem {
  public ToolHead() {
    super("tool_head", CreativeTabs.TOOLS);
    this.setHasSubtypes(true);
  }
  
  public static ItemStack getToolHead(final GradientTools.Tool tool, final GradientMetals.Metal metal) {
    return getToolHead(tool, metal, 1);
  }
  
  public static ItemStack getToolHead(final GradientTools.Tool tool, final GradientMetals.Metal metal, final int amount) {
    return new ItemStack(GradientItems.TOOL_HEADS, amount, tool.id << 0x8 | metal.id);
  }
  
  @Override
  public int getMetadata(final int metadata) {
    return metadata;
  }
  
  @Override
  public String getUnlocalizedName(final ItemStack stack) {
    final int toolId = stack.getMetadata() / 0x100;
    final int metalId = stack.getMetadata() & 0xFF;
    
    return super.getUnlocalizedName() + '.' + GradientTools.tools.get(toolId).name + '.' + GradientMetals.instance.getMetal(metalId).name;
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(final Item item, final CreativeTabs tab, final NonNullList<ItemStack> list) {
    for(final GradientTools.Tool tool : GradientTools.tools) {
      for(final GradientMetals.Metal metal : GradientMetals.instance.metals) {
        list.add(getToolHead(tool, metal));
      }
    }
  }
}
