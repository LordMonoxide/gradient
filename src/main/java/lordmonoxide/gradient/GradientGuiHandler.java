package lordmonoxide.gradient;

import lordmonoxide.gradient.client.gui.GuiBronzeBoiler;
import lordmonoxide.gradient.client.gui.GuiBronzeFurnace;
import lordmonoxide.gradient.client.gui.GuiBronzeGrinder;
import lordmonoxide.gradient.client.gui.GuiBronzeOven;
import lordmonoxide.gradient.client.gui.GuiClayCast;
import lordmonoxide.gradient.client.gui.GuiClayCrucible;
import lordmonoxide.gradient.containers.ContainerBronzeBoiler;
import lordmonoxide.gradient.containers.ContainerBronzeFurnace;
import lordmonoxide.gradient.containers.ContainerBronzeGrinder;
import lordmonoxide.gradient.containers.ContainerBronzeOven;
import lordmonoxide.gradient.containers.ContainerClayCrucible;
import lordmonoxide.gradient.items.ItemClayCastUnhardened;
import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.fml.network.FMLPlayMessages;

import javax.annotation.Nullable;

public final class GradientGuiHandler {
  private GradientGuiHandler() { }

  @Nullable
  public static GuiScreen openGui(final FMLPlayMessages.OpenContainer openContainer) {
    if(openContainer.getId().equals(GuiClayCrucible.ID)) {
      final BlockPos pos = openContainer.getAdditionalData().readBlockPos();
      final ContainerClayCrucible container = getContainer(pos);

      if(container != null) {
        return new GuiClayCrucible(container);
      }
    }

    //TODO: make this entirely less bad
    if(openContainer.getId().equals(GuiClayCast.ID)) {
      return new GuiClayCast(new Container() {
        @Override
        public boolean canInteractWith(final EntityPlayer playerIn) {
          return true;
        }
      }, (ItemClayCastUnhardened)Minecraft.getInstance().player.getHeldItemMainhand().getItem());
    }

    if(openContainer.getId().equals(GuiBronzeFurnace.ID)) {
      final BlockPos pos = openContainer.getAdditionalData().readBlockPos();
      final ContainerBronzeFurnace container = getContainer(pos);

      if(container != null) {
        return new GuiBronzeFurnace(container);
      }
    }

    if(openContainer.getId().equals(GuiBronzeBoiler.ID)) {
      final BlockPos pos = openContainer.getAdditionalData().readBlockPos();
      final ContainerBronzeBoiler container = getContainer(pos);

      if(container != null) {
        return new GuiBronzeBoiler(container);
      }
    }

    if(openContainer.getId().equals(GuiBronzeOven.ID)) {
      final BlockPos pos = openContainer.getAdditionalData().readBlockPos();
      final ContainerBronzeOven container = getContainer(pos);

      if(container != null) {
        return new GuiBronzeOven(container);
      }
    }

    if(openContainer.getId().equals(GuiBronzeGrinder.ID)) {
      final BlockPos pos = openContainer.getAdditionalData().readBlockPos();
      final ContainerBronzeGrinder container = getContainer(pos);

      if(container != null) {
        return new GuiBronzeGrinder(container);
      }
    }

    return null;
  }

  @Nullable
  private static <T extends Container> T getContainer(final BlockPos pos) {
    final TileEntity obj = WorldUtils.getTileEntity(Minecraft.getInstance().world, pos, TileEntity.class);

    if(obj instanceof IInteractionObject) {
      final EntityPlayer player = Minecraft.getInstance().player;
      final InventoryPlayer inventory = player.inventory;

      return (T)((IInteractionObject)obj).createContainer(inventory, player);
    }

    return null;
  }
}
