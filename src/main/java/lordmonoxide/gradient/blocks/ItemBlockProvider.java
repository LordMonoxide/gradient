package lordmonoxide.gradient.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ItemBlockProvider {
  default ItemBlock getItemBlock(final Block block) {
    return new ItemBlock(block) {
      {
        this.setHasSubtypes(ItemBlockProvider.this.hasSubBlocks());
      }
      
      @Override
      public String getUnlocalizedName(final ItemStack stack) {
        return ItemBlockProvider.this.getItemName(this.block.getStateFromMeta(stack.getMetadata()));
      }
      
      @Override
      public int getMetadata(final int metadata) {
        return this.getHasSubtypes() ? metadata : 0;
      }
      
      @Override
      @SideOnly(Side.CLIENT)
      public void getSubItems(final Item item, final CreativeTabs tab, final NonNullList<ItemStack> subItems) {
        if(!this.getHasSubtypes()) {
          this.block.getSubBlocks(item, tab, subItems);
        } else {
          for(IBlockState state : this.block.getBlockState().getValidStates()) {
            subItems.add(new ItemStack(item, 1, this.block.getMetaFromState(state)));
          }
        }
      }
    };
  }
  
  default String getItemName(final IBlockState state) {
    return state.getBlock().getUnlocalizedName();
  }
  
  default boolean hasSubBlocks() {
    return false;
  }
}
