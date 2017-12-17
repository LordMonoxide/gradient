package lordmonoxide.gradient;

import lordmonoxide.gradient.progression.GuiJournal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class KeyBindings {
  public static final KeyBindings instance = new KeyBindings();
  
  private static final Map<KeyBinding, Consumer<KeyBinding>> keys = new HashMap<>();
  
  static {
    addKey("key.journal", Keyboard.KEY_BACKSLASH, KeyBindings::onKeyJournal);
  }
  
  private KeyBindings() { }
  
  private static void addKey(final String description, final int key, final Consumer<KeyBinding> fn) {
    final KeyBinding binding = new KeyBinding(description, key, "key.category.gradient");
  
    ClientRegistry.registerKeyBinding(binding);
    keys.put(binding, fn);
  }
  
  @SubscribeEvent
  public void onKeyInput(final InputEvent.KeyInputEvent event) {
    for(final Map.Entry<KeyBinding, Consumer<KeyBinding>> entry : keys.entrySet()) {
      if(entry.getKey().isPressed()) {
        entry.getValue().accept(entry.getKey());
      }
    }
  }
  
  private static void onKeyJournal(final KeyBinding binding) {
    Minecraft.getMinecraft().displayGuiScreen(new GuiJournal());
  }
}
