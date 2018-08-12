package lordmonoxide.gradient.blocks.pebble;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlock;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockPebble extends GradientBlock {
  @GameRegistry.ObjectHolder("gradient:pebble")
  private static final Item PEBBLE = null;

  private static final AxisAlignedBB AABB = new AxisAlignedBB(0.25d, 0.0d, 0.25d, 0.75d, 0.25d, 0.75d);

  @Nullable
  private final GradientMetals.Metal metal;

  public BlockPebble() {
    super("pebble", CreativeTabs.MATERIALS, Material.GROUND, MapColor.GRAY); //$NON-NLS-1$
    this.setHardness(0.0f);
    this.setResistance(0.0f);
    this.setLightOpacity(0);
    this.metal = null;
  }

  public BlockPebble(final GradientMetals.Metal metal) {
    super("pebble." + metal.name, CreativeTabs.MATERIALS, Material.GROUND, MapColor.GRAY); //$NON-NLS-1$
    this.setHardness(0.0f);
    this.setResistance(0.0f);
    this.setLightOpacity(0);
    this.metal = metal;
    this.setTranslationKey("pebble");
  }

  @Override
  public void getDrops(final NonNullList<ItemStack> drops, final IBlockAccess world, final BlockPos pos, final IBlockState state, final int fortune) {
    final Random rand = world instanceof World ? ((World)world).rand : RANDOM;

    if(rand.nextInt(3) == 0) {
      drops.add(new ItemStack(Items.FLINT));
    } else {
      drops.add(new ItemStack(PEBBLE));
    }

    if(this.metal != null) {
      if(rand.nextInt(3) == 0) {
        drops.add(new ItemStack(ForgeRegistries.ITEMS.getValue(GradientMod.resource("nugget." + this.metal.name))));
      }
    }
  }

  /**
   * Used to determine ambient occlusion and culling when rebuilding chunks for render
   */
  @Override
  @SuppressWarnings("deprecation")
  public boolean isOpaqueCube(final IBlockState state) {
    return false;
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isFullCube(final IBlockState state) {
    return false;
  }

  @Override
  @SuppressWarnings("deprecation")
  public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
    return AABB;
  }

  @Override
  @Nullable
  @SuppressWarnings("deprecation")
  public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess world, final BlockPos pos) {
    return NULL_AABB;
  }

  @Override
  public boolean canPlaceBlockAt(final World world, final BlockPos pos) {
    final IBlockState down = world.getBlockState(pos.down());

    return
      super.canPlaceBlockAt(world, pos) && (
        down.getMaterial() == Material.CLAY ||
        down.getMaterial() == Material.GRASS ||
        down.getMaterial() == Material.GROUND ||
        down.getMaterial() == Material.ICE ||
        down.getMaterial() == Material.PACKED_ICE ||
        down.getMaterial() == Material.ROCK ||
        down.getMaterial() == Material.SAND
      ) && down.isFullBlock();
  }
}
