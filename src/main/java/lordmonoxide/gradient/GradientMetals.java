package lordmonoxide.gradient;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.Map;

public final class GradientMetals {
  public static final GradientMetals instance = new GradientMetals();
  
  public static final GradientMetals.Metal INVALID_METAL = new GradientMetals.Metal(0, Integer.MAX_VALUE);
  
  private final Map<Integer, GradientMetals.Metal> metals = new HashMap<>();
  
  private GradientMetals() {
    this.add("ingotTin",      10,  231.93f);
    this.add("ingotCopper",   10, 1085.00f);
    this.add("ingotIron",     10, 1538.00f);
    this.add("ingotSilver",   10,  961.78f);
    this.add("ingotGold",     10, 1064.00f);
    this.add("ingotTitanium", 10, 1668.00f);
  }
  
  public void add(String oreDictName, int duration, float meltTemp) {
    this.metals.put(OreDictionary.getOreID(oreDictName), new GradientMetals.Metal(duration, meltTemp));
  }
  
  public GradientMetals.Metal get(ItemStack stack) {
    for(int id : OreDictionary.getOreIDs(stack)) {
      GradientMetals.Metal metal = this.metals.get(id);
      
      if(metal != null) {
        return metal;
      }
    }
    
    return INVALID_METAL;
  }
  
  public boolean has(ItemStack stack) {
    return this.get(stack) != INVALID_METAL;
  }
  
  public static class Metal {
    public final int   duration;
    public final float meltTemp;
    
    public Metal(int duration, float meltTemp) {
      this.duration = duration;
      this.meltTemp = meltTemp;
    }
  }
}
