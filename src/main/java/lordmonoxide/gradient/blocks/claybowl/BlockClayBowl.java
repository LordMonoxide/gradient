package lordmonoxide.gradient.blocks.claybowl;

import lordmonoxide.gradient.blocks.GradientBlock;
import lordmonoxide.gradient.blocks.ItemBlockProvider;
import lordmonoxide.gradient.blocks.heat.Hardenable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockClayBowl extends GradientBlock implements Hardenable, ItemBlockProvider {
  private static final AxisAlignedBB AABB = new AxisAlignedBB(3.0d / 16.0d, 0.0d, 3.0d / 16.0d, 1.0d - 3.0d / 16.0d, 0.5d, 1.0d - 3.0d / 16.0d);
  
  public static final PropertyBool HARDENED = PropertyBool.create("hardened");
  
  public BlockClayBowl() {
    super("clay_bowl", CreativeTabs.TOOLS, Material.CLAY, MapColor.CLAY);
    this.setDefaultState(this.blockState.getBaseState().withProperty(HARDENED, false));
    this.setResistance(2.0f);
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
  @Deprecated
  public IBlockState getStateFromMeta(final int meta) {
    final boolean hardened = meta == 1;
  
    return this.getDefaultState().withProperty(HARDENED, hardened);
  }
  
  @Override
  public int getMetaFromState(final IBlockState state) {
    return state.getValue(HARDENED) ? 1 : 0;
  }
  
  @Override
  public int damageDropped(final IBlockState state) {
    return this.getMetaFromState(state);
  }
  
  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, HARDENED);
  }
  
  @Override
  public IBlockState getHardened(final IBlockState current) {
    return this.getDefaultState().withProperty(HARDENED, true);
  }
  
  @Override
  public int getHardeningTime(final IBlockState current) {
    return 30;
  }
  
  @Override
  public boolean isHardened(final IBlockState current) {
    return current.getValue(HARDENED);
  }
  
  @Override
  public String getItemName(final IBlockState state) {
    return this.getUnlocalizedName() + '.' + (state.getValue(HARDENED) ? "hardened" : "unhardened");
  }
  
  @Override
  public boolean hasSubBlocks() {
    return true;
  }
}
