package lordmonoxide.gradient.blocks.clayfurnace;

import lordmonoxide.gradient.blocks.GradientBlock;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.ItemBlockProvider;
import lordmonoxide.gradient.blocks.heat.Hardenable;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockClayFurnace extends GradientBlock implements Hardenable, ItemBlockProvider {
  public static final PropertyBool HARDENED = PropertyBool.create("hardened");
  public static final PropertyDirection FACING = BlockHorizontal.FACING;
  
  public BlockClayFurnace() {
    super("clay_furnace", CreativeTabs.TOOLS, GradientBlocks.MATERIAL_CLAY_MACHINE);
    this.setDefaultState(this.blockState.getBaseState().withProperty(HARDENED, false).withProperty(FACING, EnumFacing.SOUTH));
    this.setResistance(5.0f);
    this.setHardness(1.0f);
  }
  
  @Override
  @Deprecated
  public boolean isOpaqueCube(final IBlockState state) {
    return false;
  }
  
  @Override
  public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
    world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
  }
  
  @Override
  @Deprecated
  public IBlockState getStateFromMeta(final int meta) {
    return this
      .getDefaultState()
        .withProperty(HARDENED, (meta & 0b1) == 1)
        .withProperty(FACING, EnumFacing.getHorizontal((meta >>> 1) & 0b11));
  }
  
  @Override
  public int getMetaFromState(final IBlockState state) {
    return (state.getValue(HARDENED) ? 1 : 0) | (state.getValue(FACING).getHorizontalIndex() << 1);
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
    return new BlockStateContainer(this, HARDENED, FACING);
  }
  
  @Override
  public int damageDropped(IBlockState state) {
    return this.getMetaFromState(state.withProperty(FACING, EnumFacing.SOUTH));
  }
  
  @Override
  public IBlockState getHardened(final IBlockState current) {
    return current.withProperty(HARDENED, true);
  }
  
  @Override
  public int getHardeningTime(final IBlockState current) {
    return 180;
  }
  
  @Override
  public boolean isHardened(final IBlockState current) {
    return current.getValue(HARDENED);
  }
  
  @Override
  public String getItemName(final IBlockState state) {
    return this.getUnlocalizedName() + "." + (state.getValue(HARDENED) ? "hardened" : "unhardened");
  }
  
  @Override
  public boolean hasSubBlocks() {
    return true;
  }
  
  @Override
  public void getSubItems(final ItemBlock item, final CreativeTabs tab, final NonNullList<ItemStack> subItems) {
    subItems.add(new ItemStack(item, 1, this.getMetaFromState(this.getDefaultState())));
    subItems.add(new ItemStack(item, 1, this.getMetaFromState(this.getDefaultState().withProperty(HARDENED, true))));
  }
}
