package lordmonoxide.gradient;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

public final class GradientFuel {
  public static final GradientFuel instance = new GradientFuel();
  
  public static final Fuel INVALID_FUEL = new Fuel(0, 0, 0, 0.0f);
  
  private final Map<Integer, Fuel> fuels = new HashMap<>();
  
  private GradientFuel() {
    this.add("string",        10,  50,  125, 20.00f);
    this.add("stickWood",     30, 100,  350,  2.16f);
    this.add("treeSapling",   60, 150,  350,  1.38f);
    this.add("plankWood",    600, 230,  750,  1.04f);
    this.add("logWood",      900, 300,  750,  0.76f);
    this.add("coal",        1200, 700, 2700,  1.50f);
  }
  
  public void add(String oreDictName, int duration, float ignitionTemp, float burnTemp, float heatPerSec) {
    this.fuels.put(OreDictionary.getOreID(oreDictName), new Fuel(duration, ignitionTemp, burnTemp, heatPerSec));
  }
  
  public Fuel get(ItemStack stack) {
    for(int id : OreDictionary.getOreIDs(stack)) {
      Fuel fuel = this.fuels.get(id);
      
      if(fuel != null) {
        return fuel;
      }
    }
    
    return INVALID_FUEL;
  }
  
  public boolean has(ItemStack stack) {
    return this.get(stack) != INVALID_FUEL;
  }
  
  public static class Fuel {
    public final int   duration;
    public final float ignitionTemp;
    public final float burnTemp;
    public final float heatPerSec;
    
    public Fuel(int duration, float ignitionTemp, float burnTemp, float heatPerSec) {
      this.duration     = duration;
      this.ignitionTemp = ignitionTemp;
      this.burnTemp     = burnTemp;
      this.heatPerSec   = heatPerSec;
    }
  }
}
