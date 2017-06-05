package lordmonoxide.gradient;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

public class GradientFuel {
  public final static GradientFuel instance = new GradientFuel();
  
  private final Map<Integer, Fuel> fuels = new HashMap<>();
  
  private GradientFuel() {
    this.add("string",        10,  50,  125, 1.000f);
    this.add("stickWood",     30, 100,  350, 0.108f);
    this.add("treeSapling",   60, 150,  350, 0.069f);
    this.add("plankWood",    600, 230,  750, 0.052f);
    this.add("logWood",      900, 300,  750, 0.038f);
    this.add("oreCoal",     1200, 700, 2700, 0.075f);
  }
  
  public void add(String oreDictName, int duration, float ignitionTemp, float burnTemp, float heatPerTick) {
    this.fuels.put(OreDictionary.getOreID(oreDictName), new Fuel(duration, ignitionTemp, burnTemp, heatPerTick));
  }
  
  public Fuel get(int oreDictId) {
    return this.fuels.get(oreDictId);
  }
  
  public Fuel get(String oreDictName) {
    return this.get(OreDictionary.getOreID(oreDictName));
  }
  
  public Fuel get(ItemStack stack) {
    for(int id : OreDictionary.getOreIDs(stack)) {
      return get(id);
    }
    
    return null;
  }
  
  public boolean has(int oreDictId) {
    return get(oreDictId) != null;
  }
  
  public boolean has(String oreDictName) {
    return this.has(OreDictionary.getOreID(oreDictName));
  }
  
  public boolean has(ItemStack stack) {
    for(int id : OreDictionary.getOreIDs(stack)) {
      if(this.has(id)) {
        return true;
      }
    }
    
    return false;
  }
  
  public static class Fuel {
    public final int   duration;
    public final float ignitionTemp;
    public final float burnTemp;
    public final float heatPerTick;
    
    public Fuel(int duration, float ignitionTemp, float burnTemp, float heatPerTick) {
      this.duration     = duration;
      this.ignitionTemp = ignitionTemp;
      this.burnTemp     = burnTemp;
      this.heatPerTick  = heatPerTick;
    }
  }
}
