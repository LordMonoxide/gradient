package lordmonoxide.gradient;

import lordmonoxide.gradient.blocks.claycast.GuiClayCast;
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
  public static final int CLAY_CAST = 2;
  
  @Override
  public Container getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
    
    switch(id) {
      case FIRE_PIT:
        if(te == null) {
          return null;
        }
        
        return new ContainerFirePit(player.inventory, (TileFirePit)te);
        
      case CLAY_CRUCIBLE:
        if(te == null) {
          return null;
        }
        
        return new ContainerClayCrucible(player.inventory, (TileClayCrucible)te);
        
      case CLAY_CAST:
        return new Container() {
          @Override
          public boolean canInteractWith(EntityPlayer playerIn) {
            return true;
          }
        };
    }
    
    return null;
  }
  
  @Override
  public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    BlockPos pos = new BlockPos(x, y, z);
    TileEntity te = world.getTileEntity(pos);
    
    switch(id) {
      case FIRE_PIT:
        if(te == null) {
          return null;
        }
        
        return new GuiFirePit((ContainerFirePit)this.getServerGuiElement(id, player, world, x, y, z), (TileFirePit)te, world.getBlockState(pos), player.inventory);
      
      case CLAY_CRUCIBLE:
        if(te == null) {
          return null;
        }
        
        return new GuiClayCrucible((ContainerClayCrucible)this.getServerGuiElement(id, player, world, x, y ,z), (TileClayCrucible)te, player.inventory);
      
      case CLAY_CAST:
        return new GuiClayCast(this.getServerGuiElement(id, player, world, x, y ,z), player.getHeldItemMainhand());
    }
    
    return null;
  }
}
