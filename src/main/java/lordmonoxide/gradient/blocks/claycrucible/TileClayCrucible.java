package lordmonoxide.gradient.blocks.claycrucible;

import lordmonoxide.gradient.blocks.heat.HeatSinker;

public class TileClayCrucible extends HeatSinker {
  @Override
  protected void tickBeforeCooldown() {
    
  }
  
  @Override
  protected void tickAfterCooldown() {
    
  }
  
  @Override
  protected float calculateHeatLoss() {
    return (float)Math.pow(this.getHeat() / 800, 2);
  }
  
  @Override
  protected float heatTransferEfficiency() {
    return 0.6f;
  }
}
