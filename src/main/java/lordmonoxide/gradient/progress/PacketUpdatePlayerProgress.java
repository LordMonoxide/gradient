package lordmonoxide.gradient.progress;

import io.netty.buffer.ByteBuf;
import lordmonoxide.gradient.GradientNet;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

public class PacketUpdatePlayerProgress implements IMessage {
  public static void send(final EntityPlayerMP player) {
    GradientNet.CHANNEL.sendTo(new PacketUpdatePlayerProgress(player), player);
  }

  private int playerId;
  private Age playerAge;

  public PacketUpdatePlayerProgress() { }

  public PacketUpdatePlayerProgress(final Entity player) {
    this.playerId = player.getEntityId();
    this.playerAge = player.getCapability(CapabilityPlayerProgress.PLAYER_PROGRESS_CAPABILITY, null).getAge();
  }

  @Override
  public void fromBytes(final ByteBuf buf) {
    this.playerId = buf.readInt();
    this.playerAge = Age.get(buf.readInt());
  }

  @Override
  public void toBytes(final ByteBuf buf) {
    buf.writeInt(this.playerId);
    buf.writeInt(this.playerAge.value());
  }

  public static class Handler implements IMessageHandler<PacketUpdatePlayerProgress, IMessage> {
    @Override
    @Nullable
    public IMessage onMessage(final PacketUpdatePlayerProgress packet, final MessageContext ctx) {
      Minecraft.getMinecraft().addScheduledTask(() -> {
        final Entity entity = Minecraft.getMinecraft().world.getEntityByID(packet.playerId);

        if(entity == null) {
          PlayerProgressEvents.deferredAgeUpdates.put(packet.playerId, packet.playerAge);
          return;
        }

        final PlayerProgress progress = entity.getCapability(CapabilityPlayerProgress.PLAYER_PROGRESS_CAPABILITY, null);

        if(progress != null) {
          progress.setAge(packet.playerAge);
        }
      });

      return null;
    }
  }
}
