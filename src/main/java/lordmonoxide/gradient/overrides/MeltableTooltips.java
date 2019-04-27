package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.science.geology.Meltable;
import lordmonoxide.gradient.science.geology.Meltables;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GradientMod.MODID, value = Dist.CLIENT)
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

    event.getToolTip().add(new TextComponentTranslation("meltable.melt_temp", meltable.meltTemp));
    event.getToolTip().add(new TextComponentTranslation("meltable.melt_time", meltable.meltTime));
    event.getToolTip().add(new TextComponentTranslation("meltable.amount", meltable.amount));
    event.getToolTip().add(new TextComponentTranslation("meltable.fluid", I18n.format("fluid." + meltable.fluid)));
  }
}
