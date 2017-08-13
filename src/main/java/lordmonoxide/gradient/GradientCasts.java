package lordmonoxide.gradient;

import com.google.common.base.Optional;
import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.block.properties.PropertyHelper;
import net.minecraft.item.Item;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class GradientCasts {
  private GradientCasts() { }
  
  public static final List<Cast> CASTS = new ArrayList<>();
  
  public static final Cast PICKAXE = register("pickaxe");
  public static final Cast MATTOCK = register("mattock");
  public static final Cast SWORD   = register("sword");
  public static final Cast HAMMER  = register("hammer");
  public static final Cast INGOT   = register("ingot", GradientItems.INGOT);
  
  public static Cast register(final String name, @Nullable final Item itemOverride) {
    final Cast cast = new Cast(name, itemOverride);
    CASTS.add(cast);
    return cast;
  }
  
  public static Cast register(final String name) {
    return register(name, null);
  }
  
  public static class Cast implements Comparable<Cast> {
    private static int currentId;
    
    public final int id;
    public final String name;
    public final Item itemOverride;
    
    public Cast(final String name, @Nullable final Item itemOverride) {
      this.id = currentId++;
      this.name = name;
      this.itemOverride = itemOverride;
    }
    
    public Cast(final String name) {
      this(name, null);
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
}
