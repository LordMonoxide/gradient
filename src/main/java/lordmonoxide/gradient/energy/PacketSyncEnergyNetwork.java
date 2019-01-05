package lordmonoxide.gradient.energy;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.longs.Long2FloatMap;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientNet;
import lordmonoxide.gradient.utils.BlockPosUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.IdentityHashMap;

public class PacketSyncEnergyNetwork implements IMessage {
  public static void send(final int dimension, final EnergyNetworkState state) {
    GradientNet.CHANNEL.sendToDimension(new PacketSyncEnergyNetwork(state), dimension);
  }

  private static final IdentityHashMap<String, Capability<?>> providers = ReflectionHelper.getPrivateValue(CapabilityManager.class, CapabilityManager.INSTANCE, "providers");

  private final EnergyNetworkState state;

  public PacketSyncEnergyNetwork() {
    this(new EnergyNetworkState());
  }

  public PacketSyncEnergyNetwork(final EnergyNetworkState state) {
    this.state = state;
  }

  @Override
  public void fromBytes(final ByteBuf buf) {
    final int storageNameLength = buf.readInt();
    final String storageName = buf.readCharSequence(storageNameLength, StandardCharsets.UTF_8).toString().intern();

    final int transferNameLength = buf.readInt();
    final String transferName = buf.readCharSequence(transferNameLength, StandardCharsets.UTF_8).toString().intern();

    this.state.setCapabilities((Capability<? extends IEnergyStorage>)providers.get(storageName), (Capability<? extends IEnergyTransfer>)providers.get(transferName));

    final int size = buf.readInt();

    for(int i = 0; i < size; i++) {
      final long serialized = buf.readLong();
      final float energy = buf.readFloat();

      this.state.add(serialized, energy);
    }
  }

  @Override
  public void toBytes(final ByteBuf buf) {
    buf.writeInt(this.state.getStorageCapability().getName().length());
    buf.writeCharSequence(this.state.getStorageCapability().getName(), StandardCharsets.UTF_8);

    buf.writeInt(this.state.getTransferCapability().getName().length());
    buf.writeCharSequence(this.state.getTransferCapability().getName(), StandardCharsets.UTF_8);

    buf.writeInt(this.state.size());

    for(final Long2FloatMap.Entry entry : this.state.entries()) {
      buf.writeLong(entry.getLongKey());
      buf.writeFloat(entry.getFloatValue());
    }
  }

  public static class Handler implements IMessageHandler<PacketSyncEnergyNetwork, IMessage> {
    @Nullable
    @Override
    public IMessage onMessage(final PacketSyncEnergyNetwork message, final MessageContext ctx) {
      GradientMod.proxy.scheduleTask(ctx, () -> {
        for(final Long2FloatMap.Entry entry : message.state.entries()) {
          final long serialized = entry.getLongKey();
          final float energy = entry.getFloatValue();

          final BlockPos pos = BlockPosUtils.getBlockPosFromSerialized(serialized);

          final World world = GradientMod.proxy.getWorld();

          if(world.isBlockLoaded(pos)) {
            final TileEntity te = world.getTileEntity(pos);

            if(te != null) {
              final EnumFacing facing = BlockPosUtils.getFacingFromSerialized(serialized);

              if(te.hasCapability(message.state.getStorageCapability(), facing)) {
                final IEnergyStorage storage = te.getCapability(message.state.getStorageCapability(), facing);
                storage.setEnergy(energy);
              }
            }
          }
        }
      });

      return null;
    }
  }
}
