package lordmonoxide.gradient.core.geology.ores;

import com.google.gson.JsonObject;
import lordmonoxide.gradient.core.geology.elements.Element;
import lordmonoxide.gradient.core.utils.JsonUtil;
import net.minecraft.util.ResourceLocation;

public class Ore {
  public final ResourceLocation name;
  public final Element basicElement;
  public final Element rawElement;

  public Ore(final ResourceLocation name, final Element basicElement, final Element rawElement) {
    this.name = name;
    this.basicElement = basicElement;
    this.rawElement = rawElement;
  }

  public Ore(final ResourceLocation name, final JsonObject json) {
    this(
      name,
      JsonUtil.getElement(json, "basicElement"),
      JsonUtil.getElement(json, "rawElement")
    );
  }
}
