package lordmonoxide.gradient.science.geology;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import lordmonoxide.gradient.GradientMod;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collection;

public final class Meltables {
  private Meltables() { }

  private static final Int2ObjectMap<Meltable> meltables = new Int2ObjectRBTreeMap<>();

  public static final Meltable INVALID_MELTABLE = new Meltable(0.0f, 0.0f, "water", 0);

  public static void registerMeltables() {
    GradientMod.logger.info("Registering meltables");

    oreLoop:
    for(final String oreName : OreDictionary.getOreNames()) {
      if(oreName.startsWith("ore")) {
        final Ore.Metal ore = Ores.getMetal(oreName.substring(3).toLowerCase());

        if(ore != Ores.INVALID_ORE_METAL) {
          final Meltable meltable = add(oreName, ore.basic.meltTime, ore.basic.meltTemp, ore.basic.name, Fluid.BUCKET_VOLUME);
          Metals.addMeltable(meltable, ore.basic);
        }
      } else if(oreName.startsWith("ingot")) {
        final Metal metal = Metals.get(oreName.substring(5).toLowerCase());

        if(metal != Metals.INVALID_METAL) {
          final Meltable meltable = add(oreName, metal.meltTime, metal.meltTemp, metal.name, Fluid.BUCKET_VOLUME);
          Metals.addMeltable(meltable, metal);
        }
      } else if(oreName.startsWith("nugget")) {
        final Metal metal = Metals.get(oreName.substring(6).toLowerCase());

        if(metal != Metals.INVALID_METAL) {
          final Meltable meltable = add(oreName, metal.meltTime / 4.0f, metal.meltTemp, metal.name, Fluid.BUCKET_VOLUME / 4);
          Metals.addMeltable(meltable, metal);
        }
      } else if(oreName.startsWith("dust")) {
        final Metal metal = Metals.get(oreName.substring(4).toLowerCase());

        if(metal != Metals.INVALID_METAL) {
          final Meltable meltable = add(oreName, metal.meltTime, metal.meltTemp, metal.name, Fluid.BUCKET_VOLUME);
          Metals.addMeltable(meltable, metal);
        }
      } else if(oreName.startsWith("block")) {
        final Metal metal = Metals.get(oreName.substring(5).toLowerCase());

        if(metal != Metals.INVALID_METAL) {
          final Meltable meltable = add(oreName, metal.meltTime * 8.0f, metal.meltTemp, metal.name, Fluid.BUCKET_VOLUME * 8);
          Metals.addMeltable(meltable, metal);
        }
      } else if(oreName.startsWith("crushed")) {
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
      }
    }

    add("oreIron", Metals.IRON.meltTime, Metals.IRON.meltTemp, "iron", Fluid.BUCKET_VOLUME);

    add("sand",       8.0f, 1200.00f, "glass", Fluid.BUCKET_VOLUME * 8);
    add("blockGlass", 8.0f, 1200.00f, "glass", Fluid.BUCKET_VOLUME * 8);
    add("dustGlass",  8.0f, 1200.00f, "glass", Fluid.BUCKET_VOLUME * 8);
    add("paneGlass",  2.0f, 1200.00f, "glass", Fluid.BUCKET_VOLUME / 2);
  }

  private static Meltable add(final String oreDict, final float meltTime, final float meltTemp, final String fluid, final int amount) {
    final Meltable meltable = new Meltable(meltTime, meltTemp, fluid, amount);
    meltables.put(OreDictionary.getOreID(oreDict), meltable);
    return meltable;
  }

  public static Meltable get(final ItemStack stack) {
    for(final int id : OreDictionary.getOreIDs(stack)) {
      final Meltable meltable = meltables.get(id);

      if(meltable != null) {
        return meltable;
      }
    }

    return INVALID_MELTABLE;
  }

  public static Collection<Meltable> all() {
    return meltables.values();
  }
}
