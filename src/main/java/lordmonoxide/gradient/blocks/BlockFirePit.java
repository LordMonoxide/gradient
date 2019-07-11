package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.heat.HeatSinkerBlock;
import lordmonoxide.gradient.items.FireStarter;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.tileentities.TileFirePit;
import lordmonoxide.gradient.utils.AgeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.Random;

@Mod.EventBusSubscriber(modid = GradientMod.MOD_ID)
public class BlockFirePit extends HeatSinkerBlock {
  private static final VoxelShape SHAPE = Block.makeCuboidShape(0.0d, 0.0d, 0.0d, 16.0d, 4.0d, 16.0d);

  public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
  public static final BooleanProperty HAS_FURNACE = BooleanProperty.create("has_furnace");

  public BlockFirePit() {
    super(Properties.create(Material.WOOD, MaterialColor.RED).hardnessAndResistance(1.0f, 5.0f));
    this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(HAS_FURNACE, false));
  }

  @Override
  public int getItemsToDropCount(final BlockState state, final int fortune, final World world, final BlockPos pos, final Random random) {
    return random.nextInt(3) + 2;
  }

  @Override
  public IItemProvider getItemDropped(final BlockState state, final World world, final BlockPos pos, final int fortune) {
    return Items.STICK;
  }

  @Override
  public void getDrops(final BlockState state, final NonNullList<ItemStack> drops, final World world, final BlockPos pos, final int fortune) {
    super.getDrops(state, drops, world, pos, fortune);

    if(state.get(HAS_FURNACE)) {
      drops.add(new ItemStack(GradientBlocks.CLAY_FURNACE_HARDENED));
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isSolid(final BlockState state) {
    return false;
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public VoxelShape getShape(final BlockState state, final IBlockReader source, final BlockPos pos, final ISelectionContext context) {
    if(!state.get(HAS_FURNACE)) {
      return SHAPE;
    }

    return VoxelShapes.fullCube();
  }

  @Override
  public int getLightValue(final BlockState state, final IEnviromentBlockReader world, final BlockPos pos) {
    final BlockState other = world.getBlockState(pos);
    if(other.getBlock() != this) {
      return other.getLightValue(world, pos);
    }

    final TileEntity te = world.getTileEntity(pos);

    if(te instanceof TileFirePit) {
      return ((TileFirePit)te).getLightLevel(state);
    }

    return super.getLightValue(state, world, pos);
  }

  @Override
  public TileFirePit createTileEntity(final BlockState state, final IBlockReader world) {
    return new TileFirePit();
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit) {
    if(!world.isRemote) {
      final TileFirePit firepit = (TileFirePit)world.getTileEntity(pos);

      if(firepit == null) {
        return false;
      }

      final ItemStack held = player.getHeldItem(hand);

      if(!player.isSneaking()) {
        if(held.getItem() instanceof FireStarter) {
          if(!firepit.isBurning()) {
            if(!player.isCreative()) {
              held.damageItem(1, player, e -> e.sendBreakAnimation(e.getActiveHand()));
            }

            firepit.light();
            return true;
          }
        }

        if(Block.getBlockFromItem(held.getItem()) instanceof BlockTorchUnlit) {
          if(firepit.isBurning()) {
            player.setHeldItem(hand, new ItemStack(((BlockTorchUnlit)((BlockItem)held.getItem()).getBlock()).lit.get(), held.getCount()));
            return true;
          }
        }

        if(held.getItem() == GradientItems.CLAY_FURNACE_HARDENED) {
          if(!state.get(HAS_FURNACE)) {
            world.setBlockState(pos, state.with(HAS_FURNACE, true));

            // Changing the blockstate replaces the tile entity... swap it
            // back to the old one.  Not sure how terrible doing this is.
            firepit.validate();
            world.setTileEntity(pos, firepit);
            firepit.attachFurnace();

            if(!player.isCreative()) {
              held.shrink(1);
            }

            return true;
          }
        }
      }

      // Remove input
      if(player.isSneaking()) {
        if(firepit.hasInput()) {
          final ItemStack input = firepit.takeInput();
          ItemHandlerHelper.giveItemToPlayer(player, input);
          return true;
        }

        if(!firepit.isBurning()) {
          for(int slot = 0; slot < TileFirePit.FUEL_SLOTS_COUNT; slot++) {
            if(firepit.hasFuel(slot)) {
              final ItemStack fuel = firepit.takeFuel(slot);
              ItemHandlerHelper.giveItemToPlayer(player, fuel);
              return true;
            }
          }
        }

        return true;
      }

      // Take stuff out
      if(firepit.hasOutput()) {
        final ItemStack output = firepit.takeOutput();
        ItemHandlerHelper.giveItemToPlayer(player, output);
        return true;
      }

      // Put stuff in
      if(!held.isEmpty()) {
        final ItemStack remaining = firepit.insertItem(held.copy(), player, state);

        if(!player.isCreative()) {
          player.setHeldItem(hand, remaining);
        }

        return true;
      }
    }

    return true;
  }

  private static final BlockPos.MutableBlockPos blockPlacedPos = new BlockPos.MutableBlockPos();

  @SubscribeEvent
  public static void blockPlacedHandler(final BlockEvent.EntityPlaceEvent event) {
    final IWorld world = event.getWorld();
    final BlockPos pos = event.getPos();

    final Age age = AgeUtils.getPlayerAge(event.getEntity());

    blockPlacedPos.setPos(pos);
    updateFirePit(world, blockPlacedPos.move(Direction.NORTH), pos, age);
    updateFirePit(world, blockPlacedPos.move(Direction.EAST), pos, age);
    updateFirePit(world, blockPlacedPos.move(Direction.SOUTH), pos, age);
    updateFirePit(world, blockPlacedPos.move(Direction.SOUTH), pos, age);
    updateFirePit(world, blockPlacedPos.move(Direction.WEST), pos, age);
    updateFirePit(world, blockPlacedPos.move(Direction.WEST), pos, age);
    updateFirePit(world, blockPlacedPos.move(Direction.NORTH), pos, age);
    updateFirePit(world, blockPlacedPos.move(Direction.NORTH), pos, age);
  }

  private static void updateFirePit(final IBlockReader world, final BlockPos firePitPos, final BlockPos placedPos, final Age age) {
    final TileEntity te = world.getTileEntity(firePitPos);

    if(te instanceof TileFirePit) {
      ((TileFirePit)te).updateHardenable(placedPos, age);
    }
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
    builder.add(FACING, HAS_FURNACE);
  }
}
