package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMetals;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientTools;
import lordmonoxide.gradient.ModelManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

public class Tool extends GradientItemTool implements ModelManager.CustomModel {
  public Tool() {
    super("tool", 0, 0, 0);
    this.setHasSubtypes(true);
  }
  
  public static ItemStack getTool(final GradientTools.Type type, final GradientMetals.Metal metal) {
    return getTool(type, metal, 1);
  }
  
  public static ItemStack getTool(final GradientTools.Type type, final GradientMetals.Metal metal, final int amount) {
    final NBTTagCompound tag = new NBTTagCompound();
    tag.setString("type", type.cast.name);
    tag.setString("metal", metal.name);
    
    final ItemStack stack = new ItemStack(GradientItems.TOOL, amount);
    stack.setTagCompound(tag);
    return stack;
  }
  
  public static ItemStack getTool(final GradientTools.Type type, final GradientMetals.Metal metal, final int amount, final int damage) {
    final NBTTagCompound tag = new NBTTagCompound();
    tag.setString("type", type.cast.name);
    tag.setString("metal", metal.name);
    
    final ItemStack stack = new ItemStack(GradientItems.TOOL, amount, damage);
    stack.setTagCompound(tag);
    return stack;
  }
  
  public static GradientTools.Type getType(final ItemStack stack) {
    if(!stack.hasTagCompound()) {
      return GradientTools.PICKAXE;
    }
    
    return GradientTools.getType(stack.getTagCompound().getString("type"));
  }
  
  public static GradientMetals.Metal getMetal(final ItemStack stack) {
    if(!stack.hasTagCompound()) {
      return GradientMetals.INVALID_METAL;
    }
    
    return GradientMetals.getMetal(stack.getTagCompound().getString("metal"));
  }
  
  @Override
  public EnumActionResult onItemUse(final EntityPlayer player, final World world, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
    return getType(player.getHeldItem(hand)).onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
  }
  
  @Override
  protected double getAttackDamage(final ItemStack stack) {
    return getType(stack).attackDamage * getMetal(stack).attackDamageMultiplier;
  }
  
  @Override
  protected double getAttackSpeed(final ItemStack stack) {
    return -4 + getType(stack).attackSpeed * getMetal(stack).attackSpeedMultiplier;
  }
  
  @Deprecated
  public int getMaxDamage(final ItemStack stack) {
    return getMetal(stack).durability - 1;
  }
  
  public Set<String> getToolClasses(final ItemStack stack) {
    final Set<String> set = new HashSet<>();
    Collections.addAll(set, getType(stack).toolClass);
    return set;
  }
  
  public int getHarvestLevel(final ItemStack stack, final String toolClass, @Nullable final EntityPlayer player, @Nullable final IBlockState blockState) {
    if(Arrays.stream(getType(stack).toolClass).anyMatch(toolClass::equals)) {
      return getMetal(stack).harvestLevel;
    }
    
    return -1;
  }
  
  @Override
  public float getDestroySpeed(ItemStack stack, IBlockState state) {
    return this.canHarvestBlock(state, stack) ? getMetal(stack).harvestSpeed : 0.0f;
  }
  
  @Override
  public String getUnlocalizedName(final ItemStack stack) {
    return super.getUnlocalizedName() + '.' + getType(stack).cast.name + '.' + getMetal(stack).name;
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> list) {
    for(final GradientTools.Type type : GradientTools.types()) {
      for(final GradientMetals.Metal metal : GradientMetals.metals) {
        if(metal.canMakeTools) {
          list.add(getTool(type, metal));
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
      final GradientTools.Type type = getType(stack);
      final GradientMetals.Metal metal = getMetal(stack);
      
      lookup.put(type.cast.name + "." + metal.name, new ModelResourceLocation(new ResourceLocation(GradientMod.MODID, this.getUnlocalizedName(stack).substring(5)), "inventory"));
    }
    
    ModelBakery.registerItemVariants(this, lookup.values().toArray(new ModelResourceLocation[lookup.size()]));
    ModelLoader.setCustomMeshDefinition(this, stack -> lookup.get(getType(stack).cast.name + "." + getMetal(stack).name));
  }
}
