package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.heat.HeatSinkerBlock;
import lordmonoxide.gradient.items.FireStarter;
import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.tileentities.TileFirePit;
import lordmonoxide.gradient.utils.AgeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.Random;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public class BlockFirePit extends HeatSinkerBlock {
  private static final VoxelShape SHAPE = Block.makeCuboidShape(0.0d, 0.0d, 0.0d, 1.0d, 0.25d, 1.0d);

  public static final DirectionProperty FACING = BlockHorizontal.HORIZONTAL_FACING;
  public static final BooleanProperty HAS_FURNACE = BooleanProperty.create("has_furnace");

  public BlockFirePit() {
    super("fire_pit", Properties.create(Material.WOOD, MaterialColor.RED).hardnessAndResistance(1.0f, 5.0f));
    this.setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.NORTH).with(HAS_FURNACE, false));
  }

  @Override
  public int getItemsToDropCount(final IBlockState state, final int fortune, final World world, final BlockPos pos, final Random random) {
    return random.nextInt(3) + 2;
  }

  @Override
  public IItemProvider getItemDropped(final IBlockState state, final World world, final BlockPos pos, final int fortune) {
    return Items.STICK;
  }

  @Override
  public void getDrops(final IBlockState state, final NonNullList<ItemStack> drops, final World world, final BlockPos pos, final int fortune) {
    super.getDrops(state, drops, world, pos, fortune);

    if(state.get(HAS_FURNACE)) {
      drops.add(new ItemStack(GradientBlocks.CLAY_FURNACE_HARDENED));
    }
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public boolean isFullCube(final IBlockState state) {
    return false;
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public VoxelShape getShape(final IBlockState state, final IBlockReader source, final BlockPos pos) {
    if(!state.get(HAS_FURNACE)) {
      return SHAPE;
    }

    return VoxelShapes.fullCube();
  }

  @Override
  public int getLightValue(final IBlockState state, final IWorldReader world, final BlockPos pos) {
    final IBlockState other = world.getBlockState(pos);
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
  public TileFirePit createTileEntity(final IBlockState state, final IBlockReader world) {
    return new TileFirePit();
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean onBlockActivated(final IBlockState state, final World world, final BlockPos pos, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
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
              held.damageItem(1, player);
            }

            firepit.light();
            return true;
          }
        }

        if(Block.getBlockFromItem(held.getItem()) instanceof BlockTorchUnlit) {
          if(firepit.isBurning()) {
            player.setHeldItem(hand, new ItemStack(((BlockTorchUnlit)((ItemBlock)held.getItem()).getBlock()).lit, held.getCount()));
            return true;
          }
        }

        if(Block.getBlockFromItem(held.getItem()) == GradientBlocks.CLAY_FURNACE_HARDENED) {
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
  public static void blockPlacedHandler(final BlockEvent.PlaceEvent event) {
    final IWorld world = event.getWorld();
    final BlockPos pos = event.getPos();

    final Age age = AgeUtils.getPlayerAge(event.getPlayer());

    blockPlacedPos.setPos(pos);
    updateFirePit(world, blockPlacedPos.move(EnumFacing.NORTH), pos, age);
    updateFirePit(world, blockPlacedPos.move(EnumFacing.EAST), pos, age);
    updateFirePit(world, blockPlacedPos.move(EnumFacing.SOUTH), pos, age);
    updateFirePit(world, blockPlacedPos.move(EnumFacing.SOUTH), pos, age);
    updateFirePit(world, blockPlacedPos.move(EnumFacing.WEST), pos, age);
    updateFirePit(world, blockPlacedPos.move(EnumFacing.WEST), pos, age);
    updateFirePit(world, blockPlacedPos.move(EnumFacing.NORTH), pos, age);
    updateFirePit(world, blockPlacedPos.move(EnumFacing.NORTH), pos, age);
  }

  private static void updateFirePit(final IBlockReader world, final BlockPos firePitPos, final BlockPos placedPos, final Age age) {
    final TileEntity te = world.getTileEntity(firePitPos);

    if(te instanceof TileFirePit) {
      ((TileFirePit)te).updateHardenable(placedPos, age);
    }
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

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(final IBlockReader world, final IBlockState state, final BlockPos pos, final EnumFacing face) {
    if(state.get(HAS_FURNACE)) {
      return GradientBlocks.CLAY_FURNACE_HARDENED.getBlockFaceShape(world, state, pos, face);
    }

    return BlockFaceShape.UNDEFINED;
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
    builder.add(FACING, HAS_FURNACE);
  }
}
