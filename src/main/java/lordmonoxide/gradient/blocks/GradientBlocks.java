package lordmonoxide.gradient.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class GradientBlocks {
  private static final List<GradientBlock> blocks = new ArrayList<>();
  private static final List<GradientBlockCraftable> craftables = new ArrayList<>();
  
  public static final Pebble PEBBLE = register(new Pebble());
  
  public static final FirePit FIRE_PIT = register(new FirePit());
  
  @SideOnly(Side.CLIENT)
  public static void addModels() {
    for(GradientBlock block : blocks) {
      ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory")); //$NON-NLS-1$
    }
  }
  
  public static void addRecipes() {
    for(GradientBlockCraftable craftable : craftables) {
      craftable.addRecipe();
    }
  }
  
  private static <T extends GradientBlock> T register(T block) {
    registerWithoutItem(block);
    GameRegistry.register(new ItemBlock(block), block.getRegistryName());
    return block;
  }
  
  private static <T extends GradientBlock> T registerWithoutItem(T block) {
    blocks.add(block);
    GameRegistry.register(block);
  
    if(block instanceof GradientBlockCraftable) {
      craftables.add((GradientBlockCraftable)block);
    }
    
    if(block instanceof ITileEntityProvider) {
      GameRegistry.registerTileEntity(((ITileEntityProvider)block).createNewTileEntity(null, 0).getClass(), block.getRegistryName().toString());
    }
    
    return block;
  }
}
