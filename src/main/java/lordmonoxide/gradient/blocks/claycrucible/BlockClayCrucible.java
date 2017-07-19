package lordmonoxide.gradient.blocks.claycrucible;

import lordmonoxide.gradient.GradientGuiHandler;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.heat.HeatSinkerBlock;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockClayCrucible extends HeatSinkerBlock implements ITileEntityProvider {
  private static final AxisAlignedBB AABB = new AxisAlignedBB(1d / 16d, 0.0d, 1d / 16d, 1d - 1d / 16d, 0.75d, 1d - 1d / 16d);
  
  public BlockClayCrucible() {
    super("clay_crucible", CreativeTabs.TOOLS, GradientBlocks.MATERIAL_CLAY_MACHINE);
  }
  
  @Override
  @SuppressWarnings("deprecation")
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  @Override
  @SuppressWarnings("deprecation")
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  @Override
  @SuppressWarnings("deprecation")
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return AABB;
  }
  
  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
    IBlockState other = world.getBlockState(pos);
    if(other.getBlock() != this) {
      return other.getLightValue(world, pos);
    }
    
    TileEntity te = world.getTileEntity(pos);
    
    if(te instanceof TileClayCrucible) {
      return ((TileClayCrucible)te).getLightLevel();
    }
    
    @SuppressWarnings("deprecation")
    int light = state.getLightValue();
    return light;
  }
  
  @Override
  public TileClayCrucible createNewTileEntity(World worldIn, int meta) {
    return new TileClayCrucible();
  }
  
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    if(!world.isRemote) {
      if(!player.isSneaking()) {
        player.openGui(GradientMod.instance, GradientGuiHandler.CLAY_CRUCIBLE, world, pos.getX(), pos.getY(), pos.getZ());
      }
    }
    
    return true;
  }
}
