package lordmonoxide.gradient.blocks.kinetic.axle;

import lordmonoxide.gradient.energy.EnergyNetworkManager;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyStorage;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyTransfer;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class BlockAxle extends BlockRotatedPillar {
  @CapabilityInject(IKineticEnergyStorage.class)
  private static Capability<IKineticEnergyStorage> STORAGE;

  @CapabilityInject(IKineticEnergyTransfer.class)
  private static Capability<IKineticEnergyTransfer> TRANSFER;

  public BlockAxle() {
    super(Material.CIRCUITS);
    this.setRegistryName("axle");
    this.setTranslationKey("axle");
    this.setCreativeTab(CreativeTabs.TOOLS);
    this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.Y));
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
  public TileAxle createTileEntity(final World world, final IBlockState state) {
    return new TileAxle();
  }

  @Override
  public boolean hasTileEntity(final IBlockState state) {
    return true;
  }
}

