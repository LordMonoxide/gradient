package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.bronzeboiler.BlockBronzeBoiler;
import lordmonoxide.gradient.blocks.claybowl.BlockClayBowl;
import lordmonoxide.gradient.blocks.claybucket.BlockClayBucket;
import lordmonoxide.gradient.blocks.claycast.BlockClayCast;
import lordmonoxide.gradient.blocks.claycast.BlockClayCastUnhardened;
import lordmonoxide.gradient.blocks.claycast.ItemClayCast;
import lordmonoxide.gradient.blocks.claycast.ItemClayCastUnhardened;
import lordmonoxide.gradient.blocks.claycrucible.BlockClayCrucible;
import lordmonoxide.gradient.blocks.claycrucible.BlockClayCrucibleUnhardened;
import lordmonoxide.gradient.blocks.clayfurnace.BlockClayFurnace;
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

import java.util.*;

public final class GradientBlocks {
  public static final Material       MATERIAL_CLAY_MACHINE = new Material(MapColor.BROWN);
  public static final MaterialLiquid MATERIAL_LIQUID_METAL = new MaterialLiquid(MapColor.GRAY);
  public static final Material       MATERIAL_BRONZE_MACHINE = new Material(MapColor.GOLD);
  
  public static final BlockOreMagnesium ORE_MAGNESIUM = RegistrationHandler.register(new BlockOreMagnesium());
  
  public static final BlockPebble PEBBLE = RegistrationHandler.register(new BlockPebble());
  
  public static final BlockFirePit FIRE_PIT = RegistrationHandler.register(new BlockFirePit());
  
  public static final BlockClayCrucibleUnhardened CLAY_CRUCIBLE_UNHARDENED = RegistrationHandler.register(new BlockClayCrucibleUnhardened());
  public static final BlockClayCastUnhardened     CLAY_CAST_UNHARDENED;
  
  static {
    final BlockClayCastUnhardened block = new BlockClayCastUnhardened();
    CLAY_CAST_UNHARDENED = RegistrationHandler.register(block, new ItemClayCastUnhardened(block));
  }
  
  public static final BlockClayBucket   CLAY_BUCKET   = RegistrationHandler.register(new BlockClayBucket());
  public static final BlockClayFurnace  CLAY_FURNACE  = RegistrationHandler.register(new BlockClayFurnace());
  public static final BlockClayCrucible CLAY_CRUCIBLE = RegistrationHandler.register(new BlockClayCrucible());
  public static final BlockClayCast     CLAY_CAST;
  
  static {
    final BlockClayCast block = new BlockClayCast();
    CLAY_CAST = RegistrationHandler.register(block, new ItemClayCast(block));
  }
  
  public static final BlockClayBowl CLAY_BOWL = RegistrationHandler.register(new BlockClayBowl());
  
  public static final BlockBronzeBoiler BRONZE_BOILER = RegistrationHandler.register(new BlockBronzeBoiler());
  
  private GradientBlocks() {
    
  }
  
  @Mod.EventBusSubscriber(modid = GradientMod.MODID)
  public static class RegistrationHandler {
    private static final Map<GradientBlock, ItemBlock> blocks = new HashMap<>();
    
    public static final Set<ItemBlock> ITEM_BLOCKS = new HashSet<>();
    
    private static <T extends GradientBlock> T register(final T block) {
      if(block instanceof ItemBlockProvider) {
        return register(block, ((ItemBlockProvider)block).getItemBlock((Block & ItemBlockProvider)block));
      }
      
      return register(block, new ItemBlock(block));
    }
    
    private static <T extends GradientBlock> T register(final T block, final ItemBlock item) {
      blocks.put(block, item);
      return block;
    }
    
    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
      System.out.println("Registering blocks");
      
      // Trigger block registration
      new GradientBlocks();
      
      blocks.keySet().forEach(event.getRegistry()::register);
    }
    
    @SubscribeEvent
    public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
      System.out.println("Registering item blocks");
      
      blocks.forEach((block, item) -> {
        item.setRegistryName(item.getBlock().getRegistryName());
        event.getRegistry().register(item);
        ITEM_BLOCKS.add(item);
      });
      
      registerTileEntities();
    }
    
    private static void registerTileEntities() {
      for(final GradientBlock block : blocks.keySet()) {
        if(block instanceof ITileEntityProvider) {
          try {
            //noinspection unchecked
            GameRegistry.registerTileEntity((Class<? extends TileEntity>)((ITileEntityProvider)block).getClass().getMethod("createNewTileEntity", World.class, int.class).getReturnType(), block.getRegistryName().toString());
          } catch(final NoSuchMethodException ignored) { }
        }
      }
    }
  }
}
