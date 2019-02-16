package lordmonoxide.gradient.core.geology.elements;

import net.minecraft.util.ResourceLocation;

public class Element {
  public final ResourceLocation name;
  public final ElementType type;

  public Element(final ResourceLocation name, final ElementType type) {
    this.name = name;
    this.type = type;
  }

  public Element(final ResourceLocation name) {
    this(name, ElementType.ELEMENT);
  }
}
