package lordmonoxide.gradient.blocks.kinetic.flywheel;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlock;
import lordmonoxide.gradient.blocks.GradientBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.Random;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public class BlockFlywheelStructure extends GradientBlock {
  private static final PropertyDirection FACING = PropertyDirection.create("facing");

  public BlockFlywheelStructure() {
    super("flywheel_structure", Material.CIRCUITS);
  }

/*
  @Override
  public void harvestBlock(final World world, final EntityPlayer player, final BlockPos pos, final IBlockState state, @Nullable final TileEntity te, final ItemStack stack) {
    final BlockPos flywheelPos = this.findFlywheel(world, pos);

    if(flywheelPos == null) {
      return;
    }

    GradientBlocks.FLYWHEEL.harvestBlock(world, player, flywheelPos, world.getBlockState(flywheelPos), te, stack);
    world.setBlockToAir(flywheelPos);
  }
*/

  @SubscribeEvent
  public static void onBlockBreak(final BlockEvent.BreakEvent event) {
    if(event.getState().getBlock() != GradientBlocks.FLYWHEEL_STRUCTURE) {
      return;
    }

    final BlockPos flywheelPos = findFlywheel(event.getWorld(), event.getPos());

    if(flywheelPos == null) {
      return;
    }

    event.getWorld().setBlockToAir(flywheelPos);
  }

  @Nullable
  private static BlockPos findFlywheel(final World world, final BlockPos myPos) {
    BlockPos pos = myPos;
    IBlockState state;

    do {
      pos = pos.offset(world.getBlockState(pos).getValue(FACING));
      state = world.getBlockState(pos);
    } while(state.getBlock() == GradientBlocks.FLYWHEEL_STRUCTURE);

    if(state.getBlock() == GradientBlocks.FLYWHEEL) {
      return pos;
    }

    return null;
  }

  @Override
  public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
    return Items.AIR;
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
  public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer, final EnumHand hand) {
    return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING, facing);
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public IBlockState getStateFromMeta(final int meta) {
    return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta));
  }

  @Override
  public int getMetaFromState(final IBlockState state) {
    return state.getValue(FACING).getIndex();
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public IBlockState withRotation(final IBlockState state, final Rotation rot) {
    return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public IBlockState withMirror(final IBlockState state, final Mirror mirror) {
    return state.withRotation(mirror.toRotation(state.getValue(FACING)));
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING);
  }
}
