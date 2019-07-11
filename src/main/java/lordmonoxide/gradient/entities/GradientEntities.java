package lordmonoxide.gradient.entities;

import lordmonoxide.gradient.GradientIds;
import lordmonoxide.gradient.GradientMod;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = GradientMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class GradientEntities {
  private GradientEntities() { }

  @ObjectHolder("gradient:pebble")
  public static EntityType<? extends ProjectileItemEntity> PEBBLE;

  @SubscribeEvent
  public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
    GradientMod.logger.info("Registering entities");

    event.getRegistry().register(EntityType.Builder.<PebbleEntity>create(PebbleEntity::new, EntityClassification.MISC).build(GradientMod.resource(GradientIds.PEBBLE).toString()).setRegistryName(GradientIds.PEBBLE));
  }
}
