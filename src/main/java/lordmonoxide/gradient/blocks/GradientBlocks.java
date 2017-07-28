package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.claybucket.BlockClayBucket;
import lordmonoxide.gradient.blocks.claybucket.BlockClayBucketUnhardened;
import lordmonoxide.gradient.blocks.claycrucible.BlockClayCrucible;
import lordmonoxide.gradient.blocks.claycrucible.BlockClayCrucibleUnhardened;
import lordmonoxide.gradient.blocks.clayfurnace.BlockClayFurnace;
import lordmonoxide.gradient.blocks.clayfurnace.BlockClayFurnaceUnhardened;
import lordmonoxide.gradient.blocks.firepit.BlockFirePit;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class GradientBlocks {
  public static final Material       MATERIAL_CLAY_MACHINE = new Material(MapColor.BROWN);
  public static final MaterialLiquid MATERIAL_LIQUID_METAL = new MaterialLiquid(MapColor.GRAY);
  
  public static final BlockPebble PEBBLE = RegistrationHandler.register(new BlockPebble());
  
  public static final BlockFirePit FIRE_PIT = RegistrationHandler.register(new BlockFirePit());
  
  public static final BlockClayBucketUnhardened   CLAY_BUCKET_UNHARDENED   = RegistrationHandler.register(new BlockClayBucketUnhardened());
  public static final BlockClayFurnaceUnhardened  CLAY_FURNACE_UNHARDENED  = RegistrationHandler.register(new BlockClayFurnaceUnhardened());
  public static final BlockClayCrucibleUnhardened CLAY_CRUCIBLE_UNHARDENED = RegistrationHandler.register(new BlockClayCrucibleUnhardened());
  
  public static final BlockClayBucket   CLAY_BUCKET   = RegistrationHandler.register(new BlockClayBucket());
  public static final BlockClayFurnace  CLAY_FURNACE  = RegistrationHandler.register(new BlockClayFurnace());
  public static final BlockClayCrucible CLAY_CRUCIBLE = RegistrationHandler.register(new BlockClayCrucible());
  
  private GradientBlocks() {
    
  }
  
  @Mod.EventBusSubscriber(modid = GradientMod.MODID)
  public static class RegistrationHandler {
    private static final List<GradientBlock> blocks = new ArrayList<>();
    private static final List<GradientBlockCraftable> craftables = new ArrayList<>();
    
    public static final Set<ItemBlock> ITEM_BLOCKS = new HashSet<>();
    
    private static <T extends GradientBlock> T register(final T block) {
      blocks.add(block);
  
      if(block instanceof GradientBlockCraftable) {
        craftables.add((GradientBlockCraftable)block);
      }
      
      return block;
    }
    
    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
      System.out.println("Registering blocks");
      
      // Trigger block registration
      new GradientBlocks();
      
      blocks.forEach(event.getRegistry()::register);
    }
    
    @SubscribeEvent
    public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
      System.out.println("Registering item blocks");
      
      blocks.stream().map(ItemBlock::new).forEach(item -> {
        item.setRegistryName(item.block.getRegistryName());
        event.getRegistry().register(item);
        ITEM_BLOCKS.add(item);
      });
      
      registerTileEntities();
    }
    
    private static void registerTileEntities() {
      for(final GradientBlock block : blocks) {
        if(block instanceof ITileEntityProvider) {
          try {
            //noinspection unchecked
            GameRegistry.registerTileEntity((Class<? extends TileEntity>)((ITileEntityProvider)block).getClass().getMethod("createNewTileEntity", World.class, int.class).getReturnType(), block.getRegistryName().toString());
          } catch(final NoSuchMethodException ignored) { }
        }
      }
    }
    
    public static void addRecipes() {
      craftables.forEach(GradientBlockCraftable::addRecipe);
    }
  }
}
