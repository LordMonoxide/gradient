package lordmonoxide.gradient.progression.components;

import lordmonoxide.gradient.progression.GuiJournalEntry;

public class JournalPageBreak extends JournalComponent {
  public JournalPageBreak(String id) {
    super(id);
  }
  
  @Override
  public void render(GuiJournalEntry gui) {
    
  }
  
  @Override
  public int getRenderedHeight(GuiJournalEntry gui) {
    return 0;
  }
}
