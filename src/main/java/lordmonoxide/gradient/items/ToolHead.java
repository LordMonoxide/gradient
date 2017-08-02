package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientTools;
import lordmonoxide.gradient.ModelManager;
import lordmonoxide.gradient.blocks.claycast.ItemClayCast;
import lordmonoxide.gradient.recipes.GradientCraftable;
import lordmonoxide.gradient.recipes.ShapelessMetaAwareRecipe;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

public class ToolHead extends GradientItem implements GradientCraftable, ModelManager.CustomModel {
  public ToolHead() {
    super("tool_head", CreativeTabs.TOOLS);
    this.setHasSubtypes(true);
  }
  
  public static ItemStack getToolHead(final GradientTools.Type type, final GradientMetals.Metal metal) {
    return getToolHead(type, metal, 1);
  }
  
  public static ItemStack getToolHead(final GradientTools.Type type, final GradientMetals.Metal metal, final int amount) {
    final NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("type", type.id);
    tag.setString("metal", metal.name);
    
    final ItemStack stack = new ItemStack(GradientItems.TOOL_HEAD, amount);
    stack.setTagCompound(tag);
    return stack;
  }
  
  public GradientTools.Type getType(final ItemStack stack) {
    if(!stack.hasTagCompound()) {
      return GradientTools.PICKAXE;
    }
    
    return GradientTools.TYPES.get(stack.getTagCompound().getInteger("type"));
  }
  
  public GradientMetals.Metal getMetal(final ItemStack stack) {
    if(!stack.hasTagCompound()) {
      return GradientMetals.INVALID_METAL;
    }
    
    return GradientMetals.instance.getMetal(stack.getTagCompound().getString("metal"));
  }
  
  @Override
  public void addRecipe() {
    final NonNullList<ItemStack> stacks = NonNullList.create();
    this.getSubItems(this, this.getCreativeTab(), stacks);
    
    for(final ItemStack stack : stacks) {
      final GradientTools.Type type = this.getType(stack);
      final GradientMetals.Metal metal = this.getMetal(stack);
      
      GameRegistry.addRecipe(new ShapelessMetaAwareRecipe(
        stack,
        GradientMetals.getBucket(metal),
        ItemClayCast.getCast(type)
      ));
    }
  }
  
  @Override
  public int getMetadata(final int metadata) {
    return metadata;
  }
  
  @Override
  public String getUnlocalizedName(final ItemStack stack) {
    return super.getUnlocalizedName() + '.' + this.getType(stack).name + '.' + this.getMetal(stack).name;
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(final Item item, final CreativeTabs tab, final NonNullList<ItemStack> list) {
    for(final GradientTools.Type type : GradientTools.TYPES) {
      for(final GradientMetals.Metal metal : GradientMetals.instance.metals) {
        list.add(getToolHead(type, metal));
      }
    }
  }
  
  @Override
  public void registerCustomModels() {
    final NonNullList<ItemStack> stacks = NonNullList.create();
    this.getSubItems(this, this.getCreativeTab(), stacks);
    
    final Map<String, ModelResourceLocation> lookup = new HashMap<>();
    
    for(final ItemStack stack : stacks) {
      final GradientTools.Type type = this.getType(stack);
      final GradientMetals.Metal metal = this.getMetal(stack);
      
      lookup.put(type.name + "." + metal.name, new ModelResourceLocation(new ResourceLocation(GradientMod.MODID, this.getUnlocalizedName(stack).substring(5)), "inventory"));
    }
    
    ModelBakery.registerItemVariants(this, lookup.values().toArray(new ModelResourceLocation[lookup.size()]));
    ModelLoader.setCustomMeshDefinition(this, stack -> lookup.get(this.getType(stack).name + "." + this.getMetal(stack).name));
  }
}
