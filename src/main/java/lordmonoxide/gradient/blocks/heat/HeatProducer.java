package lordmonoxide.gradient.blocks.heat;

public abstract class HeatProducer extends HeatSinker {
  @Override
  protected void tickAfterCooldown() {
    this.heatUp();
  }
  
  private void heatUp() {
    this.addHeat(this.calculateHeatGain() / 20.0f);
  }
  
  protected abstract float calculateHeatGain();
}
