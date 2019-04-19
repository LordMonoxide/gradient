package lordmonoxide.gradient.tileentities;

import com.google.common.collect.ImmutableMap;
import lordmonoxide.gradient.GradientFluids;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientSounds;
import lordmonoxide.gradient.blocks.BlockBellows;
import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.animation.TimeValues;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class TileBellows extends TileEntity implements ITickable {
  @CapabilityInject(IAnimationStateMachine.class)
  private static Capability<IAnimationStateMachine> ANIMATION_CAPABILITY;

  private static final int CYCLE_TICKS = 80;

  @Nullable
  private final IAnimationStateMachine asm;
  private final TimeValues.VariableValue ticksValue = new TimeValues.VariableValue(0.0f);

  private final FluidStack air = new FluidStack(GradientFluids.AIR, 3);

  private boolean active;
  private int ticks;

  private BlockPos facingPos;

  public TileBellows() {
    this.asm = GradientMod.proxy.loadAsm(GradientMod.resource("asms/block/bellows.json"), ImmutableMap.of("blowing_cycle", this.ticksValue));
  }

  @Override
  public void onLoad() {
    final EnumFacing facing = this.world.getBlockState(this.pos).getValue(BlockBellows.FACING);
    this.facingPos = this.pos.offset(facing);
  }

  public void activate() {
    if(this.world.isRemote) {
      return;
    }

    this.active = true;
    this.sync();
  }

  @Override
  public void update() {
    if(this.active) {
      if(this.ticks < CYCLE_TICKS / 2) {
        final TileFirePit firepit = WorldUtils.getTileEntity(this.world, this.facingPos, TileFirePit.class);

        if(firepit != null) {
          firepit.tank.fill(this.air, true);
        }
      }

      if(this.world.isRemote && this.ticks == 0) {
        this.world.playSound(this.pos.getX() + 0.5f, this.pos.getY() + 0.5f, this.pos.getZ() + 0.5f, GradientSounds.BELLOWS_BLOW, SoundCategory.BLOCKS, 0.8f + this.world.rand.nextFloat(), this.world.rand.nextFloat() * 0.7f + 0.3f, false);
        this.asm.transition("blowing");
      }

      this.ticks++;

      if(this.world.isRemote) {
        this.ticksValue.setValue((float)this.ticks / CYCLE_TICKS);
      }

      if(this.ticks >= CYCLE_TICKS) {
        this.active = false;
        this.ticks = 0;

        if(this.world.isRemote) {
          this.asm.transition("idle");
        }
      }

      this.markDirty();
    }
  }

  @Override
  public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
    compound.setBoolean("active", this.active);
    compound.setInteger("ticks", this.ticks);

    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(final NBTTagCompound compound) {
    this.active = compound.getBoolean("active");
    this.ticks = compound.getInteger("ticks");

    super.readFromNBT(compound);
  }

  @Override
  public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
    return
      capability == ANIMATION_CAPABILITY ||
      super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    if(capability == ANIMATION_CAPABILITY) {
      return ANIMATION_CAPABILITY.cast(this.asm);
    }

    return super.getCapability(capability, facing);
  }

  protected void sync() {
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
