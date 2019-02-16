package lordmonoxide.gradient.core.geology.ores;

import com.google.gson.JsonObject;
import lordmonoxide.gradient.core.geology.elements.Element;
import lordmonoxide.gradient.core.utils.JsonUtil;
import net.minecraft.util.ResourceLocation;

public class Ore {
  public final ResourceLocation name;
  public final Element basicElement;

  public Ore(final ResourceLocation name, final Element basicElement) {
    this.name = name;
    this.basicElement = basicElement;
  }

  public Ore(final ResourceLocation name, final JsonObject json) {
    this(
      name,
      JsonUtil.getElement(json, "basicElement")
    );
  }
}
