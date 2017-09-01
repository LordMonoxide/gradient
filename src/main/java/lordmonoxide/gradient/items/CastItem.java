package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.ModelManager;
import lordmonoxide.gradient.blocks.claycast.ItemClayCast;
import lordmonoxide.gradient.GradientCasts;
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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CastItem extends GradientItem implements GradientCraftable, ModelManager.CustomModel {
  public CastItem() {
    super("cast_item", CreativeTabs.TOOLS);
    this.setHasSubtypes(true);
  }
  
  public static ItemStack getCastItem(final GradientCasts.Cast cast, final GradientMetals.Metal metal) {
    return getCastItem(cast, metal, 1);
  }
  
  public static ItemStack getCastItem(final GradientCasts.Cast cast, final GradientMetals.Metal metal, final int amount) {
    final NBTTagCompound tag = new NBTTagCompound();
    tag.setInteger("cast", cast.id);
    tag.setString("metal", metal.name);
    
    final ItemStack stack = new ItemStack(GradientItems.CAST_ITEM, amount);
    stack.setTagCompound(tag);
    return stack;
  }
  
  public GradientCasts.Cast getCast(final ItemStack stack) {
    if(!stack.hasTagCompound()) {
      return GradientCasts.PICKAXE;
    }
    
    return GradientCasts.CASTS.get(stack.getTagCompound().getInteger("cast"));
  }
  
  public GradientMetals.Metal getMetal(final ItemStack stack) {
    if(!stack.hasTagCompound()) {
      return GradientMetals.INVALID_METAL;
    }
    
    return GradientMetals.getMetal(stack.getTagCompound().getString("metal"));
  }
  
  @Override
  public void addRecipe() {
    //TODO
    /*
    for(final GradientCasts.Cast cast : GradientCasts.CASTS) {
      for(final GradientMetals.Metal metal : GradientMetals.metals) {
        if(!cast.tool || metal.canMakeTools) {
          int amount = cast.amount / Fluid.BUCKET_VOLUME;
          
          final Object[] parts = new Object[amount + 1];
          parts[0] = ItemClayCast.getCast(cast);
          Arrays.fill(parts, 1, amount + 1, GradientMetals.getBucket(metal));
          
          final ItemStack override = cast.itemOverride.get(metal);
          
          GameRegistry.addRecipe(new ShapelessMetaAwareRecipe(
            override == null ? getCastItem(cast, metal) : override,
            parts
          ));
        }
      }
    }
    */
  }
  
  @Override
  public int getMetadata(final int metadata) {
    return metadata;
  }
  
  @Override
  public String getUnlocalizedName(final ItemStack stack) {
    return super.getUnlocalizedName() + '.' + this.getCast(stack).name + '.' + this.getMetal(stack).name;
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> list) {
    for(final GradientCasts.Cast cast : GradientCasts.CASTS) {
      for(final GradientMetals.Metal metal : GradientMetals.metals) {
        if((!cast.tool || metal.canMakeTools) && cast.itemOverride.get(metal) == null) {
          list.add(getCastItem(cast, metal));
        }
      }
    }
  }
  
  @Override
  public void registerCustomModels() {
    final NonNullList<ItemStack> stacks = NonNullList.create();
    this.getSubItems(this.getCreativeTab(), stacks);
    
    final Map<String, ModelResourceLocation> lookup = new HashMap<>();
    
    for(final ItemStack stack : stacks) {
      final GradientCasts.Cast cast = this.getCast(stack);
      final GradientMetals.Metal metal = this.getMetal(stack);
      
      lookup.put(cast.name + "." + metal.name, new ModelResourceLocation(new ResourceLocation(GradientMod.MODID, this.getUnlocalizedName(stack).substring(5)), "inventory"));
    }
    
    ModelBakery.registerItemVariants(this, lookup.values().toArray(new ModelResourceLocation[lookup.size()]));
    ModelLoader.setCustomMeshDefinition(this, stack -> lookup.get(this.getCast(stack).name + "." + this.getMetal(stack).name));
  }
}
