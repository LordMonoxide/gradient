package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.energy.EnergyNetworkManager;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyStorage;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyTransfer;
import lordmonoxide.gradient.tileentities.TileFlywheel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class BlockFlywheel extends GradientBlock {
  @CapabilityInject(IKineticEnergyStorage.class)
  private static Capability<IKineticEnergyStorage> STORAGE;

  @CapabilityInject(IKineticEnergyTransfer.class)
  private static Capability<IKineticEnergyTransfer> TRANSFER;

  public static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;

  public BlockFlywheel() {
    super("flywheel", Properties.create(Material.CIRCUITS).hardnessAndResistance(1.0f, 5.0f));
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onReplaced(final IBlockState state, final World world, final BlockPos pos, final IBlockState newState, final boolean isMoving) {
    super.onReplaced(state, world, pos, newState, isMoving);
    EnergyNetworkManager.getManager(world, STORAGE, TRANSFER).disconnect(pos);
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState rotate(final IBlockState state, final Rotation rot) {
    return state.with(FACING, rot.rotate(state.get(FACING)));
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState mirror(final IBlockState state, final Mirror mirror) {
    return state.rotate(mirror.toRotation(state.get(FACING)));
  }

  @Override
  protected void fillStateContainer(final StateContainer.Builder<Block, IBlockState> builder) {
    builder.add(FACING);
  }

  @Override
  public TileFlywheel createTileEntity(final IBlockState state, final IBlockReader world) {
    return new TileFlywheel();
  }

  @Override
  public boolean hasTileEntity(final IBlockState state) {
    return true;
  }
}
