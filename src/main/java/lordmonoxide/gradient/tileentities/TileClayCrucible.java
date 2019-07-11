package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.blocks.heat.HeatSinker;
import lordmonoxide.gradient.science.geology.Meltable;
import lordmonoxide.gradient.science.geology.Meltables;
import lordmonoxide.gradient.science.geology.Metal;
import lordmonoxide.gradient.science.geology.Metals;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

public class TileClayCrucible extends HeatSinker {
  @CapabilityInject(IItemHandler.class)
  private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY;

  @CapabilityInject(IFluidHandler.class)
  private static Capability<IFluidHandler> FLUID_HANDLER_CAPABILITY;

  public static final ModelProperty<FluidStack> FLUID = new ModelProperty<>();

  public static final int FLUID_CAPACITY = 8;

  public static final int METAL_SLOTS_COUNT = 1;
  public static final int TOTAL_SLOTS_COUNT = METAL_SLOTS_COUNT;

  public static final int FIRST_METAL_SLOT = 0;

  private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS_COUNT);
  public final FluidTank tank = new FluidTank(Fluid.BUCKET_VOLUME * FLUID_CAPACITY) {
    @Override
    protected void onContentsChanged() {
      super.onContentsChanged();
      ModelDataManager.requestModelDataRefresh(TileClayCrucible.this);
    }
  };

  private final MeltingMetal[] melting = new MeltingMetal[METAL_SLOTS_COUNT];

  private int lastLight;

  public TileClayCrucible() {
    super(GradientTileEntities.CLAY_CRUCIBLE_HARDENED);
  }

  @Nonnull
  @Override
  public IModelData getModelData() {
    return new ModelDataMap.Builder().withInitial(FLUID, this.tank.getFluid()).build();
  }

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

  public void useBucket(final PlayerEntity player, final Hand hand, final World world, final BlockPos pos, final Direction side) {
    if(FluidUtil.interactWithFluidHandler(player, hand, world, pos, side)) {
      this.sync();
    }
  }

  public void consumeMetal(final int amount) {
    final FluidStack result = this.tank.drain(amount, true);

    if(result != null && result.amount != 0) {
      this.sync();
    }
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
    boolean update = false;

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

            update = true;
          }
        }
      }
    }

    if(update) {
      this.sync();
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
  protected float calculateHeatLoss(final BlockState state) {
    return (float)Math.max(0.5d, Math.pow(this.getHeat() / 800, 2));
  }

  @Override
  protected float heatTransferEfficiency() {
    return 0.6f;
  }

  private void updateLight() {
    if(this.lastLight != this.getLightLevel()) {
      this.world.markForRerender(this.pos);
      this.world.checkLight(this.pos);

      this.lastLight = this.getLightLevel();
    }
  }

  @Override
  public CompoundNBT write(final CompoundNBT compound) {
    compound.put("inventory", this.inventory.serializeNBT());
    this.tank.writeToNBT(compound);

    final ListNBT meltings = new ListNBT();

    for(int i = 0; i < METAL_SLOTS_COUNT; i++) {
      if(this.isMelting(i)) {
        final MeltingMetal melting = this.getMeltingMetal(i);

        final CompoundNBT tag = new CompoundNBT();
        tag.putInt("slot", i);
        melting.writeToNbt(tag);
        meltings.add(tag);
      }
    }

    compound.put("melting", meltings);

    return super.write(compound);
  }

  @Override
  public void read(final CompoundNBT compound) {
    this.lastLight = this.getLightLevel();

    Arrays.fill(this.melting, null);

    final CompoundNBT inv = compound.getCompound("inventory");
    inv.remove("Size");
    this.inventory.deserializeNBT(inv);

    this.tank.readFromNBT(compound);

    final ListNBT meltings = compound.getList("melting", Constants.NBT.TAG_COMPOUND);

    for(int i = 0; i < meltings.size(); i++) {
      final CompoundNBT tag = meltings.getCompound(i);

      final int slot = tag.getInt("slot");

      if(slot < METAL_SLOTS_COUNT) {
        final Meltable meltable = Meltables.get(this.getMetalSlot(slot));
        this.melting[slot] = MeltingMetal.fromNbt(meltable, Metals.get(meltable), tag);
      }
    }

    super.read(compound);
  }

  @Override
  public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket pkt) {
    final BlockState oldState = this.world.getBlockState(this.pos);
    super.onDataPacket(net, pkt);
    this.world.notifyBlockUpdate(this.pos, oldState, this.world.getBlockState(this.pos), 2);
  }

  @Override
  public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing) {
    if(capability == ITEM_HANDLER_CAPABILITY) {
      return LazyOptional.of(() -> (T)this.inventory);
    }

    if(capability == FLUID_HANDLER_CAPABILITY) {
      return LazyOptional.of(() -> (T)this.tank);
    }

    return super.getCapability(capability, facing);
  }

  public static final class MeltingMetal {
    public final Meltable meltable;
    public final Metal metal;
    private final int meltTicksTotal;
    private int meltTicks;

    public static MeltingMetal fromNbt(final Meltable meltable, final Metal metal, final CompoundNBT tag) {
      final MeltingMetal melting = new MeltingMetal(meltable, metal, tag.getInt("ticksTotal"));
      melting.meltTicks = tag.getInt("ticks");
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

    public CompoundNBT writeToNbt(final CompoundNBT tag) {
      tag.putInt("ticksTotal", this.meltTicksTotal);
      tag.putInt("ticks", this.meltTicks);
      return tag;
    }
  }
}
