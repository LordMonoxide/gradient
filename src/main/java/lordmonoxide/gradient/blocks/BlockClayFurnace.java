package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientMaterials;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.List;

public class BlockClayFurnace extends Block {
  public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

  public static BlockClayFurnace hardened() {
    return new BlockClayFurnace(true);
  }

  public static BlockClayFurnace unhardened() {
    return new BlockClayFurnace(false);
  }

  private final boolean hardened;

  protected BlockClayFurnace(final boolean hardened) {
    super(Properties.create(hardened ? GradientMaterials.MATERIAL_CLAY_MACHINE : Material.CLAY).hardnessAndResistance(1.0f, hardened ? 5.0f : 2.0f));
    this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
    this.hardened = hardened;
  }

  @Override
  public void addInformation(final ItemStack stack, @Nullable final IBlockReader world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
    super.addInformation(stack, world, tooltip, flag);

    if(!this.hardened) {
      tooltip.add(new TranslationTextComponent("unhardened_clay.tooltip"));
    } else {
      tooltip.add(new TranslationTextComponent("block.gradient.clay_furnace_hardened.tooltip"));
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isSolid(final BlockState state) {
    return false;
  }

  @Override
  public BlockState getStateForPlacement(final BlockItemUseContext context) {
    return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
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
