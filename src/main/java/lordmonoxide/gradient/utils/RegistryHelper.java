package lordmonoxide.gradient.utils;

import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;

public class RegistryHelper<T extends ForgeRegistryEntry<T>> {
  private final IForgeRegistry<T> registry;

  public RegistryHelper(final IForgeRegistry<T> registry) {
    this.registry = registry;
  }

  public T register(final T entry, final String id) {
    this.registry.register(entry.setRegistryName(id));
    return entry;
  }
}
