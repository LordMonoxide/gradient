package lordmonoxide.gradient.progression.components;

import lordmonoxide.gradient.progression.GuiJournalEntry;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponentString;

public class JournalParagraph extends JournalComponent {
  private final String text;
  
  public JournalParagraph(String text) {
    this.text = text;
  }
  
  @Override
  public void render(GuiJournalEntry gui) {
    gui.getFontRenderer().drawSplitString(I18n.format("journal." + this.text), 0, 0, 116, 0x0);
  }
  
  @Override
  public int getRenderedHeight(GuiJournalEntry gui) {
    return gui.getFontRenderer().getWordWrappedHeight(this.text, 116) * gui.getFontRenderer().FONT_HEIGHT;
  }
}
