package lordmonoxide.gradient.blocks.claycast;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.blocks.GradientBlock;
import lordmonoxide.gradient.blocks.GradientBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
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

  public static final GradientCasts.PropertyCast CAST = GradientCasts.PropertyCast.create("cast");

  public static BlockClayCast hardened() {
    return new BlockClayCast(true);
  }

  public static BlockClayCast unhardened() {
    return new BlockClayCast(false);
  }

  private final boolean hardened;

  protected BlockClayCast(final boolean hardened) {
    super("clay_cast" + '.' + (hardened ? "hardened" : "unhardened"), CreativeTabs.TOOLS, hardened ? GradientBlocks.MATERIAL_CLAY_MACHINE : Material.CLAY);
    this.setDefaultState(this.blockState.getBaseState().withProperty(CAST, GradientCasts.PICKAXE));
    this.setResistance(hardened ? 5.0f : 2.0f);
    this.setHardness(1.0f);
    this.hardened = hardened;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(final ItemStack stack, @Nullable final World world, final List<String> tooltip, final ITooltipFlag flag) {
    super.addInformation(stack, world, tooltip, flag);

    if(this.hardened) {
      for(final GradientMetals.Metal metal : GradientMetals.metals) {
        final String metalName = I18n.format("fluid." + metal.name);
        final int metalAmount = this.getStateFromMeta(stack.getMetadata()).getValue(CAST).amountForMetal(metal);
        tooltip.add(I18n.format("tile.clay_cast.hardened.metal_amount", metalName, metalAmount));
      }
    }
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, CAST);
  }

  @Override
  @Deprecated
  public IBlockState getStateFromMeta(final int meta) {
    return this.getDefaultState().withProperty(CAST, GradientCasts.getCast(meta));
  }

  @Override
  public int getMetaFromState(final IBlockState state) {
    return state.getValue(CAST).id;
  }

  @Override
  public int damageDropped(final IBlockState state) {
    return this.getMetaFromState(state);
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
