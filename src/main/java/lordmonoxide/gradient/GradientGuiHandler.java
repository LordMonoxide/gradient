package lordmonoxide.gradient;

import lordmonoxide.gradient.blocks.claycrucible.ContainerClayCrucible;
import lordmonoxide.gradient.blocks.claycrucible.GuiClayCrucible;
import lordmonoxide.gradient.blocks.claycrucible.TileClayCrucible;
import lordmonoxide.gradient.blocks.firepit.ContainerFirePit;
import lordmonoxide.gradient.blocks.firepit.GuiFirePit;
import lordmonoxide.gradient.blocks.firepit.TileFirePit;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GradientGuiHandler implements IGuiHandler {
  public static final int FIRE_PIT = 0;
  public static final int CLAY_CRUCIBLE = 1;
  
  @Override
  public Container getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
    
    if(te == null) {
      return null;
    }
    
    switch(id) {
      case FIRE_PIT:
        return new ContainerFirePit(player.inventory, (TileFirePit)te);
        
      case CLAY_CRUCIBLE:
        return new ContainerClayCrucible(player.inventory, (TileClayCrucible)te);
    }
    
    return null;
  }
  
  @Override
  public Gui getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
    
    if(te == null) {
      return null;
    }
    
    switch(id) {
      case FIRE_PIT:
        return new GuiFirePit((ContainerFirePit)this.getServerGuiElement(id, player, world, x, y, z), (TileFirePit)te, player.inventory);
        
      case CLAY_CRUCIBLE:
        return new GuiClayCrucible((ContainerClayCrucible)this.getServerGuiElement(id, player, world, x, y ,z), (TileClayCrucible)te, player.inventory);
    }
    
    return null;
  }
}
