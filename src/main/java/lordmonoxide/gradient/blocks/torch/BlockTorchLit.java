package lordmonoxide.gradient.blocks.torch;

import lordmonoxide.gradient.blocks.GradientBlocks;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

import java.util.Random;

public class BlockTorchLit extends BlockTorch {
  public static final IUnlistedProperty<Integer> LIGHT_LEVEL = new Properties.PropertyAdapter<>(PropertyInteger.create("lightLevel", 0, 15));

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
  }

  @SuppressWarnings("deprecation")
  @Deprecated
  @Override
  public int getLightValue(final IBlockState state) {
    if(state instanceof IExtendedBlockState) {
      return ((IExtendedBlockState)state).getValue(LIGHT_LEVEL);
    }

    return 0;
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
    final IUnlistedProperty[] unlisted = {LIGHT_LEVEL};
    final IProperty[] listed = {FACING};

    return new ExtendedBlockState(this, listed, unlisted);
  }

  @Override
  public IBlockState getExtendedState(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
    final IExtendedBlockState extendedState = (IExtendedBlockState)state;

    final int light = world.getBlockState(pos).getBlock() == GradientBlocks.STANDING_TORCH ? this.lightOnStand : this.light;

    return extendedState.withProperty(LIGHT_LEVEL, light);
  }
}
