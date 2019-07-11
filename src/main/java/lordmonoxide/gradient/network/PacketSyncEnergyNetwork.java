package lordmonoxide.gradient.network;

import it.unimi.dsi.fastutil.longs.Long2FloatMap;
import lordmonoxide.gradient.GradientNet;
import lordmonoxide.gradient.energy.EnergyNetworkState;
import lordmonoxide.gradient.energy.IEnergyStorage;
import lordmonoxide.gradient.energy.IEnergyTransfer;
import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.nio.charset.StandardCharsets;
import java.util.IdentityHashMap;
import java.util.function.Supplier;

public class PacketSyncEnergyNetwork {
  public static void send(final DimensionType dimension, final EnergyNetworkState state) {
    GradientNet.CHANNEL.send(PacketDistributor.DIMENSION.with(() -> dimension), new PacketSyncEnergyNetwork(state));
  }

  private static final IdentityHashMap<String, Capability<?>> providers = ObfuscationReflectionHelper.getPrivateValue(CapabilityManager.class, CapabilityManager.INSTANCE, "providers");

  private final EnergyNetworkState state;

  public PacketSyncEnergyNetwork() {
    this(new EnergyNetworkState());
  }

  public PacketSyncEnergyNetwork(final EnergyNetworkState state) {
    this.state = state;
  }

  public static void encode(final PacketSyncEnergyNetwork packet, final PacketBuffer buffer) {
    buffer.writeInt(packet.state.getStorageCapability().getName().length());
    buffer.writeCharSequence(packet.state.getStorageCapability().getName(), StandardCharsets.UTF_8);

    buffer.writeInt(packet.state.getTransferCapability().getName().length());
    buffer.writeCharSequence(packet.state.getTransferCapability().getName(), StandardCharsets.UTF_8);

    buffer.writeInt(packet.state.size());

    for(final Long2FloatMap.Entry entry : packet.state.entries()) {
      buffer.writeLong(entry.getLongKey());
      buffer.writeFloat(entry.getFloatValue());
    }
  }

  public static PacketSyncEnergyNetwork decode(final PacketBuffer buffer) {
    final PacketSyncEnergyNetwork packet = new PacketSyncEnergyNetwork();

    final int storageNameLength = buffer.readInt();
    final String storageName = buffer.readCharSequence(storageNameLength, StandardCharsets.UTF_8).toString().intern();

    final int transferNameLength = buffer.readInt();
    final String transferName = buffer.readCharSequence(transferNameLength, StandardCharsets.UTF_8).toString().intern();

    packet.state.setCapabilities((Capability<? extends IEnergyStorage>)providers.get(storageName), (Capability<? extends IEnergyTransfer>)providers.get(transferName));

    final int size = buffer.readInt();

    for(int i = 0; i < size; i++) {
      final long serialized = buffer.readLong();
      final float energy = buffer.readFloat();

      packet.state.add(serialized, energy);
    }

    return packet;
  }

  public static void handle(final PacketSyncEnergyNetwork packet, final Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      for(final Long2FloatMap.Entry entry : packet.state.entries()) {
        final long serialized = entry.getLongKey();
        final float energy = entry.getFloatValue();

        final BlockPos pos = WorldUtils.getBlockPosFromSerialized(serialized);

        final World world = Minecraft.getInstance().world;

        if(world.isBlockLoaded(pos)) {
          final TileEntity te = world.getTileEntity(pos);

          if(te != null) {
            final Direction facing = WorldUtils.getFacingFromSerialized(serialized);

            te
              .getCapability(packet.state.getStorageCapability(), facing)
              .ifPresent(storage -> storage.setEnergy(energy));
          }
        }
      }
    });
  }
}
