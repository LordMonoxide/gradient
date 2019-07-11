package lordmonoxide.gradient.network;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientNet;
import lordmonoxide.gradient.blocks.heat.HeatSinker;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class PacketUpdateHeatNeighbours {
  public static void send(final BlockPos entityPos, final BlockPos updatePos) {
    GradientNet.CHANNEL.send(PacketDistributor.ALL.noArg(), new PacketUpdateHeatNeighbours(entityPos, updatePos));
  }

  private final BlockPos entityPos;
  private final BlockPos updatePos;

  public PacketUpdateHeatNeighbours(final BlockPos entityPos, final BlockPos updatePos) {
    this.entityPos = entityPos;
    this.updatePos = updatePos;
  }

  public static void encode(final PacketUpdateHeatNeighbours packet, final PacketBuffer buffer) {
    buffer.writeInt(packet.entityPos.getX());
    buffer.writeInt(packet.entityPos.getY());
    buffer.writeInt(packet.entityPos.getZ());
    buffer.writeInt(packet.updatePos.getX());
    buffer.writeInt(packet.updatePos.getY());
    buffer.writeInt(packet.updatePos.getZ());
  }

  public static PacketUpdateHeatNeighbours decode(final PacketBuffer buffer) {
    try {
      return new PacketUpdateHeatNeighbours(
        new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()),
        new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt())
      );
    } catch(final IndexOutOfBoundsException e) {
      GradientMod.logger.info("Invalid position in PacketUpdateHeatNeighbours", e);
      return new PacketUpdateHeatNeighbours(BlockPos.ZERO, BlockPos.ZERO);
    }
  }

  public static void handle(final PacketUpdateHeatNeighbours packet, final Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      final TileEntity te = Minecraft.getInstance().world.getTileEntity(packet.entityPos);

      if(!(te instanceof HeatSinker)) {
        return;
      }

      ((HeatSinker)te).updateSink(packet.updatePos);
    });
  }
}
