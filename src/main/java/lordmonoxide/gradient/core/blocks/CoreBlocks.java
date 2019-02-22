package lordmonoxide.gradient.core.blocks;

import lordmonoxide.gradient.core.GradientCore;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = GradientCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CoreBlocks {
  private CoreBlocks() { }

  public static final Block PEBBLE = new BlockPebble().setRegistryName(GradientCore.resource("pebble"));
  public static final Block SALT = new BlockSalt().setRegistryName(GradientCore.resource("salt_block"));

  public static final Block ORE = new BlockOre().setRegistryName(GradientCore.resource("ore"));

  @SubscribeEvent
  public static void registerBlocks(final RegistryEvent.Register<Block> event) {
    GradientCore.LOGGER.info("REGISTERING CORE BLOCKS");

    final IForgeRegistry<Block> registry = event.getRegistry();

    registry.register(PEBBLE);
    registry.register(SALT);

    registry.register(ORE);
  }
}
