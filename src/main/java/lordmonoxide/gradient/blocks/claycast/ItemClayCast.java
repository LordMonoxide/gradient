package lordmonoxide.gradient.blocks.claycast;

import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.init.CastRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemClayCast extends ItemBlock {
  public ItemClayCast(final BlockClayCast block) {
    super(block);
    this.setHasSubtypes(true);
  }

  public static ItemStack getCast(final CastRegistry.Cast cast, final int amount) {
    return new ItemStack(Item.getItemFromBlock(GradientBlocks.CLAY_CAST), amount, cast.id);
  }

  public static ItemStack getCast(final CastRegistry.Cast cast) {
    return getCast(cast, 1);
  }

  @Override
  public int getMetadata(final int damage) {
    return damage;
  }

  @Override
  public String getTranslationKey(final ItemStack stack) {
    return super.getTranslationKey(stack) + '.' + CastRegistry.getCast(stack.getMetadata()).getRegistryName();
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> list) {
    for(final CastRegistry.Cast cast : GameRegistry.findRegistry(CastRegistry.Cast.class)) {
      list.add(new ItemStack(this, 1, cast.id));
    }
  }
}
