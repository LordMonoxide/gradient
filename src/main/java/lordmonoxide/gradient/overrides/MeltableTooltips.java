package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.recipes.MeltingRecipe;
import lordmonoxide.gradient.utils.RecipeUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = GradientMod.MODID, value = Side.CLIENT)
public final class MeltableTooltips {
  private MeltableTooltips() { }

  private static final Map<Item, Map<ItemStack, MeltingRecipe>> cache = new HashMap<>();

  @SubscribeEvent
  public static void clearRecipeCache(final RegistryEvent.Register<IRecipe> event) {
    cache.clear();
  }

//  @SubscribeEvent
  public static void addMeltableInfoToTooltips(final ItemTooltipEvent event) {
    final ItemStack stack = event.getItemStack();

    if(stack.isEmpty()) {
      return;
    }

    final Map<ItemStack, MeltingRecipe> recipes = cache.computeIfAbsent(stack.getItem(), item -> new HashMap<>());
    MeltingRecipe recipe = null;
    boolean missing = true;

    for(final Map.Entry<ItemStack, MeltingRecipe> entry : recipes.entrySet()) {
      if(ItemStack.areItemStacksEqualUsingNBTShareTag(stack, entry.getKey())) {
        recipe = entry.getValue();
        missing = false;
        break;
      }
    }

    if(missing) {
      recipe = RecipeUtils.findRecipe(MeltingRecipe.class, r -> r.matches(stack));
      recipes.put(stack, recipe);
    }

    if(recipe == null) {
      return;
    }

    event.getToolTip().add(I18n.format("meltable.melt_temp", recipe.getMeltTemp()));
    event.getToolTip().add(I18n.format("meltable.melt_time", recipe.getMeltTime()));
    event.getToolTip().add(I18n.format("meltable.amount", recipe.getOutput().amount));
    event.getToolTip().add(I18n.format("meltable.fluid", recipe.getOutput().getLocalizedName()));
  }
}
