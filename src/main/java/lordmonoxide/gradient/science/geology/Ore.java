package lordmonoxide.gradient.science.geology;

public abstract class Ore {
  public final String name;

  public Ore(final String name) {
    this.name = name;
  }

  public static class Metal extends Ore {
    public final lordmonoxide.gradient.science.geology.Metal metal;

    public Metal(final String name, final lordmonoxide.gradient.science.geology.Metal metal) {
      super(name);
      this.metal = metal;
    }
  }
}
