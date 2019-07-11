package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientTools;
import lordmonoxide.gradient.science.geology.Metals;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
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
  public ActionResultType onItemUse(final ItemUseContext context) {
    return GradientItems.tool(GradientTools.MATTOCK, Metals.COPPER).onItemUse(context);
  }

  @Override
  public boolean itemInteractionForEntity(final ItemStack itemstack, final PlayerEntity player, final LivingEntity entity, final Hand hand) {
    return GradientItems.tool(GradientTools.MATTOCK, Metals.COPPER).itemInteractionForEntity(itemstack, player, entity, hand);
  }

  @Override
  public Set<ToolType> getToolTypes(final ItemStack stack) {
    return this.toolTypes;
  }

  @Override
  public int getHarvestLevel(final ItemStack stack, final ToolType toolType, @Nullable final PlayerEntity player, @Nullable final BlockState blockState) {
    if(!this.toolTypes.contains(toolType)) {
      return -1;
    }

    return 0;
  }
}
