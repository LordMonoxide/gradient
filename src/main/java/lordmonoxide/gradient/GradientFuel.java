package lordmonoxide.gradient;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

public final class GradientFuel {
  private GradientFuel() { }
  
  public static final Fuel INVALID_FUEL = new Fuel(0, 0, 0, 0.0f);
  
  private static final Map<Integer, Fuel> fuels = new HashMap<>();
  
  static {
    add("igniter",        5,  50,  500, 32.00f);
    add("string",         5,  50,  125, 18.00f);
    add("stickWood",     30, 100,  350,  2.16f);
    add("treeSapling",   60, 150,  350,  1.38f);
    add("plankWood",    600, 230,  750,  1.04f);
    add("logWood",      900, 300,  750,  0.76f);
    add("coal",        1200, 700, 2700,  1.50f);
    
    add("infinicoal", Integer.MAX_VALUE, 0, 2700, 1.50f);
  }
  
  public static void add(final String oreDictName, final int duration, final float ignitionTemp, final float burnTemp, final float heatPerSec) {
    fuels.put(OreDictionary.getOreID(oreDictName), new Fuel(duration, ignitionTemp, burnTemp, heatPerSec));
  }
  
  public static Fuel get(final ItemStack stack) {
    for(int id : OreDictionary.getOreIDs(stack)) {
      Fuel fuel = fuels.get(id);
      
      if(fuel != null) {
        return fuel;
      }
    }
    
    return INVALID_FUEL;
  }
  
  public static boolean has(final ItemStack stack) {
    return get(stack) != INVALID_FUEL;
  }
  
  public static class Fuel {
    public final int   duration;
    public final float ignitionTemp;
    public final float burnTemp;
    public final float heatPerSec;
    
    public Fuel(final int duration, final float ignitionTemp, final float burnTemp, final float heatPerSec) {
      this.duration     = duration;
      this.ignitionTemp = ignitionTemp;
      this.burnTemp     = burnTemp;
      this.heatPerSec   = heatPerSec;
    }
  }
  
  public static final class BurningFuel {
    public final GradientFuel.Fuel fuel;
    public final long burnStart;
    public final long burnUntil;
    
    public BurningFuel(final GradientFuel.Fuel fuel) {
      this(fuel, System.currentTimeMillis(), System.currentTimeMillis() + fuel.duration * 1000L);
    }
    
    public BurningFuel(final GradientFuel.Fuel fuel, final long burnStart, final long burnUntil) {
      this.fuel = fuel;
      this.burnStart = burnStart;
      this.burnUntil = burnUntil;
    }
    
    public boolean isDepleted() {
      return System.currentTimeMillis() >= this.burnUntil;
    }
    
    public float burnPercent() {
      return (float)(System.currentTimeMillis() - this.burnStart) / (this.burnUntil - this.burnStart);
    }
  }
}
