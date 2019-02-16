package lordmonoxide.gradient.core.geology.elements;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
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
import java.util.NoSuchElementException;

public final class Elements {
  private Elements() { }

  private static final Map<ResourceLocation, Element> elements = new HashMap<>();
  private static final Collection<Element> elementsRead = Collections.unmodifiableCollection(elements.values());

  public static void reload(final IResourceManager resourceManager) {
    elements.clear();

    GradientCore.LOGGER.info("Reloading elements");
    for(final ResourceLocation location : resourceManager.getAllResourceLocations("elements", s -> s.endsWith(".json"))) {
      try {
        loadElement(resourceManager.getResource(location));
      } catch(final IOException e) {
        GradientCore.LOGGER.error("Failed to load element {}: {}", location, e);
      }
    }
  }

  private static void loadElement(final IResource resource) {
    final ResourceLocation oldLoc = resource.getLocation();
    final String path = oldLoc.getPath();
    final ResourceLocation loc = new ResourceLocation(oldLoc.getNamespace(), path.substring("elements/".length(), path.length() - ".json".length()));

    GradientCore.LOGGER.debug("Loading element {}", loc);

    final JsonObject json = JsonUtil.getJsonFromResource(resource);

    final ElementType type;
    try {
      type = JsonUtil.getEnum(json, "type", ElementType.ELEMENT, ElementType.class);
    } catch(final IllegalArgumentException e) {
      throw new JsonParseException("Unknown element type in " + loc, e);
    }

    switch(type) {
      case ELEMENT:
        add(new Element(loc));
        break;

      case METAL:
        add(new Metal(loc, json));
        break;
    }
  }

  private static void add(final Element element) {
    GradientCore.LOGGER.info("Added {} {}", element.type, element.name);
    elements.put(element.name, element);
  }

  public static Collection<Element> all() {
    return elementsRead;
  }

  public static Element get(final ResourceLocation name) throws NoSuchElementException {
    final Element element = elements.get(name);

    if(element == null) {
      throw new NoSuchElementException("No such element " + name);
    }

    return element;
  }
}
