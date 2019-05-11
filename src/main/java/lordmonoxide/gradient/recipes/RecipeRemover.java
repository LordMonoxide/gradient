package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.GradientMod;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GradientMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class RecipeRemover {
  private RecipeRemover() { }

  //TODO
/*
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

              toRemove.add(recipe);

              break outerLoop;
            }
          }
        }
      }
    }

    for(final IRecipe recipe : toRemove) {
      registry.remove(recipe.getId());
    }

    for(final IRecipe recipe : toAdd) {
      registry.register(recipe);
    }

    registry.register(new AgeGatedShapelessToolRecipe(
      GradientMod.resource("sticks.from.planks.with.axe"),
      GradientMod.MODID,
      Age.AGE1,
      new ItemStack(Items.STICK, 2),
      NonNullList.from(Ingredient.EMPTY, new IngredientOre("plankWood"), new IngredientOre("toolAxe"))
    ));

    if(toRemove.isEmpty()) {
      GradientMod.logger.warn("Failed to replace plank recipes!");
    } else {
      GradientMod.logger.info("Replaced {} plank recipes!", toRemove.size());
    }
  }
*/
}
