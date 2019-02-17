package lordmonoxide.gradient.core.geology.elements;

import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class Metal extends Element {
  private static final int ALPHA_MASK = 0xFF000000;

  public final int   meltTime;
  public final float meltTemp;
  public final float hardness;
  public final float weight;

  public final int colourDiffuse;
  public final int colourSpecular;
  public final int colourShadow1;
  public final int colourShadow2;
  public final int colourEdge1;
  public final int colourEdge2;
  public final int colourEdge3;

  public Metal(final ResourceLocation name, final float meltTemp, final float hardness, final float weight, final int colourDiffuse, final int colourSpecular, final int colourShadow1, final int colourShadow2, final int colourEdge1, final int colourEdge2, final int colourEdge3) {
    super(name, ElementType.METAL);

    this.meltTime = Math.round(hardness * 7.5f);
    this.meltTemp = meltTemp;
    this.hardness = hardness;
    this.weight   = weight;

    this.colourDiffuse = colourDiffuse;
    this.colourSpecular = colourSpecular;
    this.colourShadow1 = colourShadow1;
    this.colourShadow2 = colourShadow2;
    this.colourEdge1 = colourEdge1;
    this.colourEdge2 = colourEdge2;
    this.colourEdge3 = colourEdge3;
  }

  Metal(final ResourceLocation name, final JsonObject json) {
    this(
      name,
      JsonUtils.getFloat(json, "meltTemp"),
      JsonUtils.getFloat(json, "hardness"),
      JsonUtils.getFloat(json, "weight"),
      readInt(json, "colourDiffuse"),
      readInt(json, "colourSpecular"),
      readInt(json, "colourShadow1"),
      readInt(json, "colourShadow2"),
      readInt(json, "colourEdge1"),
      readInt(json, "colourEdge2"),
      readInt(json, "colourEdge3")
    );
  }

  private static int readInt(final JsonObject json, final String key) {
    final int rgb = Integer.parseInt(JsonUtils.getString(json, key), 16);
    return ALPHA_MASK | Integer.reverseBytes(rgb) >>> 8;
  }
}
