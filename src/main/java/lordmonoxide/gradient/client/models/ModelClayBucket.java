package lordmonoxide.gradient.client.models;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import lordmonoxide.gradient.GradientMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.BakedItemModel;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ItemTextureQuadConverter;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.ModelStateComposition;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Quat4f;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = GradientMod.MODID, value = Dist.CLIENT)
public final class ModelClayBucket implements IUnbakedModel {
  @SubscribeEvent
  public static void onTextureStitch(final TextureStitchEvent.Pre event) {
    LoaderClayBucket.INSTANCE.register(event.getMap());
  }

  @SubscribeEvent
  public static void onModelRegister(final ModelRegistryEvent event) {
    ModelLoaderRegistry.registerLoader(LoaderClayBucket.INSTANCE);
  }

  public static void setBucketModelDefinition(final Item item) {
    ModelLoader.setCustomMeshDefinition(item, stack -> LOCATION);
    ModelBakery.registerItemVariants(item, LOCATION);
  }

  private static final ModelResourceLocation LOCATION = new ModelResourceLocation(GradientMod.resource("clay_bucket"), "inventory");

  // minimal Z offset to prevent depth-fighting
  private static final float NORTH_Z_COVER = 7.496f / 16.0f;
  private static final float SOUTH_Z_COVER = 8.504f / 16.0f;
  private static final float NORTH_Z_FLUID = 7.498f / 16.0f;
  private static final float SOUTH_Z_FLUID = 8.502f / 16.0f;

  private static final IUnbakedModel MODEL = new ModelClayBucket();

  @Nullable
  private final ResourceLocation baseLocation;
  @Nullable
  private final ResourceLocation liquidLocation;
  @Nullable
  private final ResourceLocation coverLocation;
  @Nullable
  private final Fluid fluid;

  private final boolean flipGas;
  private final boolean tint;

  public ModelClayBucket() {
    this(null, null, null, null, false, true);
  }

  public ModelClayBucket(@Nullable final ResourceLocation baseLocation, @Nullable final ResourceLocation liquidLocation, @Nullable final ResourceLocation coverLocation, @Nullable final Fluid fluid, final boolean flipGas, final boolean tint) {
    this.baseLocation = baseLocation;
    this.liquidLocation = liquidLocation;
    this.coverLocation = coverLocation;
    this.fluid = fluid;
    this.flipGas = flipGas;
    this.tint = tint;
  }

  @Override
  public Collection<ResourceLocation> getTextures(final Function<ResourceLocation, IUnbakedModel> modelGetter, final Set<String> missingTextureErrors) {
    final ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();

    if(this.baseLocation != null) {
      builder.add(this.baseLocation);
    }
    if(this.liquidLocation != null) {
      builder.add(this.liquidLocation);
    }
    if(this.coverLocation != null) {
      builder.add(this.coverLocation);
    }
    if(this.fluid != null) {
      builder.add(this.fluid.getStill());
    }

    return builder.build();
  }

  @Override
  public Collection<ResourceLocation> getDependencies() {
    return Collections.emptyList();
  }

  @Override
  public IBakedModel bake(final Function<ResourceLocation, IUnbakedModel> modelGetter, final Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, IModelState state, final boolean uvlock, final VertexFormat format) {
    //TODO
    final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transformMap = PerspectiveMapWrapper.getTransforms(state);

    // if the fluid is lighter than air, will manipulate the initial state to be rotated 180? to turn it upside down
    if(this.flipGas && this.fluid != null && this.fluid.isLighterThanAir()) {
      state = new ModelStateComposition(state, TRSRTransformation.blockCenterToCorner(new TRSRTransformation(null, new Quat4f(0, 0, 1, 0), null, null)));
    }

    final TRSRTransformation transform = state.apply(Optional.empty()).orElse(TRSRTransformation.identity());
    TextureAtlasSprite fluidSprite = null;

    if(this.fluid != null) {
      fluidSprite = bakedTextureGetter.apply(this.fluid.getStill());
    }

    final ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
    TextureAtlasSprite particleSprite = null;

    if(this.baseLocation != null) {
      // build base (insidest)
      final IBakedModel model = new ItemLayerModel(ImmutableList.of(this.baseLocation)).bake(state, format, bakedTextureGetter);
      builder.addAll(model.getQuads(null, null, 0));
      particleSprite = model.getParticleTexture();
    }

    if(this.liquidLocation != null && fluidSprite != null) {
      final TextureAtlasSprite liquid = bakedTextureGetter.apply(this.liquidLocation);
      // build liquid layer (inside)
      builder.addAll(ItemTextureQuadConverter.convertTexture(format, transform, liquid, fluidSprite, NORTH_Z_FLUID, EnumFacing.NORTH, this.tint ? this.fluid.getColor() : 0xFFFFFFFF, 1));
      builder.addAll(ItemTextureQuadConverter.convertTexture(format, transform, liquid, fluidSprite, SOUTH_Z_FLUID, EnumFacing.SOUTH, this.tint ? this.fluid.getColor() : 0xFFFFFFFF, 1));
      particleSprite = fluidSprite;
    }

    if(this.coverLocation != null) {
      // cover (the actual item around the other two)
      final TextureAtlasSprite cover = bakedTextureGetter.apply(this.coverLocation);
      builder.add(ItemTextureQuadConverter.genQuad(format, transform, 0, 0, 16, 16, NORTH_Z_COVER, cover, EnumFacing.NORTH, 0xFFFFFFFF, 2));
      builder.add(ItemTextureQuadConverter.genQuad(format, transform, 0, 0, 16, 16, SOUTH_Z_COVER, cover, EnumFacing.SOUTH, 0xFFFFFFFF, 2));
      if(particleSprite == null) {
        particleSprite = cover;
      }
    }

    return new BakedClayBucket(this, builder.build(), particleSprite, format, Maps.immutableEnumMap(transformMap), Maps.newHashMap(), transform.isIdentity());
  }

  /**
   * Sets the fluid in the model.
   * "fluid" - Name of the fluid in the fluid registry.
   * "flipGas" - If "true" the model will be flipped upside down if the fluid is lighter than air. If "false" it won't.
   * "applyTint" - If "true" the model will tint the fluid quads according to the fluid's base color.
   * <p/>
   * If the fluid can't be found, water is used.
   */
  @Override
  public ModelClayBucket process(final ImmutableMap<String, String> customData) {
    final String fluidName = customData.get("fluid");
    Fluid fluid = FluidRegistry.getFluid(fluidName);

    if(fluid == null) {
      fluid = this.fluid;
    }

    boolean flip = this.flipGas;
    if(customData.containsKey("flipGas")) {
      final String flipStr = customData.get("flipGas");
      if("true".equals(flipStr)) {
        flip = true;
      } else if("false".equals(flipStr)) {
        flip = false;
      } else {
        throw new IllegalArgumentException(String.format("ClayBucket custom data \"flipGas\" must have value \'true\' or \'false\' (was \'%s\')", flipStr));
      }
    }

    boolean tint = this.tint;
    if(customData.containsKey("applyTint")) {
      final String string = customData.get("applyTint");
      switch(string) {
        case "true":
          tint = true;
          break;
        case "false":
          tint = false;
          break;
        default:
          throw new IllegalArgumentException(String.format("ClayBucket custom data \"applyTint\" must have value \'true\' or \'false\' (was \'%s\')", string));
      }
    }

    // create new model with correct liquid
    return new ModelClayBucket(this.baseLocation, this.liquidLocation, this.coverLocation, fluid, flip, tint);
  }

  /**
   * Allows to use different textures for the model.
   * There are 3 layers:
   * base - The empty bucket/container
   * fluid - A texture representing the liquid portion. Non-transparent = liquid
   * cover - An overlay that's put over the liquid (optional)
   * <p/>
   * If no liquid is given a hardcoded variant for the bucket is used.
   */
  @Override
  public ModelClayBucket retexture(final ImmutableMap<String, String> textures) {
    final ResourceLocation base  = textures.containsKey("base")  ? new ResourceLocation(textures.get("base"))  : this.baseLocation;
    final ResourceLocation fluid = textures.containsKey("fluid") ? new ResourceLocation(textures.get("fluid")) : this.liquidLocation;
    final ResourceLocation cover = textures.containsKey("cover") ? new ResourceLocation(textures.get("cover")) : this.coverLocation;
    return new ModelClayBucket(base, fluid, cover, this.fluid, this.flipGas, this.tint);
  }

  public enum LoaderClayBucket implements ICustomModelLoader {
    INSTANCE;

    @Override
    public boolean accepts(final ResourceLocation modelLocation) {
      return modelLocation.getNamespace().equals(GradientMod.MODID) && modelLocation.getPath().contains("clay_bucket_dynamic");
    }

    @Override
    public IUnbakedModel loadModel(final ResourceLocation modelLocation) {
      return MODEL;
    }

    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager) {
      // no need to clear cache since we create a new model instance
    }

    private void register(final TextureMap map) {
      // only create these textures if they are not added by a resource pack

      if(getResource(GradientMod.resource("textures/items/clay_bucket_cover.png")) == null) {
        final ResourceLocation bucketCover = GradientMod.resource("items/clay_bucket_cover");
        final BucketCoverSprite bucketCoverSprite = new BucketCoverSprite(bucketCover);
        map.setTextureEntry(bucketCoverSprite);
      }

      if(getResource(GradientMod.resource("textures/items/clay_bucket_base.png")) == null) {
        final ResourceLocation bucketBase = GradientMod.resource("items/clay_bucket_base");
        final BucketBaseSprite bucketBaseSprite = new BucketBaseSprite(bucketBase);
        map.setTextureEntry(bucketBaseSprite);
      }
    }

    @Nullable
    private static IResource getResource(final ResourceLocation resourceLocation) {
      try {
        return Minecraft.getInstance().getResourceManager().getResource(resourceLocation);
      } catch(final IOException ignored) {
        return null;
      }
    }
  }

  @OnlyIn(Dist.CLIENT)
  private static final class BucketBaseSprite extends TextureAtlasSprite {
    private final ResourceLocation bucket = GradientMod.resource("items/clay_bucket_empty");
    private final ImmutableList<ResourceLocation> dependencies = ImmutableList.of(this.bucket);

    private BucketBaseSprite(final ResourceLocation resourceLocation) {
      super(resourceLocation.toString());
    }

    @Override
    public boolean hasCustomLoader(@Nonnull final IResourceManager manager, @Nonnull final ResourceLocation location) {
      return true;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
      return this.dependencies;
    }

    @Override
    public boolean load(@Nonnull final IResourceManager manager, @Nonnull final ResourceLocation location, @Nonnull final Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
      final TextureAtlasSprite sprite = textureGetter.apply(this.bucket);
      this.width = sprite.getWidth();
      this.height = sprite.getHeight();
      final int[][] pixels = sprite.getFrameTextureData(0);
      this.clearFramesTextureData();
      this.framesTextureData.add(pixels);
      return false;
    }
  }

  /**
   * Creates a bucket cover sprite from the vanilla resource.
   */
  @OnlyIn(Dist.CLIENT)
  private static final class BucketCoverSprite extends TextureAtlasSprite {
    private final ResourceLocation bucket = GradientMod.resource("items/clay_bucket_empty");
    private final ResourceLocation bucketCoverMask = GradientMod.resource("items/clay_bucket_cover_mask");
    private final ImmutableList<ResourceLocation> dependencies = ImmutableList.of(this.bucket, this.bucketCoverMask);

    private BucketCoverSprite(final ResourceLocation resourceLocation) {
      super(resourceLocation.toString());
    }

    @Override
    public boolean hasCustomLoader(@Nonnull final IResourceManager manager, @Nonnull final ResourceLocation location) {
      return true;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
      return this.dependencies;
    }

    @Override
    public boolean load(@Nonnull final IResourceManager manager, @Nonnull final ResourceLocation location, @Nonnull final Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
      final TextureAtlasSprite sprite = textureGetter.apply(this.bucket);
      final TextureAtlasSprite alphaMask = textureGetter.apply(this.bucketCoverMask);
      this.width = sprite.getWidth();
      this.height = sprite.getHeight();
      final int[][] pixels = new int[Minecraft.getInstance().gameSettings.mipmapLevels + 1][];
      pixels[0] = new int[this.width * this.height];

      // use the alpha mask if it fits, otherwise leave the cover texture blank
      if(alphaMask.getWidth() == this.width && alphaMask.getHeight() == this.height) {
        final int[][] oldPixels = sprite.getFrameTextureData(0);
        final int[][] alphaPixels = alphaMask.getFrameTextureData(0);

        for(int p = 0; p < this.width * this.height; p++) {
          final int alphaMultiplier = alphaPixels[0][p] >>> 24;
          final int oldPixel = oldPixels[0][p];
          final int oldPixelAlpha = oldPixel >>> 24;
          final int newAlpha = oldPixelAlpha * alphaMultiplier / 0xFF;
          pixels[0][p] = (oldPixel & 0xFFFFFF) + (newAlpha << 24);
        }
      }

      this.clearFramesTextureData();
      this.framesTextureData.add(pixels);
      return false;
    }
  }

  @OnlyIn(Dist.CLIENT)
  private static final class BakedClayBucketOverrideHandler extends ItemOverrideList {
    private static final BakedClayBucketOverrideHandler INSTANCE = new BakedClayBucketOverrideHandler();

    @Override
    public IBakedModel getModelWithOverrides(final IBakedModel originalModel, final ItemStack stack, @Nullable final World world, @Nullable final EntityLivingBase entity) {
      final FluidStack fluidStack = FluidUtil.getFluidContained(stack);

      // not a fluid item apparently
      if(fluidStack == null) {
        // empty bucket
        return originalModel;
      }

      final BakedClayBucket model = (BakedClayBucket)originalModel;

      final Fluid fluid = fluidStack.getFluid();
      final String name = fluid.getName();

      if(!model.cache.containsKey(name)) {
        final IUnbakedModel parent = model.parent.process(ImmutableMap.of("fluid", name));
        final Function<ResourceLocation, TextureAtlasSprite> textureGetter = location -> Minecraft.getInstance().getTextureMapBlocks().getAtlasSprite(location.toString());

        final IBakedModel bakedModel = parent.bake(new SimpleModelState(model.getTransforms()), model.format, textureGetter);
        model.cache.put(name, bakedModel);
        return bakedModel;
      }

      return model.cache.get(name);
    }
  }

  // the dynamic bucket is based on the empty bucket
  @OnlyIn(Dist.CLIENT)
  private static final class BakedClayBucket extends BakedItemModel {
    private final ModelClayBucket parent;
    private final Map<String, IBakedModel> cache; // contains all the baked models since they'll never change
    private final VertexFormat format;

    BakedClayBucket(final ModelClayBucket parent, final ImmutableList<BakedQuad> quads, final TextureAtlasSprite particle, final VertexFormat format, final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms, final Map<String, IBakedModel> cache, final boolean untransformed) {
      super(quads, particle, transforms, BakedClayBucketOverrideHandler.INSTANCE, untransformed);
      this.format = format;
      this.parent = parent;
      this.cache = cache;
    }

    //TODO
    private ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> getTransforms() {
      return this.transforms;
    }
  }
}
