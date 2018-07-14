package lordmonoxide.gradient.progression.components;

import lordmonoxide.gradient.progression.GuiJournalEntry;

public abstract class JournalComponent {
  public final String id;

  protected JournalComponent(final String id) {
    this.id = id;
  }

  public abstract void render(GuiJournalEntry gui);
  public abstract int getRenderedHeight(GuiJournalEntry gui);

  public boolean isAvailable() {
    return true;
  }

  public boolean isCompletable() {
    return true;
  }

  public boolean isCompleted() {
    return true;
  }
}
