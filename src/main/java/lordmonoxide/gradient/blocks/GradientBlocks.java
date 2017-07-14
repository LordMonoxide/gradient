package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.blocks.claybucket.BlockClayBucket;
import lordmonoxide.gradient.blocks.claybucket.BlockClayBucketUnhardened;
import lordmonoxide.gradient.blocks.claycrucible.BlockClayCrucible;
import lordmonoxide.gradient.blocks.claycrucible.BlockClayCrucibleUnhardened;
import lordmonoxide.gradient.blocks.clayfurnace.BlockClayFurnace;
import lordmonoxide.gradient.blocks.clayfurnace.BlockClayFurnaceUnhardened;
import lordmonoxide.gradient.blocks.firepit.BlockFirePit;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public final class GradientBlocks {
  private static final List<GradientBlock> blocks = new ArrayList<>();
  private static final List<GradientBlockCraftable> craftables = new ArrayList<>();
  
  public static final Material MATERIAL_CLAY_MACHINE = new Material(MapColor.BROWN);
  
  public static final BlockPebble PEBBLE = register(new BlockPebble());
  
  public static final BlockFirePit FIRE_PIT = register(new BlockFirePit());
  
  public static final BlockClayBucketUnhardened   CLAY_BUCKET_UNHARDENED   = register(new BlockClayBucketUnhardened());
  public static final BlockClayFurnaceUnhardened  CLAY_FURNACE_UNHARDENED  = register(new BlockClayFurnaceUnhardened());
  public static final BlockClayCrucibleUnhardened CLAY_CRUCIBLE_UNHARDENED = register(new BlockClayCrucibleUnhardened());
  
  public static final BlockClayBucket   CLAY_BUCKET   = register(new BlockClayBucket());
  public static final BlockClayFurnace  CLAY_FURNACE  = register(new BlockClayFurnace());
  public static final BlockClayCrucible CLAY_CRUCIBLE = register(new BlockClayCrucible());
  
  private GradientBlocks() {
    
  }
  
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
      try {
        //noinspection unchecked
        GameRegistry.registerTileEntity((Class<? extends TileEntity>)((ITileEntityProvider)block).getClass().getMethod("createNewTileEntity", World.class, int.class).getReturnType(), block.getRegistryName().toString());
      } catch(NoSuchMethodException ignored) {
        
      }
    }
    
    return block;
  }
}
