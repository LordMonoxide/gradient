package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientGuiHandler;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.firepit.TileFirePit;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.Random;

public class FirePit extends GradientBlock implements GradientBlockCraftable, ITileEntityProvider {
  private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0d, 0.0d, 0.0d, 1.0d, 0.3d, 1.0d);
  
  public FirePit() {
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
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  
  @Override
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return AABB;
  }
  
  @Override
  public TileFirePit createNewTileEntity(World worldIn, int meta) {
    return new TileFirePit();
  }
  
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
    if(!world.isRemote) {
      //TileFirePit tile = (TileFirePit)world.getTileEntity(pos);
  
      if(!player.isSneaking()) {
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
