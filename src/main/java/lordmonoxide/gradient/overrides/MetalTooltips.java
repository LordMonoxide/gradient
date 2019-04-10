package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.science.geology.Metal;
import lordmonoxide.gradient.science.geology.Metals;
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
    if(event.getItemStack().isEmpty() || Metals.getMeltable(event.getItemStack()) == Metals.INVALID_MELTABLE) {
      return;
    }

    final Metals.Meltable meltable = Metals.getMeltable(event.getItemStack());
    final Metal metal = meltable.metal;

    event.getToolTip().add(I18n.format("metal.melt_temp", meltable.meltTemp));
    event.getToolTip().add(I18n.format("metal.melt_time", metal.meltTime * meltable.meltModifier));
    event.getToolTip().add(I18n.format("metal.amount", meltable.amount));
  }
}
