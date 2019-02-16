package lordmonoxide.gradient.core.utils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public final class NbtUtil {
  private NbtUtil() { }

  public static NBTTagCompound setResourceLocation(final NBTTagCompound tag, final ResourceLocation loc) {
    tag.setString("namespace", loc.getNamespace());
    tag.setString("path", loc.getPath());
    return tag;
  }

  public static ResourceLocation getResourceLocation(final NBTTagCompound tag) {
    return new ResourceLocation(tag.getString("namespace"), tag.getString("path"));
  }
}
