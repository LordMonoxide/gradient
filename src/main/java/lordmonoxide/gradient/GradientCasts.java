package lordmonoxide.gradient;

import lordmonoxide.gradient.blocks.GradientBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public final class GradientCasts {
  private GradientCasts() { }

  private static final Map<String, Cast> CASTS = new LinkedHashMap<>();

  public static final Cast PICKAXE = register("pickaxe").isValid(metal -> metal.canMakeTools).add();
  public static final Cast MATTOCK = register("mattock").isValid(metal -> metal.canMakeTools).add();
  public static final Cast SWORD   = register("sword")  .isValid(metal -> metal.canMakeTools).add();
  public static final Cast HAMMER  = register("hammer") .isValid(metal -> metal.canMakeTools).add();
  public static final Cast INGOT   = register("ingot")  .isValid(metal -> metal.canMakeIngots).add();

  public static final Cast BLOCK = register("block")
    .itemOverride(metal -> new ItemStack(GradientBlocks.CAST_BLOCK.get(metal)))
    .amount(metal -> Fluid.BUCKET_VOLUME * 8)
    .amount(GradientMetals.GLASS, Fluid.BUCKET_VOLUME)
    .add();

  public static CastBuilder register(final String name) {
    return new CastBuilder(name);
  }

  public static Collection<Cast> casts() {
    return CASTS.values();
  }

  public static Cast getCast(final String name) {
    final Cast cast = CASTS.get(name);

    if(cast == null) {
      return PICKAXE;
    }

    return cast;
  }

  public static Cast getCast(final int id) {
    for(final Cast cast : CASTS.values()) {
      if(cast.id == id) {
        return cast;
      }
    }

    return PICKAXE;
  }

  public static class Cast {
    private static int currentId;

    public final int id;
    public final String name;
    private final Map<GradientMetals.Metal, Function<GradientMetals.Metal, Boolean>> isValid;
    private final Map<GradientMetals.Metal, Function<GradientMetals.Metal, Integer>> amount;
    private final Map<GradientMetals.Metal, Function<GradientMetals.Metal, ItemStack>> itemOverride;

    public Cast(final String name, final Map<GradientMetals.Metal, Function<GradientMetals.Metal, Boolean>> isValid, final Map<GradientMetals.Metal, Function<GradientMetals.Metal, Integer>> amount, final Map<GradientMetals.Metal, Function<GradientMetals.Metal, ItemStack>> itemOverride) {
      this.id = currentId++;
      this.name = name;
      this.isValid = isValid;
      this.amount = amount;
      this.itemOverride = itemOverride;
    }

    public boolean isValidForMetal(final GradientMetals.Metal metal) {
      final Function<GradientMetals.Metal, Boolean> validFn = this.isValid.get(metal);
      return validFn != null ? validFn.apply(metal) : false;
    }

    public int amountForMetal(final GradientMetals.Metal metal) {
      final Function<GradientMetals.Metal, Integer> amountFn = this.amount.get(metal);
      return amountFn != null ? amountFn.apply(metal) : 0;
    }

    @Nullable
    public ItemStack itemForMetal(final GradientMetals.Metal metal) {
      final Function<GradientMetals.Metal, ItemStack> stackFn = this.itemOverride.get(metal);
      return stackFn != null ? stackFn.apply(metal) : null;
    }

    @Override
    public boolean equals(final Object o) {
      assert o instanceof Cast;

      return this.id == ((Cast)o).id;
    }

    @Override
    public int hashCode() {
      return Integer.hashCode(this.id);
    }
  }

  public static final class CastBuilder {
    private final String name;

    private final Map<GradientMetals.Metal, Function<GradientMetals.Metal, Boolean>> isValid = new HashMap<>();
    private final Map<GradientMetals.Metal, Function<GradientMetals.Metal, Integer>> amount = new HashMap<>();
    private final Map<GradientMetals.Metal, Function<GradientMetals.Metal, ItemStack>> itemOverride = new HashMap<>();

    private CastBuilder(final String name) {
      this.name = name;
      this.isValid(metal -> true);
      this.amount(metal -> Fluid.BUCKET_VOLUME);
    }

    public CastBuilder isValid(final Function<GradientMetals.Metal, Boolean> isValid) {
      GradientMetals.metals.forEach(metal -> this.isValid.put(metal, isValid));
      return this;
    }

    public CastBuilder isValid(final GradientMetals.Metal metal, final boolean isValid) {
      this.isValid.put(metal, m -> isValid);
      return this;
    }

    public CastBuilder amount(final Function<GradientMetals.Metal, Integer> amount) {
      GradientMetals.metals.forEach(metal -> this.amount.put(metal, amount));
      return this;
    }

    public CastBuilder amount(final GradientMetals.Metal metal, final int amount) {
      this.amount.put(metal, m -> amount);
      return this;
    }

    public CastBuilder itemOverride(final Function<GradientMetals.Metal, ItemStack> callback) {
      GradientMetals.metals.forEach(metal -> this.itemOverride.put(metal, callback));
      return this;
    }

    public CastBuilder itemOverride(final GradientMetals.Metal metal, final ItemStack stack) {
      this.itemOverride.put(metal, m -> stack);
      return this;
    }

    public Cast add() {
      final Cast cast = new Cast(this.name, this.isValid, this.amount, this.itemOverride);
      CASTS.put(this.name, cast);
      return cast;
    }
  }
}
