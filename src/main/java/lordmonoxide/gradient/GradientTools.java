package lordmonoxide.gradient;

import com.google.common.base.Optional;
import net.minecraft.block.properties.PropertyHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class GradientTools {
  private GradientTools() { }
  
  public static final List<Type> TYPES = new ArrayList<>();
  
  public static final Type PICKAXE = register(GradientCasts.PICKAXE).tool("pickaxe")      .weapon(1.0d, 1.0d).add();
  public static final Type MATTOCK = register(GradientCasts.MATTOCK).tool("axe", "shovel").weapon(4.5d, 0.5d).onItemUse(GradientTools::onMattockUse).add();
  public static final Type SWORD   = register(GradientCasts.SWORD)  .tool("sword")        .weapon(3.0d, 1.0f).add();
  public static final Type HAMMER  = register(GradientCasts.HAMMER).add();
  
  public static ToolBuilder register(final GradientCasts.Cast cast) {
    return new ToolBuilder(cast);
  }
  
  private static EnumActionResult onItemUsePass(final EntityPlayer player, final World world, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
    return EnumActionResult.PASS;
  }
  
  private static EnumActionResult onMattockUse(final EntityPlayer player, final World world, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
    return Items.STONE_HOE.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
  }
  
  public static class Type implements Comparable<Type> {
    private static int currentId;
    
    public final int id;
    public final GradientCasts.Cast cast;
    public final String[] toolClass;
    
    public final double attackDamage;
    public final double attackSpeed;
    
    private final OnItemUse onItemUse;
    
    public Type(final GradientCasts.Cast cast, final String[] toolClass, final double attackDamage, final double attackSpeed, final OnItemUse onItemUse) {
      this.id = currentId++;
      this.cast = cast;
      this.toolClass = toolClass;
      
      this.attackDamage = attackDamage;
      this.attackSpeed  = attackSpeed;
      
      this.onItemUse = onItemUse;
    }
    
    public EnumActionResult onItemUse(final EntityPlayer player, final World world, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
      return this.onItemUse.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }
    
    @Override
    public int compareTo(final Type o) {
      assert o != null;
      
      return this.id == o.id ? 0 : this.id > o.id ? 1 : -1;
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
  
  public static class PropertyTool extends PropertyHelper<Type> {
    public static PropertyTool create(final String name) {
      return new PropertyTool(name);
    }
    
    protected PropertyTool(final String name) {
      super(name, Type.class);
    }
    
    @Override
    public Collection<Type> getAllowedValues() {
      return TYPES;
    }
    
    @Override
    public Optional<Type> parseValue(final String value) {
      for(final Type type : TYPES) {
        if(type.cast.name.equals(value)) {
          return Optional.of(type);
        }
      }
      
      return Optional.absent();
    }
    
    @Override
    public String getName(final Type type) {
      return type.cast.name;
    }
  }
  
  public interface OnItemUse {
    EnumActionResult onItemUse(final EntityPlayer player, final World world, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ);
  }
  
  public static final class ToolBuilder {
    private final GradientCasts.Cast cast;
    
    private String[] toolClass;
    
    private double attackDamage;
    private double attackSpeed;
    
    private OnItemUse onItemUse = GradientTools::onItemUsePass;
    
    private ToolBuilder(final GradientCasts.Cast cast) {
      this.cast = cast;
    }
    
    public ToolBuilder tool(final String... toolClass) {
      this.toolClass = toolClass;
      return this;
    }
    
    public ToolBuilder weapon(double attackDamage, double attackSpeed) {
      this.attackDamage = attackDamage;
      this.attackSpeed  = attackSpeed;
      return this;
    }
    
    public ToolBuilder onItemUse(final OnItemUse onItemUse) {
      this.onItemUse = onItemUse;
      return this;
    }
    
    public Type add() {
      final Type type = new Type(this.cast, this.toolClass, this.attackDamage, this.attackSpeed, this.onItemUse);
      TYPES.add(type);
      return type;
    }
  }
}
