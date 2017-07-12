package lordmonoxide.gradient.progression.components;

import lordmonoxide.gradient.progression.GuiJournalEntry;
import lordmonoxide.gradient.progression.JournalEntry;

import java.util.Arrays;

public class JournalRequirePrevious extends JournalComponent {
  private final JournalEntry[] entries;
  
  public JournalRequirePrevious(JournalEntry... entries) {
    this.entries = entries;
  }
  
  @Override
  public void render(GuiJournalEntry gui) {
    
  }
  
  @Override
  public int getRenderedHeight(GuiJournalEntry gui) {
    return 0;
  }
  
  @Override
  public boolean isAvailable() {
    return Arrays.stream(this.entries).allMatch(JournalEntry::isCompleted);
  }
}
