package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.GradientFluids;
import lordmonoxide.gradient.blocks.heat.HeatSinker;
import lordmonoxide.gradient.science.geology.Meltable;
import lordmonoxide.gradient.science.geology.Meltables;
import lordmonoxide.gradient.science.geology.Metal;
import lordmonoxide.gradient.science.geology.Metals;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Arrays;

public class TileClayCrucible extends HeatSinker {
  @CapabilityInject(IItemHandler.class)
  private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY;

  @CapabilityInject(IFluidHandler.class)
  private static Capability<IFluidHandler> FLUID_HANDLER_CAPABILITY;

  public static final int FLUID_CAPACITY = 8;

  public static final int METAL_SLOTS_COUNT = 1;
  public static final int TOTAL_SLOTS_COUNT = METAL_SLOTS_COUNT;

  public static final int FIRST_METAL_SLOT = 0;

  private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS_COUNT) {
    @Override
    protected void onContentsChanged(final int slot) {
      super.onContentsChanged(slot);
      TileClayCrucible.this.sync();
    }
  };

  public final FluidTank tank = new FluidTank(Fluid.BUCKET_VOLUME * FLUID_CAPACITY) {
    @Override
    public boolean canFillFluidType(final FluidStack fluid) {
      return super.canFillFluidType(fluid) && GradientFluids.METALS.containsValue(fluid.getFluid());
    }

    @Override
    public boolean canDrainFluidType(@Nullable final FluidStack fluid) {
      return super.canDrainFluidType(fluid) && GradientFluids.METALS.containsValue(fluid.getFluid());
    }

    @Override
    protected void onContentsChanged() {
      super.onContentsChanged();
      TileClayCrucible.this.sync();
    }
  };

  private final MeltingMetal[] melting = new MeltingMetal[METAL_SLOTS_COUNT];

  private int lastLight;

  public boolean isMelting(final int slot) {
    return this.melting[slot] != null;
  }

  public MeltingMetal getMeltingMetal(final int slot) {
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

  public void consumeMetal(final int amount) {
    this.tank.drain(amount, true);
  }

  @Override
  protected void tickBeforeCooldown() {
    if(!this.world.isRemote) {
      this.meltMetal();
    }

    this.checkForMoltenMetal();
  }

  @Override
  protected void tickAfterCooldown() {
    this.updateLight();
  }

  private void meltMetal() {
    boolean update = false;

    for(int slot = 0; slot < METAL_SLOTS_COUNT; slot++) {
      if(!this.isMelting(slot) && !this.getMetalSlot(slot).isEmpty()) {
        final Meltable meltable = Meltables.get(this.getMetalSlot(slot));

        if(this.canMelt(meltable)) {
          this.melting[slot] = new MeltingMetal(meltable, Metals.get(meltable));
          update = true;
        }
      }
    }

    if(update) {
      this.sync();
    }
  }

  private void checkForMoltenMetal() {
    for(int slot = 0; slot < METAL_SLOTS_COUNT; slot++) {
      if(this.isMelting(slot)) {
        final MeltingMetal melting = this.getMeltingMetal(slot);

        melting.tick();

        if(!this.world.isRemote) {
          if(melting.isMelted()) {
            this.melting[slot] = null;
            this.setMetalSlot(slot, ItemStack.EMPTY);

            final FluidStack fluid = new FluidStack(melting.meltable.getFluid(), melting.meltable.amount);
            this.tank.fill(fluid, true);
          }
        }
      }
    }
  }

  private ItemStack getMetalSlot(final int slot) {
    return this.inventory.getStackInSlot(FIRST_METAL_SLOT + slot);
  }

  private void setMetalSlot(final int slot, final ItemStack stack) {
    this.inventory.setStackInSlot(FIRST_METAL_SLOT + slot, stack);
  }

  private boolean canMelt(final Meltable meltable) {
    return (this.tank.getFluid() == null || this.tank.getFluid().getFluid() == meltable.getFluid()) && this.getHeat() >= meltable.meltTemp;
  }

  @Override
  protected float calculateHeatLoss(final IBlockState state) {
    return (float)Math.max(0.5d, Math.pow(this.getHeat() / 800, 2));
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
  public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
    compound.setTag("inventory", this.inventory.serializeNBT());
    this.tank.writeToNBT(compound);

    final NBTTagList meltings = new NBTTagList();

    for(int i = 0; i < METAL_SLOTS_COUNT; i++) {
      if(this.isMelting(i)) {
        final MeltingMetal melting = this.getMeltingMetal(i);

        final NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("slot", i);
        melting.writeToNbt(tag);
        meltings.appendTag(tag);
      }
    }

    compound.setTag("melting", meltings);

    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(final NBTTagCompound compound) {
    this.lastLight = this.getLightLevel();

    Arrays.fill(this.melting, null);

    final NBTTagCompound inv = compound.getCompoundTag("inventory");
    inv.removeTag("Size");
    this.inventory.deserializeNBT(inv);

    this.tank.readFromNBT(compound);

    final NBTTagList meltings = compound.getTagList("melting", Constants.NBT.TAG_COMPOUND);

    for(int i = 0; i < meltings.tagCount(); i++) {
      final NBTTagCompound tag = meltings.getCompoundTagAt(i);

      final int slot = tag.getInteger("slot");

      if(slot < METAL_SLOTS_COUNT) {
        final Meltable meltable = Meltables.get(this.getMetalSlot(slot));
        this.melting[slot] = MeltingMetal.fromNbt(meltable, Metals.get(meltable), tag);
      }
    }

    super.readFromNBT(compound);
  }

  @Override
  public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity pkt) {
    final IBlockState oldState = this.world.getBlockState(this.pos);
    super.onDataPacket(net, pkt);
    this.world.notifyBlockUpdate(this.pos, oldState, this.world.getBlockState(this.pos), 2);
  }

  @Override
  public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
    return
      capability == ITEM_HANDLER_CAPABILITY ||
      capability == FLUID_HANDLER_CAPABILITY ||
      super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    if(capability == ITEM_HANDLER_CAPABILITY) {
      return ITEM_HANDLER_CAPABILITY.cast(this.inventory);
    }

    if(capability == FLUID_HANDLER_CAPABILITY) {
      return FLUID_HANDLER_CAPABILITY.cast(this.tank);
    }

    return super.getCapability(capability, facing);
  }

  public static final class MeltingMetal {
    public final Meltable meltable;
    public final Metal metal;
    private final int meltTicksTotal;
    private int meltTicks;

    public static MeltingMetal fromNbt(final Meltable meltable, final Metal metal, final NBTTagCompound tag) {
      final MeltingMetal melting = new MeltingMetal(meltable, metal, tag.getInteger("ticksTotal"));
      melting.meltTicks = tag.getInteger("ticks");
      return melting;
    }

    private MeltingMetal(final Meltable meltable, final Metal metal) {
      this(meltable, metal, (int)(meltable.meltTime * 20));
    }

    private MeltingMetal(final Meltable meltable, final Metal metal, final int meltTicksTotal) {
      this.meltable = meltable;
      this.metal = metal;
      this.meltTicksTotal = meltTicksTotal;
    }

    public MeltingMetal tick() {
      this.meltTicks++;
      return this;
    }

    public boolean isMelted() {
      return this.meltTicks >= this.meltTicksTotal;
    }

    public float meltPercent() {
      return (float)this.meltTicks / this.meltTicksTotal;
    }

    public NBTTagCompound writeToNbt(final NBTTagCompound tag) {
      tag.setInteger("ticksTotal", this.meltTicksTotal);
      tag.setInteger("ticks", this.meltTicks);
      return tag;
    }
  }
}
