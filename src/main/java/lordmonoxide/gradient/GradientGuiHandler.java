package lordmonoxide.gradient;

import lordmonoxide.gradient.blocks.firepit.ContainerFirePit;
import lordmonoxide.gradient.blocks.firepit.GuiFirePit;
import lordmonoxide.gradient.blocks.firepit.TileFirePit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GradientGuiHandler implements IGuiHandler {
  public static final int FIRE_PIT = 0;
  
  @Override
  public Container getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    switch(id) {
      case FIRE_PIT:
        return new ContainerFirePit(player.inventory, (TileFirePit)world.getTileEntity(new BlockPos(x, y, z)));
    }
    
    return null;
  }
  
  @Override
  public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
    switch(id) {
      case FIRE_PIT:
        return new GuiFirePit(getServerGuiElement(id, player, world, x, y, z), player.inventory);
    }
    
    return null;
  }
}
