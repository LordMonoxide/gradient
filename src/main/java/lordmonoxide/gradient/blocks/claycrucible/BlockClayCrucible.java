package lordmonoxide.gradient.blocks.claycrucible;

import lordmonoxide.gradient.GradientGuiHandler;
import lordmonoxide.gradient.GradientMetals;
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
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class BlockClayCrucible extends HeatSinkerBlock implements ITileEntityProvider {
  private static final AxisAlignedBB AABB = new AxisAlignedBB(1d / 16d, 0.0d, 1d / 16d, 1d - 1d / 16d, 0.75d, 1d - 1d / 16d);
  
  public BlockClayCrucible() {
    super("clay_crucible", CreativeTabs.TOOLS, GradientBlocks.MATERIAL_CLAY_MACHINE);
    this.setResistance(5.0f);
    this.setHardness(1.0f);
  }
  
  @Override
  @Deprecated
  public boolean isOpaqueCube(final IBlockState state) {
    return false;
  }
  
  @Override
  @Deprecated
  public boolean isFullCube(final IBlockState state) {
    return false;
  }
  
  @Override
  @Deprecated
  public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
    return AABB;
  }
  
  @Override
  public int getLightValue(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
    final IBlockState other = world.getBlockState(pos);
    if(other.getBlock() != this) {
      return other.getLightValue(world, pos);
    }
  
    final TileEntity te = world.getTileEntity(pos);
    
    if(te instanceof TileClayCrucible) {
      return ((TileClayCrucible)te).getLightLevel();
    }
    
    return state.getLightValue(world, pos);
  }
  
  @Override
  public TileClayCrucible createNewTileEntity(final World worldIn, final int meta) {
    return new TileClayCrucible();
  }
  
  @Override
  public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
    if(!world.isRemote) {
      if(!player.isSneaking()) {
        final TileClayCrucible te = (TileClayCrucible)world.getTileEntity(pos);
        
        if(te == null) {
          return false;
        }
        
        if(FluidUtil.getFluidHandler(player.getHeldItem(hand)) != null) {
          final FluidStack fluid = FluidUtil.getFluidContained(player.getHeldItem(hand));
          
          // Make sure the fluid handler is either empty, or contains metal
          if(fluid != null) {
            final GradientMetals.Metal metal = GradientMetals.getMetalForFluid(fluid.getFluid());
            
            if(metal == GradientMetals.INVALID_METAL) {
              return true;
            }
          }
          
          te.useBucket(player, hand, world, pos, side);
          
          return true;
        }
        
        player.openGui(GradientMod.instance, GradientGuiHandler.CLAY_CRUCIBLE, world, pos.getX(), pos.getY(), pos.getZ());
      }
    }
    
    return true;
  }
}
