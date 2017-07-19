package lordmonoxide.gradient.blocks.claycrucible;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.blocks.heat.HeatSinker;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class TileClayCrucible extends HeatSinker {
  public static final int METAL_SLOTS_COUNT = 10;
  public static final int TOTAL_SLOTS_COUNT = METAL_SLOTS_COUNT;
  
  public static final int FIRST_METAL_SLOT = 0;
  
  private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS_COUNT);
  
  private final MeltingMetal[] melting = new MeltingMetal[METAL_SLOTS_COUNT];
  
  private final Map<GradientMetals.Metal, MoltenMetal> molten = new HashMap<>();
  
  private int lastLight;
  
  public boolean isMelting(int slot) {
    return this.melting[slot] != null;
  }
  
  public MeltingMetal getMeltingMetal(int slot) {
    return this.melting[slot];
  }
  
  //TODO
  public int getLightLevel() {
    if(this.getHeat() == 0) {
      return 0;
    }
    
    return Math.min((int)(this.getHeat() / 800 * 11) + 4, 15);
  }
  
  @Override
  protected void tickBeforeCooldown() {
    this.meltMetal();
    this.checkForMoltenMetal();
  }
  
  @Override
  protected void tickAfterCooldown() {
    this.updateLight();
  }
  
  private void meltMetal() {
    for(int slot = 0; slot < METAL_SLOTS_COUNT; slot++) {
      if(!this.isMelting(slot) && !this.getMetalSlot(slot).isEmpty()) {
        GradientMetals.Metal metal = GradientMetals.instance.get(this.getMetalSlot(slot).getMetadata());
        
        if(this.canMelt(metal)) {
          this.melting[slot] = new MeltingMetal(metal);
        }
      }
    }
  }
  
  private void checkForMoltenMetal() {
    for(int slot = 0; slot < METAL_SLOTS_COUNT; slot++) {
      if(this.isMelting(slot)) {
        MeltingMetal melting = this.getMeltingMetal(slot);
        
        if(melting.isMelted()) {
          this.melting[slot] = null;
          this.setMetalSlot(slot, ItemStack.EMPTY);
          
          MoltenMetal molten = this.molten.get(melting.metal);
          
          if(molten == null) {
            //TODO
            molten = new MoltenMetal(melting.metal, 1);
            this.molten.put(molten.metal, molten);
          }
          
          
        }
      }
    }
  }
  
  private ItemStack getMetalSlot(int slot) {
    return this.inventory.getStackInSlot(FIRST_METAL_SLOT + slot);
  }
  
  private void setMetalSlot(int slot, ItemStack stack) {
    this.inventory.setStackInSlot(FIRST_METAL_SLOT + slot, stack);
  }
  
  private boolean canMelt(GradientMetals.Metal metal) {
    return this.getHeat() >= metal.meltTemp;
  }
  
  @Override
  protected float calculateHeatLoss() {
    return (float)Math.pow(this.getHeat() / 800, 2);
  }
  
  @Override
  protected float heatTransferEfficiency() {
    return 0.6f;
  }
  
  private void updateLight() {
    if(this.lastLight != this.getLightLevel()) {
      this.getWorld().markBlockRangeForRenderUpdate(this.pos, this.pos);
      this.getWorld().checkLight(this.pos);
      
      this.lastLight = this.getLightLevel();
    }
  }
  
  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
  }
  
  @Nullable
  @Override
  public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)this.inventory : super.getCapability(capability, facing);
  }
  
  public static final class MeltingMetal {
    public final GradientMetals.Metal metal;
    private long meltStart;
    private long meltUntil;
    
    private MeltingMetal(GradientMetals.Metal metal) {
      this.metal = metal;
      this.meltStart = Minecraft.getSystemTime();
      this.meltUntil = this.meltStart + metal.meltTime * 1000L;
    }
    
    public boolean isMelted() {
      return Minecraft.getSystemTime() >= this.meltUntil;
    }
    
    public float meltPercent() {
      return (float)(Minecraft.getSystemTime() - this.meltStart) / (this.meltUntil - this.meltStart);
    }
  }
  
  public static final class MoltenMetal {
    public final GradientMetals.Metal metal;
    private float amount;
    
    private MoltenMetal(GradientMetals.Metal metal, float amount) {
      this.metal  = metal;
      this.amount = amount;
    }
    
    public float amount() {
      return this.amount;
    }
    
    private void addMetal(float amount) {
      this.amount += amount;
    }
  }
}
