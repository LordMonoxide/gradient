package lordmonoxide.gradient;

import com.google.common.collect.ImmutableList;
import lordmonoxide.gradient.recipes.OreDictHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GradientMetals {
  private GradientMetals() { }

  public static final List<Metal> metals = new ArrayList<>();
  public static final List<Alloy> alloys = new ArrayList<>();

  private static final Map<Integer, Meltable> meltables = new HashMap<>();

  public static final Metal    INVALID_METAL    = new Metal("invalid", Float.POSITIVE_INFINITY, 0.0f, 0.0f, false, false, false);
  public static final Meltable INVALID_MELTABLE = new Meltable(INVALID_METAL, 0, 0);

  public static final Metal COPPER    = addMetal("copper",    1085.00f, 3.0f,  63.55f).add();
  public static final Metal TIN       = addMetal("tin",        231.93f, 1.5f, 118.71f).add();
  public static final Metal IRON      = addMetal("iron",      1538.00f, 4.0f,  55.85f).add();
  public static final Metal GOLD      = addMetal("gold",      1064.00f, 2.0f, 196.97f).add();
  public static final Metal BRONZE    = addMetal("bronze",     950.00f, 3.5f, 182.26f).add();
  public static final Metal MAGNESIUM = addMetal("magnesium",  650.00f, 2.5f,  24.31f).disableTools().add();

  public static final Metal GLASS = addMetal("glass", 1200.00f, 5.0f, 50.0f).disableTools().disableNuggets().disableIngots().add();

  static {
    addAlloy(metalStack(BRONZE, 4), COPPER, COPPER, COPPER, TIN);
  }

  static void registerMeltables() {
    for(final String oreName : OreDictionary.getOreNames()) {
      if(oreName.startsWith("ore")) {
        addMeltable(oreName, oreName.substring(3).toLowerCase(), 1, Fluid.BUCKET_VOLUME);
      } else if(oreName.startsWith("ingot")) {
        addMeltable(oreName, oreName.substring(5).toLowerCase(), 1, Fluid.BUCKET_VOLUME);
      } else if(oreName.startsWith("nugget")) {
        final Meltable meltable = addMeltable(oreName, oreName.substring(6).toLowerCase(), 1.0f / 4.0f, Fluid.BUCKET_VOLUME / 4);

        if(meltable != INVALID_MELTABLE) {
          meltable.metal.nugget = OreDictHelper.getFirst(oreName);
        }
      } else if(oreName.startsWith("dust")) {
        addMeltable(oreName, oreName.substring(4).toLowerCase(), 1, Fluid.BUCKET_VOLUME);
      } else if(oreName.startsWith("block")) {
        addMeltable(oreName, oreName.substring(5).toLowerCase(), 8, Fluid.BUCKET_VOLUME * 8);
      } else if(oreName.startsWith("alloyNugget")) {
        final Metal metal = getMetal(oreName.substring(11).toLowerCase());

        for(final Alloy alloy : alloys) {
          if(alloy.output.metal == metal) {
            float hottest = 0;

            for(final Metal component : alloy.inputs) {
              if(component.meltTemp > hottest) {
                hottest = component.meltTemp;
              }
            }

            addMeltable(oreName, metal, alloy.inputs.size() / 4.0f, hottest, Fluid.BUCKET_VOLUME / 4 * alloy.output.amount);
            break;
          }
        }
      }
    }

    addMeltable("sand",       GLASS, 8, Fluid.BUCKET_VOLUME * 8);
    addMeltable("blockGlass", GLASS, 8, Fluid.BUCKET_VOLUME * 8);
    addMeltable("paneGlass",  GLASS, 8.0f / 16.0f, Fluid.BUCKET_VOLUME * 8 / 16);
  }

  public static ItemStack getBucket(final GradientMetals.MetalStack metal) {
    final ItemStack stack = getBucket(metal.metal);
    stack.grow(metal.amount - 1);

    return stack;
  }

  public static ItemStack getBucket(final GradientMetals.Metal metal) {
    return FluidUtil.getFilledBucket(new FluidStack(metal.getFluid(), Fluid.BUCKET_VOLUME));
  }

  private static Meltable addMeltable(final String oreName, final String metal, final float meltModifier, final int amount) {
    final Metal m = getMetal(metal);

    if(m != INVALID_METAL) {
      return addMeltable(oreName, m, meltModifier, amount);
    }

    return INVALID_MELTABLE;
  }

  private static Meltable addMeltable(final String oreDict, final Metal metal, final float meltModifier, final float meltTemp, final int amount) {
    final Meltable meltable = new Meltable(metal, meltModifier, meltTemp, amount);
    meltables.put(OreDictionary.getOreID(oreDict), meltable);
    return meltable;
  }

  private static Meltable addMeltable(final String oreDict, final Metal metal, final float meltModifier, final int amount) {
    final Meltable meltable = new Meltable(metal, meltModifier, amount);
    meltables.put(OreDictionary.getOreID(oreDict), meltable);
    return meltable;
  }

  public static MetalBuilder addMetal(final String name, final float meltTemp, final float hardness, final float weight) {
    return new MetalBuilder(name, meltTemp, hardness, weight);
  }

  public static Alloy addAlloy(final MetalStack output, final Metal... input) {
    final Alloy alloy = new Alloy(output, input);
    alloys.add(alloy);
    return alloy;
  }

  public static Metal getMetal(final String name) {
    for(final Metal metal : metals) {
      if(metal.name.equals(name)) {
        return metal;
      }
    }

    return INVALID_METAL;
  }

  public static Metal getMetal(final int index) {
    return metals.get(index);
  }

  private static MetalStack metalStack(final Metal metal, final int amount) {
    return new MetalStack(metal, amount);
  }

  private static MetalStack metalStack(final String metal, final int amount) {
    return metalStack(getMetal(metal), amount);
  }

  private static MetalStack metalStack(final Metal metal) {
    return metalStack(metal, 1);
  }

  private static MetalStack metalStack(final String metal) {
    return metalStack(metal, Fluid.BUCKET_VOLUME);
  }

  public static Meltable getMeltable(final ItemStack stack) {
    for(final int id : OreDictionary.getOreIDs(stack)) {
      final Meltable meltable = meltables.get(id);

      if(meltable != null) {
        return meltable;
      }
    }

    return INVALID_MELTABLE;
  }

  public static boolean hasMetal(final String name) {
    return getMetal(name) != INVALID_METAL;
  }

  public static boolean hasMeltable(final ItemStack stack) {
    return getMeltable(stack) != INVALID_MELTABLE;
  }

  public static Metal getMetalForFluid(final Fluid fluid) {
    for(final Metal metal : metals) {
      if(metal.fluid == fluid) {
        return metal;
      }
    }

    return INVALID_METAL;
  }

  public static final class Metal {
    private static int currentId;

    public final int    id;
    public final String name;
    public final int    meltTime;
    public final float  meltTemp;
    public final float  hardness;
    public final float  weight;

    public final int durability;

    public final int harvestLevel;
    public final float harvestSpeed;

    public final double attackDamageMultiplier;
    public final double attackSpeedMultiplier;

    public final boolean canMakeTools;
    public final boolean canMakeNuggets;
    public final boolean canMakeIngots;
    public final boolean canMakePlates;
    public final boolean canMakeDustWithMortar;

    private ItemStack nugget;
    Fluid fluid;

    private Metal(final String name, final float meltTemp, final float hardness, final float weight, final boolean canMakeTools, final boolean canMakeNuggets, final boolean canMakeIngots) {
      this.id = currentId++;
      this.name = name;
      this.meltTime = Math.round(hardness * 7.5f);
      this.meltTemp = meltTemp;
      this.hardness = hardness;
      this.weight   = weight;

      this.durability = Math.round(hardness * 35);

      this.harvestLevel = Math.round(hardness / 2);
      this.harvestSpeed = 1 / weight * 130;

      this.attackDamageMultiplier = hardness / 2 * weight / 100;
      this.attackSpeedMultiplier  = 1 / weight * 100;

      this.canMakeTools = canMakeTools;
      this.canMakeNuggets = canMakeNuggets;
      this.canMakeIngots = canMakeIngots;
      this.canMakePlates = hardness <= 4.0f;
      this.canMakeDustWithMortar = hardness <= 2.5f;
    }

    public ItemStack getNugget() {
      return this.nugget.copy();
    }

    public Fluid getFluid() {
      return this.fluid;
    }
  }

  public static class MetalStack {
    public final Metal metal;
    public final int amount;

    public MetalStack(final Metal metal, final int amount) {
      this.metal  = metal;
      this.amount = amount;
    }
  }

  public static class Alloy {
    public final MetalStack output;
    public final List<Metal> inputs;

    public Alloy(final MetalStack output, final Metal... inputs) {
      this.output = output;
      this.inputs = ImmutableList.copyOf(inputs);
    }
  }

  public static class Meltable {
    public final Metal metal;
    public final float meltModifier;
    public final float meltTemp;
    public final int   amount;

    public Meltable(final Metal metal, final float meltModifier, final float meltTemp, final int amount) {
      this.metal        = metal;
      this.meltModifier = meltModifier;
      this.meltTemp     = meltTemp;
      this.amount       = amount;
    }

    public Meltable(final Metal metal, final float meltModifier, final int amount) {
      this(metal, meltModifier, metal.meltTemp, amount);
    }
  }

  public static final class MetalBuilder {
    private final String name;
    private final float  meltTemp;
    private final float  hardness;
    private final float  weight;

    private boolean canMakeTools = true;
    private boolean canMakeNuggets = true;
    private boolean canMakeIngots = true;

    private MetalBuilder(final String name, final float meltTemp, final float hardness, final float weight) {
      this.name = name;
      this.meltTemp = meltTemp;
      this.hardness = hardness;
      this.weight = weight;
    }

    public MetalBuilder disableTools() {
      this.canMakeTools = false;
      return this;
    }

    public MetalBuilder disableNuggets() {
      this.canMakeNuggets = false;
      return this;
    }

    public MetalBuilder disableIngots() {
      this.canMakeIngots = false;
      return this;
    }

    public Metal add() {
      final Metal metal = new Metal(this.name, this.meltTemp, this.hardness, this.weight, this.canMakeTools, this.canMakeNuggets, this.canMakeIngots);
      metals.add(metal);
      return metal;
    }
  }
}
