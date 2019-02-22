package lordmonoxide.gradient.core.tileentities;

import lordmonoxide.gradient.core.GradientCore;
import lordmonoxide.gradient.core.blocks.CoreBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GradientCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CoreTiles {
  private CoreTiles() { }

  //TODO: data fixer type???
  public static final TileEntityType<TileOre> TILE_ORE_TYPE = (TileEntityType<TileOre>)new TileEntityType<>(TileOre::new, null).setRegistryName(CoreBlocks.ORE.getRegistryName());

  @SubscribeEvent
  public static void registerTiles(final RegistryEvent.Register<TileEntityType<?>> event) {
    GradientCore.LOGGER.info("REGISTERING CORE TILES");

    event.getRegistry().register(TILE_ORE_TYPE);
  }
}
