package lordmonoxide.gradient;

import com.google.common.base.Optional;
import net.minecraft.block.properties.PropertyHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class GradientTools {
  private GradientTools() { }
  
  public static final List<Tool> tools = new ArrayList<>();
  private static final List<String> names = new ArrayList<>();
  
  public static final Tool PICKAXE = register("pickaxe");
  public static final Tool MATTOCK = register("mattock");
  
  public static Tool register(final String name) {
    Tool tool = new Tool(name);
    tools.add(tool);
    names.add(name);
    return tool;
  }
  
  public static class Tool implements Comparable<Tool> {
    private static int currentId;
    
    public final int id;
    public final String name;
    
    public Tool(final String name) {
      this.id = currentId++;
      this.name = name;
    }
  
    @Override
    public int compareTo(final Tool o) {
      assert o != null;
      
      return this.id == o.id ? 0 : this.id > o.id ? 1 : -1;
    }
    
    @Override
    public boolean equals(final Object o) {
      assert o instanceof Tool;
      
      return this.id == ((Tool)o).id;
    }
  }
  
  public static class PropertyTool extends PropertyHelper<Tool> {
    public static PropertyTool create(final String name) {
      return new PropertyTool(name);
    }
    
    protected PropertyTool(final String name) {
      super(name, Tool.class);
    }
    
    @Override
    public Collection<Tool> getAllowedValues() {
      return tools;
    }
    
    @Override
    public Optional<Tool> parseValue(final String value) {
      for(final Tool tool : tools) {
        if(tool.name.equals(value)) {
          return Optional.of(tool);
        }
      }
      
      return Optional.absent();
    }
    
    @Override
    public String getName(final Tool tool) {
      return tool.name;
    }
  }
}
