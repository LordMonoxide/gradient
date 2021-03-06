package lordmonoxide.gradient.progress;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public final class CapabilityPlayerProgress {
  private CapabilityPlayerProgress() { }

  public static final ResourceLocation ID = GradientMod.resource("progress");

  @CapabilityInject(PlayerProgress.class)
  public static Capability<PlayerProgress> PLAYER_PROGRESS_CAPABILITY;

  public static void register() {
    CapabilityManager.INSTANCE.register(PlayerProgress.class, new Capability.IStorage<PlayerProgress>() {
      @Override
      public NBTBase writeNBT(final Capability<PlayerProgress> capability, final PlayerProgress instance, final EnumFacing side) {
        final NBTTagCompound tags = new NBTTagCompound();

        tags.setString("age", instance.getAge().name());

        return tags;
      }

      @Override
      public void readNBT(final Capability<PlayerProgress> capability, final PlayerProgress instance, final EnumFacing side, final NBTBase base) {
        if(!(base instanceof NBTTagCompound)) {
          return;
        }

        final NBTTagCompound nbt = (NBTTagCompound)base;

        if(nbt.hasKey("age")) {
          instance.setAge(Age.valueOf(nbt.getString("age")));
        } else {
          instance.setAge(Age.AGE1);
        }
      }
    }, PlayerProgress::new);
  }
}
