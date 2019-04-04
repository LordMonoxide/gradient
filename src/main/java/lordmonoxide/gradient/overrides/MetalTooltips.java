package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = GradientMod.MODID, value = Dist.CLIENT)
public final class MetalTooltips {
  private MetalTooltips() { }

  @SubscribeEvent
  public static void addMetalInfoToTooltips(final ItemTooltipEvent event) {
    if(event.getItemStack().isEmpty() || !GradientMetals.hasMeltable(event.getItemStack())) {
      return;
    }

    final GradientMetals.Meltable meltable = GradientMetals.getMeltable(event.getItemStack());
    final GradientMetals.Metal metal = meltable.metal;

    event.getToolTip().add(new TextComponentTranslation("metal.melt_temp", meltable.meltTemp));
    event.getToolTip().add(new TextComponentTranslation("metal.melt_time", metal.meltTime * meltable.meltModifier));
    event.getToolTip().add(new TextComponentTranslation("metal.amount", meltable.amount));
  }
}
