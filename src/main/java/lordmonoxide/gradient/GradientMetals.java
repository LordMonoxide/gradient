package lordmonoxide.gradient;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GradientMetals {
  public static final GradientMetals instance = new GradientMetals();
  
  public static final Metal    INVALID_METAL    = instance.addMetal("invalid", 0, Integer.MAX_VALUE);
  public static final Meltable INVALID_MELTABLE = new Meltable(INVALID_METAL, 0, 0);
  
  public final List<Metal> metals = new ArrayList<>();
  public final List<Alloy> alloys = new ArrayList<>();
  
  private final Map<Integer, Meltable> meltables = new HashMap<>();
  
  static {
    instance.addMetal("copper", 20, 1085.00f);
    instance.addMetal("tin",    15,  231.93f);
    instance.addMetal("iron",   30, 1538.00f);
    instance.addMetal("gold",   20, 1064.00f);
    instance.addMetal("bronze", 20,  950.00f);
    
    instance.addAlloy(instance.metalStack("bronze", 4), instance.getMetal("copper"), instance.getMetal("copper"), instance.getMetal("copper"), instance.getMetal("tin"));
  }
  
  void registerMeltables() {
    for(String oreName : OreDictionary.getOreNames()) {
      if(oreName.startsWith("ore")) {
        this.addMeltable(oreName, oreName.substring(3).toLowerCase(), 1, Fluid.BUCKET_VOLUME);
      } else if(oreName.startsWith("ingot")) {
        this.addMeltable(oreName, oreName.substring(5).toLowerCase(), 1, Fluid.BUCKET_VOLUME);
      } else if(oreName.startsWith("nugget")) {
        Meltable meltable = this.addMeltable(oreName, oreName.substring(6).toLowerCase(), 1.0f / 4.0f, Fluid.BUCKET_VOLUME / 4);
        
        if(meltable != INVALID_MELTABLE) {
          meltable.metal.nugget = OreDictionary.getOres(oreName).get(0);
        }
      }
    }
  }
  
  private Meltable addMeltable(String oreName, String metal, float meltModifier, int amount) {
    Metal m = this.getMetal(metal);
    
    if(m != INVALID_METAL) {
      return this.addMeltable(oreName, m, meltModifier, amount);
    }
    
    return INVALID_MELTABLE;
  }
  
  private Meltable addMeltable(String oreDict, Metal metal, float meltModifier, int amount) {
    Meltable meltable = new Meltable(metal, meltModifier, amount);
    this.meltables.put(OreDictionary.getOreID(oreDict), meltable);
    return meltable;
  }
  
  public Metal addMetal(String name, int meltTime, float meltTemp) {
    Metal metal = new Metal(name, meltTime, meltTemp);
    this.metals.add(metal);
    return metal;
  }
  
  public Alloy addAlloy(MetalStack output, Metal... input) {
    Alloy alloy = new Alloy(output, input);
    this.alloys.add(alloy);
    return alloy;
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
  
  private MetalStack metalStack(String metal, int amount) {
    return new MetalStack(this.getMetal(metal), amount);
  }
  
  private MetalStack metalStack(String metal) {
    return this.metalStack(metal, Fluid.BUCKET_VOLUME);
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
  
  @Nullable
  public Metal getMetalForFluid(Fluid fluid) {
    return this.metals.stream().filter(metal -> metal.fluid == fluid).findFirst().orElse(null);
  }
  
  public static class Metal {
    private static int currentId;
    
    public final int    id;
    public final String name;
    public final int    meltTime;
    public final float  meltTemp;
    
    private ItemStack nugget;
    Fluid fluid;
    
    public Metal(String name, int meltTime, float meltTemp) {
      this.id = currentId++;
      this.name = name;
      this.meltTime = meltTime;
      this.meltTemp = meltTemp;
    }
    
    public ItemStack getNugget() {
      return this.nugget.copy();
    }
    
    public Fluid getFluid() {
      return this.fluid;
    }
  }
  
  public static class MetalStack {
    public final Metal metal;
    public final int amount;
    
    public MetalStack(Metal metal, int amount) {
      this.metal  = metal;
      this.amount = amount;
    }
  }
  
  public static class Alloy {
    public final MetalStack output;
    public final List<Metal> inputs;
    
    public Alloy(MetalStack output, Metal... inputs) {
      this.output = output;
      this.inputs = ImmutableList.copyOf(inputs);
    }
  }
  
  public static class Meltable {
    public final Metal metal;
    public final float meltModifier;
    public final int   amount;
    
    public Meltable(Metal metal, float meltModifier, int amount) {
      this.metal        = metal;
      this.meltModifier = meltModifier;
      this.amount       = amount;
    }
  }
}
