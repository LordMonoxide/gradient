package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.heat.HeatSinker;
import lordmonoxide.gradient.client.gui.GuiClayCrucible;
import lordmonoxide.gradient.containers.ContainerClayCrucible;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
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

import javax.annotation.Nullable;
import java.util.Arrays;

public class TileClayCrucible extends HeatSinker implements IInteractionObject {
  @CapabilityInject(IItemHandler.class)
  private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY;

  @CapabilityInject(IFluidHandler.class)
  private static Capability<IFluidHandler> FLUID_HANDLER_CAPABILITY;

  public static final int METAL_SLOTS_COUNT = 1;
  public static final int TOTAL_SLOTS_COUNT = METAL_SLOTS_COUNT;

  public static final int FIRST_METAL_SLOT = 0;

  private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS_COUNT);
  public final FluidTank tank = new FluidTank(Fluid.BUCKET_VOLUME * 8);

  private final MeltingMetal[] melting = new MeltingMetal[METAL_SLOTS_COUNT];

  private int lastLight;

  public TileClayCrucible() {
    super(GradientTileEntities.CLAY_CRUCIBLE);
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

  public void useBucket(final EntityPlayer player, final EnumHand hand, final World world, final BlockPos pos, final EnumFacing side) {
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
        final GradientMetals.Meltable meltable = GradientMetals.getMeltable(this.getMetalSlot(slot));

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
        final MeltingMetal melting = this.getMeltingMetal(slot);

        melting.tick();

        if(!this.world.isRemote) {
          if(melting.isMelted()) {
            final ItemStack stack = this.getMetalSlot(slot);

            this.melting[slot] = null;
            this.setMetalSlot(slot, ItemStack.EMPTY);

            final FluidStack fluid = new FluidStack(melting.meltable.metal.getFluid(), GradientMetals.getMeltable(stack).amount);

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

  private boolean canMelt(final GradientMetals.Meltable meltable) {
    return (this.tank.getFluid() == null || this.tank.getFluid().getFluid() == meltable.metal.getFluid()) && this.getHeat() >= meltable.meltTemp;
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
      this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
      this.world.checkLight(this.pos);

      this.lastLight = this.getLightLevel();
    }
  }

  @Override
  public NBTTagCompound write(final NBTTagCompound compound) {
    compound.put("inventory", this.inventory.serializeNBT());
    this.tank.writeToNBT(compound);

    final NBTTagList meltings = new NBTTagList();

    for(int i = 0; i < METAL_SLOTS_COUNT; i++) {
      if(this.isMelting(i)) {
        final MeltingMetal melting = this.getMeltingMetal(i);

        final NBTTagCompound tag = new NBTTagCompound();
        tag.putInt("slot", i);
        melting.writeToNbt(tag);
        meltings.add(tag);
      }
    }

    compound.put("melting", meltings);

    return super.write(compound);
  }

  @Override
  public void read(final NBTTagCompound compound) {
    this.lastLight = this.getLightLevel();

    Arrays.fill(this.melting, null);

    final NBTTagCompound inv = compound.getCompound("inventory");
    inv.remove("Size");
    this.inventory.deserializeNBT(inv);

    this.tank.readFromNBT(compound);

    final NBTTagList meltings = compound.getList("melting", Constants.NBT.TAG_COMPOUND);

    for(int i = 0; i < meltings.size(); i++) {
      final NBTTagCompound tag = meltings.getCompound(i);

      final int slot = tag.getInt("slot");

      if(slot < METAL_SLOTS_COUNT) {
        this.melting[slot] = MeltingMetal.fromNbt(GradientMetals.getMeltable(this.getMetalSlot(slot)), tag);
      }
    }

    super.read(compound);
  }

  @Override
  public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    if(capability == ITEM_HANDLER_CAPABILITY) {
      return LazyOptional.of(() -> (T)this.inventory);
    }

    if(capability == FLUID_HANDLER_CAPABILITY) {
      return LazyOptional.of(() -> (T)this.tank);
    }

    return super.getCapability(capability, facing);
  }

  @Override
  public ContainerClayCrucible createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
    return new ContainerClayCrucible(playerInventory, this);
  }

  @Override
  public String getGuiID() {
    return GuiClayCrucible.ID.toString();
  }

  @Override
  public ITextComponent getName() {
    return GradientBlocks.CLAY_CRUCIBLE_HARDENED.getNameTextComponent();
  }

  @Override
  public boolean hasCustomName() {
    return false;
  }

  @Nullable
  @Override
  public ITextComponent getCustomName() {
    return null;
  }

  public static final class MeltingMetal {
    public final GradientMetals.Meltable meltable;
    private final int meltTicksTotal;
    private int meltTicks;

    public static MeltingMetal fromNbt(final GradientMetals.Meltable metal, final NBTTagCompound tag) {
      final MeltingMetal melting = new MeltingMetal(metal, tag.getInt("ticksTotal"));
      melting.meltTicks = tag.getInt("ticks");
      return melting;
    }

    private MeltingMetal(final GradientMetals.Meltable meltable) {
      this(meltable, (int)(meltable.metal.meltTime * meltable.meltModifier * 20));
    }

    private MeltingMetal(final GradientMetals.Meltable meltable, final int meltTicksTotal) {
      this.meltable = meltable;
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
      tag.putInt("ticksTotal", this.meltTicksTotal);
      tag.putInt("ticks", this.meltTicks);
      return tag;
    }
  }
}
