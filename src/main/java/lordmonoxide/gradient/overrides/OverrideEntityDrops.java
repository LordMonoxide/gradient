package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Iterator;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
@GameRegistry.ObjectHolder("gradient")
public final class OverrideEntityDrops {
  private OverrideEntityDrops() { }

  public static final Item HIDE_COW = null;
  public static final Item HIDE_DONKEY = null;
  public static final Item HIDE_HORSE = null;
  public static final Item HIDE_LLAMA = null;
  public static final Item HIDE_MULE = null;
  public static final Item HIDE_OCELOT = null;
  public static final Item HIDE_PIG = null;
  public static final Item HIDE_POLAR_BEAR = null;
  public static final Item HIDE_SHEEP = null;
  public static final Item HIDE_WOLF = null;

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
      event.getDrops().add(new EntityItem(entity.world, x, y, z, new ItemStack(HIDE_COW, amount)));
      return;
    }

    if(entity instanceof EntityDonkey) {
      event.getDrops().add(new EntityItem(entity.world, x, y, z, new ItemStack(HIDE_DONKEY, amount)));
      return;
    }

    if(entity instanceof EntityHorse) {
      event.getDrops().add(new EntityItem(entity.world, x, y, z, new ItemStack(HIDE_HORSE, amount)));
      return;
    }

    if(entity instanceof EntityLlama) {
      event.getDrops().add(new EntityItem(entity.world, x, y, z, new ItemStack(HIDE_LLAMA, amount)));
      return;
    }

    if(entity instanceof EntityMule) {
      event.getDrops().add(new EntityItem(entity.world, x, y, z, new ItemStack(HIDE_MULE, amount)));
      return;
    }

    if(entity instanceof EntityOcelot) {
      event.getDrops().add(new EntityItem(entity.world, x, y, z, new ItemStack(HIDE_OCELOT, amount)));
      return;
    }

    if(entity instanceof EntityPig) {
      event.getDrops().add(new EntityItem(entity.world, x, y, z, new ItemStack(HIDE_PIG, amount)));
      return;
    }

    if(entity instanceof EntityPolarBear) {
      event.getDrops().add(new EntityItem(entity.world, x, y, z, new ItemStack(HIDE_POLAR_BEAR, amount)));
      return;
    }

    if(entity instanceof EntitySheep) {
      event.getDrops().add(new EntityItem(entity.world, x, y, z, new ItemStack(HIDE_SHEEP, amount)));
      return;
    }

    if(entity instanceof EntityWolf) {
      event.getDrops().add(new EntityItem(entity.world, x, y, z, new ItemStack(HIDE_WOLF, amount)));
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
}
