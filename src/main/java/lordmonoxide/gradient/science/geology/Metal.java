package lordmonoxide.gradient.science.geology;

import com.google.common.collect.ImmutableList;
import lordmonoxide.gradient.science.chemistry.Element;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.Fluid;

public class Metal {
  public final String name;
  public final int   meltTime;
  public final float meltTemp;
  public final float hardness;
  public final float weight;

  public final int durability;

  public final int harvestLevel;
  public final float harvestSpeed;

  public final double attackDamageMultiplier;
  public final double attackSpeedMultiplier;

  public final boolean canMakeIngots;
  public final boolean canMakeTools;
  public final boolean canMakePlates;
  public final boolean canMakeDustWithBasicGrinder;

  public final int colourDiffuse;
  public final int colourSpecular;
  public final int colourShadow1;
  public final int colourShadow2;
  public final int colourEdge1;
  public final int colourEdge2;
  public final int colourEdge3;

  public final ImmutableList<MetalElement> elements;

  public Metal(final String name, final float meltTemp, final float hardness, final float weight, final boolean canMakeIngots, final int colourDiffuse, final int colourSpecular, final int colourShadow1, final int colourShadow2, final int colourEdge1, final int colourEdge2, final int colourEdge3, final NonNullList<MetalElement> elements) {
    this.name = name;
    this.meltTime = Math.round(hardness * 7.5f);
    this.meltTemp = meltTemp;
    this.hardness = hardness;
    this.weight   = weight;

    this.durability = Math.round(hardness * 35);

    this.harvestLevel = Math.round(hardness / 2);
    this.harvestSpeed = 1 / weight * 130;

    this.attackDamageMultiplier = hardness / 2 * weight / 100;
    this.attackSpeedMultiplier  = 1 / weight * 100;

    this.canMakeIngots = canMakeIngots;
    this.canMakeTools = hardness >= 3.0f;
    this.canMakePlates = hardness <= 4.0f;
    this.canMakeDustWithBasicGrinder = hardness <= 2.5f;

    this.colourDiffuse = colourDiffuse;
    this.colourSpecular = colourSpecular;
    this.colourShadow1 = colourShadow1;
    this.colourShadow2 = colourShadow2;
    this.colourEdge1 = colourEdge1;
    this.colourEdge2 = colourEdge2;
    this.colourEdge3 = colourEdge3;

    this.elements = ImmutableList.copyOf(elements);
  }

  public Fluid getFluid() {
    return null; //TODO FluidRegistry.getFluid(this.name);
  }

  public static class MetalElement {
    public final Element element;
    public final float percentage;

    public MetalElement(final Element element, final float percentage) {
      this.element = element;
      this.percentage = percentage;
    }
  }
}
