package lordmonoxide.gradient.blocks.heat;

import lordmonoxide.gradient.blocks.GradientBlock;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public abstract class Hardenable extends GradientBlock {
  protected Hardenable(final String name, final CreativeTabs creativeTab, final Material material, final MapColor mapColor) {
    super(name, creativeTab, material, mapColor);
  }

  protected Hardenable(final String name, final CreativeTabs creativeTab, final Material material) {
    super(name, creativeTab, material);
  }

  public abstract IBlockState getHardened(final IBlockState current);
  public abstract int getHardeningTime(final IBlockState current);

  public boolean isHardened(final IBlockState current) {
    return false;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(final ItemStack stack, @Nullable final World world, final List<String> tooltip, final ITooltipFlag flag) {
    @SuppressWarnings("deprecation")
    final IBlockState state = this.getStateFromMeta(stack.getMetadata());

    if(!this.isHardened(state)) {
      tooltip.add(I18n.format("hardenable.tooltip", this.getHardeningTime(state)));
    }
  }
}
