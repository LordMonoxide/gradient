package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMetals;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockClayCast extends GradientBlock {
  private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0d, 0.0d, 0.0d, 1.0d, 2.0d / 16.0d, 1.0d);

  public static BlockClayCast hardened(final GradientCasts.Cast cast) {
    return new BlockClayCast(cast, true);
  }

  public static BlockClayCast unhardened(final GradientCasts.Cast cast) {
    return new BlockClayCast(cast, false);
  }

  public final GradientCasts.Cast cast;
  private final boolean hardened;

  private BlockClayCast(final GradientCasts.Cast cast, final boolean hardened) {
    super("clay_cast" + '.' + cast.name + '.' + (hardened ? "hardened" : "unhardened"), CreativeTabs.TOOLS, hardened ? GradientBlocks.MATERIAL_CLAY_MACHINE : Material.CLAY);
    this.setResistance(hardened ? 5.0f : 2.0f);
    this.setHardness(1.0f);
    this.cast = cast;
    this.hardened = hardened;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(final ItemStack stack, @Nullable final World world, final List<String> tooltip, final ITooltipFlag flag) {
    super.addInformation(stack, world, tooltip, flag);

    if(!this.hardened) {
      tooltip.add(I18n.format("tile.clay_cast.unhardened.tooltip"));
    } else {
      for(final GradientMetals.Metal metal : GradientMetals.metals) {
        tooltip.add(I18n.format("tile.clay_cast.hardened.tooltip"));
        final String metalName = I18n.format("fluid." + metal.name);
        final int metalAmount = this.cast.amountForMetal(metal);
        tooltip.add(I18n.format("tile.clay_cast.hardened.metal_amount", metalName, metalAmount));
      }
    }
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public boolean isSideSolid(final IBlockState state, final IBlockAccess world, final BlockPos pos, final EnumFacing side) {
    return side == EnumFacing.DOWN;
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(final IBlockAccess world, final IBlockState state, final BlockPos pos, final EnumFacing face) {
    if(face == EnumFacing.DOWN) {
      return BlockFaceShape.SOLID;
    }

    return BlockFaceShape.UNDEFINED;
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public boolean isOpaqueCube(final IBlockState state) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public boolean isFullCube(final IBlockState state) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
    return AABB;
  }
}
