package lordmonoxide.gradient.hacks;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// A very big thanks to alcatrazEscapee (No Tree Punching, https://github.com/alcatrazEscapee/no-tree-punching)
// This code is used with permission.

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class FixToolBreakingNotFiringHarvestDropEvents {
  private FixToolBreakingNotFiringHarvestDropEvents() { }

  @SubscribeEvent
  public static void breakEvent(final BlockEvent.BreakEvent event) {
    final EntityPlayer player = event.getPlayer();

    if(player != null) {
      player.getCapability(CapabilityPlayerItem.CAPABILITY, null).ifPresent(cap -> cap.setStack(player.getHeldItemMainhand()));
    }
  }

  @SubscribeEvent
  public static void attachEntityCapabilities(final AttachCapabilitiesEvent<Entity> event) {
    if(event.getObject() instanceof EntityPlayer) {
      event.addCapability(CapabilityPlayerItem.ID, new CapabilityPlayerItem.Instance());
    }
  }

  public static final class CapabilityPlayerItem {
    private CapabilityPlayerItem() { }

    public static final ResourceLocation ID = GradientMod.resource("player_item");

    @CapabilityInject(IPlayerItem.class)
    public static Capability<IPlayerItem> CAPABILITY;

    public static void register() {
      CapabilityManager.INSTANCE.register(IPlayerItem.class, new Storage(), Instance::new);
    }

    public static final class Instance implements IPlayerItem, ICapabilityProvider {
      private final LazyOptional<IPlayerItem> optional = LazyOptional.of(() -> this);
      private ItemStack stack;

      @Override
      public ItemStack getStack() {
        return this.stack;
      }

      @Override
      public void setStack(final ItemStack stack) {
        this.stack = stack.copy();
      }

      @Override
      public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> capability, @Nullable final EnumFacing facing) {
        if(capability == CapabilityPlayerItem.CAPABILITY) {
          return this.optional.cast();
        }

        return LazyOptional.empty();
      }
    }

    private static final class Storage implements Capability.IStorage<IPlayerItem> {
      @Nonnull
      @Override
      public INBTBase writeNBT(final Capability<IPlayerItem> capability, final IPlayerItem instance, final EnumFacing facing) {
        final NBTTagCompound nbt = new NBTTagCompound();
        return instance.getStack().write(nbt);
      }

      @Override
      public void readNBT(final Capability<IPlayerItem> capability, final IPlayerItem instance, final EnumFacing facing, final INBTBase nbt) {
        if(nbt instanceof NBTTagCompound) {
          instance.setStack(ItemStack.read((NBTTagCompound)nbt));
        }
      }
    }
  }

  public interface IPlayerItem {
    ItemStack getStack();

    void setStack(final ItemStack stack);
  }
}
