package lordmonoxide.gradient.items;

import lordmonoxide.gradient.items.armour.ClothPants;
import lordmonoxide.gradient.items.armour.ClothShirt;
import lordmonoxide.gradient.items.armour.GradientArmour;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public final class GradientItems {
  private static final List<Item> items = new ArrayList<>();
  private static final List<GradientItemCraftable> craftables = new ArrayList<>();
  
  public static final ItemArmor.ArmorMaterial MATERIAL_CLOTH = EnumHelper.addArmorMaterial("cloth", "cloth", 5, new int[] {0, 1, 2, 0}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0f);
  
  public static final GradientItem FIBRE = register(new Fibre());
  public static final GradientItem CLOTH = register(new Cloth());
  
  public static final GradientItemTool BONE_NEEDLE = register(new BoneNeedle());
  
  public static final GradientItem     FIRE_STARTER  = register(new FireStarter());
  public static final GradientItemTool STONE_HAMMER  = register(new StoneHammer());
  public static final GradientItemTool STONE_MATTOCK = register(new StoneMattock());
  public static final GradientItemTool FLINT_KNIFE   = register(new FlintKnife());
  
  public static final GradientArmour CLOTH_SHIRT = register(new ClothShirt());
  public static final GradientArmour CLOTH_PANTS = register(new ClothPants());
  
  static {
    OreDictionary.registerOre("coal", Items.COAL);
    
    OreDictionary.registerOre("string", FIBRE);
    OreDictionary.registerOre("cloth",  CLOTH);
    
    OreDictionary.registerOre("needle", BONE_NEEDLE.getWildcardItemStack());
    OreDictionary.registerOre("toolHammer", STONE_HAMMER.getWildcardItemStack());
    OreDictionary.registerOre("toolMattock", STONE_MATTOCK.getWildcardItemStack());
    
    MATERIAL_CLOTH.setRepairItem(CLOTH.getDefaultInstance());
  }
  
  private GradientItems() { }
  
  @SideOnly(Side.CLIENT)
  public static void addModels() {
    for(Item item : items) {
      if(!item.getHasSubtypes()) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory")); //$NON-NLS-1$
      } else {
        NonNullList<ItemStack> stacks = NonNullList.create();
        item.getSubItems(item, null, stacks);
        
        if(item instanceof ItemBlock) {
          for(ItemStack stack : stacks) {
            ModelLoader.setCustomModelResourceLocation(item, stack.getMetadata(), new ModelResourceLocation(item.getRegistryName(), "inventory")); //$NON-NLS-1$
          }
        } else {
          for(ItemStack stack : stacks) {
            ModelLoader.setCustomModelResourceLocation(item, stack.getMetadata(), new ModelResourceLocation(item.getUnlocalizedName(stack).substring(5), "inventory")); //$NON-NLS-1$
          }
        }
      }
    }
  }
  
  public static void addRecipes() {
    for(GradientItemCraftable craftable : craftables) {
      craftable.addRecipe();
    }
  }
  
  private static <T extends Item> T register(T item) {
    items.add(item);
    GameRegistry.register(item);
    
    if(item instanceof GradientItemCraftable) {
      craftables.add((GradientItemCraftable)item);
    }
    
    return item;
  }
}
