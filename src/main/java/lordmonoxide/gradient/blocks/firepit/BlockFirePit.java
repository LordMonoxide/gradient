package lordmonoxide.gradient.blocks.firepit;

import lordmonoxide.gradient.GradientGuiHandler;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlock;
import lordmonoxide.gradient.blocks.GradientBlockCraftable;
import lordmonoxide.gradient.items.FireStarter;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Random;

public class BlockFirePit extends GradientBlock implements GradientBlockCraftable, ITileEntityProvider {
  private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0d, 0.0d, 0.0d, 1.0d, 0.3d, 1.0d);
  
  public BlockFirePit() {
    super("fire_pit", CreativeTabs.TOOLS, Material.WOOD, MapColor.RED); //$NON-NLS-1$
    this.setHardness(0.0f);
    this.setResistance(0.0f);
    this.setLightOpacity(0);
  }
  
  /**
   * Returns the quantity of items to drop on block destruction.
   */
  @Override
  public int quantityDropped(Random rand) {
    return rand.nextInt(5);
  }
  
  /**
   * Get the Item that this Block should drop when harvested.
   */
  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.STICK;
  }
  
  /**
   * Used to determine ambient occlusion and culling when rebuilding chunks for render
   */
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
    
    if(te instanceof TileFirePit) {
      return ((TileFirePit)te).getLightLevel();
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
        if(player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() instanceof FireStarter) {
          TileFirePit tile = (TileFirePit)world.getTileEntity(pos);
          
          if(!tile.isBurning()) {
            tile.light();
          }
        }
        
        player.openGui(GradientMod.instance, GradientGuiHandler.FIRE_PIT, world, pos.getX(), pos.getY(), pos.getZ());
      }
    }
    
    return true;
  }
  
  @Override
  public void addRecipe() {
    GameRegistry.addShapelessRecipe(
      new ItemStack(this),
      Items.STICK,
      Items.STICK,
      Items.STICK,
      Items.STICK,
      Items.STICK
    );
  }
}
