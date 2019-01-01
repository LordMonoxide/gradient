package lordmonoxide.gradient.config;

import lordmonoxide.gradient.GradientMod;
import net.minecraftforge.common.config.Config;

@Config(modid = GradientMod.MODID)
public final class EnergyNetworkConfig {
  @Config.Comment("Enable verbose debug logging for connecting/disconnecting nodes")
  public static boolean enableNodeDebug;

  @Config.Comment("Enable verbose debug logging for pathfinding nodes")
  public static boolean enablePathDebug;

  @Config.Comment("Enable verbose debug logging for ticking nodes")
  public static boolean enableTickDebug;

  private EnergyNetworkConfig() { }
}
