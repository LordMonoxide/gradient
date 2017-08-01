package lordmonoxide.gradient.blocks.claycast;

import lordmonoxide.gradient.GradientTools;
import lordmonoxide.gradient.blocks.GradientBlocks;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemClayCast extends ItemBlock {
  public ItemClayCast(final Block block) {
    super(block);
    this.setHasSubtypes(true);
  }
  
  public static ItemStack getCast(final GradientTools.Tool tool, final int amount) {
    return new ItemStack(Item.getItemFromBlock(GradientBlocks.CLAY_CAST), amount, tool.id);
  }
  
  public static ItemStack getCast(final GradientTools.Tool tool) {
    return getCast(tool, 1);
  }
  
  @Override
  public int getMetadata(final int damage) {
    return damage;
  }
  
  @Override
  public String getUnlocalizedName(final ItemStack stack) {
    return super.getUnlocalizedName(stack) + "." + GradientTools.tools.get(stack.getMetadata()).name;
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(final Item item, final CreativeTabs tab, final NonNullList<ItemStack> list) {
    for(final GradientTools.Tool tool : GradientTools.tools) {
      list.add(new ItemStack(this, 1, tool.id));
    }
  }
}