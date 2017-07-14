package lordmonoxide.gradient.progression;

import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.items.GradientItems;
import lordmonoxide.gradient.progression.components.JournalGather;
import lordmonoxide.gradient.progression.components.JournalParagraph;
import lordmonoxide.gradient.progression.components.JournalRequirePrevious;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public final class Journal {
  public static final Journal instance = new Journal();
  
  public final NonNullList<JournalEntry> entries = NonNullList.create();
  
  public final JournalEntry gettingStarted;
  public final JournalEntry textiles;
  
  private Journal() {
    this.gettingStarted = this.add(
      new JournalEntry(
        "getting_started",
        new ItemStack(Items.STICK),
        JournalEntry.EntryType.UNLOCKABLE,
        0, 0,
        
        paragraph("getting_started.p1"),
        paragraph("getting_started.p2"),
        
        gather(
          "getting_started.gather1",
          new ItemStack(GradientItems.FIBRE, 10),
          new ItemStack(Items.STICK, 10),
          new ItemStack(GradientBlocks.PEBBLE, 5)
        )
      )
    );
    
    this.textiles = this.add(
      new JournalEntry(
        "textiles",
        new ItemStack(GradientItems.CLOTH),
        JournalEntry.EntryType.NORMAL,
        -3, 0,
        
        requirePrevious(
          "textiles.req1",
          this.gettingStarted
        )
      )
    );
  }
  
  public JournalEntry add(JournalEntry entry) {
    this.entries.add(entry);
    return entry;
  }
  
  private static JournalParagraph paragraph(String id) {
    return new JournalParagraph(id);
  }
  
  private static JournalGather gather(String id, ItemStack... stacks) {
    return new JournalGather(id, Minecraft.getMinecraft().player, stacks);
  }
  
  private static JournalRequirePrevious requirePrevious(String id, JournalEntry... entries) {
    return new JournalRequirePrevious(id, entries);
  }
}