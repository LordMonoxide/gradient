package lordmonoxide.gradient.blocks.claycast;

import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.GradientCasts;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemClayCast extends ItemBlock {
  public ItemClayCast(final BlockClayCast block) {
    super(block);
    this.setHasSubtypes(true);
  }
  
  public static ItemStack getCast(final GradientCasts.Cast cast, final int amount) {
    return new ItemStack(Item.getItemFromBlock(GradientBlocks.CLAY_CAST), amount, cast.id);
  }
  
  public static ItemStack getCast(final GradientCasts.Cast cast) {
    return getCast(cast, 1);
  }
  
  @Override
  public int getMetadata(final int damage) {
    return damage;
  }
  
  @Override
  public String getUnlocalizedName(final ItemStack stack) {
    return super.getUnlocalizedName(stack) + '.' + GradientCasts.getCast(stack.getMetadata()).name;
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> list) {
    for(final GradientCasts.Cast cast : GradientCasts.casts()) {
      list.add(new ItemStack(this, 1, cast.id));
    }
  }
}
