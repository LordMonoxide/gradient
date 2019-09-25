package lordmonoxide.gradient.blocks.heat;

public abstract class HeatProducer extends HeatSinker {
  @Override
  protected void tickAfterCooldown(final float tickScale) {
    this.heatUp(tickScale);
  }

  private void heatUp(final float tickScale) {
    this.addHeat(this.calculateHeatGain() / 20.0f * tickScale);
  }

  protected abstract float calculateHeatGain();
}
