package lordmonoxide.gradient;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public final class GradientMetals {
  private GradientMetals() { }
  
  public static final List<Metal> metals = new ArrayList<>();
  public static final List<Alloy> alloys = new ArrayList<>();
  
  private static final Map<Integer, Meltable> meltables = new HashMap<>();
  
  public static final Metal    INVALID_METAL    = addMetal("invalid", 0, Integer.MAX_VALUE, 0, 0, 0, 0, 0);
  public static final Meltable INVALID_MELTABLE = new Meltable(INVALID_METAL, 0, 0);
  
  static {
    addMetal("copper",    20, 1085.00f, 100, 2, 1.6f, 0.8d, 1.0d);
    addMetal("tin",       15,  231.93f,  20, 0, 1.0f, 0.5d, 1.0d);
    addMetal("iron",      30, 1538.00f, 150, 2, 2.0f, 1.1d, 1.0d);
    addMetal("gold",      20, 1064.00f,  30, 0, 1.0f, 0.5d, 1.0d);
    addMetal("bronze",    20,  950.00f, 130, 2, 1.8f, 1.0d, 1.0d);
    addMetal("magnesium", 15,  650.00f,  20, 0, 1.0f, 0.5d, 1.0d);
    
    addAlloy(metalStack("bronze", 4), getMetal("copper"), getMetal("copper"), getMetal("copper"), getMetal("tin"));
  }
  
  static void registerMeltables() {
    for(final String oreName : OreDictionary.getOreNames()) {
      if(oreName.startsWith("ore")) {
        addMeltable(oreName, oreName.substring(3).toLowerCase(), 1, Fluid.BUCKET_VOLUME);
      } else if(oreName.startsWith("ingot")) {
        addMeltable(oreName, oreName.substring(5).toLowerCase(), 1, Fluid.BUCKET_VOLUME);
      } else if(oreName.startsWith("nugget")) {
        final Meltable meltable = addMeltable(oreName, oreName.substring(6).toLowerCase(), 1.0f / 4.0f, Fluid.BUCKET_VOLUME / 4);
        
        if(meltable != INVALID_MELTABLE) {
          meltable.metal.nugget = OreDictionary.getOres(oreName).get(0);
        }
      }
    }
  }
  
  public static ItemStack getBucket(final GradientMetals.MetalStack metal) {
    final ItemStack stack = getBucket(metal.metal);
    stack.grow(metal.amount - 1);
    
    return stack;
  }
  
  public static ItemStack getBucket(final GradientMetals.Metal metal) {
    return UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, metal.getFluid());
  }
  
  private static Meltable addMeltable(final String oreName, final String metal, final float meltModifier, final int amount) {
    final Metal m = getMetal(metal);
    
    if(m != INVALID_METAL) {
      return addMeltable(oreName, m, meltModifier, amount);
    }
    
    return INVALID_MELTABLE;
  }
  
  private static Meltable addMeltable(final String oreDict, final Metal metal, final float meltModifier, final int amount) {
    final Meltable meltable = new Meltable(metal, meltModifier, amount);
    meltables.put(OreDictionary.getOreID(oreDict), meltable);
    return meltable;
  }
  
  public static Metal addMetal(final String name, final int meltTime, final float meltTemp, final int durability, final int harvestLevel, final float harvestSpeed, final double attackDamage, final double attackSpeed) {
    final Metal metal = new Metal(name, meltTime, meltTemp, durability, harvestLevel, harvestSpeed, attackDamage, attackSpeed);
    metals.add(metal);
    return metal;
  }
  
  public static Alloy addAlloy(final MetalStack output, final Metal... input) {
    final Alloy alloy = new Alloy(output, input);
    alloys.add(alloy);
    return alloy;
  }
  
  public static Metal getMetal(final String name) {
    for(Metal metal : metals) {
      if(metal.name.equals(name)) {
        return metal;
      }
    }
    
    return INVALID_METAL;
  }
  
  public static Metal getMetal(final int index) {
    return metals.get(index);
  }
  
  private static MetalStack metalStack(final String metal, final int amount) {
    return new MetalStack(getMetal(metal), amount);
  }
  
  private static MetalStack metalStack(final String metal) {
    return metalStack(metal, Fluid.BUCKET_VOLUME);
  }
  
  public static Meltable getMeltable(final ItemStack stack) {
    for(int id : OreDictionary.getOreIDs(stack)) {
      Meltable meltable = meltables.get(id);
      
      if(meltable != null) {
        return meltable;
      }
    }
    
    return INVALID_MELTABLE;
  }
  
  public static boolean hasMetal(final String name) {
    return getMetal(name) != INVALID_METAL;
  }
  
  public static boolean hasMeltable(final ItemStack stack) {
    return getMeltable(stack) != INVALID_MELTABLE;
  }
  
  public static Metal getMetalForFluid(final Fluid fluid) {
    for(Metal metal : metals) {
      if(metal.fluid == fluid) {
        return metal;
      }
    }
    
    return INVALID_METAL;
  }
  
  public static class Metal {
    private static int currentId;
    
    public final int    id;
    public final String name;
    public final int    meltTime;
    public final float  meltTemp;
    
    public final int durability;
    
    public final int harvestLevel;
    public final float harvestSpeed;
    
    public final double attackDamageMultiplier;
    public final double attackSpeedMultiplier;
    
    private ItemStack nugget;
    Fluid fluid;
    
    public Metal(final String name, final int meltTime, final float meltTemp, final int durability, final int harvestLevel, final float harvestSpeed, final double attackDamageMultiplier, final double attackSpeedMultiplier) {
      this.id = currentId++;
      this.name = name;
      this.meltTime = meltTime;
      this.meltTemp = meltTemp;
      
      this.durability = durability;
      
      this.harvestLevel = harvestLevel;
      this.harvestSpeed = harvestSpeed;
      
      this.attackDamageMultiplier = attackDamageMultiplier;
      this.attackSpeedMultiplier  = attackSpeedMultiplier;
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
    
    public MetalStack(final Metal metal, final int amount) {
      this.metal  = metal;
      this.amount = amount;
    }
  }
  
  public static class Alloy {
    public final MetalStack output;
    public final List<Metal> inputs;
    
    public Alloy(final MetalStack output, final Metal... inputs) {
      this.output = output;
      this.inputs = ImmutableList.copyOf(inputs);
    }
  }
  
  public static class Meltable {
    public final Metal metal;
    public final float meltModifier;
    public final int   amount;
    
    public Meltable(final Metal metal, final float meltModifier, final int amount) {
      this.metal        = metal;
      this.meltModifier = meltModifier;
      this.amount       = amount;
    }
  }
}
