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
  
  public static final Cast PICKAXE = register("pickaxe").tool().add();
  public static final Cast MATTOCK = register("mattock").tool().add();
  public static final Cast SWORD   = register("sword").tool().add();
  public static final Cast HAMMER  = register("hammer").tool().add();
  public static final Cast INGOT   = register("ingot").itemOverride(GradientItems.INGOT).add();
  
  public static CastBuilder register(final String name) {
    return new CastBuilder(name);
  }
  
  public static class Cast implements Comparable<Cast> {
    private static int currentId;
    
    public final int id;
    public final String name;
    public final boolean tool;
    public final Item itemOverride;
    
    public Cast(final String name, final boolean tool, @Nullable final Item itemOverride) {
      this.id = currentId++;
      this.name = name;
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
    
    private boolean tool;
    
    private Item itemOverride;
    
    private CastBuilder(final String name) {
      this.name = name;
    }
    
    public CastBuilder tool() {
      this.tool = true;
      return this;
    }
    
    public CastBuilder itemOverride(final Item itemOverride) {
      this.itemOverride = itemOverride;
      return this;
    }
    
    public Cast add() {
      final Cast cast = new Cast(this.name, this.tool, this.itemOverride);
      CASTS.add(cast);
      return cast;
    }
  }
}
