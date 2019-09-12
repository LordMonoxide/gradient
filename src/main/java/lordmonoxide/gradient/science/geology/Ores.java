package lordmonoxide.gradient.science.geology;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class Ores {
  private Ores() { }

  private static final Map<String, Ore> ores = new LinkedHashMap<>();
  private static final Map<String, Ore.Metal> metals = new LinkedHashMap<>();

  public static final Ore.Metal INVALID_ORE_METAL = new Ore.Metal("invalid", Metals.INVALID_METAL, Metals.INVALID_METAL);

  public static final Ore.Metal AZURITE     = addOre("azurite", o -> o.metal(Metals.AZURITE).basic(Metals.COPPER));
  public static final Ore.Metal CASSITERITE = addOre("cassiterite", o -> o.metal(Metals.CASSITERITE).basic(Metals.TIN));
  public static final Ore.Metal COPPER      = addOre("copper", o -> o.metal(Metals.COPPER));
  public static final Ore.Metal GOLD        = addOre("gold", o -> o.metal(Metals.GOLD));
  public static final Ore.Metal GRAPHITE    = addOre("graphite", o -> o.metal(Metals.GRAPHITE));
  public static final Ore.Metal HEMATITE    = addOre("hematite", o -> o.metal(Metals.HEMATITE).basic(Metals.IRON));
  public static final Ore.Metal PYRITE      = addOre("pyrite", o -> o.metal(Metals.PYRITE));
  public static final Ore.Metal SPHALERITE  = addOre("sphalerite", o -> o.metal(Metals.SPHALERITE).basic(Metals.ZINC));

  // Mod compatibility - not used by Gradient
  public static final Ore.Metal LEAD        = addOre("lead", o -> o.metal(Metals.LEAD));
  public static final Ore.Metal TIN         = addOre("tin", o -> o.metal(Metals.TIN));

  public static Ore.Metal addOre(final String name, final Consumer<MetalOreBuilder> builder) {
    final MetalOreBuilder mb = new MetalOreBuilder();
    builder.accept(mb);
    final Ore.Metal ore = new Ore.Metal(name, mb.metal, mb.basic);
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
    private Metal basic;

    public MetalOreBuilder metal(final Metal metal) {
      this.metal = metal;

      if(this.basic == null) {
        this.basic = metal;
      }

      return this;
    }

    public MetalOreBuilder basic(final Metal basic) {
      this.basic = basic;
      return this;
    }
  }
}
