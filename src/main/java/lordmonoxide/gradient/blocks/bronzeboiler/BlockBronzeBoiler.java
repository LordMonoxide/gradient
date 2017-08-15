package lordmonoxide.gradient.blocks.bronzeboiler;

import lordmonoxide.gradient.GradientGuiHandler;
import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlock;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.items.Plate;
import lordmonoxide.gradient.recipes.GradientCraftable;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockBronzeBoiler extends GradientBlock implements GradientCraftable, ITileEntityProvider {
  public static final PropertyDirection FACING = BlockHorizontal.FACING;
  
  private static final Fluid WATER = FluidRegistry.getFluid("water");
  
  public BlockBronzeBoiler() {
    super("bronze_boiler", CreativeTabs.TOOLS, GradientBlocks.MATERIAL_BRONZE_MACHINE); //$NON-NLS-1$
    this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    this.setResistance(5.0f);
    this.setHardness(1.0f);
  }
  
  @Override
  public TileBronzeBoiler createNewTileEntity(final World world, final int meta) {
    return new TileBronzeBoiler();
  }
  
  @Override
  public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
    if(!world.isRemote) {
      if(!player.isSneaking()) {
        final TileBronzeBoiler te = (TileBronzeBoiler)world.getTileEntity(pos);
        
        if(te == null) {
          return false;
        }
        
        if(FluidUtil.getFluidHandler(player.getHeldItem(hand)) != null) {
          final FluidStack fluid = FluidUtil.getFluidContained(player.getHeldItem(hand));
          
          // Make sure the fluid handler is either empty, or contains water
          if(fluid != null && fluid.getFluid() != WATER) {
            return true;
          }
          
          te.useBucket(player, hand, world, pos, side);
          
          return true;
        }
        
        player.openGui(GradientMod.instance, GradientGuiHandler.BRONZE_BOILER, world, pos.getX(), pos.getY(), pos.getZ());
      }
    }
    
    return true;
  }
  
  @Override
  public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
    world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
  }
  
  @Override
  @Deprecated
  public IBlockState getStateFromMeta(final int meta) {
    EnumFacing facing = EnumFacing.getHorizontal(meta);
    
    return this.getDefaultState().withProperty(FACING, facing);
  }
  
  @Override
  public int getMetaFromState(final IBlockState state) {
    return state.getValue(FACING).getHorizontalIndex();
  }
  
  @Override
  @Deprecated
  public IBlockState withRotation(final IBlockState state, final Rotation rot) {
    return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
  }
  
  @Override
  @Deprecated
  public IBlockState withMirror(final IBlockState state, final Mirror mirror) {
    return state.withRotation(mirror.toRotation(state.getValue(FACING)));
  }
  
  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING);
  }
  
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(
      new ItemStack(this),
      "PPP",
      "P P",
      "PPP",
      'P', Plate.getPlate(GradientMetals.getMetal("bronze"))
    );
  }
}
