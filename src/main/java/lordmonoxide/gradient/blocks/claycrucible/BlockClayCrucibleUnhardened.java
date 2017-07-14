package lordmonoxide.gradient.blocks.claycrucible;

import lordmonoxide.gradient.blocks.GradientBlock;
import lordmonoxide.gradient.blocks.GradientBlockCraftable;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.Hardenable;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockClayCrucibleUnhardened extends GradientBlock implements GradientBlockCraftable, Hardenable {
  public BlockClayCrucibleUnhardened() {
    super("clay_crucible_unhardened", CreativeTabs.TOOLS, Material.CLAY, MapColor.CLAY);
  }
  
  @Override
  public void addRecipe() {
    GameRegistry.addShapedRecipe(
        new ItemStack(this),
        "C C",
        "C C",
        "CCC",
        'C', Items.CLAY_BALL
    );
  }
  
  @Override
  @SuppressWarnings("deprecation")
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  @Override
  public Block getHardened() {
    return GradientBlocks.CLAY_CRUCIBLE;
  }
  
  @Override
  public int getHardeningTime() {
    return 180;
  }
}
