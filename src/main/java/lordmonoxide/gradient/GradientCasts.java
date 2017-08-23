package lordmonoxide.gradient;

import com.google.common.base.Optional;
import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.block.properties.PropertyHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import java.util.*;

public final class GradientCasts {
  private GradientCasts() { }
  
  public static final List<Cast> CASTS = new ArrayList<>();
  
  public static final Cast PICKAXE = register("pickaxe").tool().add();
  public static final Cast MATTOCK = register("mattock").tool().add();
  public static final Cast SWORD   = register("sword").tool().add();
  public static final Cast HAMMER  = register("hammer").tool().add();
  public static final Cast INGOT   = register("ingot").itemOverride(GradientItems.INGOT).add();
  public static final Cast BLOCK   = register("block").itemOverride(GradientItems.BLOCK).itemOverride(GradientMetals.GLASS, new ItemStack(Blocks.GLASS)).amount(Fluid.BUCKET_VOLUME * 8).add();
  
  public static CastBuilder register(final String name) {
    return new CastBuilder(name);
  }
  
  public static class Cast implements Comparable<Cast> {
    private static int currentId;
    
    public final int id;
    public final String name;
    public final int amount;
    public final boolean tool;
    public final Map<GradientMetals.Metal, ItemStack> itemOverride;
    
    public Cast(final String name, final int amount, final boolean tool, final Map<GradientMetals.Metal, ItemStack> itemOverride) {
      this.id = currentId++;
      this.name = name;
      this.amount = amount;
      this.tool = tool;
      this.itemOverride = itemOverride;
    }
    
    @Override
    public int compareTo(final Cast o) {
      assert o != null;
      
      return this.id == o.id ? 0 : this.id > o.id ? 1 : -1;
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
      return CASTS;
    }
    
    @Override
    public Optional<Cast> parseValue(final String value) {
      for(final Cast cast : CASTS) {
        if(cast.name.equals(value)) {
          return Optional.of(cast);
        }
      }
      
      return Optional.absent();
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
    
    private Map<GradientMetals.Metal, ItemStack> itemOverride = new HashMap<>();
    
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
  
    public CastBuilder itemOverride(final ItemStack item) {
      GradientMetals.metals.forEach(metal -> this.itemOverride.put(metal, item));
      return this;
    }
  
    public CastBuilder itemOverride(final Item item) {
      GradientMetals.metals.forEach(metal -> this.itemOverride.put(metal, new ItemStack(item, 1, metal.id)));
      return this;
    }
  
    public CastBuilder itemOverride(final GradientMetals.Metal metal, final ItemStack item) {
      this.itemOverride.put(metal, item);
      return this;
    }
    
    public Cast add() {
      final Cast cast = new Cast(this.name, this.amount, this.tool, this.itemOverride);
      CASTS.add(cast);
      return cast;
    }
  }
}
