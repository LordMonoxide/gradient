package lordmonoxide.gradient.client.models;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class BakedModelFluid implements IBakedModel {
  private static final float offset = 0.00005f;
  private static final float[] x = {0.0f, 0.0f, 1.0f, 1.0f};
  private static final float[] z = {0.0f, 1.0f, 1.0f, 0.0f};

  private final Fluid fluid;
  private final VertexFormat format;
  private final EnumMap<Direction, List<BakedQuad>> faceQuads;

  public BakedModelFluid(final Fluid fluid, final int capacity, final int level, final float yOffset, final float height) {
    this(fluid, capacity, level, yOffset, height, x, z);
  }

  public BakedModelFluid(final Fluid fluid, final int capacity, final int level, final float yOffset, final float height, final float[] x, final float[] z) {
    this.fluid = fluid;

    this.format = DefaultVertexFormats.ITEM;
    this.faceQuads = Maps.newEnumMap(Direction.class);

    for(final Direction side : Direction.values()) {
      this.faceQuads.put(side, ImmutableList.of());
    }

    final float filled = (float)level / capacity;

    final float y = Math.min(filled * height, height - offset);

    final TextureAtlasSprite texture = this.getParticleTexture();

    // Vertices are defined counter-clockwise,
    // starting at the top left corner of the face.

    // top

    Direction side = Direction.UP;

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

    side = Direction.DOWN;
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

    side = Direction.EAST;

    quadBuilder = new UnpackedBakedQuad.Builder(this.format);
    quadBuilder.setQuadOrientation(side);
    quadBuilder.setTexture(texture);

    this.putVertex(quadBuilder, side, x[2] - offset, yOffset + y, z[2], texture.getInterpolatedU(0), texture.getInterpolatedV(16 * (1 - filled)));
    this.putVertex(quadBuilder, side, x[2] - offset, yOffset + offset, z[2], texture.getInterpolatedU(0), texture.getInterpolatedV(16));
    this.putVertex(quadBuilder, side, x[3] - offset, yOffset + offset, z[3], texture.getInterpolatedU(16), texture.getInterpolatedV(16));
    this.putVertex(quadBuilder, side, x[3] - offset, yOffset + y, z[3], texture.getInterpolatedU(16), texture.getInterpolatedV(16 * (1 - filled)));

    this.faceQuads.put(side, ImmutableList.of(quadBuilder.build()));

    // west

    side = Direction.WEST;

    quadBuilder = new UnpackedBakedQuad.Builder(this.format);
    quadBuilder.setQuadOrientation(side);
    quadBuilder.setTexture(texture);

    this.putVertex(quadBuilder, side, x[0] + offset, yOffset + y, z[0], texture.getInterpolatedU(0), texture.getInterpolatedV(16 * (1 - filled)));
    this.putVertex(quadBuilder, side, x[0] + offset, yOffset + offset, z[0], texture.getInterpolatedU(0), texture.getInterpolatedV(16));
    this.putVertex(quadBuilder, side, x[1] + offset, yOffset + offset, z[1], texture.getInterpolatedU(16), texture.getInterpolatedV(16));
    this.putVertex(quadBuilder, side, x[1] + offset, yOffset + y, z[1], texture.getInterpolatedU(16), texture.getInterpolatedV(16 * (1 - filled)));

    this.faceQuads.put(side, ImmutableList.of(quadBuilder.build()));

    // south

    side = Direction.SOUTH;

    quadBuilder = new UnpackedBakedQuad.Builder(this.format);
    quadBuilder.setQuadOrientation(side);
    quadBuilder.setTexture(texture);

    this.putVertex(quadBuilder, side, x[1], yOffset + y, z[1] - offset, texture.getInterpolatedU(0), texture.getInterpolatedV(16 * (1 - filled)));
    this.putVertex(quadBuilder, side, x[1], yOffset + offset, z[1] - offset, texture.getInterpolatedU(0), texture.getInterpolatedV(16));
    this.putVertex(quadBuilder, side, x[2], yOffset + offset, z[2] - offset, texture.getInterpolatedU(16), texture.getInterpolatedV(16));
    this.putVertex(quadBuilder, side, x[2], yOffset + y, z[2] - offset, texture.getInterpolatedU(16), texture.getInterpolatedV(16 * (1 - filled)));

    this.faceQuads.put(side, ImmutableList.of(quadBuilder.build()));

    // north

    side = Direction.NORTH;

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
  public List<BakedQuad> getQuads(@Nullable final BlockState state, @Nullable final Direction side, final Random rand) {
    if(side != null) {
      return this.faceQuads.get(side);
    }

    return this.faceQuads.get(Direction.UP);
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
    final String fluidTextureLoc = (
      this.fluid.getStill() != null ?
        this.fluid.getStill() :
        this.fluid.getFlowing() != null ?
          this.fluid.getFlowing() :
          null //TODO FluidRegistry.WATER.getStill()
    ).toString();

    return Minecraft.getInstance().getTextureMap().getAtlasSprite(fluidTextureLoc);
  }

  @SuppressWarnings("deprecation")
  @Override
  public ItemCameraTransforms getItemCameraTransforms() {
    return ItemCameraTransforms.DEFAULT;
  }

  @Override
  public ItemOverrideList getOverrides() {
    return ItemOverrideList.EMPTY;
  }

  private void putVertex(final UnpackedBakedQuad.Builder builder, final Direction side, final float x, final float y, final float z, final float u, final float v) {
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
