package lordmonoxide.gradient.progression;

import com.google.common.collect.Lists;
import lordmonoxide.gradient.progression.components.JournalComponent;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class JournalEntry {
  public final String name;
  public final ItemStack icon;
  public final EntryType type;
  public final int x;
  public final int y;
  public final List<JournalComponent> components;
  
  public JournalEntry(String name, ItemStack icon, EntryType type, int x, int y, JournalComponent... components) {
    this.name = name;
    this.icon = icon;
    this.type = type;
    this.x = x;
    this.y = y;
    this.components = Collections.unmodifiableList(Lists.newArrayList(components));
  }
  
  public boolean isAvailable() {
    return this.components.stream().allMatch(JournalComponent::isAvailable);
  }
  
  public boolean isCompletable() {
    return this.components.stream().allMatch(JournalComponent::isCompletable);
  }
  
  public boolean isCompleted() {
    return this.components.stream().allMatch(JournalComponent::isCompleted);
  }
  
  public enum EntryType  {
    NORMAL    (  0, 202, 26, 26),
    UNLOCKABLE( 26, 202, 26, 26),
    SPECIAL   ( 52, 202, 26, 26);
    
    public final int textureX;
    public final int textureY;
    public final int textureW;
    public final int textureH;
    
    EntryType(int textureX, int textureY, int textureW, int textureH) {
      this.textureX = textureX;
      this.textureY = textureY;
      this.textureW = textureW;
      this.textureH = textureH;
    }
  }
}
