package lordmonoxide.gradient.blocks.heat;

import lordmonoxide.gradient.blocks.GradientBlock;
import lordmonoxide.gradient.blocks.claycrucible.TileClayCrucible;
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
  protected HeatSinkerBlock(String name, CreativeTabs creative_tab, Material material, MapColor map_color) {
    super(name, creative_tab, material, map_color);
  }
  
  protected HeatSinkerBlock(String name, CreativeTabs creative_tab, Material material) {
    super(name, creative_tab, material);
  }
  
  @Override
  public abstract HeatSinker createNewTileEntity(World worldIn, int meta);
  
  @Override
  @SuppressWarnings("deprecation")
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos neighbor) {
    super.neighborChanged(state, world, pos, blockIn, neighbor);
    
    TileEntity te = world.getTileEntity(pos);
    
    if(te instanceof HeatSinker) {
      ((HeatSinker)te).updateSink(neighbor);
    }
  }
}
