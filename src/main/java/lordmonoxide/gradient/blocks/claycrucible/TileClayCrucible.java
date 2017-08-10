package lordmonoxide.gradient.blocks.claycrucible;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.blocks.heat.HeatSinker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Arrays;

public class TileClayCrucible extends HeatSinker {
  public static final int METAL_SLOTS_COUNT = 1;
  public static final int TOTAL_SLOTS_COUNT = METAL_SLOTS_COUNT;
  
  public static final int FIRST_METAL_SLOT = 0;
  
  private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS_COUNT);
  private final FluidTank tank = new FluidTank(Fluid.BUCKET_VOLUME * 8);
  
  private final MeltingMetal[] melting = new MeltingMetal[METAL_SLOTS_COUNT];
  
  private int lastLight;
  
  public boolean isMelting(int slot) {
    return this.melting[slot] != null;
  }
  
  public MeltingMetal getMeltingMetal(int slot) {
    return this.melting[slot];
  }
  
  @Nullable
  public FluidStack getMoltenMetal() {
    return this.tank.getFluid();
  }
  
  //TODO
  public int getLightLevel() {
    if(this.getHeat() == 0) {
      return 0;
    }
    
    return Math.min((int)(this.getHeat() / 800 * 11) + 4, 15);
  }
  
  public FluidActionResult useBucket(ItemStack bucket, EntityPlayer player) {
    FluidActionResult result = FluidUtil.interactWithFluidHandler(bucket, this.tank, player);
    
    if(result.success) {
      this.sync();
    }
    
    return result;
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
        GradientMetals.Meltable meltable = GradientMetals.instance.getMeltable(this.getMetalSlot(slot));
        
        if(this.canMelt(meltable)) {
          this.melting[slot] = new MeltingMetal(meltable);
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
          
          FluidStack fluid = new FluidStack(melting.meltable.metal.getFluid(), GradientMetals.instance.getMeltable(stack).amount);
          
          this.tank.fill(fluid, true);
          
          update = true;
        }
      }
    }
    
    if(update) {
      this.sync();
    }
  }
  
  private ItemStack getMetalSlot(int slot) {
    return this.inventory.getStackInSlot(FIRST_METAL_SLOT + slot);
  }
  
  private void setMetalSlot(int slot, ItemStack stack) {
    this.inventory.setStackInSlot(FIRST_METAL_SLOT + slot, stack);
  }
  
  private boolean canMelt(GradientMetals.Meltable meltable) {
    return (this.tank.getFluid() == null || this.tank.getFluid().getFluid() == meltable.metal.getFluid()) && this.getHeat() >= meltable.metal.meltTemp;
  }
  
  @Override
  protected float calculateHeatLoss(IBlockState state) {
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
    this.tank.writeToNBT(compound);
    
    NBTTagList meltings = new NBTTagList();
    
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
    
    compound.setTag("melting", meltings);
    
    return super.writeToNBT(compound);
  }
  
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    this.lastLight = this.getLightLevel();
    
    Arrays.fill(this.melting, null);
    this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
    this.tank.readFromNBT(compound);
    
    NBTTagList meltings = compound.getTagList("melting", Constants.NBT.TAG_COMPOUND);
    
    for(int i = 0; i < meltings.tagCount(); i++) {
      NBTTagCompound tag = meltings.getCompoundTagAt(i);
      
      int slot = tag.getInteger("slot");
      
      if(slot < METAL_SLOTS_COUNT) {
        this.melting[slot] = new MeltingMetal(GradientMetals.instance.getMeltable(this.getMetalSlot(slot)));
        this.melting[slot].meltStart = tag.getLong("start") + Minecraft.getSystemTime();
        this.melting[slot].meltUntil = tag.getLong("until") + Minecraft.getSystemTime();
      }
    }
    
    super.readFromNBT(compound);
  }
  
  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    return
      capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
      capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ||
      super.hasCapability(capability, facing);
  }
  
  @Nullable
  @Override
  public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
    return
      capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)this.inventory :
      capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? (T)this.tank :
      super.getCapability(capability, facing);
  }
  
  public static final class MeltingMetal {
    public final GradientMetals.Meltable meltable;
    private long meltStart;
    private long meltUntil;
    
    private MeltingMetal(GradientMetals.Meltable meltable) {
      this.meltable  = meltable;
      this.meltStart = Minecraft.getSystemTime();
      this.meltUntil = (long)(this.meltStart + meltable.metal.meltTime * meltable.meltModifier * 1000L);
    }
    
    public boolean isMelted() {
      return Minecraft.getSystemTime() >= this.meltUntil;
    }
    
    public float meltPercent() {
      return (float)(Minecraft.getSystemTime() - this.meltStart) / (this.meltUntil - this.meltStart);
    }
  }
}
