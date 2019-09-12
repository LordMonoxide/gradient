package lordmonoxide.gradient.client.models;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.BlockBronzeBoiler;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.tileentities.TileBronzeBoiler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
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
public class BakedModelBronzeBoiler implements IBakedModel {
  private static final Map<String, IBakedModel[]> FLUID_MODELS = new HashMap<>();

  private final IBakedModel baseModel;

  public BakedModelBronzeBoiler(final IBakedModel baseModel) {
    this.baseModel = baseModel;
  }

  @Override
  public List<BakedQuad> getQuads(@Nullable final IBlockState state, @Nullable final EnumFacing side, final long rand) {
    // Frame
    if(MinecraftForgeClient.getRenderLayer() == BlockRenderLayer.CUTOUT_MIPPED) {
      return this.baseModel.getQuads(state, side, rand);
    }

    // Fluid
    if(MinecraftForgeClient.getRenderLayer() == BlockRenderLayer.TRANSLUCENT) {
      final IExtendedBlockState exState = (IExtendedBlockState)state;
      final int waterLevel = exState.getValue(BlockBronzeBoiler.WATER_LEVEL);
      final int steamLevel = exState.getValue(BlockBronzeBoiler.STEAM_LEVEL);

      final List<BakedQuad> quads = new ArrayList<>();

      if(waterLevel > 0) {
        quads.addAll(FLUID_MODELS.get("water")[waterLevel - 1].getQuads(null, side, rand));
      }

      if(steamLevel > 0) {
        quads.addAll(FLUID_MODELS.get("steam")[steamLevel - 1].getQuads(null, side, rand));
      }

      return quads;
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

    for(int x = 0; x < capacity; x++) {
      bakedFluidModels[x] = new BakedModelFluid(fluid, capacity, x + 1, yOffset, height);
    }

    return bakedFluidModels;
  }

  @SubscribeEvent
  public static void onModelBakeEvent(final ModelBakeEvent event) {
    GradientMod.logger.info("Adding fluids to bronze boiler model");

    // generate fluid models for all registered fluids for 16 levels each

    FLUID_MODELS.put("water", getFluidModels(FluidRegistry.WATER, TileBronzeBoiler.WATER_CAPACITY, 1.0f / 16.0f, 6.0f / 16.0f));
    FLUID_MODELS.put("steam", getFluidModels(FluidRegistry.getFluid("steam"), TileBronzeBoiler.STEAM_CAPACITY, 9.0f / 16.0f, 6.0f / 16.0f));

    // get ModelResourceLocations of all tank block variants from the registry except "inventory"

    for(final ModelResourceLocation loc : event.getModelRegistry().getKeys()) {
      if(loc.getNamespace().equals(GradientMod.MODID) && loc.getPath().equals(GradientBlocks.BRONZE_BOILER.getRegistryName().getPath()) && !"inventory".equals(loc.getVariant())) {
        final IBakedModel registeredModel = event.getModelRegistry().getObject(loc);
        final IBakedModel replacementModel = new BakedModelBronzeBoiler(registeredModel);
        event.getModelRegistry().putObject(loc, replacementModel);
      }
    }
  }
}
