package lordmonoxide.gradient.client.models;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.BlockMixingBasin;
import lordmonoxide.gradient.blocks.GradientBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.registry.RegistrySimple;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = GradientMod.MODID, value = Side.CLIENT)
public class BakedModelMixingBasin implements IBakedModel {
  private static final Map<String, IBakedModel[]> FLUID_MODELS = new HashMap<>();

  private final IBakedModel baseModel;

  public BakedModelMixingBasin(final IBakedModel baseModel) {
    this.baseModel = baseModel;
  }

  @Override
  public List<BakedQuad> getQuads(@Nullable final IBlockState state, @Nullable final EnumFacing side, final long rand) {
    // Frame
    if(MinecraftForgeClient.getRenderLayer() == BlockRenderLayer.SOLID) {
      return this.baseModel.getQuads(state, side, rand);
    }

    // Fluid
    if(MinecraftForgeClient.getRenderLayer() == BlockRenderLayer.TRANSLUCENT) {
      final IExtendedBlockState exState = (IExtendedBlockState)state;
      final Boolean hasWater = exState.getValue(BlockMixingBasin.HAS_WATER);

      if(hasWater) {
        return FLUID_MODELS.get("water")[0].getQuads(null, side, rand);
      }
    }

    return new ArrayList<>();
  }

  @Override
  public boolean isAmbientOcclusion() {
    return false;
  }

  @Override
  public boolean isGui3d() {
    return this.baseModel.isGui3d();
  }

  @Override
  public boolean isBuiltInRenderer() {
    return this.baseModel.isBuiltInRenderer();
  }

  @Override
  public TextureAtlasSprite getParticleTexture() {
    return this.baseModel.getParticleTexture();
  }

  @SuppressWarnings("deprecation")
  @Override
  public ItemCameraTransforms getItemCameraTransforms() {
    return this.baseModel.getItemCameraTransforms();
  }

  @Override
  public ItemOverrideList getOverrides() {
    return ItemOverrideList.NONE;
  }

  private static IBakedModel[] getFluidModels(final Fluid fluid, final int capacity, final float yOffset, final float height) {
    final IBakedModel[] bakedFluidModels = new IBakedModel[capacity];

    final float offset = 3.0f / 16.0f;
    final float[] x = {offset, offset, 1.0f - offset, 1.0f - offset};
    final float[] z = {offset, 1.0f - offset, 1.0f - offset, offset};

    for(int y = 0; y < capacity; y++) {
      bakedFluidModels[y] = new BakedModelFluid(fluid, capacity, y + 1, yOffset, height, x, z);
    }

    return bakedFluidModels;
  }

  @SubscribeEvent
  public static void onModelBakeEvent(final ModelBakeEvent event) {
    FLUID_MODELS.put("water", getFluidModels(FluidRegistry.WATER, 1, 1.0f / 16.0f, 5.0f / 16.0f));

    // get ModelResourceLocations of all tank block variants from the registry except "inventory"

    final RegistrySimple<ModelResourceLocation, IBakedModel> registry = (RegistrySimple<ModelResourceLocation, IBakedModel>)event.getModelRegistry();

    for(final ModelResourceLocation loc : registry.getKeys()) {
      if(loc.getNamespace().equals(GradientMod.MODID) && loc.getPath().equals(GradientBlocks.MIXING_BASIN.getRegistryName().getPath()) && !"inventory".equals(loc.getVariant())) {
        final IBakedModel registeredModel = event.getModelRegistry().getObject(loc);
        final IBakedModel replacementModel = new BakedModelMixingBasin(registeredModel);
        event.getModelRegistry().putObject(loc, replacementModel);
      }
    }
  }
}
