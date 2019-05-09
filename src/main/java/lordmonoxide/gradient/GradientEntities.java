package lordmonoxide.gradient;

import lordmonoxide.gradient.entities.EntityPebble;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = GradientMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GradientEntities {
  private GradientEntities() { }

  @ObjectHolder("gradient:pebble")
  public static EntityType<?> PEBBLE;

  @SubscribeEvent
  public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
    GradientMod.logger.info("Registering entities");

    event.getRegistry().register(EntityType.Builder.create(EntityPebble.class, EntityPebble::new).build(GradientMod.resource(GradientIds.PEBBLE).toString()).setRegistryName(GradientIds.PEBBLE));
  }
}
