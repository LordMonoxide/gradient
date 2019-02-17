package lordmonoxide.gradient.age1;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(GradientAge1.MODID)
public final class GradientAge1 {
  public static final String MODID = "gradient-age1";

  public static final Logger LOGGER = LogManager.getLogger();

  public GradientAge1() {
    MinecraftForge.EVENT_BUS.register(this);
  }

  public static ResourceLocation resource(final String path) {
    return new ResourceLocation(MODID, path);
  }
}
