package lordmonoxide.gradient;

import com.google.common.base.Optional;
import lordmonoxide.gradient.blocks.GradientBlocks;
import net.minecraft.block.properties.PropertyHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class GradientCasts {
  private GradientCasts() { }
  
  private static final Map<String, Cast> CASTS = new HashMap<>();
  
  public static final Cast PICKAXE = register("pickaxe").tool().add();
  public static final Cast MATTOCK = register("mattock").tool().add();
  public static final Cast SWORD   = register("sword").tool().add();
  public static final Cast HAMMER  = register("hammer").tool().add();
  public static final Cast INGOT   = register("ingot").add();
  
  public static final Cast BLOCK = register("block")
    .itemOverride(metal -> new ItemStack(GradientBlocks.CAST_BLOCK.get(metal)))
    .amount(Fluid.BUCKET_VOLUME * 8)
    .add();
  
  public static CastBuilder register(final String name) {
    return new CastBuilder(name);
  }
  
  public static Collection<Cast> casts() {
    return CASTS.values();
  }
  
  public static Cast getCast(final String name) {
    Cast cast = CASTS.get(name);
    
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
  
  public static class Cast implements Comparable<Cast> {
    private static int currentId;
    
    public final int id;
    public final String name;
    public final int amount;
    public final boolean tool;
    public final Map<GradientMetals.Metal, Function<GradientMetals.Metal, ItemStack>> itemOverride;
    
    public Cast(final String name, final int amount, final boolean tool, final Map<GradientMetals.Metal, Function<GradientMetals.Metal, ItemStack>> itemOverride) {
      this.id = currentId++;
      this.name = name;
      this.amount = amount;
      this.tool = tool;
      this.itemOverride = itemOverride;
    }
    
    @Override
    public int compareTo(final Cast o) {
      assert o != null;
      
      return Integer.compare(this.id, o.id);
    }
    
    @Override
    public boolean equals(final Object o) {
      assert o instanceof Cast;
      
      return this.id == ((Cast)o).id;
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
      return CASTS.values();
    }
    
    @Override
    public Optional<Cast> parseValue(final String value) {
      Cast cast = CASTS.get(value);
      
      if(cast == null) {
        return Optional.absent();
      }
      
      return Optional.of(cast);
    }
    
    @Override
    public String getName(final Cast cast) {
      return cast.name;
    }
  }
  
  public static final class CastBuilder {
    private final String name;
    
    private int amount = 1000;
    private boolean tool;
    
    private final Map<GradientMetals.Metal, Function<GradientMetals.Metal, ItemStack>> itemOverride = new HashMap<GradientMetals.Metal, Function<GradientMetals.Metal, ItemStack>>();
    
    private CastBuilder(final String name) {
      this.name = name;
    }
    
    public CastBuilder amount(int amount) {
      this.amount = amount;
      return this;
    }
    
    public CastBuilder tool() {
      this.tool = true;
      return this;
    }
    
    public CastBuilder itemOverride(final Function<GradientMetals.Metal, ItemStack> callback) {
      GradientMetals.metals.forEach(metal -> this.itemOverride.put(metal, callback));
      return this;
    }
    
    public CastBuilder itemOverride(final GradientMetals.Metal metal, final Function<GradientMetals.Metal, ItemStack> callback) {
      this.itemOverride.put(metal, callback);
      return this;
    }
    
    public Cast add() {
      final Cast cast = new Cast(this.name, this.amount, this.tool, this.itemOverride);
      CASTS.put(this.name, cast);
      return cast;
    }
  }
}
