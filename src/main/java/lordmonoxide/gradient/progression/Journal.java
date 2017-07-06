package lordmonoxide.gradient.progression;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public final class Journal {
  public static final Journal instance = new Journal();
  
  public final NonNullList<JournalEntry> entries = NonNullList.create();
  
  private Journal() {
    this.add(new JournalEntry("getting_started", new ItemStack(Items.STICK), JournalEntry.EntryType.UNLOCKABLE, 0, 0));
  }
  
  public void add(JournalEntry entry) {
    this.entries.add(entry);
  }
}
