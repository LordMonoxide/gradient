package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.horse.DonkeyEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.passive.horse.MuleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Iterator;
import java.util.Random;

@Mod.EventBusSubscriber(modid = GradientMod.MOD_ID)
public final class OverrideEntityDrops {
  private OverrideEntityDrops() { }

  @ObjectHolder("quark:tallow")
  private static Item TALLOW;

  @SubscribeEvent
  public static void addHideDrops(final LivingDropsEvent event) {
    // Remove leather drops
    if(!(event.getEntity() instanceof PlayerEntity)) {
      for(final Iterator<ItemEntity> dropIterator = event.getDrops().iterator(); dropIterator.hasNext(); ) {
        final ItemStack drop = dropIterator.next().getItem();

        if(drop.getItem() == Items.LEATHER) {
          dropIterator.remove();
        }
      }
    }

    final Entity entity = event.getEntity();
    final World world = entity.world;

    final double x = entity.posX + 0.5d;
    final double y = entity.posY + 0.5d;
    final double z = entity.posZ + 0.5d;

    int amount = 1;
    for(int i = 0; i < event.getLootingLevel(); i++) {
      if(world.rand.nextInt(10) < 8) {
        amount++;
      }
    }

    if(entity instanceof CowEntity) {
      event.getDrops().add(new ItemEntity(entity.world, x, y, z, new ItemStack(GradientItems.HIDE_COW, amount)));
      return;
    }

    if(entity instanceof DonkeyEntity) {
      event.getDrops().add(new ItemEntity(entity.world, x, y, z, new ItemStack(GradientItems.HIDE_DONKEY, amount)));
      return;
    }

    if(entity instanceof HorseEntity) {
      event.getDrops().add(new ItemEntity(entity.world, x, y, z, new ItemStack(GradientItems.HIDE_HORSE, amount)));
      return;
    }

    if(entity instanceof LlamaEntity) {
      event.getDrops().add(new ItemEntity(entity.world, x, y, z, new ItemStack(GradientItems.HIDE_LLAMA, amount)));
      return;
    }

    if(entity instanceof MuleEntity) {
      event.getDrops().add(new ItemEntity(entity.world, x, y, z, new ItemStack(GradientItems.HIDE_MULE, amount)));
      return;
    }

    if(entity instanceof OcelotEntity) {
      event.getDrops().add(new ItemEntity(entity.world, x, y, z, new ItemStack(GradientItems.HIDE_OCELOT, amount)));
      return;
    }

    if(entity instanceof PigEntity) {
      event.getDrops().add(new ItemEntity(entity.world, x, y, z, new ItemStack(GradientItems.HIDE_PIG, amount)));
      return;
    }

    if(entity instanceof PolarBearEntity) {
      event.getDrops().add(new ItemEntity(entity.world, x, y, z, new ItemStack(GradientItems.HIDE_POLAR_BEAR, amount)));
      return;
    }

    if(entity instanceof SheepEntity) {
      event.getDrops().add(new ItemEntity(entity.world, x, y, z, new ItemStack(GradientItems.HIDE_SHEEP, amount)));
      return;
    }

    if(entity instanceof WolfEntity) {
      event.getDrops().add(new ItemEntity(entity.world, x, y, z, new ItemStack(GradientItems.HIDE_WOLF, amount)));
      return;
    }
  }

  @SubscribeEvent
  public static void noSpiderString(final LivingDropsEvent event) {
    if(event.getEntity() instanceof SpiderEntity) {
      for(final Iterator<ItemEntity> dropIterator = event.getDrops().iterator(); dropIterator.hasNext(); ) {
        final ItemStack drop = dropIterator.next().getItem();

        if(drop.getItem() == Items.STRING) {
          dropIterator.remove();
        }
      }
    }
  }

  @SubscribeEvent
  public static void noZombieIngots(final LivingDropsEvent event) {
    if(event.getEntity() instanceof ZombieEntity) {
      for(final Iterator<ItemEntity> dropIterator = event.getDrops().iterator(); dropIterator.hasNext(); ) {
        final ItemStack drop = dropIterator.next().getItem();

        if(drop.getItem() == Items.IRON_INGOT) {
          dropIterator.remove();
        }
      }
    }
  }

  @SubscribeEvent
  public static void skeletonsOnlyDropArrowsIfTheyHaveBows(final LivingDropsEvent event) {
    if(event.getEntity() instanceof SkeletonEntity) {
      if(event.getEntityLiving().getHeldItemMainhand().getItem() != Items.BOW) {
        for(final Iterator<ItemEntity> dropIterator = event.getDrops().iterator(); dropIterator.hasNext(); ) {
          final ItemStack drop = dropIterator.next().getItem();

          if(drop.getItem() == Items.ARROW) {
            dropIterator.remove();
          }
        }
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static void noTallow(final LivingDropsEvent event) {
    for(final Iterator<ItemEntity> dropIterator = event.getDrops().iterator(); dropIterator.hasNext(); ) {
      final ItemStack drop = dropIterator.next().getItem();

      if(drop.getItem() == TALLOW) {
        dropIterator.remove();
      }
    }
  }

  @SubscribeEvent
  public static void everythingDropsBones(final LivingDropsEvent event) {
    final Entity entity = event.getEntity();

    if(
      entity instanceof ZombieEntity ||
      entity instanceof AbstractIllagerEntity ||
      entity instanceof CreeperEntity ||
      entity instanceof WitchEntity ||
      entity instanceof CowEntity ||
      entity instanceof HorseEntity ||
      entity instanceof OcelotEntity ||
      entity instanceof PigEntity ||
      entity instanceof PolarBearEntity ||
      entity instanceof SheepEntity ||
      entity instanceof WolfEntity ||
      entity instanceof VillagerEntity
    ) {
      final World world = entity.world;
      final Random rand = world.rand;

      int amount = rand.nextInt(2);
      for(int i = 0; i < event.getLootingLevel(); i++) {
        if(world.rand.nextInt(10) < 5) {
          amount++;
        }
      }

      event.getDrops().add(new ItemEntity(world, entity.posX, entity.posY, entity.posZ, new ItemStack(Items.BONE, amount)));
    }
  }
}
