package lordmonoxide.gradient.progress;

import net.minecraft.nbt.INBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerProgressProvider implements ICapabilitySerializable<INBTBase> {
  private final Capability<PlayerProgress> cap = CapabilityPlayerProgress.PLAYER_PROGRESS_CAPABILITY;
  private final PlayerProgress instance = this.cap.getDefaultInstance();

  @Override
  @Nullable
  public INBTBase serializeNBT() {
    return this.cap.writeNBT(this.instance, null);
  }

  @Override
  public void deserializeNBT(final INBTBase nbt) {
    this.cap.readNBT(this.instance, null, nbt);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> capability, @Nullable final EnumFacing facing) {
    return capability == this.cap ? LazyOptional.of(() -> (T)this.instance) : LazyOptional.empty();
  }
}
