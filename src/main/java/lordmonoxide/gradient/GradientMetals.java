package lordmonoxide.gradient;

import java.util.HashMap;
import java.util.Map;

public final class GradientMetals {
  public static final GradientMetals instance = new GradientMetals();
  
  public static final GradientMetals.Metal INVALID_METAL = new GradientMetals.Metal("invalid", 0, Integer.MAX_VALUE);
  
  private final Map<Integer, GradientMetals.Metal> metals = new HashMap<>();
  
  private GradientMetals() {
    this.add("copper", 10, 1085.00f);
  }
  
  public void add(String name, int meltTime, float meltTemp) {
    //this.metals.put(OreDictionary.getOreID(name), new GradientMetals.Metal(name, meltTime, meltTemp));
    GradientMetals.Metal metal = new GradientMetals.Metal(name, meltTime, meltTemp);
    this.metals.put(metal.id, metal);
  }
  
  public Map<Integer, GradientMetals.Metal> get() {
    return this.metals;
  }
  
  public GradientMetals.Metal get(int id) {
    GradientMetals.Metal metal = this.metals.get(id);
    
    if(metal != null) {
      return metal;
    }
    
    return INVALID_METAL;
  }
  
  public boolean has(int id) {
    return this.get(id) != INVALID_METAL;
  }
  
  public static class Metal {
    private static int currentId;
    
    public final int    id;
    public final String name;
    public final int    meltTime;
    public final float  meltTemp;
    
    public Metal(String name, int meltTime, float meltTemp) {
      this.id = currentId++;
      this.name = name;
      this.meltTime = meltTime;
      this.meltTemp = meltTemp;
    }
  }
}
