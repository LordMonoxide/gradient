package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class FurnaceRecipes {
  private FurnaceRecipes() { }

  @SubscribeEvent
  public static void registerRecipes(final RegistryEvent.Register<IRecipe> event) {
    GradientMod.logger.info("Registering furnace recipes...");

    net.minecraft.item.crafting.FurnaceRecipes.instance().addSmelting(GradientItems.SUGARCANE_PASTE, new ItemStack(Items.SUGAR), 0.25f);
    net.minecraft.item.crafting.FurnaceRecipes.instance().addSmelting(GradientItems.DOUGH, new ItemStack(Items.BREAD, 2), 0.35f);
  }
}
