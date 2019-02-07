package lordmonoxide.gradient.core;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(GradientCore.MODID)
public class GradientCore {
  public static final String MODID = "gradient-core";

  public static final Logger LOGGER = LogManager.getLogger();

  public static ResourceLocation resource(final String path) {
    return new ResourceLocation(MODID, path);
  }
}
