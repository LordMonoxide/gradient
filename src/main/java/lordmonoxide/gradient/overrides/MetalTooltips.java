package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = GradientMod.MODID, value = Side.CLIENT)
public final class MetalTooltips {
  private MetalTooltips() { }

  @SubscribeEvent
  public static void addMetalInfoToTooltips(final ItemTooltipEvent event) {
    if(event.getItemStack().isEmpty() || !GradientMetals.hasMeltable(event.getItemStack())) {
      return;
    }

    final GradientMetals.Meltable meltable = GradientMetals.getMeltable(event.getItemStack());
    final GradientMetals.Metal metal = meltable.metal;

    event.getToolTip().add(I18n.format("metal.melt_temp", meltable.meltTemp));
    event.getToolTip().add(I18n.format("metal.melt_time", metal.meltTime * meltable.meltModifier));
    event.getToolTip().add(I18n.format("metal.amount", meltable.amount));
  }
}
