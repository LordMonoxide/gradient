package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.AbstractIllager;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Iterator;
import java.util.Random;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class OverrideEntityDrops {
  private OverrideEntityDrops() { }

  @GameRegistry.ObjectHolder("quark:tallow")
  private static final Item TALLOW = null;

  @SubscribeEvent
  public static void addHideDrops(final LivingDropsEvent event) {
    // Remove leather drops
    if(!(event.getEntity() instanceof EntityPlayer)) {
      for(final Iterator<EntityItem> dropIterator = event.getDrops().iterator(); dropIterator.hasNext(); ) {
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

    if(entity instanceof EntityCow) {
      event.getDrops().add(new EntityItem(entity.world, x, y, z, new ItemStack(GradientItems.HIDE_COW, amount)));
      return;
    }

    if(entity instanceof EntityDonkey) {
      event.getDrops().add(new EntityItem(entity.world, x, y, z, new ItemStack(GradientItems.HIDE_DONKEY, amount)));
      return;
    }

    if(entity instanceof EntityHorse) {
      event.getDrops().add(new EntityItem(entity.world, x, y, z, new ItemStack(GradientItems.HIDE_HORSE, amount)));
      return;
    }

    if(entity instanceof EntityLlama) {
      event.getDrops().add(new EntityItem(entity.world, x, y, z, new ItemStack(GradientItems.HIDE_LLAMA, amount)));
      return;
    }

    if(entity instanceof EntityMule) {
      event.getDrops().add(new EntityItem(entity.world, x, y, z, new ItemStack(GradientItems.HIDE_MULE, amount)));
      return;
    }

    if(entity instanceof EntityOcelot) {
      event.getDrops().add(new EntityItem(entity.world, x, y, z, new ItemStack(GradientItems.HIDE_OCELOT, amount)));
      return;
    }

    if(entity instanceof EntityPig) {
      event.getDrops().add(new EntityItem(entity.world, x, y, z, new ItemStack(GradientItems.HIDE_PIG, amount)));
      return;
    }

    if(entity instanceof EntityPolarBear) {
      event.getDrops().add(new EntityItem(entity.world, x, y, z, new ItemStack(GradientItems.HIDE_POLAR_BEAR, amount)));
      return;
    }

    if(entity instanceof EntitySheep) {
      event.getDrops().add(new EntityItem(entity.world, x, y, z, new ItemStack(GradientItems.HIDE_SHEEP, amount)));
      return;
    }

    if(entity instanceof EntityWolf) {
      event.getDrops().add(new EntityItem(entity.world, x, y, z, new ItemStack(GradientItems.HIDE_WOLF, amount)));
      return;
    }
  }

  @SubscribeEvent
  public static void noSpiderString(final LivingDropsEvent event) {
    if(event.getEntity() instanceof EntitySpider) {
      for(final Iterator<EntityItem> dropIterator = event.getDrops().iterator(); dropIterator.hasNext(); ) {
        final ItemStack drop = dropIterator.next().getItem();

        if(drop.getItem() == Items.STRING) {
          dropIterator.remove();
        }
      }
    }
  }

  @SubscribeEvent
  public static void noZombieIngots(final LivingDropsEvent event) {
    if(event.getEntity() instanceof EntityZombie) {
      for(final Iterator<EntityItem> dropIterator = event.getDrops().iterator(); dropIterator.hasNext(); ) {
        final ItemStack drop = dropIterator.next().getItem();

        if(drop.getItem() == Items.IRON_INGOT) {
          dropIterator.remove();
        }
      }
    }
  }

  @SubscribeEvent
  public static void skeletonsOnlyDropArrowsIfTheyHaveBows(final LivingDropsEvent event) {
    if(event.getEntity() instanceof EntitySkeleton) {
      if(event.getEntityLiving().getHeldItemMainhand().getItem() != Items.BOW) {
        for(final Iterator<EntityItem> dropIterator = event.getDrops().iterator(); dropIterator.hasNext(); ) {
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
    for(final Iterator<EntityItem> dropIterator = event.getDrops().iterator(); dropIterator.hasNext(); ) {
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
      entity instanceof EntityZombie ||
      entity instanceof AbstractIllager ||
      entity instanceof EntityCreeper ||
      entity instanceof EntityWitch ||
      entity instanceof EntityCow ||
      entity instanceof AbstractHorse ||
      entity instanceof EntityOcelot ||
      entity instanceof EntityPig ||
      entity instanceof EntityPolarBear ||
      entity instanceof EntitySheep ||
      entity instanceof EntityWolf ||
      entity instanceof EntityVillager
    ) {
      final World world = entity.world;
      final Random rand = world.rand;

      int amount = rand.nextInt(2);
      for(int i = 0; i < event.getLootingLevel(); i++) {
        if(world.rand.nextInt(10) < 5) {
          amount++;
        }
      }

      event.getDrops().add(new EntityItem(world, entity.posX, entity.posY, entity.posZ, new ItemStack(Items.BONE, amount)));
    }
  }
}
