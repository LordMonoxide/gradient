package lordmonoxide.gradient.progression.components;

import lordmonoxide.gradient.progression.GuiJournalEntry;
import lordmonoxide.gradient.progression.JournalEntry;

import java.util.Arrays;

public class JournalRequirePrevious extends JournalComponent {
  private final JournalEntry[] entries;

  public JournalRequirePrevious(final String id, final JournalEntry... entries) {
    super(id);
    this.entries = entries;
  }

  @Override
  public void render(final GuiJournalEntry gui) {

  }

  @Override
  public int getRenderedHeight(final GuiJournalEntry gui) {
    return 0;
  }

  @Override
  public boolean isAvailable() {
    return Arrays.stream(this.entries).allMatch(JournalEntry::isCompleted);
  }
}
