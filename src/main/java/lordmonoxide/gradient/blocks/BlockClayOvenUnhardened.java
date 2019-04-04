package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.tileentities.TileFirePit;
import lordmonoxide.gradient.utils.AgeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockClayOvenUnhardened extends GradientBlock {
  private static final VoxelShape SHAPE = Block.makeCuboidShape(2.0d / 16.0d, 0.0d, 2.0d / 16.0d, 14.0d / 16.0d, 6.0d / 16.0d, 14.0d / 16.0d);

  public static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;

  public BlockClayOvenUnhardened() {
    super("clay_oven.unhardened", Properties.create(Material.CLAY).hardnessAndResistance(1.0f, 2.0f));
    this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.NORTH));
  }

  @Override
  public void addInformation(final ItemStack stack, @Nullable final IBlockReader world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
    super.addInformation(stack, world, tooltip, flag);
    tooltip.add(new TextComponentTranslation("unhardened_clay.tooltip"));
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(final IBlockReader world, final IBlockState state, final BlockPos pos, final EnumFacing face) {
    return BlockFaceShape.UNDEFINED;
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public boolean isFullCube(final IBlockState state) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public VoxelShape getShape(final IBlockState state, final IBlockReader source, final BlockPos pos) {
    return SHAPE;
  }

  @Override
  public IBlockState getStateForPlacement(final BlockItemUseContext context) {
    return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
  }

  @Override
  public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, @Nullable final EntityLivingBase placer, final ItemStack stack) {
    final TileEntity te = world.getTileEntity(pos);

    if(te instanceof TileFirePit) {
      ((TileFirePit)te).updateSurroundingHardenables(AgeUtils.getPlayerAge(placer));
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState rotate(final IBlockState state, final Rotation rot) {
    return state.with(FACING, rot.rotate(state.get(FACING)));
  }

  @SuppressWarnings("deprecation")
  @Override
  public IBlockState mirror(final IBlockState state, final Mirror mirror) {
    return state.rotate(mirror.toRotation(state.get(FACING)));
  }

  @Override
  protected void fillStateContainer(final StateContainer.Builder<Block, IBlockState> builder) {
    builder.add(FACING);
  }
}
