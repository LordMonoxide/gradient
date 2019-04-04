package lordmonoxide.gradient;

import lordmonoxide.gradient.network.PacketLightBronzeFurnace;
import lordmonoxide.gradient.network.PacketSwitchCast;
import lordmonoxide.gradient.network.PacketSyncEnergyNetwork;
import lordmonoxide.gradient.network.PacketUpdateBronzeBoilerSteamSink;
import lordmonoxide.gradient.network.PacketUpdateHeatNeighbours;
import lordmonoxide.gradient.network.PacketUpdatePlayerProgress;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public final class GradientNet {
  private GradientNet() { }

  private static final String PROTOCOL_VERSION = "1";
  public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
    .named(new ResourceLocation(GradientMod.MODID, "main_channel"))
    .clientAcceptedVersions(PROTOCOL_VERSION::equals)
    .serverAcceptedVersions(PROTOCOL_VERSION::equals)
    .networkProtocolVersion(() -> PROTOCOL_VERSION)
    .simpleChannel();

  private static int id;

  static void register() {
    CHANNEL.registerMessage(id++, PacketUpdatePlayerProgress.class, PacketUpdatePlayerProgress::encode, PacketUpdatePlayerProgress::decode, PacketUpdatePlayerProgress::handle);

    CHANNEL.registerMessage(id++, PacketSwitchCast.class, PacketSwitchCast::encode, PacketSwitchCast::decode, PacketSwitchCast::handle);
    CHANNEL.registerMessage(id++, PacketUpdateHeatNeighbours.class, PacketUpdateHeatNeighbours::encode, PacketUpdateHeatNeighbours::decode, PacketUpdateHeatNeighbours::handle);
    CHANNEL.registerMessage(id++, PacketLightBronzeFurnace.class, PacketLightBronzeFurnace::encode, PacketLightBronzeFurnace::decode, PacketLightBronzeFurnace::handle);
    CHANNEL.registerMessage(id++, PacketUpdateBronzeBoilerSteamSink.class, PacketUpdateBronzeBoilerSteamSink::encode, PacketUpdateBronzeBoilerSteamSink::decode, PacketUpdateBronzeBoilerSteamSink::handle);

    CHANNEL.registerMessage(id++, PacketSyncEnergyNetwork.class, PacketSyncEnergyNetwork::encode, PacketSyncEnergyNetwork::decode, PacketSyncEnergyNetwork::handle);
  }
}
