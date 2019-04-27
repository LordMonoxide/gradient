package lordmonoxide.gradient.science.geology;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.Fluid;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class Meltables {
  private Meltables() { }

  private static final Map<Item, Meltable> meltables = new HashMap<>();

  public static final Meltable INVALID_MELTABLE = new Meltable(0.0f, 0.0f, "water", 0);

  public static void registerMeltables() {
    GradientMod.logger.info("Registering meltables");

    Tags.Items.ORES.getAllElements().forEach(item -> {
      final Ore.Metal ore = Ores.getMetal(item.getRegistryName().getPath().substring(3));

      if(ore != Ores.INVALID_ORE_METAL) {
        final Meltable meltable = add(item, ore.basic.meltTime, ore.basic.meltTemp, ore.basic.name, Fluid.BUCKET_VOLUME);
        Metals.addMeltable(meltable, ore.basic);
      }
    });

    Tags.Items.INGOTS.getAllElements().forEach(item -> {
      final Metal metal = Metals.get(item.getRegistryName().getPath().substring(5));

      if(metal != Metals.INVALID_METAL) {
        final Meltable meltable = add(item, metal.meltTime, metal.meltTemp, metal.name, Fluid.BUCKET_VOLUME);
        Metals.addMeltable(meltable, metal);
      }
    });

    Tags.Items.NUGGETS.getAllElements().forEach(item -> {
      final Metal metal = Metals.get(item.getRegistryName().getPath().substring(6));

      if(metal != Metals.INVALID_METAL) {
        final Meltable meltable = add(item, metal.meltTime / 4.0f, metal.meltTemp, metal.name, Fluid.BUCKET_VOLUME / 4);
        Metals.addMeltable(meltable, metal);
      }
    });

    Tags.Items.DUSTS.getAllElements().forEach(item -> {
      final Metal metal = Metals.get(item.getRegistryName().getPath().substring(4));

      if(metal != Metals.INVALID_METAL) {
        final Meltable meltable = add(item, metal.meltTime, metal.meltTemp, metal.name, Fluid.BUCKET_VOLUME);
        Metals.addMeltable(meltable, metal);
      }
    });

    Tags.Items.STORAGE_BLOCKS.getAllElements().forEach(item -> {
      final Metal metal = Metals.get(item.getRegistryName().getPath().substring(5));

      if(metal != Metals.INVALID_METAL) {
        final Meltable meltable = add(item, metal.meltTime * 8.0f, metal.meltTemp, metal.name, Fluid.BUCKET_VOLUME * 8);
        Metals.addMeltable(meltable, metal);
      }
    });

    // TODO: crushed, purified, plates
/*
    oreLoop:
    for(final String oreName : OreDictionary.getOreNames()) {
      if(oreName.startsWith("crushed")) {
        final Metal metal = Metals.get(oreName.substring(7).toLowerCase());

        if(metal != Metals.INVALID_METAL) {
          final Meltable meltable = add(oreName, metal.meltTime, metal.meltTemp, metal.name, Fluid.BUCKET_VOLUME);
          Metals.addMeltable(meltable, metal);
        }
      } else if(oreName.startsWith("purified")) {
        final Metal metal = Metals.get(oreName.substring(8).toLowerCase());

        if(metal != Metals.INVALID_METAL) {
          final Meltable meltable = add(oreName, metal.meltTime, metal.meltTemp, metal.name, Fluid.BUCKET_VOLUME);
          Metals.addMeltable(meltable, metal);
        }
      } else if(oreName.startsWith("plate")) {
        final Metal metal = Metals.get(oreName.substring(5).toLowerCase());

        if(metal != Metals.INVALID_METAL) {
          final Meltable meltable = add(oreName, metal.meltTime, metal.meltTemp, metal.name, Fluid.BUCKET_VOLUME);
          Metals.addMeltable(meltable, metal);
        }
      } else if(oreName.startsWith("alloyNugget")) {
        final Metal metal = Metals.get(oreName.substring(11).toLowerCase());

        float hottest = 0;

        for(final Metal.MetalElement component : metal.elements) {
          final Metal m = Metals.get(component.element.name);

          if(m == Metals.INVALID_METAL) {
            continue oreLoop;
          }

          if(m.meltTemp > hottest) {
            hottest = m.meltTemp;
          }
        }

        add(oreName, metal, metal.elements.size() / 4.0f, hottest, Fluid.BUCKET_VOLUME / 4 * alloy.output.amount);
      }
    }
*/

    add(GradientItems.alloyNugget(Metals.BRONZE), Metals.COPPER.meltTime / 4.0f * 3.0f + Metals.TIN.meltTime / 4.0f, Metals.COPPER.meltTemp, "bronze", Fluid.BUCKET_VOLUME);

    Tags.Items.ORES_IRON.getAllElements().forEach(item -> {
      add(item, Metals.IRON.meltTime, Metals.IRON.meltTemp, "iron", Fluid.BUCKET_VOLUME);
    });

    add(Blocks.SAND.asItem(),       8.0f, 1200.00f, "glass", Fluid.BUCKET_VOLUME * 8);
    add(Blocks.GLASS.asItem(),      8.0f, 1200.00f, "glass", Fluid.BUCKET_VOLUME * 8);
    add(Blocks.GLASS_PANE.asItem(), 2.0f, 1200.00f, "glass", Fluid.BUCKET_VOLUME / 2);
  }

  private static Meltable add(final Item item, final float meltTime, final float meltTemp, final String fluid, final int amount) {
    final Meltable meltable = new Meltable(meltTime, meltTemp, fluid, amount);
    meltables.put(item, meltable);
    return meltable;
  }

  public static Meltable get(final ItemStack stack) {
    return meltables.getOrDefault(stack.getItem(), INVALID_MELTABLE);
  }

  public static Collection<Meltable> all() {
    return meltables.values();
  }
}
