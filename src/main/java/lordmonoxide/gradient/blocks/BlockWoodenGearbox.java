package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.energy.EnergyNetworkManager;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyStorage;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyTransfer;
import lordmonoxide.gradient.tileentities.TileWoodenGearbox;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class BlockWoodenGearbox extends GradientBlock {
  @CapabilityInject(IKineticEnergyStorage.class)
  private static Capability<IKineticEnergyStorage> STORAGE;

  @CapabilityInject(IKineticEnergyTransfer.class)
  private static Capability<IKineticEnergyTransfer> TRANSFER;

  public BlockWoodenGearbox() {
    super("wooden_gearbox", CreativeTabs.TOOLS, Material.CIRCUITS);
    this.setResistance(5.0f);
    this.setHardness(1.0f);
  }

  @Override
  public void breakBlock(final World world, final BlockPos pos, final IBlockState state) {
    super.breakBlock(world, pos, state);
    EnergyNetworkManager.getManager(world, STORAGE, TRANSFER).queueDisconnection(pos);
  }

  @Override
  public TileWoodenGearbox createTileEntity(final World world, final IBlockState state) {
    return new TileWoodenGearbox();
  }

  @Override
  public boolean hasTileEntity(final IBlockState state) {
    return true;
  }
}
