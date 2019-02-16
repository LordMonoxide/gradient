package lordmonoxide.gradient.core.geology.ores;

import com.google.gson.JsonObject;
import lordmonoxide.gradient.core.GradientCore;
import lordmonoxide.gradient.core.utils.JsonUtil;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Ores {
  private Ores() { }

  private static final Map<ResourceLocation, Ore> ores = new HashMap<>();
  private static final Collection<Ore> oresRead = Collections.unmodifiableCollection(ores.values());

  public static void reload(final IResourceManager resourceManager) {
    ores.clear();

    GradientCore.LOGGER.info("Reloading ores");
    for(final ResourceLocation location : resourceManager.getAllResourceLocations("ores", s -> s.endsWith(".json"))) {
      try {
        loadOre(resourceManager.getResource(location));
      } catch(final IOException e) {
        GradientCore.LOGGER.error("Failed to load ore {}: {}", location, e);
      }
    }
  }

  private static void loadOre(final IResource resource) {
    final ResourceLocation oldLoc = resource.getLocation();
    final String path = oldLoc.getPath();
    final ResourceLocation loc = new ResourceLocation(oldLoc.getNamespace(), path.substring("ores/".length(), path.length() - ".json".length()));

    GradientCore.LOGGER.debug("Loading ore {}", loc);

    final JsonObject json = JsonUtil.getJsonFromResource(resource);
    add(new Ore(loc, json));
  }

  private static void add(final Ore ore) {
    GradientCore.LOGGER.info("Added ore {}", ore.name);
    ores.put(ore.name, ore);
  }

  public static Collection<Ore> all() {
    return oresRead;
  }

  public static Ore get(final ResourceLocation name) {
    return ores.get(name);
  }
}
