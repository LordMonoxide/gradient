package lordmonoxide.gradient;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GradientTools {
  private GradientTools() { }

  private static final Map<String, Type> TYPES = new HashMap<>();

  public static final Type PICKAXE = register(GradientCasts.PICKAXE).tool("pickaxe")      .weapon(1.0d, 1.0d, 2).add();
  public static final Type MATTOCK = register(GradientCasts.MATTOCK).tool("axe", "shovel").weapon(4.5d, 0.5d, 2).onItemUse(GradientTools::onMattockUse).tooltip(GradientTools::mattockTooltip).add();
  public static final Type SWORD   = register(GradientCasts.SWORD)  .tool("sword")        .weapon(3.0d, 1.0f, 1).add();
  public static final Type HAMMER  = register(GradientCasts.HAMMER).add();

  public static ToolBuilder register(final GradientCasts.Cast cast) {
    return new ToolBuilder(cast);
  }

  public static Collection<Type> types() {
    return TYPES.values();
  }

  public static Type getType(final String name) {
    final Type type = TYPES.get(name);

    if(type == null) {
      return PICKAXE;
    }

    return type;
  }

  private static EnumActionResult onItemUsePass(final EntityPlayer player, final World world, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
    return EnumActionResult.PASS;
  }

  private static EnumActionResult onMattockUse(final EntityPlayer player, final World world, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
    final IBlockState state = world.getBlockState(pos);

    // Handled in event handler; need this here to stop from placing items in offhand (see #541)
    if(state.getBlock() == Blocks.LOG || state.getBlock() == Blocks.LOG2) {
      return EnumActionResult.SUCCESS;
    }

    return Items.STONE_HOE.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
  }

  private static void noTooltip(final ItemStack stack, @Nullable final World world, final List<String> tooltip) {

  }

  private static void mattockTooltip(final ItemStack stack, @Nullable final World world, final List<String> tooltip) {
    tooltip.add(I18n.format("item.stone_mattock.tooltip"));
  }

  public static class Type implements Comparable<Type> {
    private static int currentId;

    public final int id;
    public final GradientCasts.Cast cast;
    public final String[] toolClass;

    public final double attackDamage;
    public final double attackSpeed;
    public final int attackDurabilityLost;

    private final OnItemUse onItemUse;
    private final Tooltip tooltip;

    public Type(final GradientCasts.Cast cast, final String[] toolClass, final double attackDamage, final double attackSpeed, final int attackDurabilityLost, final OnItemUse onItemUse, final Tooltip tooltip) {
      this.id = currentId++;
      this.cast = cast;
      this.toolClass = toolClass;

      this.attackDamage = attackDamage;
      this.attackSpeed  = attackSpeed;
      this.attackDurabilityLost = attackDurabilityLost;

      this.onItemUse = onItemUse;
      this.tooltip = tooltip;
    }

    public EnumActionResult onItemUse(final EntityPlayer player, final World world, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
      return this.onItemUse.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }

    public void tooltip(final ItemStack stack, @Nullable final World world, final List<String> tooltip) {
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
    EnumActionResult onItemUse(final EntityPlayer player, final World world, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ);
  }

  @FunctionalInterface
  public interface Tooltip {
    void tooltip(final ItemStack stack, @Nullable final World world, final List<String> tooltip);
  }

  public static final class ToolBuilder {
    private final GradientCasts.Cast cast;

    private String[] toolClass;

    private double attackDamage;
    private double attackSpeed;
    private int attackDurabilityLost;

    private OnItemUse onItemUse = GradientTools::onItemUsePass;
    private Tooltip tooltip = GradientTools::noTooltip;

    private ToolBuilder(final GradientCasts.Cast cast) {
      this.cast = cast;
    }

    public ToolBuilder tool(final String... toolClass) {
      this.toolClass = toolClass;
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

    public ToolBuilder tooltip(final Tooltip tooltip) {
      this.tooltip = tooltip;
      return this;
    }

    public Type add() {
      final Type type = new Type(this.cast, this.toolClass, this.attackDamage, this.attackSpeed, this.attackDurabilityLost, this.onItemUse, this.tooltip);
      TYPES.put(this.cast.name, type);
      return type;
    }
  }
}
