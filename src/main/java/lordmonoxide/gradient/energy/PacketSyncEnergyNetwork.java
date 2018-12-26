package lordmonoxide.gradient.energy;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.longs.Long2FloatMap;
import lordmonoxide.gradient.GradientNet;
import lordmonoxide.gradient.utils.BlockPosUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class PacketSyncEnergyNetwork implements IMessage {
  public static void send(final EnergyNetworkState state) {
    GradientNet.CHANNEL.sendToAll(new PacketSyncEnergyNetwork(state));
  }

  private final EnergyNetworkState state;

  public PacketSyncEnergyNetwork() {
    this(new EnergyNetworkState());
  }

  public PacketSyncEnergyNetwork(final EnergyNetworkState state) {
    this.state = state;
  }

  @Override
  public void fromBytes(final ByteBuf buf) {
    final int size = buf.readInt();

    for(int i = 0; i < size; i++) {
      final long serialized = buf.readLong();
      final float energy = buf.readFloat();

      final BlockPos pos = BlockPosUtils.getBlockPosFromSerialized(serialized);
      final EnumFacing facing = BlockPosUtils.getFacingFromSerialized(serialized);

      System.out.println(pos + " " + facing + ": " + energy);
    }
  }

  @Override
  public void toBytes(final ByteBuf buf) {
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
      Minecraft.getMinecraft().addScheduledTask(() -> {
      });

      return null;
    }
  }
}
