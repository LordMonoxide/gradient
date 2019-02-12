package lordmonoxide.gradient.core.items;

import lordmonoxide.gradient.core.GradientCore;
import lordmonoxide.gradient.core.blocks.CoreBlocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
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
  public static final Item SALT = new Item(new Item.Builder().group(ItemGroup.FOOD)).setRegistryName(GradientCore.resource("salt"));

  public static final Item HIDE_COW        = new Item(new Item.Builder().group(ItemGroup.MATERIALS)).setRegistryName(GradientCore.resource("hide_cow"));
  public static final Item HIDE_DONKEY     = new Item(new Item.Builder().group(ItemGroup.MATERIALS)).setRegistryName(GradientCore.resource("hide_donkey"));
  public static final Item HIDE_HORSE      = new Item(new Item.Builder().group(ItemGroup.MATERIALS)).setRegistryName(GradientCore.resource("hide_horse"));
  public static final Item HIDE_LLAMA      = new Item(new Item.Builder().group(ItemGroup.MATERIALS)).setRegistryName(GradientCore.resource("hide_llama"));
  public static final Item HIDE_MULE       = new Item(new Item.Builder().group(ItemGroup.MATERIALS)).setRegistryName(GradientCore.resource("hide_mule"));
  public static final Item HIDE_OCELOT     = new Item(new Item.Builder().group(ItemGroup.MATERIALS)).setRegistryName(GradientCore.resource("hide_ocelot"));
  public static final Item HIDE_PIG        = new Item(new Item.Builder().group(ItemGroup.MATERIALS)).setRegistryName(GradientCore.resource("hide_pig"));
  public static final Item HIDE_POLAR_BEAR = new Item(new Item.Builder().group(ItemGroup.MATERIALS)).setRegistryName(GradientCore.resource("hide_polar_bear"));
  public static final Item HIDE_SHEEP      = new Item(new Item.Builder().group(ItemGroup.MATERIALS)).setRegistryName(GradientCore.resource("hide_sheep"));
  public static final Item HIDE_WOLF       = new Item(new Item.Builder().group(ItemGroup.MATERIALS)).setRegistryName(GradientCore.resource("hide_wolf"));

  @SubscribeEvent(priority = EventPriority.LOW)
  public static void registerItems(final RegistryEvent.Register<Item> event) {
    GradientCore.LOGGER.info("REGISTERING CORE ITEMS");

    final IForgeRegistry<Item> registry = event.getRegistry();

    registry.register(FIBRE);
    registry.register(new ItemPebble(CoreBlocks.PEBBLE, new Item.Builder().group(ItemGroup.MATERIALS)).setRegistryName(CoreBlocks.PEBBLE.getRegistryName()));
    registry.register(new ItemBlock(CoreBlocks.SALT, new Item.Builder().group(ItemGroup.FOOD)).setRegistryName(CoreBlocks.SALT.getRegistryName()));
    registry.register(SALT);

    registry.register(HIDE_COW);
    registry.register(HIDE_DONKEY);
    registry.register(HIDE_HORSE);
    registry.register(HIDE_LLAMA);
    registry.register(HIDE_MULE);
    registry.register(HIDE_OCELOT);
    registry.register(HIDE_PIG);
    registry.register(HIDE_POLAR_BEAR);
    registry.register(HIDE_SHEEP);
    registry.register(HIDE_WOLF);
  }
}
