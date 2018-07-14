package lordmonoxide.gradient.items;

import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.claybowl.BlockClayBowl;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MushroomStew extends ItemSoup {
  public MushroomStew() {
    super(6);
    this.setTranslationKey("mushroom_stew");
    this.setRegistryName("mushroom_stew");
    this.setCreativeTab(CreativeTabs.FOOD);
  }

  @Override
  public ItemStack onItemUseFinish(final ItemStack stack, final World world, final EntityLivingBase entityLiving) {
    super.onItemUseFinish(stack, world, entityLiving);

    final Block bowl = GradientBlocks.CLAY_BOWL;
    return new ItemStack(bowl, 1, bowl.getMetaFromState(bowl.getDefaultState().withProperty(BlockClayBowl.HARDENED, true)));
  }
}
