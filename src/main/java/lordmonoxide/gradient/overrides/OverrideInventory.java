package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.inventory.ContainerPlayer3x3Crafting;
import lordmonoxide.gradient.inventory.GuiInventory3x3Crafting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OverrideInventory {
  public static final OverrideInventory instance = new OverrideInventory();
  
  private OverrideInventory() { }
  
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onGuiOpen(GuiOpenEvent event) {
    if(event.getGui() != null && event.getGui().getClass() == GuiInventory.class) {
      event.setGui(new GuiInventory3x3Crafting(Minecraft.getMinecraft().thePlayer));
    }
  }
  
  @SubscribeEvent
  public void onEntityJoinWorld(EntityJoinWorldEvent event) {
    if(event.getEntity() instanceof EntityPlayer) {
      EntityPlayer player = (EntityPlayer)event.getEntity();
      
      player.inventoryContainer = new ContainerPlayer3x3Crafting(player.inventory, !player.worldObj.isRemote, player);
      player.openContainer = player.inventoryContainer;
    }
  }
}
