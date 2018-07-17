package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public final class RecipeRemover {
  private RecipeRemover() { }

  private static final Container DUMMY_CONTAINER = new Container() {
    @Override
    public boolean canInteractWith(final EntityPlayer player) {
      return true;
    }
  };

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static void remove(final RegistryEvent.Register<IRecipe> event) {
    final String[] toRemove = {
      "minecraft:wooden_pickaxe",
      "minecraft:stone_pickaxe",
      "minecraft:iron_pickaxe",
      "minecraft:golden_pickaxe",
      "minecraft:diamond_pickaxe",

      "minecraft:wooden_sword",
      "minecraft:stone_sword",
      "minecraft:iron_sword",
      "minecraft:golden_sword",
      "minecraft:diamond_sword",

      "minecraft:wooden_shovel",
      "minecraft:stone_shovel",
      "minecraft:iron_shovel",
      "minecraft:golden_shovel",
      "minecraft:diamond_shovel",

      "minecraft:wooden_hoe",
      "minecraft:stone_hoe",
      "minecraft:iron_hoe",
      "minecraft:golden_hoe",
      "minecraft:diamond_hoe",

      "minecraft:wooden_axe",
      "minecraft:stone_axe",
      "minecraft:iron_axe",
      "minecraft:golden_axe",
      "minecraft:diamond_axe",

      "minecraft:leather_helmet",
      "minecraft:leather_chestplate",
      "minecraft:leather_leggings",
      "minecraft:leather_boots",

      "minecraft:iron_block",
      "minecraft:iron_nugget",
      "minecraft:iron_ingot_from_block",
      "minecraft:iron_ingot_from_nuggets",
      "minecraft:gold_block",
      "minecraft:gold_nugget",
      "minecraft:gold_ingot_from_block",
      "minecraft:gold_ingot_from_nuggets",

      "minecraft:stick",
      "minecraft:furnace",
      "minecraft:torch",
      "minecraft:string_to_wool",

      "minecraft:sugar",
      "minecraft:bread",

      "natura:common/barley_flour",
      "natura:common/wheat_flour",
      "natura:common/bread",

      "extrautils2:teleporter",
    };

    final IForgeRegistryModifiable<IRecipe> registry = (IForgeRegistryModifiable<IRecipe>)event.getRegistry();

    for(final String loc : toRemove) {
      if(registry.remove(new ResourceLocation(loc)) == null) {
        System.out.println("Failed to remove recipe: " + loc);
      }
    }
  }

  @SubscribeEvent
  public static void replacePlankRecipes(final RegistryEvent.Register<IRecipe> event) {
    final List<IRecipe> toAdd = new ArrayList<>();
    final List<IRecipe> toRemove = new ArrayList<>();

    final IForgeRegistryModifiable<IRecipe> registry = (IForgeRegistryModifiable<IRecipe>)event.getRegistry();

    int removed = 0;

    for(final IRecipe recipe : registry) {
      final ItemStack output = recipe.getRecipeOutput();

      outerLoop:
      for(final ItemStack stackPlankBlock : OreDictionary.getOres("plankWood")) {
        if(!(stackPlankBlock.getItem() instanceof ItemBlock)) {
          continue;
        }

        final Block blockPlank = ((ItemBlock)stackPlankBlock.getItem()).getBlock();

        for(final IBlockState statePlank : blockPlank.getBlockState().getValidStates()) {
          final ItemStack stackPlank = stackPlankBlock.copy();
          stackPlank.setItemDamage(blockPlank.getMetaFromState(statePlank));

          if(output.isItemEqual(stackPlank)) {
            for(final ItemStack stackLogBlock : OreDictionary.getOres("logWood")) {
              if(!(stackLogBlock.getItem() instanceof ItemBlock)) {
                continue;
              }

              final Block blockLog = ((ItemBlock)stackLogBlock.getItem()).getBlock();

              for(final IBlockState stateLog : blockLog.getBlockState().getValidStates()) {
                final ItemStack stackLog = stackLogBlock.copy();
                stackLog.setItemDamage(blockLog.getMetaFromState(stateLog));

                final InventoryCrafting inv = new InventoryCrafting(DUMMY_CONTAINER, 2, 2);
                inv.setInventorySlotContents(0, stackLog);

                if(recipe.matches(inv, null)) {
                  toAdd.add(new ShapelessToolRecipe(
                    GradientMod.MODID,
                    new ItemStack(output.getItem(), 2, output.getMetadata()),
                    NonNullList.from(null, Ingredient.fromStacks(stackLog), new IngredientOre("toolMattock"))
                  ).setRegistryName(GradientMod.resource(output.getTranslationKey() + ".from." + stackLog.getTranslationKey() + ".with.mattock")));

                  toRemove.add(recipe);

                  removed++;

                  break outerLoop;
                }
              }
            }
          }
        }
      }
    }

    for(final IRecipe recipe : toRemove) {
      registry.remove(recipe.getRegistryName());
    }

    for(final IRecipe recipe : toAdd) {
      registry.register(recipe);
    }

    registry.register(new ShapelessToolRecipe(
      GradientMod.MODID,
      new ItemStack(Items.STICK, 2),
      NonNullList.from(null, new IngredientOre("plankWood"), new IngredientOre("toolMattock"))
    ).setRegistryName(GradientMod.resource("sticks.from.planks.with.mattock")));

    if(removed == 0) {
      System.out.println("Failed to replaced plank recipes!");
    } else {
      System.out.println("Replaced " + removed + " plank recipes!");
    }
  }
}
