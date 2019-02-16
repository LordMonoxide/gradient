package lordmonoxide.gradient.core.geology.elements;

import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class Metal extends Element {
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
      Integer.parseInt(JsonUtils.getString(json, "colourDiffuse"), 16),
      Integer.parseInt(JsonUtils.getString(json, "colourSpecular"), 16),
      Integer.parseInt(JsonUtils.getString(json, "colourShadow1"), 16),
      Integer.parseInt(JsonUtils.getString(json, "colourShadow2"), 16),
      Integer.parseInt(JsonUtils.getString(json, "colourEdge1"), 16),
      Integer.parseInt(JsonUtils.getString(json, "colourEdge2"), 16),
      Integer.parseInt(JsonUtils.getString(json, "colourEdge3"), 16)
    );
  }
}
