package lordmonoxide.gradient.science.geology;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class Ores {
  private Ores() { }

  private static final Map<String, Ore> ores = new HashMap<>();
  private static final Map<String, Ore.Metal> metals = new HashMap<>();

  public static final Ore.Metal INVALID_ORE_METAL = new Ore.Metal("invalid", Metals.INVALID_METAL);

  public static final Ore.Metal AZURITE     = addOre("azurite", o -> o.metal(Metals.AZURITE));
  public static final Ore.Metal CASSITERITE = addOre("cassiterite", o -> o.metal(Metals.CASSITERITE));
  public static final Ore.Metal COPPER      = addOre("copper", o -> o.metal(Metals.COPPER));
  public static final Ore.Metal GOLD        = addOre("gold", o -> o.metal(Metals.GOLD));
  public static final Ore.Metal GRAPHITE    = addOre("graphite", o -> o.metal(Metals.GRAPHITE));
  public static final Ore.Metal HEMATITE    = addOre("hematite", o -> o.metal(Metals.HEMATITE));
  public static final Ore.Metal PYRITE      = addOre("pyrite", o -> o.metal(Metals.PYRITE));
  public static final Ore.Metal SPHALERITE  = addOre("sphalerite", o -> o.metal(Metals.SPHALERITE));

  public static Ore.Metal addOre(final String name, final Consumer<MetalOreBuilder> builder) {
    final MetalOreBuilder mb = new MetalOreBuilder();
    builder.accept(mb);
    final Ore.Metal ore = new Ore.Metal(name, mb.metal);
    ores.put(name, ore);
    metals.put(name, ore);
    return ore;
  }

  public static Ore get(final String name) {
    return ores.getOrDefault(name, INVALID_ORE_METAL);
  }

  public static Ore.Metal getMetal(final String name) {
    return metals.getOrDefault(name, INVALID_ORE_METAL);
  }

  public static Collection<Ore> all() {
    return ores.values();
  }

  public static Collection<Ore.Metal> metals() {
    return metals.values();
  }

  private static class MetalOreBuilder {
    private Metal metal;

    public MetalOreBuilder metal(final Metal metal) {
      this.metal = metal;
      return this;
    }
  }
}
