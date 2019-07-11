package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientMaterials;
import lordmonoxide.gradient.blocks.heat.HeatSinkerBlock;
import lordmonoxide.gradient.tileentities.TileClayOven;
import lordmonoxide.gradient.tileentities.TileFirePit;
import lordmonoxide.gradient.utils.AgeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;

public class BlockClayOvenHardened extends HeatSinkerBlock {
  private static final VoxelShape SHAPE = Block.makeCuboidShape(2.0d, 0.0d, 2.0d, 14.0d, 6.0d, 14.0d);

  public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

  public BlockClayOvenHardened() {
    super(Properties.create(GradientMaterials.MATERIAL_CLAY_MACHINE).hardnessAndResistance(1.0f, 5.0f));
    this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
  }

  @Override
  public void addInformation(final ItemStack stack, @Nullable final IBlockReader world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
    super.addInformation(stack, world, tooltip, flag);
    tooltip.add(new TranslationTextComponent("block.gradient.clay_oven_hardened.tooltip"));
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public VoxelShape getShape(final BlockState state, final IBlockReader source, final BlockPos pos, final ISelectionContext context) {
    return SHAPE;
  }

  @Override
  public TileClayOven createTileEntity(final BlockState state, final IBlockReader world) {
    return new TileClayOven();
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit) {
    if(!world.isRemote) {
      final TileClayOven oven = (TileClayOven)world.getTileEntity(pos);

      if(oven == null) {
        return false;
      }

      // Remove input
      if(player.isSneaking()) {
        if(oven.hasInput()) {
          final ItemStack input = oven.takeInput();
          ItemHandlerHelper.giveItemToPlayer(player, input);
          return true;
        }

        return true;
      }

      // Take stuff out
      if(oven.hasOutput()) {
        final ItemStack output = oven.takeOutput();
        ItemHandlerHelper.giveItemToPlayer(player, output);
        return true;
      }

      final ItemStack held = player.getHeldItem(hand);

      // Put stuff in
      if(!held.isEmpty()) {
        final ItemStack remaining = oven.insertItem(held.copy(), player);

        if(!player.isCreative()) {
          player.setHeldItem(hand, remaining);
        }

        return true;
      }
    }

    return true;
  }

  @Override
  public BlockState getStateForPlacement(final BlockItemUseContext context) {
    return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
  }

  @Override
  public void onBlockPlacedBy(final World world, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer, final ItemStack stack) {
    final TileEntity te = world.getTileEntity(pos);

    if(te instanceof TileFirePit) {
      ((TileFirePit)te).updateSurroundingHardenables(AgeUtils.getPlayerAge(placer));
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState rotate(final BlockState state, final Rotation rot) {
    return state.with(FACING, rot.rotate(state.get(FACING)));
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState mirror(final BlockState state, final Mirror mirror) {
    return state.rotate(mirror.toRotation(state.get(FACING)));
  }

  @Override
  protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }
}
