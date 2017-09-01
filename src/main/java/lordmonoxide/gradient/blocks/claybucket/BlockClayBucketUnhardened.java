package lordmonoxide.gradient.blocks.claybucket;

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

public class BlockClayBucketUnhardened extends GradientBlock implements GradientCraftable, Hardenable {
  private static final AxisAlignedBB AABB = new AxisAlignedBB(3.0d / 16.0d, 0.0d, 3.0d / 16.0d, 1.0d - 3.0d / 16.0d, 0.5d, 1.0d - 3.0d / 16.0d);
  
  public BlockClayBucketUnhardened() {
    super("clay_bucket_unhardened", CreativeTabs.TOOLS, Material.CLAY, MapColor.CLAY);
    this.setResistance(2.0f);
    this.setHardness(1.0f);
  }
  
  @Override
  public void addRecipe() {
    //TODO
    /*GameRegistry.addShapedRecipe(
      new ItemStack(this),
      "C C",
      "CCC",
      'C', Items.CLAY_BALL
    );*/
  }
  
  @Override
  @Deprecated
  public boolean isOpaqueCube(final IBlockState state) {
    return false;
  }
  
  @Override
  @Deprecated
  public boolean isFullCube(final IBlockState state) {
    return false;
  }
  
  @Override
  @Deprecated
  public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
    return AABB;
  }
  
  @Override
  public IBlockState getHardened(final IBlockState current) {
    return GradientBlocks.CLAY_BUCKET.getDefaultState();
  }
  
  @Override
  public int getHardeningTime(final IBlockState current) {
    return 60;
  }
}
