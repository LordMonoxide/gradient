package lordmonoxide.gradient.config;

import lordmonoxide.gradient.GradientMod;
import net.minecraftforge.common.config.Config;

@Config(modid = GradientMod.MODID, category = "")
public final class GradientConfig {
  private GradientConfig() { }

  public static final WorldGenConfig worldgen = new WorldGenConfig();
  public static final EnetConfig enet = new EnetConfig();

  public static final class WorldGenConfig {
    private WorldGenConfig() { }

    @Config.Comment("Disable vanilla ore generation (will default to true once new oregen code is ready)")
    public boolean disableVanillaOres;

    @Config.Comment("Enable ore generation")
    public boolean generateOres = true;
  }

  public static final class EnetConfig {
    private EnetConfig() { }

    @Config.Comment("Enable verbose debug logging for connecting/disconnecting nodes")
    public boolean enableNodeDebug;

    @Config.Comment("Enable verbose debug logging for pathfinding nodes")
    public boolean enablePathDebug;

    @Config.Comment("Enable verbose debug logging for ticking nodes")
    public boolean enableTickDebug;
  }
}
