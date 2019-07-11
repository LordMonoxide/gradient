package lordmonoxide.gradient;

import net.minecraft.block.BlockState;
import net.minecraft.block.LogBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class GradientTools {
  private GradientTools() { }

  private static final Map<String, Type> TYPES = new HashMap<>();

  public static final Type PICKAXE = register(GradientCasts.PICKAXE).tool(ToolType.PICKAXE)             .weapon(1.0d, 1.0d, 2).add();
  public static final Type MATTOCK = register(GradientCasts.MATTOCK).tool(ToolType.AXE, ToolType.SHOVEL).weapon(4.5d, 0.5d, 2).onItemUse(GradientTools::onMattockUse).onEntityInteract(GradientTools::onEntityInteractShear).tooltip(GradientTools::mattockTooltip).add();
  public static final Type SWORD   = register(GradientCasts.SWORD)                                      .weapon(3.0d, 1.0f, 1).add();

  public static ToolBuilder register(final GradientCasts.Cast cast) {
    return new ToolBuilder(cast);
  }

  public static Collection<Type> types() {
    return TYPES.values();
  }

  private static ActionResultType onItemUsePass(final ItemUseContext context) {
    return ActionResultType.PASS;
  }

  private static ActionResultType onMattockUse(final ItemUseContext context) {
    final IWorld world = context.getWorld();
    final BlockPos pos = context.getPos();
    final BlockState state = world.getBlockState(pos);

    // Handled in event handler; need this here to stop from placing items in offhand (see #541)
    if(state.getBlock() instanceof LogBlock) {
      return ActionResultType.SUCCESS;
    }

    return Items.STONE_HOE.onItemUse(context);
  }

  private static void noTooltip(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip) {

  }

  public static boolean onEntityInteractPass(final ItemStack itemstack, final PlayerEntity player, final LivingEntity entity, final Hand hand) {
    return false;
  }

  public static boolean onEntityInteractShear(final ItemStack itemstack, final PlayerEntity player, final LivingEntity entity, final Hand hand) {
    if(entity.world.isRemote) {
      return false;
    }

    if(entity instanceof IShearable) {
      final IShearable target = (IShearable)entity;
      final BlockPos pos = new BlockPos(entity.posX, entity.posY, entity.posZ);

      if(target.isShearable(itemstack, entity.world, pos)) {
        itemstack.damageItem(1, entity, e -> e.sendBreakAnimation(e.getActiveHand()));

        final List<ItemStack> drops = target.onSheared(itemstack, entity.world, pos, EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemstack));

        if(drops.isEmpty()) {
          return true;
        }

        final Random rand = new Random();

        // Get one random drop from the list
        final ItemStack stack = drops.get(rand.nextInt(drops.size()));

        final ItemEntity ent = entity.entityDropItem(stack, 1.0f);
        ent.setMotion(ent.getMotion().add((rand.nextFloat() - rand.nextFloat()) * 0.1f, rand.nextFloat() * 0.05f, (rand.nextFloat() - rand.nextFloat()) * 0.1f));
      }

      return true;
    }

    return false;
  }

  private static void mattockTooltip(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip) {
    tooltip.add(new TranslationTextComponent("item.gradient.stone_mattock.tooltip"));
  }

  public static class Type implements Comparable<Type> {
    private static int currentId;

    public final int id;
    public final GradientCasts.Cast cast;
    public final ToolType[] toolTypes;

    public final double attackDamage;
    public final double attackSpeed;
    public final int attackDurabilityLost;

    private final OnItemUse onItemUse;
    private final OnEntityInteract onEntityInteract;
    private final Tooltip tooltip;

    public Type(final GradientCasts.Cast cast, final ToolType[] toolTypes, final double attackDamage, final double attackSpeed, final int attackDurabilityLost, final OnItemUse onItemUse, final OnEntityInteract onEntityInteract, final Tooltip tooltip) {
      this.id = currentId++;
      this.cast = cast;
      this.toolTypes = toolTypes;

      this.attackDamage = attackDamage;
      this.attackSpeed  = attackSpeed;
      this.attackDurabilityLost = attackDurabilityLost;

      this.onItemUse = onItemUse;
      this.onEntityInteract = onEntityInteract;
      this.tooltip = tooltip;
    }

    public ActionResultType onItemUse(final ItemUseContext context) {
      return this.onItemUse.onItemUse(context);
    }

    public boolean onEntityInteract(final ItemStack itemstack, final PlayerEntity player, final LivingEntity entity, final Hand hand) {
      return this.onEntityInteract.onEntityInteract(itemstack, player, entity, hand);
    }

    public void tooltip(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip) {
      this.tooltip.tooltip(stack, world, tooltip);
    }

    @Override
    public int compareTo(final Type o) {
      assert o != null;

      return Integer.compare(this.id, o.id);
    }

    @Override
    public boolean equals(final Object o) {
      assert o instanceof Type;

      return this.id == ((Type)o).id;
    }

    @Override
    public int hashCode() {
      return this.id;
    }
  }

  @FunctionalInterface
  public interface OnItemUse {
    ActionResultType onItemUse(final ItemUseContext context);
  }

  @FunctionalInterface
  public interface OnEntityInteract {
    boolean onEntityInteract(final ItemStack itemstack, final PlayerEntity player, final LivingEntity entity, final Hand hand);
  }

  @FunctionalInterface
  public interface Tooltip {
    void tooltip(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip);
  }

  public static final class ToolBuilder {
    private final GradientCasts.Cast cast;

    private ToolType[] toolTypes = new ToolType[0];

    private double attackDamage;
    private double attackSpeed;
    private int attackDurabilityLost;

    private OnItemUse onItemUse = GradientTools::onItemUsePass;
    private OnEntityInteract onEntityInteract = GradientTools::onEntityInteractPass;
    private Tooltip tooltip = GradientTools::noTooltip;

    private ToolBuilder(final GradientCasts.Cast cast) {
      this.cast = cast;
    }

    public ToolBuilder tool(final ToolType... toolTypes) {
      this.toolTypes = toolTypes;
      return this;
    }

    public ToolBuilder weapon(final double attackDamage, final double attackSpeed, final int attackDurabilityLost) {
      this.attackDamage = attackDamage;
      this.attackSpeed  = attackSpeed;
      this.attackDurabilityLost = attackDurabilityLost;
      return this;
    }

    public ToolBuilder onItemUse(final OnItemUse onItemUse) {
      this.onItemUse = onItemUse;
      return this;
    }

    public ToolBuilder onEntityInteract(final OnEntityInteract onEntityInteract) {
      this.onEntityInteract = onEntityInteract;
      return this;
    }

    public ToolBuilder tooltip(final Tooltip tooltip) {
      this.tooltip = tooltip;
      return this;
    }

    public Type add() {
      final Type type = new Type(this.cast, this.toolTypes, this.attackDamage, this.attackSpeed, this.attackDurabilityLost, this.onItemUse, this.onEntityInteract, this.tooltip);
      TYPES.put(this.cast.name, type);
      return type;
    }
  }
}
