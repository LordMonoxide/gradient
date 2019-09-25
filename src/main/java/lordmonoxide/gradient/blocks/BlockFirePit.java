package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientSounds;
import lordmonoxide.gradient.blocks.heat.HeatSinkerBlock;
import lordmonoxide.gradient.items.FireStarter;
import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.tileentities.TileFirePit;
import lordmonoxide.gradient.utils.AgeUtils;
import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Random;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public class BlockFirePit extends HeatSinkerBlock {
  private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0d, 0.0d, 0.0d, 1.0d, 0.25d, 1.0d);

  public static final PropertyDirection FACING = BlockHorizontal.FACING;
  public static final PropertyBool HAS_FURNACE = PropertyBool.create("has_furnace");

  public BlockFirePit() {
    super("fire_pit", CreativeTabs.TOOLS, Material.WOOD, MapColor.RED); //$NON-NLS-1$
    this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(HAS_FURNACE, false));
    this.setLightOpacity(0);
    this.setResistance(5.0f);
    this.setHardness(1.0f);
  }

  @Override
  public int quantityDropped(final Random rand) {
    return rand.nextInt(3) + 2;
  }

  @Override
  public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
    return Items.STICK;
  }

  @Override
  public void getDrops(final NonNullList<ItemStack> drops, final IBlockAccess world, final BlockPos pos, final IBlockState state, final int fortune) {
    super.getDrops(drops, world, pos, state, fortune);

    if(state.getValue(HAS_FURNACE)) {
      drops.add(new ItemStack(GradientBlocks.CLAY_FURNACE_HARDENED));
    }
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public boolean isOpaqueCube(final IBlockState state) {
    return false;
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
  public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
    if(!state.getValue(HAS_FURNACE)) {
      return AABB;
    }

    return Block.FULL_BLOCK_AABB;
  }

  @Override
  public int getLightValue(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
    final IBlockState other = world.getBlockState(pos);
    if(other.getBlock() != this) {
      return other.getLightValue(world, pos);
    }

    final TileFirePit te = WorldUtils.getTileEntity(world, pos, TileFirePit.class);

    if(te != null) {
      return te.getLightLevel(state);
    }

    return super.getLightValue(state, world, pos);
  }

  @Override
  public TileFirePit createTileEntity(final World world, final IBlockState state) {
    return new TileFirePit();
  }

  @Override
  public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
    if(!world.isRemote) {
      final TileFirePit firepit = WorldUtils.getTileEntity(world, pos, TileFirePit.class);

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

            world.playSound(null, pos, GradientSounds.FIRE_STARTER, SoundCategory.NEUTRAL, 1.0f, world.rand.nextFloat() * 0.1f + 0.9f);
            firepit.light();
            return true;
          }
        }

        if(Block.getBlockFromItem(held.getItem()) instanceof BlockTorchUnlit) {
          if(firepit.isBurning()) {
            world.playSound(null, pos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.NEUTRAL, 0.15f, world.rand.nextFloat() * 0.1f + 0.9f);
            player.setHeldItem(hand, new ItemStack(((BlockTorchUnlit)((ItemBlock)held.getItem()).getBlock()).lit, held.getCount()));
            return true;
          }
        }

        if(Block.getBlockFromItem(held.getItem()) == GradientBlocks.CLAY_FURNACE_HARDENED) {
          if(!state.getValue(HAS_FURNACE)) {
            world.playSound(null, pos, SoundEvents.BLOCK_STONE_PLACE, SoundCategory.NEUTRAL, 1.0f, world.rand.nextFloat() * 0.1f + 0.9f);
            world.setBlockState(pos, state.withProperty(HAS_FURNACE, true));

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
        final ItemStack remaining = firepit.insertItem(held.copy(), player);

        if(remaining.getCount() != held.getCount()) {
          world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7f + 1.0f) * 2.0f);
        }

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
    if(!(event.getEntity() instanceof EntityPlayer)) {
      return;
    }

    final World world = event.getWorld();
    final BlockPos pos = event.getPos();

    final Age age = AgeUtils.getPlayerAge((EntityLivingBase)event.getEntity());

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

  private static void updateFirePit(final World world, final BlockPos firePitPos, final BlockPos placedPos, final Age age) {
    final TileFirePit te = WorldUtils.getTileEntity(world, firePitPos, TileFirePit.class);

    if(te != null) {
      te.updateHardenable(placedPos, age);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
    return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
  }

  @Override
  public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
    final TileFirePit te = WorldUtils.getTileEntity(world, pos, TileFirePit.class);

    if(te != null) {
      te.updateSurroundingHardenables(AgeUtils.getPlayerAge(placer));
    }
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public IBlockState getStateFromMeta(final int meta) {
    final EnumFacing facing = EnumFacing.byHorizontalIndex(meta & 0b11);
    final boolean hasFurnace = meta >>> 2 == 1;

    return this.getDefaultState().withProperty(FACING, facing).withProperty(HAS_FURNACE, hasFurnace);
  }

  @Override
  public int getMetaFromState(final IBlockState state) {
    return state.getValue(FACING).getHorizontalIndex() | (state.getValue(HAS_FURNACE) ? 1 : 0) << 2;
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public boolean isSideSolid(final IBlockState state, final IBlockAccess world, final BlockPos pos, final EnumFacing side) {
    if(state.getValue(HAS_FURNACE)) {
      return GradientBlocks.CLAY_FURNACE_HARDENED.isSideSolid(state, world, pos, side);
    }

    return false;
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(final IBlockAccess world, final IBlockState state, final BlockPos pos, final EnumFacing face) {
    if(state.getValue(HAS_FURNACE)) {
      return GradientBlocks.CLAY_FURNACE_HARDENED.getBlockFaceShape(world, state, pos, face);
    }

    return BlockFaceShape.UNDEFINED;
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public IBlockState withRotation(final IBlockState state, final Rotation rot) {
    return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public IBlockState withMirror(final IBlockState state, final Mirror mirror) {
    return state.withRotation(mirror.toRotation(state.getValue(FACING)));
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer.Builder(this).add(FACING, HAS_FURNACE).build();
  }
}
