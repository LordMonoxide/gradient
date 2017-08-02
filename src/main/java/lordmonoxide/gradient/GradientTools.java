package lordmonoxide.gradient;

import com.google.common.base.Optional;
import net.minecraft.block.properties.PropertyHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class GradientTools {
  private GradientTools() { }
  
  public static final List<Type> TYPES = new ArrayList<>();
  private static final List<String> names = new ArrayList<>();
  
  public static final Type PICKAXE = register("pickaxe", new String[] {"pickaxe"});
  public static final Type MATTOCK = register("mattock", new String[] {"axe", "shovel"});
  
  public static Type register(final String name, final String[] toolClass) {
    Type type = new Type(name, toolClass);
    TYPES.add(type);
    names.add(name);
    return type;
  }
  
  public static class Type implements Comparable<Type> {
    private static int currentId;
    
    public final int id;
    public final String name;
    public final String[] toolClass;
    
    public Type(final String name, final String[] toolClass) {
      this.id = currentId++;
      this.name = name;
      this.toolClass = toolClass;
    }
  
    @Override
    public int compareTo(final Type o) {
      assert o != null;
      
      return this.id == o.id ? 0 : this.id > o.id ? 1 : -1;
    }
    
    @Override
    public boolean equals(final Object o) {
      assert o instanceof Type;
      
      return this.id == ((Type)o).id;
    }
  }
  
  public static class PropertyTool extends PropertyHelper<Type> {
    public static PropertyTool create(final String name) {
      return new PropertyTool(name);
    }
    
    protected PropertyTool(final String name) {
      super(name, Type.class);
    }
    
    @Override
    public Collection<Type> getAllowedValues() {
      return TYPES;
    }
    
    @Override
    public Optional<Type> parseValue(final String value) {
      for(final Type type : TYPES) {
        if(type.name.equals(value)) {
          return Optional.of(type);
        }
      }
      
      return Optional.absent();
    }
    
    @Override
    public String getName(final Type type) {
      return type.name;
    }
  }
}
