package lordmonoxide.gradient.blocks.heat;

import lordmonoxide.gradient.blocks.GradientBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class HeatSinkerBlock extends GradientBlock implements ITileEntityProvider {
  protected HeatSinkerBlock(final String name, final CreativeTabs creativeTab, final Material material, final MapColor mapColor) {
    super(name, creativeTab, material, mapColor);
  }
  
  protected HeatSinkerBlock(final String name, final CreativeTabs creativeTab, final Material material) {
    super(name, creativeTab, material);
  }
  
  @Override
  public abstract HeatSinker createNewTileEntity(final World world, final int meta);
  
  @Override
  @Deprecated
  public void neighborChanged(final IBlockState state, final World world, final BlockPos pos, final Block block, final BlockPos neighbor) {
    super.neighborChanged(state, world, pos, block, neighbor);
    
    final TileEntity te = world.getTileEntity(pos);
    
    if(te instanceof HeatSinker) {
      ((HeatSinker)te).updateSink(neighbor);
      PacketUpdateHeatNeighbours.send(pos, neighbor);
    }
  }
}
