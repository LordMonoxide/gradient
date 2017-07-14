package lordmonoxide.gradient.blocks.heat;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public abstract class HeatSinker extends TileEntity implements ITickable {
  private float heat;
  
  public boolean hasHeat() {
    return this.heat != 0;
  }
  
  public float getHeat() {
    return this.heat;
  }
  
  protected void setHeat(float heat) {
    this.heat = heat;
  }
  
  protected void addHeat(float heat) {
    this.heat += heat;
  }
  
  protected void removeHeat(float heat) {
    this.heat = Math.max(0, this.heat - heat);
  }
  
  @Override
  public void update() {
    if(!this.hasHeat()) {
      return;
    }
    
    this.tickBeforeCooldown();
    this.coolDown();
    this.tickAfterCooldown();
  }
  
  protected abstract void tickBeforeCooldown();
  protected abstract void tickAfterCooldown();
  
  private void coolDown() {
    this.removeHeat(this.calculateHeatLoss() / 20.0f);
  }
  
  protected abstract float calculateHeatLoss();
}
