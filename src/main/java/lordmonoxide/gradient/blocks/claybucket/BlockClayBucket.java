package lordmonoxide.gradient.blocks.claybucket;

import lordmonoxide.gradient.blocks.GradientBlock;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.Random;

public class BlockClayBucket extends GradientBlock {
  private static final AxisAlignedBB AABB = new AxisAlignedBB(3.0d / 16.0d, 0.0d, 3.0d / 16.0d, 1.0d - 3.0d / 16.0d, 0.5d, 1.0d - 3.0d / 16.0d);
  
  public BlockClayBucket() {
    super("clay_bucket", CreativeTabs.TOOLS, Material.CLAY, MapColor.BROWN);
  }
  
  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    //TODO
    return Items.BUCKET;
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
}
