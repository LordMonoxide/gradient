package lordmonoxide.gradient.items;

import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class StoneMattock extends GradientItemWorldTool {
  private Set<ToolType> toolTypes = new HashSet<>();

  public StoneMattock() {
    super("stone_mattock", 0.5f, -2.4f, 4, 2, new Properties().defaultMaxDamage(50));
    this.toolTypes.add(ToolType.AXE);
    this.toolTypes.add(ToolType.SHOVEL);
  }

  @Override
  public void addInformation(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip, final ITooltipFlag flagIn) {
    super.addInformation(stack, world, tooltip, flagIn);
    tooltip.add(new TextComponentTranslation("item.stone_mattock.tooltip"));
  }

  @Override
  public EnumActionResult onItemUse(final ItemUseContext context) {
    final IWorld world = context.getWorld();
    final BlockPos pos = context.getPos();
    final IBlockState state = world.getBlockState(pos);

    // Handled in event handler; need this here to stop from placing items in offhand (see #541)
    if(state.getBlock() instanceof BlockLog) {
      return EnumActionResult.SUCCESS;
    }

    return Items.STONE_HOE.onItemUse(context);
  }

  @Override
  public boolean itemInteractionForEntity(final ItemStack itemstack, final EntityPlayer player, final EntityLivingBase entity, final EnumHand hand) {
    if(entity.world.isRemote) {
      return false;
    }

    if(entity instanceof IShearable) {
      final IShearable target = (IShearable)entity;
      final BlockPos pos = new BlockPos(entity.posX, entity.posY, entity.posZ);

      if(target.isShearable(itemstack, entity.world, pos)) {
        itemstack.damageItem(1, entity);

        final List<ItemStack> drops = target.onSheared(itemstack, entity.world, pos, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemstack));

        if(drops.isEmpty()) {
          return true;
        }

        final Random rand = new Random();

        // Get one random drop from the list
        final ItemStack stack = drops.get(rand.nextInt(drops.size()));

        final EntityItem ent = entity.entityDropItem(stack, 1.0f);
        ent.motionY += rand.nextFloat() * 0.05F;
        ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1f;
        ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1f;
      }

      return true;
    }

    return false;
  }

  @Override
  public Set<ToolType> getToolTypes(final ItemStack stack) {
    return this.toolTypes;
  }

  @Override
  public int getHarvestLevel(final ItemStack stack, final ToolType toolType, @Nullable final EntityPlayer player, @Nullable final IBlockState blockState) {
    if(!this.toolTypes.contains(toolType)) {
      return -1;
    }

    return 0;
  }
}
