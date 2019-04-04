package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.GradientMod;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GradientMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class FurnaceRecipes {
  private FurnaceRecipes() { }

  //TODO
/*
  @SubscribeEvent
  public static void registerRecipes(final RegistryEvent.Register<IRecipe> event) {
    GradientMod.logger.info("Registering furnace recipes...");

    net.minecraft.item.crafting.FurnaceRecipes.instance().addSmelting(GradientItems.SUGARCANE_PASTE, new ItemStack(Items.SUGAR), 0.25f);
    net.minecraft.item.crafting.FurnaceRecipes.instance().addSmelting(GradientItems.DOUGH, new ItemStack(Items.BREAD, 2), 0.35f);
  }
*/
}
