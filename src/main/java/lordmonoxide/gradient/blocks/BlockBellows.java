package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.tileentities.TileBellows;
import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BlockBellows extends Block {
  public static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;
  //TODO private static final IUnlistedProperty<IModelState> ANIMATION = Properties.AnimationProperty;

  private static final VoxelShape SHAPE_NORTH = Block.makeCuboidShape(2.0d, 2.0d, 0.0d, 14.0d, 14.0d, 13.0d);
  private static final VoxelShape SHAPE_SOUTH = Block.makeCuboidShape(2.0d, 2.0d, 3.0d, 14.0d, 14.0d,  1.0d);
  private static final VoxelShape SHAPE_EAST  = Block.makeCuboidShape(3.0d, 2.0d, 2.0d,  1.0d, 14.0d, 14.0d);
  private static final VoxelShape SHAPE_WEST  = Block.makeCuboidShape(0.0d, 2.0d, 2.0d, 13.0d, 14.0d, 14.0d);

  public BlockBellows() {
    super(Properties.create(Material.CIRCUITS).hardnessAndResistance(1.0f, 5.0f));
    this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.NORTH));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(final ItemStack stack, @Nullable final IBlockReader world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
    super.addInformation(stack, world, tooltip, flag);
    tooltip.add(new TextComponentTranslation("block.gradient.bellows.tooltip"));
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public boolean onBlockActivated(final IBlockState state, final World world, final BlockPos pos, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
    if(!world.isRemote) {
      final TileBellows bellows = WorldUtils.getTileEntity(world, pos, TileBellows.class);

      if(bellows == null) {
        return false;
      }

      if(!player.isSneaking()) {
        bellows.activate();
        return true;
      }
    }

    return true;
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
  public boolean isSolid(final IBlockState state) {
    return false;
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
    switch(state.get(FACING)) {
      case NORTH:
        return SHAPE_NORTH;
      case SOUTH:
        return SHAPE_SOUTH;
      case EAST:
        return SHAPE_EAST;
      case WEST:
        return SHAPE_WEST;
    }

    return SHAPE_NORTH;
  }

  @Override
  public IBlockState getStateForPlacement(final BlockItemUseContext context) {
    if(context.getFace().getAxis() == EnumFacing.Axis.Y) {
      return super.getStateForPlacement(context).with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    return super.getStateForPlacement(context).with(FACING, context.getFace().getOpposite());
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public IBlockState rotate(final IBlockState state, final Rotation rot) {
    return state.with(FACING, rot.rotate(state.get(FACING)));
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public IBlockState mirror(final IBlockState state, final Mirror mirror) {
    return state.rotate(mirror.toRotation(state.get(FACING)));
  }

  @Override
  protected void fillStateContainer(final StateContainer.Builder<Block, IBlockState> builder) {
    builder.add(FACING); //TODO .add(ANIMATION);
  }

  @Override
  public TileBellows createTileEntity(final IBlockState state, final IBlockReader world) {
    return new TileBellows();
  }

  @Override
  public boolean hasTileEntity(final IBlockState state) {
    return true;
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public EnumBlockRenderType getRenderType(final IBlockState state) {
    return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
  }
}
