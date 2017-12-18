package lordmonoxide.gradient.blocks.bronzefurnace;

import ic2.core.ref.FluidName;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerFluidMap;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileBronzeFurnace extends TileEntity implements ITickable {
  private static final Fluid STEAM = FluidRegistry.getFluid(FluidName.steam.getName());
  
  public static final int INPUT_SLOT = 0;
  public static final int OUTPUT_SLOT = 1;
  public static final int COOKING_SLOT = 2;
  
  private final ItemStackHandler inventory = new ItemStackHandler(3);
  
  public final FluidTank tankSteam = new FluidTank(Fluid.BUCKET_VOLUME * 16);
  public final FluidHandlerFluidMap tanks = new FluidHandlerFluidMap() {
    public int fill(FluidStack resource, boolean doFill) {
      final int amount = super.fill(resource, doFill);
      
      if(amount != 0) {
        TileBronzeFurnace.this.sync();
      }
      
      return amount;
    }
  };
  
  private long nextSync = 0;
  
  private int cookTicks = 0;
  
  public TileBronzeFurnace() {
    this.tanks.addHandler(STEAM, this.tankSteam);
  }
  
  public boolean useBucket(final EntityPlayer player, final EnumHand hand, final World world, final BlockPos pos, final EnumFacing side) {
    if(FluidUtil.interactWithFluidHandler(player, hand, world, pos, side)) {
      this.sync();
      return true;
    }
    
    return false;
  }
  
  @Override
  public void update() {
    this.cook();
    
    if(!this.getWorld().isRemote) {
      if(System.currentTimeMillis() >= this.nextSync) {
        this.nextSync = System.currentTimeMillis() + 10000L;
        this.sync();
      }
    }
  }
  
  public boolean isCooking() {
    return !this.inventory.getStackInSlot(COOKING_SLOT).isEmpty();
  }
  
  private boolean isCooked() {
    return this.cookTicks >= 200;
  }
  
  public float getCookPercent() {
    return this.cookTicks / 200.0f;
  }
  
  private ItemStack getInputStack() {
    return this.inventory.getStackInSlot(INPUT_SLOT);
  }
  
  private void cook() {
    if(!this.isCooking() && !this.getInputStack().isEmpty()) {
      final ItemStack cooked = FurnaceRecipes.instance().getSmeltingResult(this.getInputStack()).copy();
      
      if(this.inventory.insertItem(OUTPUT_SLOT, cooked, true).isEmpty()) {
        this.inventory.extractItem(INPUT_SLOT, 1, false);
        this.inventory.setStackInSlot(COOKING_SLOT, cooked);
        this.cookTicks = 0;
      }
    }
    
    if(this.isCooking()) {
      if(this.tankSteam.drain(5, true).amount >= 5) {
        this.cookTicks++;
      }
      
      if(this.isCooked()) {
        final ItemStack cooked = this.inventory.extractItem(COOKING_SLOT, 1, false);
        this.inventory.insertItem(OUTPUT_SLOT, cooked, false);
      }
    }
  }
  
  @Override
  public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
    compound.setTag("inventory", this.inventory.serializeNBT());
    compound.setTag("steam", this.tankSteam.writeToNBT(new NBTTagCompound()));
    compound.setInteger("cookTicks", this.cookTicks);
    return super.writeToNBT(compound);
  }
  
  @Override
  public void readFromNBT(final NBTTagCompound compound) {
    this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
    this.tankSteam.readFromNBT(compound.getCompoundTag("steam"));
    this.cookTicks = compound.getInteger("cookTicks");
    super.readFromNBT(compound);
  }
  
  @Override
  public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
    return
      capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ||
      capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ||
      super.hasCapability(capability, facing);
  }
  
  @Nullable
  @Override
  public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return (T)this.tanks;
    }
    
    return
      capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)this.inventory :
      super.getCapability(capability, facing);
  }
  
  private void sync() {
    if(!this.getWorld().isRemote) {
      final IBlockState state = this.getWorld().getBlockState(this.getPos());
      this.getWorld().notifyBlockUpdate(this.getPos(), state, state, 3);
      this.markDirty();
    }
  }
  
  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
  }
  
  @Override
  public NBTTagCompound getUpdateTag() {
    return this.writeToNBT(new NBTTagCompound());
  }
  
  @Override
  public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity pkt) {
    this.readFromNBT(pkt.getNbtCompound());
  }
}