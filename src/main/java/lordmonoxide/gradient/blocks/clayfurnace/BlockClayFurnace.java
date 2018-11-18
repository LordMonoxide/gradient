package lordmonoxide.gradient.blocks.clayfurnace;

import lordmonoxide.gradient.blocks.GradientBlock;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.ItemBlockProvider;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockClayFurnace extends GradientBlock implements ItemBlockProvider {
  public static final PropertyDirection FACING = BlockHorizontal.FACING;

  public static BlockClayFurnace hardened() {
    return new BlockClayFurnace(true);
  }

  public static BlockClayFurnace unhardened() {
    return new BlockClayFurnace(false);
  }

  protected BlockClayFurnace(final boolean hardened) {
    super("clay_furnace" + '.' + (hardened ? "hardened" : "unhardened"), CreativeTabs.TOOLS, hardened ? GradientBlocks.MATERIAL_CLAY_MACHINE : Material.CLAY);
    this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    this.setResistance(hardened ? 5.0f : 2.0f);
    this.setHardness(1.0f);
  }

  @Override
  @Deprecated
  public boolean isOpaqueCube(final IBlockState state) {
    return false;
  }

  @Override
  public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
    world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
  }

  @Override
  @Deprecated
  public IBlockState getStateFromMeta(final int meta) {
    return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
  }

  @Override
  public int getMetaFromState(final IBlockState state) {
    return state.getValue(FACING).getHorizontalIndex();
  }

  @Override
  @Deprecated
  public IBlockState withRotation(final IBlockState state, final Rotation rot) {
    return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
  }

  @Override
  @Deprecated
  public IBlockState withMirror(final IBlockState state, final Mirror mirror) {
    return state.withRotation(mirror.toRotation(state.getValue(FACING)));
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING);
  }
}
