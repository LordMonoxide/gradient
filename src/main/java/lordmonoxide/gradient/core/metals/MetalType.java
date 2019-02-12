package lordmonoxide.gradient.core.metals;

public enum MetalType implements IMetalType {
  COPPER     ("copper",      1085.00f, 3.0f,  63.55f, 0xFFFFB88B, 0xFFFFB88B, 0xFFFF6300, 0xFFDE5600, 0xFFDB2400, 0xFF792F00, 0xFF492914),
  TIN        ("tin",          231.93f, 1.5f, 118.71f, 0xFFEFEFEF, 0xFFF9F9F9, 0xFFCCCCCC, 0xFFB7B7B7, 0xFFADADAD, 0xFF8E8E8E, 0xFF777777),
  IRON       ("iron",        1538.00f, 4.0f,  55.85f, 0xFFD8D8D8, 0xFFFFFFFF, 0xFF969696, 0xFF727272, 0xFF7F7F7F, 0xFF444444, 0xFF353535),
  GOLD       ("gold",        1064.00f, 2.0f, 196.97f, 0xFFFFFF8B, 0xFFFFFFFF, 0xFFDEDE00, 0xFFDC7613, 0xFF868600, 0xFF505000, 0xFF3C3C00),
  BRONZE     ("bronze",       950.00f, 3.5f, 182.26f, 0xFFFFE48B, 0xFFFFEAA8, 0xFFFFC400, 0xFFDEAA00, 0xFFDB7800, 0xFF795D00, 0xFF795D00),
  MAGNESIUM  ("magnesium",    650.00f, 2.5f,  24.31f, 0xFFF9F9F9, 0xFFFFFFFF, 0xFFCCCCCC, 0xFFB7B7B7, 0xFF727272, 0xFF444444, 0xFF262626),
  HEMATITE   ("hematite",    1548.00f, 4.0f,  55.85f, 0xFFB8B8B8, 0xFFDFDFDF, 0xFF767676, 0xFF525252, 0xFF5F5F5F, 0xFF242424, 0xFF151515),
  PYRITE     ("pyrite",      1188.00f, 6.0f, 119.98f, 0xFFAFA67F, 0xFFFFF098, 0xFF736C4F, 0xFF5F5941, 0xFF44453F, 0xFF40413C, 0xFF3D3E39),
  GRAPHITE   ("graphite",    3730.00f, 1.5f,  12.01f, 0xFF807875, 0xFFBDBDBD, 0xFF383431, 0xFF222222, 0xFF4F4D50, 0xFF404050, 0xFF2F2D30),
  CASSITERITE("cassiterite", 1127.00f, 6.5f, 150.71f, 0xFF7F7F7F, 0xFFB9B9B9, 0xFF707070, 0xFF676767, 0xFF6D6D6D, 0xFF595959, 0xFF505050);

  private final String name;
  private final int    meltTime;
  private final float  meltTemp;
  private final float  hardness;
  private final float  weight;

  private final int colourDiffuse;
  private final int colourSpecular;
  private final int colourShadow1;
  private final int colourShadow2;
  private final int colourEdge1;
  private final int colourEdge2;
  private final int colourEdge3;

  MetalType(final String name, final float meltTemp, final float hardness, final float weight, final int colourDiffuse, final int colourSpecular, final int colourShadow1, final int colourShadow2, final int colourEdge1, final int colourEdge2, final int colourEdge3) {
    this.name = name;
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

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public int getMeltTime() {
    return this.meltTime;
  }

  @Override
  public float getMeltTemp() {
    return this.meltTemp;
  }

  @Override
  public float getHardness() {
    return this.hardness;
  }

  @Override
  public float getWeight() {
    return this.weight;
  }

  @Override
  public int getColourDiffuse() {
    return this.colourDiffuse;
  }

  @Override
  public int getColourSpecular() {
    return this.colourSpecular;
  }

  @Override
  public int getColourShadow1() {
    return this.colourShadow1;
  }

  @Override
  public int getColourShadow2() {
    return this.colourShadow2;
  }

  @Override
  public int getColourEdge1() {
    return this.colourEdge1;
  }

  @Override
  public int getColourEdge2() {
    return this.colourEdge2;
  }

  @Override
  public int getColourEdge3() {
    return this.colourEdge3;
  }
}
