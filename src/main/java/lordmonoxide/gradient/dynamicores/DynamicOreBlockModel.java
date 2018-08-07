package lordmonoxide.gradient.dynamicores;

import com.google.common.collect.ImmutableMap;
import lordmonoxide.gradient.GradientMod;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DynamicOreBlockModel implements ICustomModelLoader {
  @Override
  public boolean accepts(final ResourceLocation modelLocation) {
    return modelLocation.getNamespace().equals(GradientMod.MODID) && modelLocation.getPath().startsWith("ore_");
  }

  @Override
  public IModel loadModel(final ResourceLocation modelLocation) throws Exception {
    return ModelLoaderRegistry
      .getModel(new ResourceLocation("minecraft:block/cube_all"))
      .retexture(ImmutableMap.of("all", GradientMod.resource("blocks/" + modelLocation.getPath()).toString()));
  }

  @Override
  public void onResourceManagerReload(final IResourceManager resourceManager) { }
}
