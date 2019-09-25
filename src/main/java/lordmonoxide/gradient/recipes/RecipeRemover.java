package lordmonoxide.gradient.recipes;

import ic2.api.item.IC2Items;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.progress.Age;
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
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
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

      "minecraft:bone_meal_from_bone",
      "minecraft:bone_meal_from_block",
      "minecraft:bone_block",
      "minecraft:sugar",
      "minecraft:bread",
      "minecraft:leather",

      "natura:common/barley_flour",
      "natura:common/wheat_flour",
      "natura:common/bread",

      "extrautils2:teleporter",
      "extrautils2:golden_lasso",

      "buildcraftcore:gear_wood",
    };

    final IForgeRegistryModifiable<IRecipe> registry = (IForgeRegistryModifiable<IRecipe>)event.getRegistry();

    for(final String loc : toRemove) {
      if(registry.remove(new ResourceLocation(loc)) == null) {
        GradientMod.logger.warn("Failed to remove recipe: {}", loc);
      }
    }
  }

  //GOTCHA: This can't use the recipe registry event because Forestry doesn't register its recipes properly
  public static void replacePlankRecipes(final IForgeRegistryModifiable<IRecipe> registry) {
    final List<ItemStack> planks = new ArrayList<>();

    for(final ItemStack stackPlankBlock : OreDictionary.getOres("plankWood")) {
      if(!(stackPlankBlock.getItem() instanceof ItemBlock)) {
        continue;
      }

      final Block blockPlank = ((ItemBlock)stackPlankBlock.getItem()).getBlock();

      for(final IBlockState statePlank : blockPlank.getBlockState().getValidStates()) {
        final ItemStack stackPlank = stackPlankBlock.copy();
        stackPlank.setItemDamage(blockPlank.getMetaFromState(statePlank));
        planks.add(stackPlank);
      }
    }

    final List<ItemStack> logs = new ArrayList<>();

    for(final ItemStack stackLogBlock : OreDictionary.getOres("logWood")) {
      if(!(stackLogBlock.getItem() instanceof ItemBlock)) {
        continue;
      }

      final Block blockLog = ((ItemBlock)stackLogBlock.getItem()).getBlock();

      for(final IBlockState stateLog : blockLog.getBlockState().getValidStates()) {
        final ItemStack stackLog = stackLogBlock.copy();
        stackLog.setItemDamage(blockLog.getMetaFromState(stateLog));
        logs.add(stackLog);
      }
    }

    final InventoryCrafting inv = new InventoryCrafting(DUMMY_CONTAINER, 2, 2);

    final List<IRecipe> toAdd = new ArrayList<>();
    final List<IRecipe> toRemove = new ArrayList<>();

    for(final IRecipe recipe : registry) {
      final ItemStack output = recipe.getRecipeOutput();

      outerLoop:
      for(final ItemStack stackPlank : planks) {
        if(output.isItemEqual(stackPlank)) {
          for(final ItemStack stackLog : logs) {
            inv.setInventorySlotContents(0, stackLog);

            if(recipe.matches(inv, null)) {
              toAdd.add(new AgeGatedShapelessToolRecipe(
                GradientMod.MODID,
                Age.AGE1,
                new ItemStack(output.getItem(), 2, output.getMetadata()),
                NonNullList.from(Ingredient.EMPTY, Ingredient.fromStacks(stackLog), new IngredientOre("toolAxe"))
              ).setRegistryName(GradientMod.resource(output.getTranslationKey() + '@' + output.getMetadata() + ".from." + stackLog.getTranslationKey() + '@' + stackLog.getMetadata() + ".with.axe")));

              if(Loader.isModLoaded("ic2")) {
                addIc2PlanksRecipe(toAdd, stackLog, output);
              }

              toRemove.add(recipe);

              break outerLoop;
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

    registry.register(new AgeGatedShapelessToolRecipe(
      GradientMod.MODID,
      Age.AGE1,
      new ItemStack(Items.STICK, 2),
      NonNullList.from(Ingredient.EMPTY, new IngredientOre("plankWood"), new IngredientOre("toolAxe"))
    ).setRegistryName(GradientMod.resource("sticks.from.planks.with.axe")));

    if(Loader.isModLoaded("ic2")) {
      addIc2SticksRecipe(registry);
    }

    if(toRemove.isEmpty()) {
      GradientMod.logger.warn("Failed to replace plank recipes!");
    } else {
      GradientMod.logger.info("Replaced {} plank recipes!", toRemove.size());
    }
  }

  @Optional.Method(modid = "ic2")
  private static void addIc2PlanksRecipe(final List<IRecipe> toAdd, final ItemStack stackLog, final ItemStack output) {
    final ItemStack chainsaw = new ItemStack(IC2Items.getItem("chainsaw").getItem(), 1, OreDictionary.WILDCARD_VALUE);

    toAdd.add(new AgeGatedShapelessToolRecipe(
      GradientMod.MODID,
      Age.AGE1,
      new ItemStack(output.getItem(), 2, output.getMetadata()),
      NonNullList.from(Ingredient.EMPTY, Ingredient.fromStacks(stackLog), Ingredient.fromStacks(chainsaw))
    ).setRegistryName(GradientMod.resource(output.getTranslationKey() + '@' + output.getMetadata() + ".from." + stackLog.getTranslationKey() + '@' + stackLog.getMetadata() + ".with.chainsaw")));
  }

  @Optional.Method(modid = "ic2")
  private static void addIc2SticksRecipe(final IForgeRegistry<IRecipe> registry) {
    final ItemStack chainsaw = new ItemStack(IC2Items.getItem("chainsaw").getItem(), 1, OreDictionary.WILDCARD_VALUE);

    registry.register(new AgeGatedShapelessToolRecipe(
      GradientMod.MODID,
      Age.AGE1,
      new ItemStack(Items.STICK, 2),
      NonNullList.from(Ingredient.EMPTY, new IngredientOre("plankWood"), Ingredient.fromStacks(chainsaw))
    ).setRegistryName(GradientMod.resource("sticks.from.planks.with.chainsaw")));
  }
}
