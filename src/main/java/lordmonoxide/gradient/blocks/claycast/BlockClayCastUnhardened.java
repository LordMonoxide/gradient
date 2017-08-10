package lordmonoxide.gradient.blocks.claycast;

import lordmonoxide.gradient.GradientTools;
import lordmonoxide.gradient.blocks.GradientBlock;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.heat.Hardenable;
import lordmonoxide.gradient.recipes.GradientCraftable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockClayCastUnhardened extends GradientBlock implements GradientCraftable, Hardenable {
  private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0d, 0.0d, 0.0d, 1.0d, 2.0d / 16.0d, 1.0d);
  
  private static final GradientTools.PropertyTool TOOL = GradientTools.PropertyTool.create("tool");
  
  public BlockClayCastUnhardened() {
    super("clay_cast_unhardened", CreativeTabs.TOOLS, Material.CLAY, MapColor.CLAY);
    this.setDefaultState(this.blockState.getBaseState().withProperty(TOOL, GradientTools.PICKAXE));
    this.setResistance(2.0f);
    this.setHardness(1.0f);
  }
  
  @Override
  public void addRecipe() {
    GameRegistry.addShapelessRecipe(
      new ItemStack(this, 1, this.getMetaFromState(this.getDefaultState())),
      Items.CLAY_BALL, Blocks.SAND
    );
  }
  
  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, TOOL);
  }
  
  @Override
  public IBlockState getStateFromMeta(final int meta) {
    return this.getDefaultState().withProperty(TOOL, GradientTools.TYPES.get(meta));
  }
  
  @Override
  public int getMetaFromState(final IBlockState state) {
    return state.getValue(TOOL).id;
  }
  
  @Override
  public int damageDropped(final IBlockState state) {
    return this.getMetaFromState(state);
  }
  
  @Override
  @SuppressWarnings("deprecation")
  public boolean isOpaqueCube(final IBlockState state) {
    return false;
  }
  
  @Override
  @SuppressWarnings("deprecation")
  public boolean isFullCube(final IBlockState state) {
    return false;
  }
  
  @Override
  @SuppressWarnings("deprecation")
  public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
    return AABB;
  }
  
  @Override
  public IBlockState getHardened(final IBlockState current) {
    return GradientBlocks.CLAY_CAST.getDefaultState().withProperty(TOOL, current.getValue(TOOL));
  }
  
  @Override
  public int getHardeningTime() {
    return 60;
  }
}
