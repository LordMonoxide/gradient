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
  
  public static ItemStack getCast(final GradientTools.Type type, final int amount) {
    return new ItemStack(Item.getItemFromBlock(GradientBlocks.CLAY_CAST), amount, type.id);
  }
  
  public static ItemStack getCast(final GradientTools.Type type) {
    return getCast(type, 1);
  }
  
  @Override
  public int getMetadata(final int damage) {
    return damage;
  }
  
  @Override
  public String getUnlocalizedName(final ItemStack stack) {
    return super.getUnlocalizedName(stack) + "." + GradientTools.TYPES.get(stack.getMetadata()).name;
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(final Item item, final CreativeTabs tab, final NonNullList<ItemStack> list) {
    for(final GradientTools.Type type : GradientTools.TYPES) {
      list.add(new ItemStack(this, 1, type.id));
    }
  }
}
