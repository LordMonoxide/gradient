package lordmonoxide.gradient.blocks.firepit;

import lordmonoxide.gradient.GradientGuiHandler;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.clayfurnace.BlockClayFurnace;
import lordmonoxide.gradient.blocks.heat.HeatSinkerBlock;
import lordmonoxide.gradient.items.FireStarter;
import lordmonoxide.gradient.recipes.GradientCraftable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.List;
import java.util.Random;

public class BlockFirePit extends HeatSinkerBlock implements GradientCraftable, ITileEntityProvider {
  private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0d, 0.0d, 0.0d, 1.0d, 0.3d, 1.0d);
  
  public static final PropertyDirection FACING = BlockHorizontal.FACING;
  public static final PropertyBool HAS_FURNACE = PropertyBool.create("has_furnace");
  
  public BlockFirePit() {
    super("fire_pit", CreativeTabs.TOOLS, Material.WOOD, MapColor.RED); //$NON-NLS-1$
    this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(HAS_FURNACE, false));
    this.setLightOpacity(0);
    this.setResistance(5.0f);
    this.setHardness(1.0f);
  }
  
  @Override
  public int quantityDropped(Random rand) {
    return rand.nextInt(3) + 2;
  }
  
  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.STICK;
  }
  
  @Override
  public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    List<ItemStack> ret = super.getDrops(world, pos, state, fortune);
    
    if(state.getValue(HAS_FURNACE)) {
      ret.add(new ItemStack(GradientBlocks.CLAY_FURNACE));
    }
    
    return ret;
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
    if(!state.getValue(HAS_FURNACE)) {
      return AABB;
    }
    
    return Block.FULL_BLOCK_AABB;
  }
  
  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
    IBlockState other = world.getBlockState(pos);
    if(other.getBlock() != this) {
      return other.getLightValue(world, pos);
    }
    
    TileEntity te = world.getTileEntity(pos);
    
    if(te instanceof TileFirePit) {
      return ((TileFirePit)te).getLightLevel(state);
    }
    
    @SuppressWarnings("deprecation")
    int light = state.getLightValue();
    return light;
  }
  
  @Override
  public TileFirePit createNewTileEntity(World worldIn, int meta) {
    return new TileFirePit();
  }
  
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    if(!world.isRemote) {
      if(!player.isSneaking()) {
        ItemStack stack = player.getHeldItem(hand);
        
        if(stack.getItem() instanceof FireStarter) {
          TileFirePit tile = (TileFirePit)world.getTileEntity(pos);
          
          if(!tile.isBurning()) {
            tile.light();
            return true;
          }
        }
        
        if(stack.getItem() instanceof ItemBlock && ((ItemBlock)stack.getItem()).block instanceof BlockClayFurnace) {
          if(!state.getValue(HAS_FURNACE)) {
            TileFirePit te = (TileFirePit)world.getTileEntity(pos);
            
            world.setBlockState(pos, state.withProperty(HAS_FURNACE, true));
            
            // Changing the blockstate replaces the tile entity... swap it
            // back to the old one.  Not sure how terrible doing this is.
            te.validate();
            world.setTileEntity(pos, te);
            te.attachFurnace();
            
            stack.shrink(1);
            return true;
          }
        }
        
        player.openGui(GradientMod.instance, GradientGuiHandler.FIRE_PIT, world, pos.getX(), pos.getY(), pos.getZ());
      }
    }
    
    return true;
  }
  
  @Override
  @Deprecated
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos neighbor) {
    super.neighborChanged(state, world, pos, blockIn, neighbor);
    
    TileEntity te = world.getTileEntity(pos);
    
    if(te instanceof TileFirePit) {
      ((TileFirePit)te).updateHardenable(neighbor);
    }
  }
  
  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
  }
  
  @Override
  @Deprecated
  public IBlockState getStateFromMeta(int meta) {
    EnumFacing facing = EnumFacing.getHorizontal(meta & 0b11);
    boolean hasFurnace = (meta >>> 2) == 1;
    
    return this.getDefaultState().withProperty(FACING, facing).withProperty(HAS_FURNACE, hasFurnace);
  }
  
  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(FACING).getHorizontalIndex() | ((state.getValue(HAS_FURNACE) ? 1 : 0) << 2);
  }
  
  @Override
  @Deprecated
  public IBlockState withRotation(IBlockState state, Rotation rot) {
    return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
  }
  
  @Override
  @Deprecated
  public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
    return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
  }
  
  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING, HAS_FURNACE);
  }
  
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ShapelessOreRecipe(
      this,
      "stickWood",
      "stickWood",
      "stickWood",
      "stickWood",
      "stickWood"
    ));
  }
}
