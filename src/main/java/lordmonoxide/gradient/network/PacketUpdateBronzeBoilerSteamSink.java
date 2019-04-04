package lordmonoxide.gradient.network;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientNet;
import lordmonoxide.gradient.tileentities.TileBronzeBoiler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class PacketUpdateBronzeBoilerSteamSink {
  public static void send(final BlockPos entityPos, final BlockPos updatePos) {
    GradientNet.CHANNEL.send(PacketDistributor.ALL.noArg(), new PacketUpdateBronzeBoilerSteamSink(entityPos, updatePos));
  }

  private final BlockPos entityPos;
  private final BlockPos updatePos;

  public PacketUpdateBronzeBoilerSteamSink(final BlockPos entityPos, final BlockPos updatePos) {
    this.entityPos = entityPos;
    this.updatePos = updatePos;
  }

  public static void encode(final PacketUpdateBronzeBoilerSteamSink packet, final PacketBuffer buffer) {
    buffer.writeInt(packet.entityPos.getX());
    buffer.writeInt(packet.entityPos.getY());
    buffer.writeInt(packet.entityPos.getZ());
    buffer.writeInt(packet.updatePos.getX());
    buffer.writeInt(packet.updatePos.getY());
    buffer.writeInt(packet.updatePos.getZ());
  }

  public static PacketUpdateBronzeBoilerSteamSink decode(final PacketBuffer buffer) {
    try {
      return new PacketUpdateBronzeBoilerSteamSink(
        new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()),
        new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt())
      );
    } catch(final IndexOutOfBoundsException e) {
      GradientMod.logger.info("Invalid position in PacketUpdateBronzeBoilerSteamSink", e);
      return new PacketUpdateBronzeBoilerSteamSink(BlockPos.ORIGIN, BlockPos.ORIGIN);
    }
  }

  public static void handle(final PacketUpdateBronzeBoilerSteamSink packet, final Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      final TileEntity te = Minecraft.getInstance().world.getTileEntity(packet.entityPos);

      if(!(te instanceof TileBronzeBoiler)) {
        return;
      }

      ((TileBronzeBoiler)te).updateOutput(packet.updatePos);
    });
  }
}
