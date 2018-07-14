package lordmonoxide.gradient.blocks.firepit;

import lordmonoxide.gradient.GradientGuiHandler;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.clayfurnace.BlockClayFurnace;
import lordmonoxide.gradient.blocks.heat.HeatSinkerBlock;
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
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

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
      final Block furnace = GradientBlocks.CLAY_FURNACE;
      drops.add(new ItemStack(furnace, 1, furnace.getMetaFromState(furnace.getDefaultState().withProperty(BlockClayFurnace.HARDENED, true))));
    }
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

    return state.getLightValue(world, pos);
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {
    return true;
  }

  @Override
  public TileFirePit createTileEntity(World world, IBlockState state) {
    return new TileFirePit();
  }

  @Override
  public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
    if(!world.isRemote) {
      if(!player.isSneaking()) {
        final ItemStack stack = player.getHeldItem(hand);

        if(stack.getItem() instanceof FireStarter) {
          final TileFirePit tile = (TileFirePit)world.getTileEntity(pos);

          if(tile == null) {
            return false;
          }

          if(!tile.isBurning()) {
            tile.light();
            return true;
          }
        }

        if(stack.getItem() instanceof ItemBlock && ((ItemBlock)stack.getItem()).getBlock() instanceof BlockClayFurnace) {
          if(!state.getValue(HAS_FURNACE)) {
            if(!GradientBlocks.CLAY_FURNACE.getStateFromMeta(stack.getMetadata()).getValue(BlockClayFurnace.HARDENED)) {
              return false;
            }

            final TileFirePit te = (TileFirePit)world.getTileEntity(pos);

            if(te == null) {
              return false;
            }

            world.setBlockState(pos, state.withProperty(HAS_FURNACE, true));

            // Changing the blockstate replaces the tile entity... swap it
            // back to the old one.  Not sure how terrible doing this is.
            te.validate();
            world.setTileEntity(pos, te);
            te.attachFurnace();

            if(!player.isCreative()) {
              stack.shrink(1);
            }

            return true;
          }
        }

        player.openGui(GradientMod.instance, GradientGuiHandler.FIRE_PIT, world, pos.getX(), pos.getY(), pos.getZ());
      }
    }

    return true;
  }

  @Override
  @Deprecated
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
  public IBlockState getStateFromMeta(final int meta) {
    final EnumFacing facing = EnumFacing.byHorizontalIndex(meta & 0b11);
    final boolean hasFurnace = (meta >>> 2) == 1;

    return this.getDefaultState().withProperty(FACING, facing).withProperty(HAS_FURNACE, hasFurnace);
  }

  @Override
  public int getMetaFromState(final IBlockState state) {
    return state.getValue(FACING).getHorizontalIndex() | ((state.getValue(HAS_FURNACE) ? 1 : 0) << 2);
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
    return new BlockStateContainer(this, FACING, HAS_FURNACE);
  }
}
