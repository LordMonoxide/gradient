package lordmonoxide.gradient.blocks.claycrucible;

import lordmonoxide.gradient.GradientFood;
import lordmonoxide.gradient.GradientFuel;
import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.blocks.heat.HeatSinker;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
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
    if(!this.world.isRemote) {
      this.meltMetal();
      this.checkForMoltenMetal();
    }
  }
  
  @Override
  protected void tickAfterCooldown() {
    this.updateLight();
  }
  
  private void meltMetal() {
    boolean update = false;
    
    for(int slot = 0; slot < METAL_SLOTS_COUNT; slot++) {
      if(!this.isMelting(slot) && !this.getMetalSlot(slot).isEmpty()) {
        GradientMetals.Metal metal = GradientMetals.instance.getMeltable(this.getMetalSlot(slot)).metal;
        
        if(this.canMelt(metal)) {
          this.melting[slot] = new MeltingMetal(metal);
          update = true;
        }
      }
    }
    
    if(update) {
      this.sync();
    }
  }
  
  private void checkForMoltenMetal() {
    boolean update = false;
    
    for(int slot = 0; slot < METAL_SLOTS_COUNT; slot++) {
      if(this.isMelting(slot)) {
        MeltingMetal melting = this.getMeltingMetal(slot);
        
        if(melting.isMelted()) {
          ItemStack stack = this.getMetalSlot(slot);
          
          this.melting[slot] = null;
          this.setMetalSlot(slot, ItemStack.EMPTY);
          
          MoltenMetal molten = this.molten.get(melting.metal);
          
          if(molten == null) {
            molten = new MoltenMetal(melting.metal, 0);
            this.molten.put(molten.metal, molten);
          }
          
          molten.addMetal(GradientMetals.instance.hasMeltable(stack) ? GradientMetals.instance.getMeltable(stack).amount : 1);
          
          update = true;
        }
      }
    }
    
    if(update) {
      this.sync();
    }
  }
  
  public Collection<MoltenMetal> getMoltenMetals() {
    return this.molten.values();
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
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setTag("inventory", this.inventory.serializeNBT());
    
    NBTTagList meltings = new NBTTagList();
    NBTTagList moltens  = new NBTTagList();
    
    for(int i = 0; i < METAL_SLOTS_COUNT; i++) {
      if(this.isMelting(i)) {
        MeltingMetal melting = this.getMeltingMetal(i);
        
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("slot", i);
        tag.setLong("start", melting.meltStart - Minecraft.getSystemTime());
        tag.setLong("until", melting.meltUntil - Minecraft.getSystemTime());
        meltings.appendTag(tag);
      }
    }
    
    for(MoltenMetal molten : this.molten.values()) {
      NBTTagCompound tag = new NBTTagCompound();
      tag.setString("metal", molten.metal.name);
      tag.setFloat("amount", molten.amount);
      moltens.appendTag(tag);
    }
    
    compound.setTag("melting", meltings);
    compound.setTag("molten", moltens);
    
    return super.writeToNBT(compound);
  }
  
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    this.lastLight = this.getLightLevel();
    
    Arrays.fill(this.melting, null);
    this.molten.clear();
    
    this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
    
    NBTTagList meltings = compound.getTagList("melting", Constants.NBT.TAG_COMPOUND);
    NBTTagList moltens = compound.getTagList("molten", Constants.NBT.TAG_COMPOUND);
    
    for(int i = 0; i < meltings.tagCount(); i++) {
      NBTTagCompound tag = meltings.getCompoundTagAt(i);
      
      int slot = tag.getInteger("slot");
      
      if(slot < METAL_SLOTS_COUNT) {
        this.melting[slot] = new MeltingMetal(GradientMetals.instance.getMeltable(this.getMetalSlot(slot)).metal);
        this.melting[slot].meltStart = tag.getLong("start") + Minecraft.getSystemTime();
        this.melting[slot].meltUntil = tag.getLong("until") + Minecraft.getSystemTime();
      }
    }
    
    for(int i = 0; i < moltens.tagCount(); i++) {
      NBTTagCompound tag = moltens.getCompoundTagAt(i);
      
      GradientMetals.Metal metal = GradientMetals.instance.getMetal(tag.getString("metal"));
      this.molten.put(metal, new MoltenMetal(metal, tag.getFloat("amount")));
    }
    
    super.readFromNBT(compound);
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
