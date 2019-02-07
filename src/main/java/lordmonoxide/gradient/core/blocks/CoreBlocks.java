package lordmonoxide.gradient.core.blocks;

import lordmonoxide.gradient.core.GradientCore;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = GradientCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CoreBlocks {
  private CoreBlocks() { }

  public static final BlockPebble PEBBLE = new BlockPebble();

  @SubscribeEvent(priority = EventPriority.LOW)
  public static void registerItems(final RegistryEvent.Register<Block> event) {
    GradientCore.LOGGER.info("REGISTERING CORE BLOCKS");

    final IForgeRegistry<Block> registry = event.getRegistry();

    registry.register(PEBBLE);
  }
}
