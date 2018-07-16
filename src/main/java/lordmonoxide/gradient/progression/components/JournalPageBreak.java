package lordmonoxide.gradient.progression.components;

import lordmonoxide.gradient.progression.GuiJournalEntry;

public class JournalPageBreak extends JournalComponent {
  public JournalPageBreak(final String id) {
    super(id);
  }

  @Override
  public void render(final GuiJournalEntry gui) {

  }

  @Override
  public int getRenderedHeight(final GuiJournalEntry gui) {
    return 0;
  }
}
