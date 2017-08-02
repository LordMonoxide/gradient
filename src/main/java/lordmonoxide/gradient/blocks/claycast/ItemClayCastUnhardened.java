package lordmonoxide.gradient.blocks.claycast;

import lordmonoxide.gradient.GradientGuiHandler;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientTools;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemClayCastUnhardened extends ItemBlock {
  public ItemClayCastUnhardened(final Block block) {
    super(block);
    this.setHasSubtypes(true);
  }
  
  @Override
  public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
    final ActionResult<ItemStack> result = super.onItemRightClick(world, player, hand);
    
    if(result.getType() != EnumActionResult.SUCCESS) {
      if(!world.isRemote) {
        final BlockPos pos = player.getPosition();
        player.openGui(GradientMod.instance, GradientGuiHandler.CLAY_CAST, world, pos.getX(), pos.getY(), pos.getZ());
        
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
      }
    }
    
    return result;
  }
  
  @Override
  public int getMetadata(final int damage) {
    return damage;
  }
  
  @Override
  public String getUnlocalizedName(final ItemStack stack) {
    return super.getUnlocalizedName(stack) + "." + GradientTools.TYPES.get(stack.getMetadata()).name;
  }
  
  @Override
  @SideOnly(Side.CLIENT)
  public void getSubItems(final Item item, final CreativeTabs tab, final NonNullList<ItemStack> list) {
    for(final GradientTools.Type type : GradientTools.TYPES) {
      list.add(new ItemStack(this, 1, type.id));
    }
  }
}
