package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientTools;
import lordmonoxide.gradient.science.geology.Metal;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tool extends GradientItemWorldTool {
  public final GradientTools.Type type;
  public final Metal metal;

  private final Set<ToolType> toolTypes = new HashSet<>();

  public Tool(final GradientTools.Type type, final Metal metal) {
    super(metal.harvestSpeed, (float)(-4 + type.attackSpeed * metal.attackSpeedMultiplier), (int)(type.attackDamage * metal.attackDamageMultiplier), type.attackDurabilityLost, new Properties().group(ItemGroup.TOOLS).defaultMaxDamage(metal.durability));
    this.type = type;
    this.metal = metal;
    Collections.addAll(this.toolTypes, type.toolTypes);
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public void addInformation(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
    super.addInformation(stack, world, tooltip, flag);
    this.type.tooltip(stack, world, tooltip);
  }

  @Override
  public ActionResultType onItemUse(final ItemUseContext context) {
    return this.type.onItemUse(context);
  }

  @Override
  public Set<ToolType> getToolTypes(final ItemStack stack) {
    return this.toolTypes;
  }

  @Override
  public boolean itemInteractionForEntity(final ItemStack itemstack, final PlayerEntity player, final LivingEntity entity, final Hand hand) {
    return this.type.onEntityInteract(itemstack, player, entity, hand);
  }

  @Override
  public int getHarvestLevel(final ItemStack stack, final ToolType toolType, @Nullable final PlayerEntity player, @Nullable final BlockState blockState) {
    if(!this.toolTypes.contains(toolType)) {
      return -1;
    }

    return this.metal.harvestLevel;
  }
}
