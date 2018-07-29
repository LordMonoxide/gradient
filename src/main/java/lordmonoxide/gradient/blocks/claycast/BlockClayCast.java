package lordmonoxide.gradient.blocks.claycast;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlock;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.init.CastRegistry;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public class BlockClayCast extends GradientBlock {
  private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0d, 0.0d, 0.0d, 1.0d, 2.0d / 16.0d, 1.0d);

  public static final CastRegistry.PropertyCast CAST = CastRegistry.PropertyCast.create("cast");

  public BlockClayCast() {
    super("clay_cast", CreativeTabs.TOOLS, GradientBlocks.MATERIAL_CLAY_MACHINE);
    this.setResistance(5.0f);
    this.setHardness(1.0f);
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onCastRegistryLoad(final RegistryEvent.Register<CastRegistry.Cast> event) {
    GradientMod.logger.info("Registering default blockstate for BlockClayCast");
    this.setDefaultState(this.blockState.getBaseState().withProperty(CAST, event.getRegistry().iterator().next()));
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(final ItemStack stack, @Nullable final World player, final List<String> tooltip, final ITooltipFlag advanced) {
    for(final GradientMetals.Metal metal : GradientMetals.metals) {
      final String metalName = I18n.format("fluid." + metal.name);
      final int metalAmount = this.getStateFromMeta(stack.getMetadata()).getValue(CAST).amountForMetal(metal);
      tooltip.add(I18n.format("tile.clay_cast.metal_amount", metalName, metalAmount));
    }
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, CAST);
  }

  @Override
  @Deprecated
  public IBlockState getStateFromMeta(final int meta) {
    return this.getDefaultState().withProperty(CAST, CastRegistry.getCast(meta));
  }

  @Override
  public int getMetaFromState(final IBlockState state) {
    return state.getValue(CAST).id;
  }

  @Override
  public int damageDropped(final IBlockState state) {
    return this.getMetaFromState(state);
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
    return AABB;
  }
}
