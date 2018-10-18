package lordmonoxide.gradient.progress;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerProgressProvider implements ICapabilitySerializable<NBTBase> {
  private final Capability<PlayerProgress> cap = CapabilityPlayerProgress.PLAYER_PROGRESS_CAPABILITY;
  private final PlayerProgress instance = this.cap.getDefaultInstance();

  @Override
  @Nullable
  public NBTBase serializeNBT() {
    return this.cap.writeNBT(this.instance, null);
  }

  @Override
  public void deserializeNBT(final NBTBase nbt) {
    this.cap.readNBT(this.instance, null, nbt);
  }

  @Override
  public boolean hasCapability(@Nonnull final Capability<?> capability, @Nullable final EnumFacing facing) {
    return capability == this.cap;
  }

  @Nullable
  @Override
  public <T> T getCapability(@Nonnull final Capability<T> capability, @Nullable final EnumFacing facing) {
    return capability == this.cap ? this.cap.cast(this.instance) : null;
  }
}
