package lordmonoxide.gradient.science.geology;

public abstract class Ore {
  public final String name;

  public Ore(final String name) {
    this.name = name;
  }

  public static class Metal extends Ore {
    public final lordmonoxide.gradient.science.geology.Metal metal;
    public final lordmonoxide.gradient.science.geology.Metal basic;

    public Metal(final String name, final lordmonoxide.gradient.science.geology.Metal metal, lordmonoxide.gradient.science.geology.Metal basic) {
      super(name);
      this.metal = metal;
      this.basic = basic;
    }
  }
}
