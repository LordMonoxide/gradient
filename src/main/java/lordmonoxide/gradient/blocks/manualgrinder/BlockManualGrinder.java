package lordmonoxide.gradient.blocks.manualgrinder;

import lordmonoxide.gradient.blocks.GradientBlock;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraftforge.common.ForgeHooks;

public class BlockManualGrinder extends GradientBlock {
  private static final AxisAlignedBB AABB = new AxisAlignedBB(1.0d / 16.0d, 0.0d, 1.0d / 16.0d, 15.0d / 16.0d, 2.0d / 16.0d, 15.0d / 16.0d);

  public static final PropertyDirection FACING = BlockHorizontal.FACING;

  public BlockManualGrinder() {
    super("manual_grinder", CreativeTabs.TOOLS, Material.ROCK);
    this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    this.setLightOpacity(0);
    this.setResistance(5.0f);
    this.setHardness(1.0f);
  }

  @Override
  public TileManualGrinder createTileEntity(final World world, final IBlockState state) {
    return new TileManualGrinder();
  }

  @Override
  public boolean hasTileEntity(final IBlockState state) {
    return true;
  }

  @Override
  public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
    if(!world.isRemote) {
      final TileEntity tile = world.getTileEntity(pos);

      if(!(tile instanceof TileManualGrinder)) {
        return false;
      }

      final TileManualGrinder grinder = (TileManualGrinder)tile;

      // Remove input
      if(player.isSneaking()) {
        if(grinder.hasInput()) {
          final ItemStack stack = grinder.takeInput();

          if(!player.inventory.addItemStackToInventory(stack)) {
            ForgeHooks.onPlayerTossEvent(player, stack, false);
          }
        }

        return true;
      }

      // Take stuff out
      if(grinder.hasOutput()) {
        final ItemStack stack = grinder.takeOutput();

        if(!player.inventory.addItemStackToInventory(stack)) {
          ForgeHooks.onPlayerTossEvent(player, stack, false);
        }

        return true;
      }

      final ItemStack held = player.getHeldItem(hand);

      // Put stuff in
      if(!held.isEmpty()) {
        final ItemStack remaining = grinder.insertItem(held);
        player.setHeldItem(hand, remaining);
        return true;
      }

      grinder.crank();
      return true;
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
    final EnumFacing facing = EnumFacing.byHorizontalIndex(meta);
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
}
