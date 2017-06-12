package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientFuel;
import lordmonoxide.gradient.blocks.firepit.ContainerFirePit;
import lordmonoxide.gradient.blocks.firepit.TileFirePit;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class BurningTooltips {
  public static final BurningTooltips instance = new BurningTooltips();
  
  private BurningTooltips() { }
  
  @SubscribeEvent
  public void addBurningInfoToTooltips(ItemTooltipEvent event) {
    if(!GradientFuel.instance.has(event.getItemStack())) {
      return;
    }
    
    GradientFuel.Fuel fuel = GradientFuel.instance.get(event.getItemStack());
    
    event.getToolTip().add(I18n.format("fuel.ignition_temp", fuel.ignitionTemp));
    event.getToolTip().add(I18n.format("fuel.burn_temp", fuel.burnTemp));
    event.getToolTip().add(I18n.format("fuel.duration", fuel.duration));
  }
}
