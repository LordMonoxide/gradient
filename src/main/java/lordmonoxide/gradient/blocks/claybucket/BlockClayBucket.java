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
    this.setResistance(5.0f);
    this.setHardness(1.0f);
  }
  
  @Override
  public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
    //TODO
    return Items.BUCKET;
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
}
