package lordmonoxide.gradient;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GradientMetals {
  public static final GradientMetals instance = new GradientMetals();
  
  public static final Metal    INVALID_METAL    = instance.addMetal("invalid", 0, Integer.MAX_VALUE);
  public static final Meltable INVALID_MELTABLE = new Meltable(INVALID_METAL, 0, 0);
  
  private final List<Metal> metals = new ArrayList<>();
  private final Map<Integer, Meltable> meltables = new HashMap<>();
  
  static {
    instance.addMetal("copper", 20, 1085.00f);
    instance.addMetal("iron",   30, 1538.00f);
  }
  
  public void registerMeltables() {
    for(String oreName : OreDictionary.getOreNames()) {
      if(oreName.startsWith("ingot")) {
        this.addMeltable(oreName, oreName.substring(5, 6).toLowerCase() + oreName.substring(6), 1, 1);
      } else if(oreName.startsWith("nugget")) {
        this.addMeltable(oreName, oreName.substring(6, 7).toLowerCase() + oreName.substring(7), 1.0f / 9.0f, 1.0f / 9.0f);
      }
    }
  }
  
  private void addMeltable(String oreName, String metal, float meltModifier, float amount) {
    Metal m = this.getMetal(metal);
    
    if(m != INVALID_METAL) {
      this.addMeltable(oreName, m, meltModifier, amount);
    }
  }
  
  private void addMeltable(String oreDict, Metal metal, float meltModifier, float amount) {
    this.meltables.put(OreDictionary.getOreID(oreDict), new Meltable(metal, meltModifier, amount));
  }
  
  public Metal addMetal(String name, int meltTime, float meltTemp) {
    Metal metal = new Metal(name, meltTime, meltTemp);
    this.metals.add(metal);
    return metal;
  }
  
  public List<Metal> getMetals() {
    return this.metals;
  }
  
  public Metal getMetal(String name) {
    for(Metal metal : this.metals) {
      if(metal.name.equals(name)) {
        return metal;
      }
    }
    
    return INVALID_METAL;
  }
  
  public Metal getMetal(int index) {
    return this.metals.get(index);
  }
  
  public Meltable getMeltable(ItemStack stack) {
    for(int id : OreDictionary.getOreIDs(stack)) {
      Meltable meltable = this.meltables.get(id);
      
      if(meltable != null) {
        return meltable;
      }
    }
    
    return INVALID_MELTABLE;
  }
  
  public boolean hasMetal(String name) {
    return this.getMetal(name) != INVALID_METAL;
  }
  
  public boolean hasMeltable(ItemStack stack) {
    return this.getMeltable(stack) != INVALID_MELTABLE;
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
  
  public static class Meltable {
    public final Metal metal;
    public final float meltModifier;
    public final float amount;
    
    public Meltable(Metal metal, float meltModifier, float amount) {
      this.metal        = metal;
      this.meltModifier = meltModifier;
      this.amount       = amount;
    }
  }
}
