package lordmonoxide.gradient.progression;

import javafx.geometry.Pos;
import net.minecraft.item.ItemStack;

public class JournalEntry {
  public final String name;
  public final ItemStack icon;
  public final EntryType type;
  public final int x;
  public final int y;
  
  public JournalEntry(String name, ItemStack icon, EntryType type, int x, int y) {
    this.name = name;
    this.icon = icon;
    this.type = type;
    this.x = x;
    this.y = y;
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
