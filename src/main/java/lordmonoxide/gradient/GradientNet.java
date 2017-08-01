package lordmonoxide.gradient;

import lordmonoxide.gradient.blocks.claycast.PacketSwitchCast;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public final class GradientNet {
  private GradientNet() { }
  
  public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(GradientMod.MODID);
  
  private static int id;
  
  static void register() {
    CHANNEL.registerMessage(PacketSwitchCast.Handler.class, PacketSwitchCast.class, id++, Side.SERVER);
  }
}
