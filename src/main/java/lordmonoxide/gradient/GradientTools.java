package lordmonoxide.gradient;

import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GradientTools {
  private GradientTools() { }

  private static final Map<String, Type> TYPES = new HashMap<>();

  public static final Type PICKAXE = register(GradientCasts.PICKAXE).tool(ToolType.PICKAXE)             .weapon(1.0d, 1.0d, 2).add();
  public static final Type MATTOCK = register(GradientCasts.MATTOCK).tool(ToolType.AXE, ToolType.SHOVEL).weapon(4.5d, 0.5d, 2).onItemUse(GradientTools::onMattockUse).tooltip(GradientTools::mattockTooltip).add();
  public static final Type SWORD   = register(GradientCasts.SWORD)                                      .weapon(3.0d, 1.0f, 1).add();
  public static final Type HAMMER  = register(GradientCasts.HAMMER).add();

  public static ToolBuilder register(final GradientCasts.Cast cast) {
    return new ToolBuilder(cast);
  }

  public static Collection<Type> types() {
    return TYPES.values();
  }

  private static EnumActionResult onItemUsePass(final ItemUseContext context) {
    return EnumActionResult.PASS;
  }

  private static EnumActionResult onMattockUse(final ItemUseContext context) {
    final IWorld world = context.getWorld();
    final BlockPos pos = context.getPos();
    final IBlockState state = world.getBlockState(pos);

    // Handled in event handler; need this here to stop from placing items in offhand (see #541)
    if(state.getBlock() instanceof BlockLog) {
      return EnumActionResult.SUCCESS;
    }

    return Items.STONE_HOE.onItemUse(context);
  }

  private static void noTooltip(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {

  }

  private static void mattockTooltip(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
    tooltip.add(new TextComponentTranslation("item.stone_mattock.tooltip"));
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
    private final Tooltip tooltip;

    public Type(final GradientCasts.Cast cast, final ToolType[] toolTypes, final double attackDamage, final double attackSpeed, final int attackDurabilityLost, final OnItemUse onItemUse, final Tooltip tooltip) {
      this.id = currentId++;
      this.cast = cast;
      this.toolTypes = toolTypes;

      this.attackDamage = attackDamage;
      this.attackSpeed  = attackSpeed;
      this.attackDurabilityLost = attackDurabilityLost;

      this.onItemUse = onItemUse;
      this.tooltip = tooltip;
    }

    public EnumActionResult onItemUse(final ItemUseContext context) {
      return this.onItemUse.onItemUse(context);
    }

    public void tooltip(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
      this.tooltip.tooltip(stack, world, tooltip, flag);
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
    EnumActionResult onItemUse(final ItemUseContext context);
  }

  @FunctionalInterface
  public interface Tooltip {
    void tooltip(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag);
  }

  public static final class ToolBuilder {
    private final GradientCasts.Cast cast;

    private ToolType[] toolTypes = new ToolType[0];

    private double attackDamage;
    private double attackSpeed;
    private int attackDurabilityLost;

    private OnItemUse onItemUse = GradientTools::onItemUsePass;
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

    public ToolBuilder tooltip(final Tooltip tooltip) {
      this.tooltip = tooltip;
      return this;
    }

    public Type add() {
      final Type type = new Type(this.cast, this.toolTypes, this.attackDamage, this.attackSpeed, this.attackDurabilityLost, this.onItemUse, this.tooltip);
      TYPES.put(this.cast.name, type);
      return type;
    }
  }
}
