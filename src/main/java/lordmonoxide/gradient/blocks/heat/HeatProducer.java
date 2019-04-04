package lordmonoxide.gradient.blocks.heat;

import net.minecraft.tileentity.TileEntityType;

public abstract class HeatProducer extends HeatSinker {
  public HeatProducer(final TileEntityType<?> tileEntityType) {
    super(tileEntityType);
  }

  @Override
  protected void tickAfterCooldown() {
    this.heatUp();
  }

  private void heatUp() {
    this.addHeat(this.calculateHeatGain() / 20.0f);
  }

  protected abstract float calculateHeatGain();
}
