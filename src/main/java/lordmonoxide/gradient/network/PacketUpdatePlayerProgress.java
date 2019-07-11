package lordmonoxide.gradient.network;

import lordmonoxide.gradient.GradientNet;
import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.progress.CapabilityPlayerProgress;
import lordmonoxide.gradient.progress.PlayerProgressEvents;
import lordmonoxide.gradient.utils.AgeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class PacketUpdatePlayerProgress {
  public static void send(final ServerPlayerEntity player) {
    GradientNet.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new PacketUpdatePlayerProgress(player));
  }

  private final int playerId;
  private final Age playerAge;

  public PacketUpdatePlayerProgress(final PlayerEntity player) {
    this(player.getEntityId(), AgeUtils.getPlayerAge(player));
  }

  public PacketUpdatePlayerProgress(final int playerId, final Age playerAge) {
    this.playerId = playerId;
    this.playerAge = playerAge;
  }

  public static void encode(final PacketUpdatePlayerProgress packet, final PacketBuffer buffer) {
    buffer.writeVarInt(packet.playerId);
    buffer.writeVarInt(packet.playerAge.value());
  }

  public static PacketUpdatePlayerProgress decode(final PacketBuffer buffer) {
    return new PacketUpdatePlayerProgress(buffer.readVarInt(), Age.get(buffer.readVarInt()));
  }

  public static void handle(final PacketUpdatePlayerProgress packet, final Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      final Entity entity = Minecraft.getInstance().world.getEntityByID(packet.playerId);

      if(entity == null) {
        PlayerProgressEvents.deferAgeUpdate(packet.playerId, packet.playerAge);
        return;
      }

      entity
        .getCapability(CapabilityPlayerProgress.PLAYER_PROGRESS_CAPABILITY, null)
        .ifPresent(progress -> progress.setAge(packet.playerAge));
    });
  }
}
