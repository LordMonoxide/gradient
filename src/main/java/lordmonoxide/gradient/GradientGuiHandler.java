package lordmonoxide.gradient;

import lordmonoxide.gradient.containers.ContainerBronzeBoiler;
import lordmonoxide.gradient.client.gui.GuiBronzeBoiler;
import lordmonoxide.gradient.tileentities.TileBronzeBoiler;
import lordmonoxide.gradient.containers.ContainerBronzeFurnace;
import lordmonoxide.gradient.client.gui.GuiBronzeFurnace;
import lordmonoxide.gradient.tileentities.TileBronzeFurnace;
import lordmonoxide.gradient.containers.ContainerBronzeGrinder;
import lordmonoxide.gradient.client.gui.GuiBronzeGrinder;
import lordmonoxide.gradient.tileentities.TileBronzeGrinder;
import lordmonoxide.gradient.containers.ContainerBronzeOven;
import lordmonoxide.gradient.client.gui.GuiBronzeOven;
import lordmonoxide.gradient.tileentities.TileBronzeOven;
import lordmonoxide.gradient.client.gui.GuiClayCast;
import lordmonoxide.gradient.containers.ContainerClayCrucible;
import lordmonoxide.gradient.client.gui.GuiClayCrucible;
import lordmonoxide.gradient.tileentities.TileClayCrucible;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GradientGuiHandler implements IGuiHandler {
  public static final int CLAY_CRUCIBLE = 1;
  public static final int CLAY_CAST = 2;
  public static final int BRONZE_FURNACE = 3;
  public static final int BRONZE_BOILER = 4;
  public static final int BRONZE_OVEN = 5;
  public static final int BRONZE_GRINDER = 6;

  @Override
  public Container getServerGuiElement(final int id, final EntityPlayer player, final World world, final int x, final int y, final int z) {
    final TileEntity te = world.getTileEntity(new BlockPos(x, y, z));

    switch(id) {
      case CLAY_CRUCIBLE:
        if(te == null) {
          return null;
        }

        return new ContainerClayCrucible(player.inventory, (TileClayCrucible)te);

      case CLAY_CAST:
        return new Container() {
          @Override
          public boolean canInteractWith(final EntityPlayer playerIn) {
            return true;
          }
        };

      case BRONZE_FURNACE:
        if(te == null) {
          return null;
        }

        return new ContainerBronzeFurnace(player.inventory, (TileBronzeFurnace)te);

      case BRONZE_BOILER:
        if(te == null) {
          return null;
        }

        return new ContainerBronzeBoiler(player.inventory, (TileBronzeBoiler)te);

      case BRONZE_OVEN:
        if(te == null) {
          return null;
        }

        return new ContainerBronzeOven(player.inventory, (TileBronzeOven)te);

      case BRONZE_GRINDER:
        if(te == null) {
          return null;
        }

        return new ContainerBronzeGrinder(player.inventory, (TileBronzeGrinder)te);
    }

    return null;
  }

  @Override
  public Object getClientGuiElement(final int id, final EntityPlayer player, final World world, final int x, final int y, final int z) {
    final BlockPos pos = new BlockPos(x, y, z);
    final TileEntity te = world.getTileEntity(pos);

    switch(id) {
      case CLAY_CRUCIBLE:
        if(te == null) {
          return null;
        }

        return new GuiClayCrucible((ContainerClayCrucible)this.getServerGuiElement(id, player, world, x, y ,z), (TileClayCrucible)te, player.inventory);

      case CLAY_CAST:
        return new GuiClayCast(this.getServerGuiElement(id, player, world, x, y ,z), player.getHeldItemMainhand());

      case BRONZE_FURNACE:
        if(te == null) {
          return null;
        }

        return new GuiBronzeFurnace((ContainerBronzeFurnace)this.getServerGuiElement(id, player, world, x, y ,z), (TileBronzeFurnace)te, player.inventory);

      case BRONZE_BOILER:
        if(te == null) {
          return null;
        }

        return new GuiBronzeBoiler((ContainerBronzeBoiler)this.getServerGuiElement(id, player, world, x, y ,z), (TileBronzeBoiler)te, player.inventory);

      case BRONZE_OVEN:
        if(te == null) {
          return null;
        }

        return new GuiBronzeOven((ContainerBronzeOven)this.getServerGuiElement(id, player, world, x, y ,z), (TileBronzeOven)te, player.inventory);

      case BRONZE_GRINDER:
        if(te == null) {
          return null;
        }

        return new GuiBronzeGrinder((ContainerBronzeGrinder) this.getServerGuiElement(id, player, world, x, y ,z), (TileBronzeGrinder) te, player.inventory);
    }

    return null;
  }
}
