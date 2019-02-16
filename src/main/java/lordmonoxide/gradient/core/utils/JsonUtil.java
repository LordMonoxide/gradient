package lordmonoxide.gradient.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lordmonoxide.gradient.core.geology.elements.Element;
import lordmonoxide.gradient.core.geology.elements.Elements;
import net.minecraft.resources.IResource;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

public final class JsonUtil {
  private JsonUtil() { }

  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

  public static JsonObject getJsonFromResource(final IResource resource) {
    final Reader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8.newDecoder()));
    return JsonUtils.fromJson(GSON, reader, JsonObject.class);
  }

  public static <T extends Enum<T>> T getEnum(final JsonObject json, final String memberName, final Class<T> enumClass) throws IllegalArgumentException {
    return Enum.valueOf(enumClass, JsonUtils.getString(json, memberName).toUpperCase());
  }

  public static <T extends Enum<T>> T getEnum(final JsonObject json, final String memberName, final T fallback, final Class<T> enumClass) throws IllegalArgumentException {
    if(!JsonUtils.hasField(json, memberName)) {
      return fallback;
    }

    return getEnum(json, memberName, enumClass);
  }

  public static Element getElement(final JsonObject json, final String memberName) {
    return Elements.get(new ResourceLocation(JsonUtils.getString(json, memberName)));
  }

  public static Element getElement(final JsonObject json, final String memberName, final Element fallback) {
    try {
      return Elements.get(new ResourceLocation(JsonUtils.getString(json, memberName)));
    } catch(final NoSuchElementException e) {
      return fallback;
    }
  }
}
