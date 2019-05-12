package lordmonoxide.terra;

import net.minecraft.world.gen.placement.IPlacementConfig;

public class TerraPlacementConfig implements IPlacementConfig {
  public final int attempts;
  public final int chance;
  public final int bottom;
  public final int top;

  public TerraPlacementConfig(final int attempts, final int chance, final int bottom, final int top) {
    this.attempts = attempts;
    this.chance = chance;
    this.bottom = bottom;
    this.top = top;
  }
}
