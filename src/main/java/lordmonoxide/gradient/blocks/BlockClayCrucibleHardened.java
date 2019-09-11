package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientGuiHandler;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.heat.HeatSinkerBlock;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.items.ItemClayCastUnhardened;
import lordmonoxide.gradient.science.geology.Metal;
import lordmonoxide.gradient.science.geology.Metals;
import lordmonoxide.gradient.tileentities.TileClayCrucible;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;

public class BlockClayCrucibleHardened extends HeatSinkerBlock {
  public static final IUnlistedProperty<FluidStack> FLUID = new IUnlistedProperty<FluidStack>() {
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

  private static final AxisAlignedBB AABB = new AxisAlignedBB(1.0d / 16.0d, 0.0d, 1.0d / 16.0d, 15.0d / 16.0d, 15.0d / 16.0d, 15.0d / 16.0d);

  public BlockClayCrucibleHardened() {
    super("clay_crucible.hardened", CreativeTabs.TOOLS, GradientBlocks.MATERIAL_CLAY_MACHINE);
    this.setResistance(5.0f);
    this.setHardness(1.0f);
  }

  @Override
  public void addInformation(final ItemStack stack, @Nullable final World worldIn, final List<String> tooltip, final ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
    tooltip.add(I18n.format("tile.clay_crucible.hardened.tooltip"));
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public boolean isSideSolid(final IBlockState state, final IBlockAccess world, final BlockPos pos, final EnumFacing side) {
    return false;
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(final IBlockAccess world, final IBlockState state, final BlockPos pos, final EnumFacing face) {
    return BlockFaceShape.UNDEFINED;
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public boolean isOpaqueCube(final IBlockState state) {
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
  public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
    return AABB;
  }

  @Override
  public int getLightValue(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
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
  public TileClayCrucible createTileEntity(final World world, final IBlockState state) {
    return new TileClayCrucible();
  }

  @Override
  public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
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
            player.sendMessage(new TextComponentTranslation("tile.clay_crucible.no_metal").setStyle(new Style().setColor(TextFormatting.RED)));
            return true;
          }

          final Metal metal = Metals.get(te.getMoltenMetal().getFluid());
          final int amount = cast.amountForMetal(metal);

          if(te.getMoltenMetal().amount < amount) {
            player.sendMessage(new TextComponentTranslation("tile.clay_crucible.not_enough_metal", amount).setStyle(new Style().setColor(TextFormatting.RED)));
            return true;
          }

          if(!cast.isValidForMetal(metal)) {
            player.sendMessage(new TextComponentTranslation("tile.clay_crucible.metal_cant_make_tools").setStyle(new Style().setColor(TextFormatting.RED)));
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
          final FluidStack fluid = FluidUtil.getFluidContained(player.getHeldItem(hand));

          // Make sure the fluid handler is either empty, or contains metal
          if(fluid != null) {
            final Metal metal = Metals.get(fluid.getFluid());

            if(metal == Metals.INVALID_METAL) {
              return true;
            }
          }

          FluidUtil.interactWithFluidHandler(player, hand, world, pos, side);
          return true;
        }

        player.openGui(GradientMod.instance, GradientGuiHandler.CLAY_CRUCIBLE, world, pos.getX(), pos.getY(), pos.getZ());
      }
    }

    return true;
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer.Builder(this).add(FLUID).build();
  }

  @Override
  public IBlockState getExtendedState(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
    final IExtendedBlockState extendedState = (IExtendedBlockState)state;

    final TileEntity te = world.getTileEntity(pos);

    if(te instanceof TileClayCrucible) {
      return extendedState.withProperty(FLUID, ((TileClayCrucible)te).tank.getFluid());
    }

    return extendedState;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }

  @Override
  public boolean canRenderInLayer(final IBlockState state, final BlockRenderLayer layer) {
    return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.TRANSLUCENT;
  }
}
