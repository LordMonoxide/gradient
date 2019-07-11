package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientCasts;
import lordmonoxide.gradient.GradientMaterials;
import lordmonoxide.gradient.blocks.heat.HeatSinkerBlock;
import lordmonoxide.gradient.containers.ClayCrucibleContainer;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.items.ItemClayCastUnhardened;
import lordmonoxide.gradient.science.geology.Metal;
import lordmonoxide.gradient.science.geology.Metals;
import lordmonoxide.gradient.tileentities.TileClayCrucible;
import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.List;

public class BlockClayCrucibleHardened extends HeatSinkerBlock implements INamedContainerProvider {
  private static final VoxelShape SHAPE = Block.makeCuboidShape(1.0d, 0.0d, 1.0d, 15.0d, 15.0d, 15.0d);

  public BlockClayCrucibleHardened() {
    super(Properties.create(GradientMaterials.MATERIAL_CLAY_MACHINE).hardnessAndResistance(1.0f, 5.0f));
  }

  @Override
  public void addInformation(final ItemStack stack, @Nullable final IBlockReader world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
    super.addInformation(stack, world, tooltip, flag);
    tooltip.add(new TranslationTextComponent("block.gradient.clay_crucible_hardened.tooltip"));
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isSolid(final BlockState state) {
    return false;
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(final BlockState state, final IBlockReader world, final BlockPos pos, final ISelectionContext context) {
    return SHAPE;
  }

  @Override
  public int getLightValue(final BlockState state, final IEnviromentBlockReader world, final BlockPos pos) {
    final BlockState other = world.getBlockState(pos);
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
  public TileClayCrucible createTileEntity(final BlockState state, final IBlockReader world) {
    return new TileClayCrucible();
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit) {
    if(!world.isRemote) {
      if(!player.isSneaking()) {
        final TileClayCrucible te = (TileClayCrucible)world.getTileEntity(pos);

        if(te == null) {
          return false;
        }

        final ItemStack stack = player.getHeldItem(hand);

        // Cast item
        if(stack.getItem() instanceof BlockItem && !(stack.getItem() instanceof ItemClayCastUnhardened) && ((BlockItem)stack.getItem()).getBlock() instanceof BlockClayCast) {
          final GradientCasts.Cast cast = ((BlockClayCast)((BlockItem)stack.getItem()).getBlock()).cast;

          if(te.getMoltenMetal() == null) {
            player.sendMessage(new TranslationTextComponent("block.gradient.clay_crucible.no_metal").setStyle(new Style().setColor(TextFormatting.RED)));
            return true;
          }

          final Metal metal = Metals.get(te.getMoltenMetal().getFluid());
          final int amount = cast.amountForMetal(metal);

          if(te.getMoltenMetal().amount < amount) {
            player.sendMessage(new TranslationTextComponent("block.gradient.clay_crucible.not_enough_metal", amount).setStyle(new Style().setColor(TextFormatting.RED)));
            return true;
          }

          if(!cast.isValidForMetal(metal)) {
            player.sendMessage(new TranslationTextComponent("block.gradient.clay_crucible.metal_cant_make_tools").setStyle(new Style().setColor(TextFormatting.RED)));
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

          te.useBucket(player, hand, world, pos, hit.getFace());

          return true;
        }

        NetworkHooks.openGui((ServerPlayerEntity)player, this, pos);
      }
    }

    return true;
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }

  @Override
  public boolean canRenderInLayer(final BlockState state, final BlockRenderLayer layer) {
    return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.TRANSLUCENT;
  }

  @Override
  public ITextComponent getDisplayName() {
    return this.getNameTextComponent();
  }

  @Nullable
  @Override
  public Container createMenu(final int id, final PlayerInventory playerInv, final PlayerEntity player) {
    final TileClayCrucible crucible = WorldUtils.getTileEntity(player.world, player.getPosition(), TileClayCrucible.class);

    if(crucible != null) {
      return new ClayCrucibleContainer(id, playerInv, crucible);
    }

    return null;
  }
}
