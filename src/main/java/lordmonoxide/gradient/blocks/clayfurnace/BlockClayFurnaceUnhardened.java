package lordmonoxide.gradient.blocks.clayfurnace;

import lordmonoxide.gradient.blocks.GradientBlock;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.heat.Hardenable;
import lordmonoxide.gradient.recipes.GradientCraftable;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockClayFurnaceUnhardened extends GradientBlock implements GradientCraftable, Hardenable {
  public static final PropertyDirection FACING = BlockHorizontal.FACING;
  
  public BlockClayFurnaceUnhardened() {
    super("clay_furnace_unhardened", CreativeTabs.TOOLS, Material.CLAY, MapColor.CLAY);
    this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    this.setResistance(2.0f);
    this.setHardness(1.0f);
  }
  
  @Override
  public void addRecipe() {
    GameRegistry.addShapedRecipe(
      new ItemStack(this),
      "CCC",
      "C C",
      "CCC",
      'C', Items.CLAY_BALL
    );
  }
  
  @Override
  @Deprecated
  public boolean isOpaqueCube(final IBlockState state) {
    return false;
  }
  
  @Override
  public IBlockState getHardened(final IBlockState current) {
    return GradientBlocks.CLAY_FURNACE.getDefaultState().withProperty(FACING, current.getValue(FACING));
  }
  
  @Override
  public int getHardeningTime() {
    return 180;
  }
  
  @Override
  public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
    world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
  }
  
  @Override
  @Deprecated
  public IBlockState getStateFromMeta(final int meta) {
    return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
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
}
