package lordmonoxide.gradient;

import lordmonoxide.gradient.network.PacketUpdateBronzeBoilerSteamSink;
import lordmonoxide.gradient.network.PacketLightBronzeFurnace;
import lordmonoxide.gradient.network.PacketSwitchCast;
import lordmonoxide.gradient.network.PacketUpdateClayMetalMixerNeighbours;
import lordmonoxide.gradient.network.PacketUpdateHeatNeighbours;
import lordmonoxide.gradient.energy.PacketSyncEnergyNetwork;
import lordmonoxide.gradient.progress.PacketUpdatePlayerProgress;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public final class GradientNet {
  private GradientNet() { }

  public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(GradientMod.MODID);

  private static int id;

  static void register() {
    CHANNEL.registerMessage(PacketUpdatePlayerProgress.Handler.class, PacketUpdatePlayerProgress.class, id++, Side.CLIENT);

    CHANNEL.registerMessage(PacketSwitchCast.Handler.class, PacketSwitchCast.class, id++, Side.SERVER);
    CHANNEL.registerMessage(PacketUpdateHeatNeighbours.Handler.class, PacketUpdateHeatNeighbours.class, id++, Side.CLIENT);
    CHANNEL.registerMessage(PacketUpdateClayMetalMixerNeighbours.Handler.class, PacketUpdateClayMetalMixerNeighbours.class, id++, Side.CLIENT);
    CHANNEL.registerMessage(PacketLightBronzeFurnace.Handler.class, PacketLightBronzeFurnace.class, id++, Side.SERVER);
    CHANNEL.registerMessage(PacketUpdateBronzeBoilerSteamSink.Handler.class, PacketUpdateBronzeBoilerSteamSink.class, id++, Side.CLIENT);

    CHANNEL.registerMessage(PacketSyncEnergyNetwork.Handler.class, PacketSyncEnergyNetwork.class, id++, Side.CLIENT);
  }
}
