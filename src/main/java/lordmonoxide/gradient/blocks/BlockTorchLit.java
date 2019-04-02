package lordmonoxide.gradient.blocks;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTorchLit extends BlockTorch {
  public static final PropertyBool STAND = PropertyBool.create("stand");

  private final int light;
  private final int lightOnStand;

  public BlockTorchLit(final String name, final float light, final float lightOnStand) {
    this.setRegistryName(name);
    this.setTranslationKey(name);
    this.setHardness(0.0f);
    this.setSoundType(SoundType.WOOD);
    this.setTickRandomly(false);
    this.light = (int)(15.0f * light);
    this.lightOnStand = (int)(15.0f * lightOnStand);
    this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP).withProperty(STAND, false));
  }

  @SuppressWarnings("deprecation")
  @Deprecated
  @Override
  public int getLightValue(final IBlockState state) {
    return state.getValue(STAND) ? this.lightOnStand : this.light;
  }

  @Override
  public int tickRate(final World world) {
    return 9600;
  }

  @Override
  public void updateTick(final World world, final BlockPos pos, final IBlockState state, final Random rand) {
    world.setBlockState(pos, GradientBlocks.FIBRE_TORCH_UNLIT.getDefaultState().withProperty(BlockTorch.FACING, state.getValue(BlockTorch.FACING)));
  }

  @Override
  public void onBlockAdded(final World world, final BlockPos pos, final IBlockState state) {
    world.scheduleUpdate(pos, this, this.tickRate(world));
    super.onBlockAdded(world, pos, state);
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING, STAND);
  }

  @Override
  public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
    final IBlockState state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer);

    if(facing == EnumFacing.UP) {
      if(world.getBlockState(pos.down()).getBlock() == GradientBlocks.STANDING_TORCH) {
        return state.withProperty(STAND, true);
      }
    }

    return state;
  }

  @Override
  public IBlockState getStateFromMeta(final int meta) {
    IBlockState state = this.getDefaultState();

    switch(meta & 0b111) {
      case 1:
        state = state.withProperty(FACING, EnumFacing.EAST);
        break;
      case 2:
        state = state.withProperty(FACING, EnumFacing.WEST);
        break;
      case 3:
        state = state.withProperty(FACING, EnumFacing.SOUTH);
        break;
      case 4:
        state = state.withProperty(FACING, EnumFacing.NORTH);
        break;
      case 5:
      default:
        state = state.withProperty(FACING, EnumFacing.UP);
    }

    if((meta & 0b1000) != 0) {
      state = state.withProperty(STAND, true);
    }

    return state;
  }

  @Override
  public int getMetaFromState(final IBlockState state) {
    return super.getMetaFromState(state) | (state.getValue(STAND) ? 0b1000 : 0);
  }
}
