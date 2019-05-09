package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMaterials;
import lordmonoxide.gradient.blocks.heat.HeatSinkerBlock;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.items.ItemClayCastUnhardened;
import lordmonoxide.gradient.science.geology.Metal;
import lordmonoxide.gradient.science.geology.Metals;
import lordmonoxide.gradient.tileentities.TileClayCrucible;
import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;

public class BlockClayCrucibleHardened extends HeatSinkerBlock {
  private static final VoxelShape SHAPE = Block.makeCuboidShape(1.0d, 0.0d, 1.0d, 15.0d, 15.0d, 15.0d);
  public static final IUnlistedProperty<FluidStack> FLUID = new IUnlistedProperty<>() {
    @Override
    public String getName() {
      return "fluid";
    }

    @Override
    public boolean isValid(final FluidStack value) {
      return true;
    }

    @Override
    public Class<FluidStack> getType() {
      return FluidStack.class;
    }

    @Override
    public String valueToString(final FluidStack value) {
      return value.getFluid().getName();
    }
  };

  public BlockClayCrucibleHardened() {
    super(Properties.create(GradientMaterials.MATERIAL_CLAY_MACHINE).hardnessAndResistance(1.0f, 5.0f));
  }

  @Override
  public void addInformation(final ItemStack stack, @Nullable final IBlockReader world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
    super.addInformation(stack, world, tooltip, flag);
    tooltip.add(new TextComponentTranslation("block.gradient.clay_crucible_hardened.tooltip"));
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(final IBlockReader world, final IBlockState state, final BlockPos pos, final EnumFacing face) {
    return BlockFaceShape.UNDEFINED;
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public boolean isFullCube(final IBlockState state) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isSolid(final IBlockState state) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(final IBlockState state, final IBlockReader world, final BlockPos pos) {
    return SHAPE;
  }

  @Override
  public int getLightValue(final IBlockState state, final IWorldReader world, final BlockPos pos) {
    final IBlockState other = world.getBlockState(pos);
    if(other.getBlock() != this) {
      return other.getLightValue(world, pos);
    }

    final TileEntity te = world.getTileEntity(pos);

    if(te instanceof TileClayCrucible) {
      return ((TileClayCrucible)te).getLightLevel();
    }

    return state.getLightValue(world, pos);
  }

  @Override
  public TileClayCrucible createTileEntity(final IBlockState state, final IBlockReader world) {
    return new TileClayCrucible();
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean onBlockActivated(final IBlockState state, final World world, final BlockPos pos, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
    if(!world.isRemote) {
      if(!player.isSneaking()) {
        final TileClayCrucible te = (TileClayCrucible)world.getTileEntity(pos);

        if(te == null) {
          return false;
        }

        final ItemStack stack = player.getHeldItem(hand);

        // Cast item
        if(stack.getItem() instanceof ItemBlock && !(stack.getItem() instanceof ItemClayCastUnhardened) && ((ItemBlock)stack.getItem()).getBlock() instanceof BlockClayCast) {
          final GradientCasts.Cast cast = ((BlockClayCast)((ItemBlock)stack.getItem()).getBlock()).cast;

          if(te.getMoltenMetal() == null) {
            player.sendMessage(new TextComponentTranslation("block.gradient.clay_crucible.no_metal").setStyle(new Style().setColor(TextFormatting.RED)));
            return true;
          }

          final Metal metal = Metals.get(te.getMoltenMetal().getFluid());
          final int amount = cast.amountForMetal(metal);

          if(te.getMoltenMetal().amount < amount) {
            player.sendMessage(new TextComponentTranslation("block.gradient.clay_crucible.not_enough_metal", amount).setStyle(new Style().setColor(TextFormatting.RED)));
            return true;
          }

          if(!cast.isValidForMetal(metal)) {
            player.sendMessage(new TextComponentTranslation("block.gradient.clay_crucible.metal_cant_make_tools").setStyle(new Style().setColor(TextFormatting.RED)));
            return true;
          }

          if(!player.isCreative()) {
            stack.shrink(1);

            te.consumeMetal(amount);
          }

          ItemHandlerHelper.giveItemToPlayer(player, GradientItems.castItem(cast, metal, 1));
          return true;
        }

        if(FluidUtil.getFluidHandler(player.getHeldItem(hand)) != null) {
          final LazyOptional<FluidStack> fluid = FluidUtil.getFluidContained(player.getHeldItem(hand));

          // Make sure the fluid handler is either empty, or contains metal
          if(fluid.isPresent()) {
            //TODO: null
            final Metal metal = Metals.get(fluid.orElse(null).getFluid());

            if(metal == Metals.INVALID_METAL) {
              return true;
            }
          }

          te.useBucket(player, hand, world, pos, side);

          return true;
        }

        NetworkHooks.openGui((EntityPlayerMP)player, te, pos);
      }
    }

    return true;
  }

  @Override
  protected void fillStateContainer(final StateContainer.Builder<Block, IBlockState> builder) {
    //TODO builder.add(FLUID);
  }

  @Override
  public IBlockState getExtendedState(final IBlockState state, final IBlockReader world, final BlockPos pos) {
    final IExtendedBlockState extendedState = (IExtendedBlockState)state;

    final TileClayCrucible te = WorldUtils.getTileEntity(world, pos, TileClayCrucible.class);

    if(te != null) {
      return extendedState.withProperty(FLUID, te.tank.getFluid());
    }

    return extendedState;
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }

  @Override
  public boolean canRenderInLayer(final IBlockState state, final BlockRenderLayer layer) {
    return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.TRANSLUCENT;
  }
}
