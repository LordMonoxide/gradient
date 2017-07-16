package lordmonoxide.gradient.blocks.claycrucible;

import lordmonoxide.gradient.GradientGuiHandler;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlock;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.firepit.TileFirePit;
import lordmonoxide.gradient.blocks.heat.HeatSinkerBlock;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockClayCrucible extends HeatSinkerBlock implements ITileEntityProvider {
  public BlockClayCrucible() {
    super("clay_crucible", CreativeTabs.TOOLS, GradientBlocks.MATERIAL_CLAY_MACHINE);
  }
  
  @Override
  @SuppressWarnings("deprecation")
  public boolean isOpaqueCube(IBlockState state) {
    return false;
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
