package lordmonoxide.gradient.blocks;

import com.google.common.collect.ImmutableMap;
import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.bronzeboiler.BlockBronzeBoiler;
import lordmonoxide.gradient.blocks.bronzefurnace.BlockBronzeFurnace;
import lordmonoxide.gradient.blocks.bronzegrinder.BlockBronzeGrinder;
import lordmonoxide.gradient.blocks.bronzeoven.BlockBronzeOven;
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
import lordmonoxide.gradient.blocks.manualgrinder.BlockManualGrinder;
import lordmonoxide.gradient.blocks.mixingbasin.BlockMixingBasin;
import lordmonoxide.gradient.blocks.pebble.BlockPebble;
import lordmonoxide.gradient.blocks.pebble.EntityPebble;
import lordmonoxide.gradient.blocks.pebble.ItemPebble;
import lordmonoxide.gradient.blocks.torch.BlockTorchLit;
import lordmonoxide.gradient.blocks.torch.BlockTorchUnlit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class GradientBlocks {
  public static final Material MATERIAL_CLAY_MACHINE   = new Material(MapColor.BROWN);
  public static final Material MATERIAL_BRONZE_MACHINE = new Material(MapColor.GOLD);

  public static final BlockOreMagnesium ORE_MAGNESIUM = RegistrationHandler.register(new BlockOreMagnesium());

  public static final BlockPebble PEBBLE;

  static {
    final BlockPebble block = new BlockPebble();
    PEBBLE = RegistrationHandler.register(block, new ItemPebble(block));
  }

  public static final BlockFirePit FIRE_PIT = RegistrationHandler.register(new BlockFirePit());
  public static final BlockTorchLit FIBRE_TORCH_LIT = RegistrationHandler.register(new BlockTorchLit("fibre_torch_lit", 0.67f));
  public static final BlockTorchUnlit FIBRE_TORCH_UNLIT = RegistrationHandler.register(new BlockTorchUnlit("fibre_torch_unlit", FIBRE_TORCH_LIT));

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

  public static final BlockBronzeMachineHull BRONZE_MACHINE_HULL = RegistrationHandler.register(new BlockBronzeMachineHull());
  public static final BlockBronzeFurnace     BRONZE_FURNACE      = RegistrationHandler.register(new BlockBronzeFurnace());
  public static final BlockBronzeBoiler      BRONZE_BOILER       = RegistrationHandler.register(new BlockBronzeBoiler());
  public static final BlockBronzeOven        BRONZE_OVEN         = RegistrationHandler.register(new BlockBronzeOven());
  public static final BlockBronzeGrinder     BRONZE_GRINDER      = RegistrationHandler.register(new BlockBronzeGrinder());

  public static final Map<GradientMetals.Metal, Block> CAST_BLOCK;

  static {
    final Map<GradientMetals.Metal, Block> map = new HashMap<>();
    map.put(GradientMetals.IRON, Blocks.IRON_BLOCK);
    map.put(GradientMetals.GOLD, Blocks.GOLD_BLOCK);
    map.put(GradientMetals.GLASS, Blocks.GLASS);

    for(final GradientMetals.Metal metal : GradientMetals.metals) {
      if(!map.containsKey(metal)) {
        map.put(metal, RegistrationHandler.register(new CastBlock(metal)));
      }
    }

    CAST_BLOCK = ImmutableMap.copyOf(map);
  }

  private GradientBlocks() { }

  @Mod.EventBusSubscriber(modid = GradientMod.MODID)
  public static class RegistrationHandler {
    private static final Map<Block, ItemBlock> blocks = new LinkedHashMap<>();

    public static final List<ItemBlock> ITEM_BLOCKS = new ArrayList<>();

    private RegistrationHandler() { }

    private static <T extends Block> T register(final T block) {
      if(block instanceof ItemBlockProvider) {
        return register(block, ((ItemBlockProvider)block).getItemBlock((Block & ItemBlockProvider)block));
      }

      return register(block, new ItemBlock(block));
    }

    private static <T extends Block> T register(final T block, final ItemBlock item) {
      blocks.put(block, item);
      return block;
    }

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
      GradientMod.logger.info("Registering blocks");

      RegistrationHandler.register(new BlockManualGrinder());
      RegistrationHandler.register(new BlockMixingBasin());

      // Trigger block registration
      new GradientBlocks();

      blocks.keySet().forEach(event.getRegistry()::register);
    }

    @SubscribeEvent
    public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
      GradientMod.logger.info("Registering item blocks");

      blocks.forEach((block, item) -> {
        item.setRegistryName(item.getBlock().getRegistryName());
        event.getRegistry().register(item);
        ITEM_BLOCKS.add(item);
      });

      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ItemBlock.getItemFromBlock(PEBBLE), new BehaviorProjectileDispense() {
        @Override
        protected IProjectile getProjectileEntity(final World world, final IPosition position, final ItemStack stack) {
          return new EntityPebble(world, position.getX(), position.getY(), position.getZ());
        }
      });

      registerTileEntities();
    }

    private static void registerTileEntities() {
      for(final Block block : blocks.keySet()) {
        if(block.hasTileEntity()) {
          try {
            //noinspection unchecked
            GameRegistry.registerTileEntity((Class<? extends TileEntity>)block.getClass().getMethod("createTileEntity", World.class, IBlockState.class).getReturnType(), block.getRegistryName());
          } catch(final NoSuchMethodException ignored) { }
        }
      }
    }
  }
}
