package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientFood;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class CookingTooltips {
  public static final CookingTooltips instance = new CookingTooltips();
  
  private CookingTooltips() { }
  
  @SubscribeEvent
  public void addCookingInfoToTooltips(ItemTooltipEvent event) {
    if(!GradientFood.instance.has(event.getItemStack())) {
      return;
    }
    
    GradientFood.Food food = GradientFood.instance.get(event.getItemStack());
    
    event.getToolTip().add(I18n.format("food.cook_temp", food.cookTemp));
    event.getToolTip().add(I18n.format("food.duration", food.duration));
    event.getToolTip().add(I18n.format("food.cooked", food.cooked.getDisplayName()));
  }
}
