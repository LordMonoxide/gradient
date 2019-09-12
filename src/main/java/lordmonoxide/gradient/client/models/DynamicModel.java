package lordmonoxide.gradient.client.models;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.animation.IClip;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@SideOnly(Side.CLIENT)
public class DynamicModel implements ICustomModelLoader {
  private final Predicate<ResourceLocation> matcher;
  private final Function<ResourceLocation, ImmutableMap<String, String>> textureLoader;
  private final ResourceLocation baseModel;
  private final boolean isItem;

  private boolean retexture = true;

  public DynamicModel(final Predicate<ResourceLocation> matcher, final Function<ResourceLocation, ImmutableMap<String, String>> textureLoader, final ResourceLocation baseModel) {
    this(matcher, textureLoader, baseModel, false);
  }

  public DynamicModel(final Predicate<ResourceLocation> matcher, final Function<ResourceLocation, ImmutableMap<String, String>> textureLoader, final ResourceLocation baseModel, final boolean isItem) {
    this.matcher = matcher;
    this.textureLoader = textureLoader;
    this.baseModel = baseModel;
    this.isItem = isItem;
  }

  public void disableRetexture() {
    this.retexture = false;
  }

  @Override
  public boolean accepts(final ResourceLocation modelLocation) {
    return this.matcher.test(modelLocation);
  }

  @Override
  public IModel loadModel(final ResourceLocation modelLocation) throws Exception {
    IModel model = ModelLoaderRegistry.getModel(this.baseModel);

    if(this.retexture) {
      model = model.retexture(this.textureLoader.apply(modelLocation));
    }

    if(this.isItem) {
      return new Model(model);
    }

    return model;
  }

  @Override
  public void onResourceManagerReload(final IResourceManager resourceManager) { }

  private static final class Model implements IModel {
    private final IModel parent;

    private Model(final IModel parent) {
      this.parent = parent;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
      return this.parent.getDependencies();
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
      return this.parent.getTextures();
    }

    @Override
    public IBakedModel bake(final IModelState state, final VertexFormat format, final Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
      return this.parent.bake(state, format, bakedTextureGetter);
    }

    @Override
    public IModelState getDefaultState() {
      return this.parent.getDefaultState();
    }

    @Override
    public Optional<? extends IClip> getClip(final String name) {
      return this.parent.getClip(name);
    }

    @Override
    public IModel process(final ImmutableMap<String, String> customData) {
      return this.parent.process(customData);
    }

    @Override
    public IModel smoothLighting(final boolean value) {
      return this.parent.smoothLighting(value);
    }

    @Override
    public IModel gui3d(final boolean value) {
      return this.parent.gui3d(value);
    }

    @Override
    public IModel uvlock(final boolean value) {
      return this.parent.uvlock(value);
    }

    @Override
    public IModel retexture(final ImmutableMap<String, String> textures) {
      return this.parent.retexture(textures);
    }
  }
}
