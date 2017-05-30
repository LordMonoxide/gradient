package lordmonoxide.gradient;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

public class GradientFuel {
  public final static GradientFuel instance = new GradientFuel();
  
  private final Map<Integer, Fuel> fuels = new HashMap<>();
  
  private GradientFuel() {
    this.add("string",        10,   0,  100);
    this.add("stickWood",     60, 100,  250);
    this.add("treeSapling",  120, 150,  250);
    this.add("plankWood",    600, 230,  750);
    this.add("logWood",      900, 300,  750);
    this.add("oreCoal",     1200, 700, 2700);
  }
  
  public void add(String oreDictName, int duration, float ignitionTemp, float burnTemp) {
    this.fuels.put(OreDictionary.getOreID(oreDictName), new Fuel(duration, ignitionTemp, burnTemp));
  }
  
  public Fuel get(int oreDictId) {
    return this.fuels.get(oreDictId);
  }
  
  public Fuel get(String oreDictName) {
    return this.get(OreDictionary.getOreID(oreDictName));
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
    
    public Fuel(int duration, float ignitionTemp, float burnTemp) {
      this.duration     = duration;
      this.ignitionTemp = ignitionTemp;
      this.burnTemp     = burnTemp;
    }
  }
}
