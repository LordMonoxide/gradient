package lordmonoxide.gradient.blocks.bronzeboiler;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;

public class BakedModelBronzeBoilerFluid implements IBakedModel {
  private static final float offset = 0.00005f;
  private static final float[] x = {0, 0, 1, 1};
  private static final float[] z = {0, 1, 1, 0};

  private final Fluid fluid;
  private final VertexFormat format;
  private final EnumMap<EnumFacing, List<BakedQuad>> faceQuads;

  public BakedModelBronzeBoilerFluid(final Fluid fluid, final int capacity, final int level, final float yOffset, final float height) {
    this.fluid = fluid;

    this.format = DefaultVertexFormats.ITEM;
    this.faceQuads = Maps.newEnumMap(EnumFacing.class);

    for(final EnumFacing side : EnumFacing.values()) {
      this.faceQuads.put(side, ImmutableList.of());
    }

    final float filled = (float)level / capacity;

    final float y = Math.min(filled * height, height - offset);

    final TextureAtlasSprite texture = this.getParticleTexture();

    // Vertices are defined counter-clockwise,
    // starting at the top left corner of the face.

    // top

    EnumFacing side = EnumFacing.UP;

    UnpackedBakedQuad.Builder quadBuilder = new UnpackedBakedQuad.Builder(this.format);
    quadBuilder.setQuadOrientation(side);
    quadBuilder.setTexture(texture);
    float X, Z;

    for(int i = 0; i < 4; i++) {
      X = Math.min(Math.max(x[i], offset), 1.0f - offset);
      Z = Math.min(Math.max(z[i], offset), 1.0f - offset);

      this.putVertex(quadBuilder, side, X, yOffset + y, Z, texture.getInterpolatedU(x[i] * 16), texture.getInterpolatedV(z[i] * 16));
    }

    this.faceQuads.put(side, ImmutableList.of(quadBuilder.build()));

    // bottom

    side = EnumFacing.DOWN;
    quadBuilder = new UnpackedBakedQuad.Builder(this.format);
    quadBuilder.setQuadOrientation(side);
    quadBuilder.setTexture(texture);

    for(int i = 0; i < 4; i++) {
      X = Math.min(Math.max(x[i], offset), 1.0f - offset);
      Z = Math.min(Math.max(z[i], offset), 1.0f - offset);

      this.putVertex(quadBuilder, side, Z, yOffset + offset, X, texture.getInterpolatedU(z[i] * 16), texture.getInterpolatedV(x[i] * 16));
    }

    this.faceQuads.put(side, ImmutableList.of(quadBuilder.build()));

    /// sides

    // east

    side = EnumFacing.EAST;

    quadBuilder = new UnpackedBakedQuad.Builder(this.format);
    quadBuilder.setQuadOrientation(side);
    quadBuilder.setTexture(texture);

    this.putVertex(quadBuilder, side, x[2] - offset, yOffset + y, z[2], texture.getInterpolatedU(0), texture.getInterpolatedV(16 * (1 - filled)));
    this.putVertex(quadBuilder, side, x[2] - offset, yOffset + offset, z[2], texture.getInterpolatedU(0), texture.getInterpolatedV(16));
    this.putVertex(quadBuilder, side, x[3] - offset, yOffset + offset, z[3], texture.getInterpolatedU(16), texture.getInterpolatedV(16));
    this.putVertex(quadBuilder, side, x[3] - offset, yOffset + y, z[3], texture.getInterpolatedU(16), texture.getInterpolatedV(16 * (1 - filled)));

    this.faceQuads.put(side, ImmutableList.of(quadBuilder.build()));

    // west

    side = EnumFacing.WEST;

    quadBuilder = new UnpackedBakedQuad.Builder(this.format);
    quadBuilder.setQuadOrientation(side);
    quadBuilder.setTexture(texture);

    this.putVertex(quadBuilder, side, x[0] + offset, yOffset + y, z[0], texture.getInterpolatedU(0), texture.getInterpolatedV(16 * (1 - filled)));
    this.putVertex(quadBuilder, side, x[0] + offset, yOffset + offset, z[0], texture.getInterpolatedU(0), texture.getInterpolatedV(16));
    this.putVertex(quadBuilder, side, x[1] + offset, yOffset + offset, z[1], texture.getInterpolatedU(16), texture.getInterpolatedV(16));
    this.putVertex(quadBuilder, side, x[1] + offset, yOffset + y, z[1], texture.getInterpolatedU(16), texture.getInterpolatedV(16 * (1 - filled)));

    this.faceQuads.put(side, ImmutableList.of(quadBuilder.build()));

    // south

    side = EnumFacing.SOUTH;

    quadBuilder = new UnpackedBakedQuad.Builder(this.format);
    quadBuilder.setQuadOrientation(side);
    quadBuilder.setTexture(texture);

    this.putVertex(quadBuilder, side, x[1], yOffset + y, z[1] - offset, texture.getInterpolatedU(0), texture.getInterpolatedV(16 * (1 - filled)));
    this.putVertex(quadBuilder, side, x[1], yOffset + offset, z[1] - offset, texture.getInterpolatedU(0), texture.getInterpolatedV(16));
    this.putVertex(quadBuilder, side, x[2], yOffset + offset, z[2] - offset, texture.getInterpolatedU(16), texture.getInterpolatedV(16));
    this.putVertex(quadBuilder, side, x[2], yOffset + y, z[2] - offset, texture.getInterpolatedU(16), texture.getInterpolatedV(16 * (1 - filled)));

    this.faceQuads.put(side, ImmutableList.of(quadBuilder.build()));

    // north

    side = EnumFacing.NORTH;

    quadBuilder = new UnpackedBakedQuad.Builder(this.format);
    quadBuilder.setQuadOrientation(side);
    quadBuilder.setTexture(texture);

    this.putVertex(quadBuilder, side, x[3], yOffset + y, z[3] + offset, texture.getInterpolatedU(0), texture.getInterpolatedV(16 * (1 - filled)));
    this.putVertex(quadBuilder, side, x[3], yOffset + offset, z[3] + offset, texture.getInterpolatedU(0), texture.getInterpolatedV(16));
    this.putVertex(quadBuilder, side, x[0], yOffset + offset, z[0] + offset, texture.getInterpolatedU(16), texture.getInterpolatedV(16));
    this.putVertex(quadBuilder, side, x[0], yOffset + y, z[0] + offset, texture.getInterpolatedU(16), texture.getInterpolatedV(16 * (1 - filled)));

    this.faceQuads.put(side, ImmutableList.of(quadBuilder.build()));
  }

  @Override
  public List<BakedQuad> getQuads(@Nullable final IBlockState state, @Nullable final EnumFacing side, final long rand) {
    if(side != null) {
      return this.faceQuads.get(side);
    }

    return this.faceQuads.get(EnumFacing.UP);
  }

  @Override
  public boolean isAmbientOcclusion() {
    return false;
  }

  @Override
  public boolean isGui3d() {
    return false;
  }

  @Override
  public boolean isBuiltInRenderer() {
    return false;
  }

  @Override
  public TextureAtlasSprite getParticleTexture() {
    final String fluidTextureLoc = (this.fluid.getStill() != null ? this.fluid.getStill() : this.fluid.getFlowing() != null ? this.fluid.getFlowing() : FluidRegistry.WATER.getStill()).toString();

    return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluidTextureLoc);
  }

  @SuppressWarnings("deprecation")
  @Override
  public ItemCameraTransforms getItemCameraTransforms() {
    return ItemCameraTransforms.DEFAULT;
  }

  @Override
  public ItemOverrideList getOverrides() {
    return ItemOverrideList.NONE;
  }

  private void putVertex(final UnpackedBakedQuad.Builder builder, final EnumFacing side, final float x, final float y, final float z, final float u, final float v) {
    for(int e = 0; e < this.format.getElementCount(); e++) {
      switch(this.format.getElement(e).getUsage()) {
        case POSITION:
          final float[] data = {x, y, z};
          builder.put(e, data);
          break;

        case COLOR:
          builder.put(e, (this.fluid.getColor() >> 16 & 0xFF) / 255.0f, (this.fluid.getColor() >> 8 & 0xFF) / 255.0f, (this.fluid.getColor() & 0xFF) / 255.0f, (this.fluid.getColor() >> 24 & 0xFF) / 255.0f);
          break;

        case UV:
          if(this.format.getElement(e).getIndex() == 0) {
            builder.put(e, u, v);
            break;
          }

        case NORMAL:
          builder.put(e, side.getXOffset(), side.getYOffset(), side.getZOffset(), 0.0f);
          break;

        default:
          builder.put(e);
          break;
      }
    }
  }
}
