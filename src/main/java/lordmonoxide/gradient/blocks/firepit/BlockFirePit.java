package lordmonoxide.gradient.blocks.firepit;

import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.heat.HeatSinkerBlock;
import lordmonoxide.gradient.blocks.torch.BlockTorchUnlit;
import lordmonoxide.gradient.items.FireStarter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Random;

public class BlockFirePit extends HeatSinkerBlock {
  private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0d, 0.0d, 0.0d, 1.0d, 0.3d, 1.0d);

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

    final TileEntity te = world.getTileEntity(pos);

    if(te instanceof TileFirePit) {
      return ((TileFirePit)te).getLightLevel(state);
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
          if(!state.getValue(HAS_FURNACE)) {
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

        if(!player.isCreative()) {
          player.setHeldItem(hand, remaining);
        }

        return true;
      }
    }

    return true;
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public void neighborChanged(final IBlockState state, final World world, final BlockPos pos, final Block block, final BlockPos neighbor) {
    super.neighborChanged(state, world, pos, block, neighbor);

    final TileEntity te = world.getTileEntity(pos);

    if(te instanceof TileFirePit) {
      ((TileFirePit)te).updateHardenable(neighbor);
    }
  }

  @Override
  public void onBlockPlacedBy(final World world, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
    world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
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
    return new BlockStateContainer(this, FACING, HAS_FURNACE);
  }
}
