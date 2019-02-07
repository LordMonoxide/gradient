package lordmonoxide.gradient.age1.items;

import lordmonoxide.gradient.age1.GradientAge1;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = GradientAge1.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class Age1Items {
  private Age1Items() { }

  public static final Item STONE_HATCHET = new ItemAxe(GradientItemTiers.STONE, 4.0f, -2.4f, new Item.Builder().group(ItemGroup.TOOLS)) { }.setRegistryName(GradientAge1.resource("stone_hatchet"));

  @SubscribeEvent
  public static void registerItems(final RegistryEvent.Register<Item> event) {
    GradientAge1.LOGGER.info("REGISTERING AGE 1 ITEMS");

    final IForgeRegistry<Item> registry = event.getRegistry();

    registry.register(STONE_HATCHET);
  }
}
