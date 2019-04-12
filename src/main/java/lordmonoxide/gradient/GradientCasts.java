package lordmonoxide.gradient;

import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.science.geology.Metal;
import lordmonoxide.gradient.science.geology.Metals;
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

  public static final Cast PICKAXE = register("pickaxe").isValid(m -> m.canMakeTools).add();
  public static final Cast MATTOCK = register("mattock").isValid(m -> m.canMakeTools).add();
  public static final Cast SWORD   = register("sword")  .isValid(m -> m.canMakeTools).add();
  public static final Cast HAMMER  = register("hammer") .isValid(m -> m.canMakeTools).add();
  public static final Cast INGOT   = register("ingot")  .isValid(m -> m.canMakeIngots).add();

  public static final Cast BLOCK = register("block")
    .item(metal -> new ItemStack(GradientBlocks.castBlock(metal)))
    .amount(metal -> Fluid.BUCKET_VOLUME * 8)
    .amount(Metals.GLASS, Fluid.BUCKET_VOLUME)
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
    private final Map<Metal, Function<Metal, Boolean>> isValid;
    private final Map<Metal, Function<Metal, Integer>> amount;
    private final Map<Metal, Function<Metal, ItemStack>> item;

    public Cast(final String name, final Map<Metal, Function<Metal, Boolean>> isValid, final Map<Metal, Function<Metal, Integer>> amount, final Map<Metal, Function<Metal, ItemStack>> item) {
      this.id = currentId++;
      this.name = name;
      this.isValid = isValid;
      this.amount = amount;
      this.item = item;
    }

    public boolean isValidForMetal(final Metal metal) {
      final Function<Metal, Boolean> validFn = this.isValid.get(metal);
      return validFn != null ? validFn.apply(metal) : false;
    }

    public int amountForMetal(final Metal metal) {
      final Function<Metal, Integer> amountFn = this.amount.get(metal);
      return amountFn != null ? amountFn.apply(metal) : 0;
    }

    @Nullable
    public ItemStack itemForMetal(final Metal metal) {
      final Function<Metal, ItemStack> stackFn = this.item.get(metal);
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

    private final Map<Metal, Function<Metal, Boolean>> isValid = new HashMap<>();
    private final Map<Metal, Function<Metal, Integer>> amount = new HashMap<>();
    private final Map<Metal, Function<Metal, ItemStack>> item = new HashMap<>();

    private CastBuilder(final String name) {
      this.name = name;
      this.isValid(metal -> true);
      this.amount(metal -> Fluid.BUCKET_VOLUME);
    }

    public CastBuilder isValid(final Function<Metal, Boolean> isValid) {
      Metals.all().forEach(metal -> this.isValid.put(metal, isValid));
      return this;
    }

    public CastBuilder isValid(final Metal metal, final boolean isValid) {
      this.isValid.put(metal, m -> isValid);
      return this;
    }

    public CastBuilder amount(final Function<Metal, Integer> amount) {
      Metals.all().forEach(metal -> this.amount.put(metal, amount));
      return this;
    }

    public CastBuilder amount(final Metal metal, final int amount) {
      this.amount.put(metal, m -> amount);
      return this;
    }

    public CastBuilder item(final Function<Metal, ItemStack> callback) {
      Metals.all().forEach(metal -> this.item.put(metal, callback));
      return this;
    }

    public CastBuilder item(final Metal metal, final ItemStack stack) {
      this.item.put(metal, m -> stack);
      return this;
    }

    public Cast add() {
      final Cast cast = new Cast(this.name, this.isValid, this.amount, this.item);
      CASTS.put(this.name, cast);
      return cast;
    }
  }
}
