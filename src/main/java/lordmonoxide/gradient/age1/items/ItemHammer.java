package lordmonoxide.gradient.age1.items;

import lordmonoxide.gradient.core.items.GradientToolType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Set;

public class ItemHammer extends ItemTool {
  public ItemHammer(final IItemTier tier, final float attackDamage, final float attackSpeed, final Item.Properties builder) {
    super(attackDamage, attackSpeed, tier, Set.of(), builder.addToolType(ToolType.PICKAXE, tier.getHarvestLevel()).addToolType(GradientToolType.HAMMER, tier.getHarvestLevel()));
  }

  //TODO: remove vvv once hammer is working
  @Override
  public float getDestroySpeed(final ItemStack stack, final IBlockState state) {
    return super.getDestroySpeed(stack, state);
  }

  @Override
  public Set<ToolType> getToolTypes(final ItemStack stack) {
    return super.getToolTypes(stack);
  }

  @Override
  public int getHarvestLevel(final ItemStack stack, final ToolType tool, @Nullable final EntityPlayer player, @Nullable final IBlockState blockState) {
    return super.getHarvestLevel(stack, tool, player, blockState);
  }
}
