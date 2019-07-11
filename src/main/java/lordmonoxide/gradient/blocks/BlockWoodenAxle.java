package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.energy.EnergyNetworkManager;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyStorage;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyTransfer;
import lordmonoxide.gradient.tileentities.TileWoodenAxle;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class BlockWoodenAxle extends RotatedPillarBlock {
  @CapabilityInject(IKineticEnergyStorage.class)
  private static Capability<IKineticEnergyStorage> STORAGE;

  @CapabilityInject(IKineticEnergyTransfer.class)
  private static Capability<IKineticEnergyTransfer> TRANSFER;

  private static final VoxelShape SHAPE_X = Block.makeCuboidShape(0.0d, 5.0d, 5.0d, 16.0d, 11.0d, 11.0d);
  private static final VoxelShape SHAPE_Y = Block.makeCuboidShape(5.0d, 0.0d, 5.0d, 11.0d, 16.0d, 11.0d);
  private static final VoxelShape SHAPE_Z = Block.makeCuboidShape(5.0d, 5.0d, 0.0d, 11.0d, 11.0d, 16.0d);

  public BlockWoodenAxle() {
    super(Properties.create(Material.CIRCUITS).hardnessAndResistance(1.0f, 5.0f));
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onReplaced(final BlockState state, final World world, final BlockPos pos, final BlockState newState, final boolean isMoving) {
    super.onReplaced(state, world, pos, newState, isMoving);
    EnergyNetworkManager.getManager(world, STORAGE, TRANSFER).disconnect(pos);
  }

  @Override
  public TileWoodenAxle createTileEntity(final BlockState state, final IBlockReader world) {
    return new TileWoodenAxle();
  }

  @Override
  public boolean hasTileEntity(final BlockState state) {
    return true;
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public boolean isFullCube(final BlockState state) {
    return false;
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public VoxelShape getShape(final BlockState state, final IBlockReader source, final BlockPos pos) {
    switch(state.get(AXIS)) {
      case X:
        return SHAPE_X;

      case Z:
        return SHAPE_Z;
    }

    return SHAPE_Y;
  }
}

