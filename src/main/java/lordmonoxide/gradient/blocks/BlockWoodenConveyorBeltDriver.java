package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.tileentities.TileWoodenConveyorBeltDriver;
import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockWoodenConveyorBeltDriver extends GradientBlock {
  public static final PropertyDirection FACING = PropertyDirection.create("facing");

  public BlockWoodenConveyorBeltDriver() {
    super("wooden_conveyor_belt_driver", CreativeTabs.TRANSPORTATION, Material.WOOD);
    this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    this.setLightOpacity(0);
    this.setResistance(5.0f);
    this.setHardness(1.0f);
  }

  @Override
  public void breakBlock(final World world, final BlockPos pos, final IBlockState state) {
    super.breakBlock(world, pos, state);
    WorldUtils.callTileEntity(world, pos, TileWoodenConveyorBeltDriver.class, TileWoodenConveyorBeltDriver::remove);
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public void neighborChanged(final IBlockState state, final World world, final BlockPos pos, final Block block, final BlockPos neighbour) {
    super.neighborChanged(state, world, pos, block, neighbour);

    final TileWoodenConveyorBeltDriver driver = WorldUtils.getTileEntity(world, pos, TileWoodenConveyorBeltDriver.class);

    if(driver != null) {
      final EnumFacing side = WorldUtils.getBlockFacing(pos, neighbour);
      if(side.getAxis().isHorizontal()) {
        if(world.getBlockState(neighbour).getBlock() == GradientBlocks.WOODEN_CONVEYOR_BELT) {
          driver.addBelt(side);
        } else {
          driver.removeBelt(side);
        }
      }
    }
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(final World world, final IBlockState state) {
    return new TileWoodenConveyorBeltDriver();
  }

  @Override
  public boolean hasTileEntity(final IBlockState state) {
    return true;
  }

  @Override
  public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer, final EnumHand hand) {
    return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING, facing.getOpposite());
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public IBlockState getStateFromMeta(final int meta) {
    final EnumFacing facing = EnumFacing.byIndex(meta);
    return this.getDefaultState().withProperty(FACING, facing);
  }

  @Override
  public int getMetaFromState(final IBlockState state) {
    return state.getValue(FACING).getIndex();
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public IBlockState withRotation(final IBlockState state, final Rotation rot) {
    return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public IBlockState withMirror(final IBlockState state, final Mirror mirror) {
    return state.withRotation(mirror.toRotation(state.getValue(FACING)));
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer.Builder(this).add(FACING).build();
  }
}
