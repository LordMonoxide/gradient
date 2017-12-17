package lordmonoxide.gradient;

import lordmonoxide.gradient.blocks.bronzeboiler.ContainerBronzeBoiler;
import lordmonoxide.gradient.blocks.bronzeboiler.GuiBronzeBoiler;
import lordmonoxide.gradient.blocks.bronzeboiler.TileBronzeBoiler;
import lordmonoxide.gradient.blocks.bronzefurnace.ContainerBronzeFurnace;
import lordmonoxide.gradient.blocks.bronzefurnace.GuiBronzeFurnace;
import lordmonoxide.gradient.blocks.bronzefurnace.TileBronzeFurnace;
import lordmonoxide.gradient.blocks.claycast.GuiClayCast;
import lordmonoxide.gradient.blocks.claycrucible.ContainerClayCrucible;
import lordmonoxide.gradient.blocks.claycrucible.GuiClayCrucible;
import lordmonoxide.gradient.blocks.claycrucible.TileClayCrucible;
import lordmonoxide.gradient.blocks.firepit.ContainerFirePit;
import lordmonoxide.gradient.blocks.firepit.GuiFirePit;
import lordmonoxide.gradient.blocks.firepit.TileFirePit;
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
  public static final int BRONZE_BOILER = 3;
  public static final int BRONZE_FURNACE = 4;
  
  @Override
  public Container getServerGuiElement(final int id, final EntityPlayer player, final World world, final int x, final int y, final int z) {
    final TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
    
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
      
      case BRONZE_BOILER:
        if(te == null) {
          return null;
        }
        
        return new ContainerBronzeBoiler(player.inventory, (TileBronzeBoiler)te);
      
      case BRONZE_FURNACE:
        if(te == null) {
          return null;
        }
        
        return new ContainerBronzeFurnace(player.inventory, (TileBronzeFurnace)te);
    }
    
    return null;
  }
  
  @Override
  public Object getClientGuiElement(final int id, final EntityPlayer player, final World world, final int x, final int y, final int z) {
    final BlockPos pos = new BlockPos(x, y, z);
    final TileEntity te = world.getTileEntity(pos);
    
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
      
      case BRONZE_BOILER:
        if(te == null) {
          return null;
        }
        
        return new GuiBronzeBoiler((ContainerBronzeBoiler)this.getServerGuiElement(id, player, world, x, y ,z), (TileBronzeBoiler)te, player.inventory);
      
      case BRONZE_FURNACE:
        if(te == null) {
          return null;
        }
        
        return new GuiBronzeFurnace((ContainerBronzeFurnace)this.getServerGuiElement(id, player, world, x, y ,z), (TileBronzeFurnace)te, player.inventory);
    }
    
    return null;
  }
}
