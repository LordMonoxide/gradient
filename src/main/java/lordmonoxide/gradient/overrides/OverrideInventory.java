package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.inventory.ContainerPlayer3x3Crafting;
import lordmonoxide.gradient.inventory.GuiInventory3x3Crafting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GradientMod.MOD_ID)
public final class OverrideInventory {
  private OverrideInventory() { }

  @OnlyIn(Dist.CLIENT)
  @SubscribeEvent
  public static void onGuiOpen(final GuiOpenEvent event) {
    if(event.getGui() != null && event.getGui().getClass() == InventoryScreen.class) {
      event.setGui(new GuiInventory3x3Crafting(Minecraft.getInstance().player));
    }
  }

  @SubscribeEvent
  public static void onEntityJoinWorld(final EntityJoinWorldEvent event) {
    if(event.getEntity() instanceof PlayerEntity) {
      final PlayerEntity player = (PlayerEntity)event.getEntity();

      if(!(player.container instanceof ContainerPlayer3x3Crafting)) {
        player.container = new ContainerPlayer3x3Crafting(player.inventory, !player.world.isRemote, player);
        player.openContainer = player.container;
      }
    }
  }
}
