package lordmonoxide.gradient.blocks.claycrucible;

import lordmonoxide.gradient.blocks.GradientBlock;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.heat.Hardenable;
import lordmonoxide.gradient.recipes.GradientCraftable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockClayCrucibleUnhardened extends GradientBlock implements GradientCraftable, Hardenable {
  private static final AxisAlignedBB AABB = new AxisAlignedBB(1d / 16d, 0.0d, 1d / 16d, 1d - 1d / 16d, 0.75d, 1d - 1d / 16d);
  
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
  @SuppressWarnings("deprecation")
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  @Override
  @SuppressWarnings("deprecation")
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return AABB;
  }
  
  @Override
  public IBlockState getHardened(final IBlockState current) {
    return GradientBlocks.CLAY_CRUCIBLE.getDefaultState();
  }
  
  @Override
  public int getHardeningTime() {
    return 180;
  }
}
