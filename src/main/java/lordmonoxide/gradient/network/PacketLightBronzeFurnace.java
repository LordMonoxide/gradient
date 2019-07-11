package lordmonoxide.gradient.network;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientNet;
import lordmonoxide.gradient.tileentities.TileBronzeFurnace;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketLightBronzeFurnace {
  public static void send(final BlockPos pos) {
    GradientNet.CHANNEL.sendToServer(new PacketLightBronzeFurnace(pos));
  }

  private final BlockPos pos;

  public PacketLightBronzeFurnace(final BlockPos pos) {
    this.pos = pos;
  }

  public static void encode(final PacketLightBronzeFurnace packet, final PacketBuffer buffer) {
    buffer.writeInt(packet.pos.getX());
    buffer.writeInt(packet.pos.getY());
    buffer.writeInt(packet.pos.getZ());
  }

  public static PacketLightBronzeFurnace decode(final PacketBuffer buffer) {
    try {
      return new PacketLightBronzeFurnace(new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt()));
    } catch(final IndexOutOfBoundsException e) {
      GradientMod.logger.error("Invalid position in PacketLightBronzeFurnace", e);
      return new PacketLightBronzeFurnace(BlockPos.ZERO);
    }
  }

  public static void handle(final PacketLightBronzeFurnace packet, final Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      final TileEntity te = ctx.get().getSender().world.getTileEntity(packet.pos);

      if(!(te instanceof TileBronzeFurnace)) {
        return;
      }

      ((TileBronzeFurnace)te).light();
    });
  }
}
