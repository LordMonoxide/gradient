package lordmonoxide.gradient.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ItemBlockProvider {
  default <T extends Block & ItemBlockProvider> ItemBlock getItemBlock(final T block) {
    return new ItemBlock(block) {
      {
        this.setHasSubtypes(ItemBlockProvider.this.hasSubBlocks());
      }

      @Override
      public String getTranslationKey(final ItemStack stack) {
        return ItemBlockProvider.this.getItemName(this.block.getStateFromMeta(stack.getMetadata()));
      }

      @Override
      public int getMetadata(final int metadata) {
        return this.getHasSubtypes() ? metadata : 0;
      }

      @Override
      @SideOnly(Side.CLIENT)
      public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> subItems) {
        ItemBlockProvider.this.getSubItems(this, tab, subItems);
      }
    };
  }

  default String getItemName(final IBlockState state) {
    return state.getBlock().getTranslationKey();
  }

  default boolean hasSubBlocks() {
    return false;
  }

  default void getSubItems(final ItemBlock item, final CreativeTabs tab, final NonNullList<ItemStack> subItems) {
    if(!this.hasSubBlocks()) {
      item.getBlock().getSubBlocks(tab, subItems);
    } else {
      for(final IBlockState state : item.getBlock().getBlockState().getValidStates()) {
        subItems.add(new ItemStack(item, 1, item.getBlock().getMetaFromState(state)));
      }
    }
  }
}
