package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.inventory.ContainerPlayer3x3Crafting;
import lordmonoxide.gradient.inventory.GuiInventory3x3Crafting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class OverrideInventory {
  private OverrideInventory() { }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public static void onGuiOpen(final GuiOpenEvent event) {
    if(event.getGui() != null && event.getGui().getClass() == GuiInventory.class) {
      event.setGui(new GuiInventory3x3Crafting(Minecraft.getMinecraft().player));
    }
  }

  @SubscribeEvent
  public static void onEntityJoinWorld(final EntityJoinWorldEvent event) {
    if(event.getEntity() instanceof EntityPlayer) {
      final EntityPlayer player = (EntityPlayer)event.getEntity();

      if(!(player.inventoryContainer instanceof ContainerPlayer3x3Crafting)) {
        player.inventoryContainer = new ContainerPlayer3x3Crafting(player.inventory, !player.world.isRemote, player);
        player.openContainer = player.inventoryContainer;
      }
    }
  }
}
