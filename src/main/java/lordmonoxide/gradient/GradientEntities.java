package lordmonoxide.gradient;

import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.entities.EntityPebble;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public class GradientEntities {
  private GradientEntities() { }

  @SubscribeEvent
  public static void registerEntities(final RegistryEvent.Register<EntityEntry> event) {
    GradientMod.logger.info("Registering entities");

    EntityRegistry.registerModEntity(
        new ResourceLocation(GradientMod.MODID, GradientBlocks.PEBBLE.getTranslationKey()),
        EntityPebble.class,
        GradientBlocks.PEBBLE.getTranslationKey(),
        1,
        GradientMod.MODID,
        20,
        1,
        true
    );
  }
}
