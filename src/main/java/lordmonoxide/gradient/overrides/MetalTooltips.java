package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientMetals;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class MetalTooltips {
  public static final MetalTooltips instance = new MetalTooltips();
  
  private MetalTooltips() { }
  
  @SubscribeEvent
  public void addMetalInfoToTooltips(final ItemTooltipEvent event) {
    if(event.getItemStack().isEmpty() || !GradientMetals.hasMeltable(event.getItemStack())) {
      return;
    }
  
    final GradientMetals.Meltable meltable = GradientMetals.getMeltable(event.getItemStack());
    final GradientMetals.Metal metal = meltable.metal;
    
    event.getToolTip().add(I18n.format("metal.melt_temp", metal.meltTemp));
    event.getToolTip().add(I18n.format("metal.melt_time", metal.meltTime * meltable.meltModifier));
    event.getToolTip().add(I18n.format("metal.amount", meltable.amount));
  }
}
