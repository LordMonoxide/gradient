package lordmonoxide.gradient.core.items;

import lordmonoxide.gradient.core.GradientCore;
import lordmonoxide.gradient.core.blocks.CoreBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = GradientCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CoreItems {
  private CoreItems() { }

  public static final Item FIBRE = new Item(new Item.Builder().group(ItemGroup.MATERIALS)).setRegistryName(GradientCore.resource("fibre"));

  @SubscribeEvent(priority = EventPriority.LOW)
  public static void registerItems(final RegistryEvent.Register<Item> event) {
    GradientCore.LOGGER.info("REGISTERING CORE ITEMS");

    final IForgeRegistry<Item> registry = event.getRegistry();

    registry.register(FIBRE);
    registry.register(new ItemPebble(CoreBlocks.PEBBLE, new Item.Builder().group(ItemGroup.MATERIALS)).setRegistryName(CoreBlocks.PEBBLE.getRegistryName()));
  }
}
