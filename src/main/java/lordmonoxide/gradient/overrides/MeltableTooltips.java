package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.science.geology.Meltable;
import lordmonoxide.gradient.science.geology.Meltables;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = GradientMod.MODID, value = Side.CLIENT)
public final class MeltableTooltips {
  private MeltableTooltips() { }

  @SubscribeEvent
  public static void addMeltableInfoToTooltips(final ItemTooltipEvent event) {
    if(event.getItemStack().isEmpty()) {
      return;
    }

    final Meltable meltable = Meltables.get(event.getItemStack());

    if(meltable == Meltables.INVALID_MELTABLE) {
      return;
    }

    event.getToolTip().add(I18n.format("meltable.melt_temp", meltable.meltTemp));
    event.getToolTip().add(I18n.format("meltable.melt_time", meltable.meltTime));
    event.getToolTip().add(I18n.format("meltable.amount", meltable.amount));
    event.getToolTip().add(I18n.format("meltable.fluid", I18n.format("fluid." + meltable.fluid)));
  }
}
