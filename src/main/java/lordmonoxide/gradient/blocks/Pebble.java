package lordmonoxide.gradient.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class Pebble extends GradientBlock {
  private static final AxisAlignedBB AABB = new AxisAlignedBB(0.25d, 0.0d, 0.25d, 0.75d, 0.25d, 0.75d);
  
  public Pebble() {
    super("pebble", CreativeTabs.MATERIALS, Material.GROUND, MapColor.GRAY); //$NON-NLS-1$
    this.setHardness(0.0f);
    this.setResistance(0.0f);
    this.setLightOpacity(0);
  }
  
  /**
   * Returns the quantity of items to drop on block destruction.
   */
  @Override
  public int quantityDropped(Random rand) {
    return rand.nextInt(3);
  }
  
  /**
   * Get the Item that this Block should drop when harvested.
   */
  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    switch(rand.nextInt(2)) {
      case 1:
        return Items.FLINT;
    }
    
    return Item.getItemFromBlock(this);
  }
  
  /**
   * Used to determine ambient occlusion and culling when rebuilding chunks for render
   */
  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return AABB;
  }
  
  @Override
  @Nullable
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
    return NULL_AABB;
  }
  
  @Override
  public boolean canPlaceBlockAt(World world, BlockPos pos) {
    IBlockState down = world.getBlockState(pos.down());
    
    return
      world.getBlockState(pos).getBlock().isReplaceable(world, pos) && (
        down.getMaterial() == Material.CLAY ||
        down.getMaterial() == Material.GRASS ||
        down.getMaterial() == Material.GROUND ||
        down.getMaterial() == Material.ICE ||
        down.getMaterial() == Material.PACKED_ICE ||
        down.getMaterial() == Material.ROCK ||
        down.getMaterial() == Material.SAND
      )
    ;
  }
}
