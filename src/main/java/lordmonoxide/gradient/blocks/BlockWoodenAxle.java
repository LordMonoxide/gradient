package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.tileentities.TileWoodenAxle;
import lordmonoxide.gradient.energy.EnergyNetworkManager;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyStorage;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyTransfer;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class BlockWoodenAxle extends BlockRotatedPillar {
  @CapabilityInject(IKineticEnergyStorage.class)
  private static Capability<IKineticEnergyStorage> STORAGE;

  @CapabilityInject(IKineticEnergyTransfer.class)
  private static Capability<IKineticEnergyTransfer> TRANSFER;

  private static final AxisAlignedBB AABB_X = new AxisAlignedBB(0.0d, 5.0d / 16.0d, 5.0d / 16.0d, 1.0d, 11.0d / 16.0d, 11.0d / 16.0d);
  private static final AxisAlignedBB AABB_Y = new AxisAlignedBB(5.0d / 16.0d, 0.0d, 5.0d / 16.0d, 11.0d / 16.0d, 1.0d, 11.0d / 16.0d);
  private static final AxisAlignedBB AABB_Z = new AxisAlignedBB(5.0d / 16.0d, 5.0d / 16.0d, 0.0d, 11.0d / 16.0d, 11.0d / 16.0d, 1.0d);

  public BlockWoodenAxle() {
    super(Material.CIRCUITS);
    this.setRegistryName("wooden_axle");
    this.setTranslationKey("wooden_axle");
    this.setCreativeTab(CreativeTabs.TOOLS);
    this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.Y));
    this.setLightOpacity(0);
    this.setResistance(5.0f);
    this.setHardness(1.0f);
  }

  @Override
  public void breakBlock(final World world, final BlockPos pos, final IBlockState state) {
    super.breakBlock(world, pos, state);
    EnergyNetworkManager.getManager(world, STORAGE, TRANSFER).disconnect(pos);
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, AXIS);
  }

  @Override
  public TileWoodenAxle createTileEntity(final World world, final IBlockState state) {
    return new TileWoodenAxle();
  }

  @Override
  public boolean hasTileEntity(final IBlockState state) {
    return true;
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public boolean isOpaqueCube(final IBlockState state) {
    return false;
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public boolean isFullCube(final IBlockState state) {
    return false;
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
    switch(state.getValue(AXIS)) {
      case X:
        return AABB_X;

      case Z:
        return AABB_Z;
    }

    return AABB_Y;
  }
}

