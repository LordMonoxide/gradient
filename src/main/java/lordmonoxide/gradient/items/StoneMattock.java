package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientTools;
import lordmonoxide.gradient.science.geology.Metals;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StoneMattock extends GradientItemWorldTool {
  private final Set<ToolType> toolTypes = new HashSet<>();

  public StoneMattock() {
    super(0.5f, -2.4f, 4, 2, new Properties().group(ItemGroup.TOOLS).defaultMaxDamage(50));
    this.toolTypes.add(ToolType.AXE);
    this.toolTypes.add(ToolType.SHOVEL);
  }

  @Override
  public void addInformation(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
    GradientItems.tool(GradientTools.MATTOCK, Metals.COPPER).addInformation(stack, world, tooltip, flag);
  }

  @Override
  public EnumActionResult onItemUse(final ItemUseContext context) {
    return GradientItems.tool(GradientTools.MATTOCK, Metals.COPPER).onItemUse(context);
  }

  @Override
  public boolean itemInteractionForEntity(final ItemStack itemstack, final EntityPlayer player, final EntityLivingBase entity, final EnumHand hand) {
    return GradientItems.tool(GradientTools.MATTOCK, Metals.COPPER).itemInteractionForEntity(itemstack, player, entity, hand);
  }

  @Override
  public Set<ToolType> getToolTypes(final ItemStack stack) {
    return this.toolTypes;
  }

  @Override
  public int getHarvestLevel(final ItemStack stack, final ToolType toolType, @Nullable final EntityPlayer player, @Nullable final IBlockState blockState) {
    if(!this.toolTypes.contains(toolType)) {
      return -1;
    }

    return 0;
  }
}
