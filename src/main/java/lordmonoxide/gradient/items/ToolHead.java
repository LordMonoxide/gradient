package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientTools;
import lordmonoxide.gradient.blocks.claycast.ItemClayCast;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ToolHead extends GradientItem implements GradientItemCraftable {
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
  
  public GradientTools.Tool getTool(final ItemStack stack) {
    return GradientTools.tools.get(stack.getMetadata() / 0x100);
  }
  
  public GradientMetals.Metal getMetal(final ItemStack stack) {
    return GradientMetals.instance.getMetal(stack.getMetadata() & 0xFF);
  }
  
  @Override
  public void addRecipe() {
    NonNullList<ItemStack> stacks = NonNullList.create();
    this.getSubItems(this, this.getCreativeTab(), stacks);
    
    for(final ItemStack stack : stacks) {
      GradientTools.Tool tool = this.getTool(stack);
      GradientMetals.Metal metal = this.getMetal(stack);
      
      GameRegistry.addShapelessRecipe(
        stack,
        GradientMetals.getBucket(metal),
        ItemClayCast.getCast(tool)
      );
    }
  }
  
  @Override
  public int getMetadata(final int metadata) {
    return metadata;
  }
  
  @Override
  public String getUnlocalizedName(final ItemStack stack) {
    return super.getUnlocalizedName() + '.' + this.getTool(stack).name + '.' + this.getMetal(stack).name;
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
