package lordmonoxide.gradient.init;

import com.google.common.base.Optional;
import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import net.minecraft.block.properties.PropertyHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
@GameRegistry.ObjectHolder("gradient")
public final class CastRegistry {
  private CastRegistry() { }

  @SuppressWarnings("ConstantConditions")
  @Nonnull
  public static final Cast PICKAXE = null;
  @SuppressWarnings("ConstantConditions")
  @Nonnull
  public static final Cast MATTOCK = null;
  @SuppressWarnings("ConstantConditions")
  @Nonnull
  public static final Cast SWORD = null;
  @SuppressWarnings("ConstantConditions")
  @Nonnull
  public static final Cast HAMMER = null;
  @SuppressWarnings("ConstantConditions")
  @Nonnull
  public static final Cast INGOT = null;
  @SuppressWarnings("ConstantConditions")
  @Nonnull
  public static final Cast BLOCK = null;

  @SubscribeEvent
  public static void createRegistry(final RegistryEvent.NewRegistry event) {
    final RegistryBuilder<Cast> builder = new RegistryBuilder<>();
    builder
        .setName(GradientMod.resource("casts"))
        .setType(Cast.class)
        .create();
  }

  @SubscribeEvent
  public static void registerCasts(final RegistryEvent.Register<Cast> event) {
    final IForgeRegistry<Cast> registry = event.getRegistry();

    registry.register(build("pickaxe").isValid(metal -> metal.canMakeTools).build());
    registry.register(build("mattock").isValid(metal -> metal.canMakeTools).build());
    registry.register(build("sword").isValid(metal -> metal.canMakeTools).build());
    registry.register(build("hammer").isValid(metal -> metal.canMakeTools).build());
    registry.register(build("ingot").isValid(metal -> metal.canMakeIngots).build());

    registry.register(
      build("block")
        .itemOverride(metal -> new ItemStack(GradientBlocks.CAST_BLOCK.get(metal)))
        .amount(metal -> Fluid.BUCKET_VOLUME * 8)
        .amount(GradientMetals.GLASS, Fluid.BUCKET_VOLUME)
        .build()
    );
  }

  public static Cast getCast(final int id) {
    for(final Cast cast : GameRegistry.findRegistry(Cast.class)) {
      if(cast.id == id) {
        return cast;
      }
    }

    return PICKAXE;
  }

  private static CastBuilder build(final String name) {
    return new CastBuilder(GradientMod.resource(name));
  }

  public static class Cast extends IForgeRegistryEntry.Impl<Cast> implements Comparable<Cast> {
    private static int currentId;

    public final int id;
    private final Map<GradientMetals.Metal, Function<GradientMetals.Metal, Boolean>> isValid;
    private final Map<GradientMetals.Metal, Function<GradientMetals.Metal, Integer>> amount;
    private final Map<GradientMetals.Metal, Function<GradientMetals.Metal, ItemStack>> itemOverride;

    public Cast(final Map<GradientMetals.Metal, Function<GradientMetals.Metal, Boolean>> isValid, final Map<GradientMetals.Metal, Function<GradientMetals.Metal, Integer>> amount, final Map<GradientMetals.Metal, Function<GradientMetals.Metal, ItemStack>> itemOverride) {
      this.id = currentId++;
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
    public int compareTo(final Cast o) {
      assert o != null;

      return Integer.compare(this.id, o.id);
    }

    @Override
    public boolean equals(final Object o) {
      return o instanceof Cast && this.id == ((Cast)o).id;
    }

    @Override
    public int hashCode() {
      return this.id;
    }
  }

  public static class PropertyCast extends PropertyHelper<Cast> {
    public static PropertyCast create(final String name) {
      return new PropertyCast(name);
    }

    protected PropertyCast(final String name) {
      super(name, Cast.class);
    }

    @Override
    public Collection<Cast> getAllowedValues() {
      return GameRegistry.findRegistry(Cast.class).getValuesCollection();
    }

    @Override
    public Optional<Cast> parseValue(final String value) {
      final Cast cast = GameRegistry.findRegistry(Cast.class).getValue(new ResourceLocation(value));

      if(cast == null) {
        return Optional.absent();
      }

      return Optional.of(cast);
    }

    @Override
    public String getName(final Cast cast) {
      return cast.getRegistryName().toString();
    }
  }

  public static final class CastBuilder {
    private final ResourceLocation name;

    private final Map<GradientMetals.Metal, Function<GradientMetals.Metal, Boolean>> isValid = new HashMap<>();
    private final Map<GradientMetals.Metal, Function<GradientMetals.Metal, Integer>> amount = new HashMap<>();
    private final Map<GradientMetals.Metal, Function<GradientMetals.Metal, ItemStack>> itemOverride = new HashMap<>();

    private CastBuilder(final ResourceLocation name) {
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

    public Cast build() {
      return new Cast(this.isValid, this.amount, this.itemOverride).setRegistryName(this.name);
    }
  }
}
