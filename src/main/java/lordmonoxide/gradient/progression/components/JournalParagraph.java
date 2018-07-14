package lordmonoxide.gradient.progression.components;

import lordmonoxide.gradient.progression.GuiJournalEntry;
import net.minecraft.client.resources.I18n;

public class JournalParagraph extends JournalComponent {
  public JournalParagraph(final String id) {
    super(id);
  }

  @Override
  public void render(final GuiJournalEntry gui) {
    gui.getFontRenderer().drawSplitString(I18n.format("journal." + this.id), 0, 0, 116, 0x0);
  }

  @Override
  public int getRenderedHeight(final GuiJournalEntry gui) {
    return gui.getFontRenderer().getWordWrappedHeight(this.id, 116) * gui.getFontRenderer().FONT_HEIGHT;
  }

  @Override
  public boolean isCompleted() {
    return true;
  }
}
