package lordmonoxide.gradient.items;

public class StoneHammer extends GradientItemTool {
  public StoneHammer() {
    super("stone_hammer", 0.5f, -2.4f, 2);
    this.setHarvestLevel("pickaxe", 2);
    this.setHarvestLevel("hammer", 2);
    this.setMaxDamage(19);
  }
}
