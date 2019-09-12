package lordmonoxide.gradient.client.tesr;

import lordmonoxide.gradient.blocks.BlockManualGrinder;
import lordmonoxide.gradient.tileentities.TileManualGrinder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.common.animation.Event;
import net.minecraftforge.common.animation.IEventHandler;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileManualGrinderRenderer extends TileEntitySpecialRenderer<TileManualGrinder> implements IEventHandler<TileManualGrinder> {
  private static BlockRendererDispatcher blockRenderer;

  @Override
  public void render(final TileManualGrinder te, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
    final IBlockState state = te.getWorld().getBlockState(te.getPos());

    if(!(state.getBlock() instanceof BlockManualGrinder)) {
      return;
    }

    this.renderAnimated(te, x, y, z, partialTicks);

    GlStateManager.pushMatrix();
    GlStateManager.translate((float)x + 0.5f, (float)y + 0.5f, (float)z + 0.5f);

    final EnumFacing facing = EnumFacing.byHorizontalIndex(te.getBlockMetadata());

    if(te.hasInput()) {
      GlStateManager.pushMatrix();

      switch(facing) {
        case NORTH:
          GlStateManager.translate(0.25f, -0.25f, 0.0f);
          break;

        case SOUTH:
          GlStateManager.translate(-0.25f, -0.25f, 0.0f);
          break;

        case EAST:
          GlStateManager.translate(0.0f, -0.25f, 0.25f);
          break;

        case WEST:
          GlStateManager.translate(0.0f, -0.25f, -0.25f);
          break;
      }

      final ItemStack input = te.getInput();

      if(input.getCount() > 1) {
        this.drawNameplate(te, Integer.toString(input.getCount()), -0.5d, -1.05d, -0.5d, 16);
      }

      GlStateManager.scale(0.5f, 0.5f, 0.5f);
      GlStateManager.rotate(-facing.getHorizontalAngle(), 0.0f, 1.0f, 0.0f);
      Minecraft.getMinecraft().getRenderItem().renderItem(input, ItemCameraTransforms.TransformType.GROUND);
      GlStateManager.popMatrix();
    }

    if(te.hasOutput()) {
      GlStateManager.pushMatrix();

      switch(facing) {
        case NORTH:
          GlStateManager.translate(-0.25f, -0.25f, 0.0f);
          break;

        case SOUTH:
          GlStateManager.translate(0.25f, -0.25f, 0.0f);
          break;

        case EAST:
          GlStateManager.translate(0.0f, -0.25f, -0.25f);
          break;

        case WEST:
          GlStateManager.translate(0.0f, -0.25f, 0.25f);
          break;
      }

      final ItemStack output = te.getOutput();

      if(output.getCount() > 1) {
        this.drawNameplate(te, Integer.toString(output.getCount()), -0.5d, -1.05d, -0.5d, 16);
      }

      GlStateManager.scale(0.5f, 0.5f, 0.5f);
      GlStateManager.rotate(-facing.getHorizontalAngle(), 0.0f, 1.0f, 0.0f);
      Minecraft.getMinecraft().getRenderItem().renderItem(te.getOutput(), ItemCameraTransforms.TransformType.GROUND);
      GlStateManager.popMatrix();
    }

    GlStateManager.popMatrix();
  }

  // Copied from AnimationTESR
  private void renderAnimated(final TileManualGrinder te, final double x, final double y, final double z, final float partialTick) {
    if(!te.hasCapability(CapabilityAnimation.ANIMATION_CAPABILITY, null)) {
      return;
    }

    if(blockRenderer == null) {
      blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
    }

    final Tessellator tessellator = Tessellator.getInstance();
    final BufferBuilder buffer = tessellator.getBuffer();
    this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    RenderHelper.disableStandardItemLighting();
    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    GlStateManager.enableBlend();
    GlStateManager.disableCull();

    if(Minecraft.isAmbientOcclusionEnabled()) {
      GlStateManager.shadeModel(GL11.GL_SMOOTH);
    } else {
      GlStateManager.shadeModel(GL11.GL_FLAT);
    }

    buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

    final BlockPos pos = te.getPos();
    final IBlockAccess world = MinecraftForgeClient.getRegionRenderCache(te.getWorld(), pos);
    IBlockState state = world.getBlockState(pos);
    if(state.getPropertyKeys().contains(Properties.StaticProperty)) {
      state = state.withProperty(Properties.StaticProperty, false);
    }

    if(state instanceof IExtendedBlockState) {
      IExtendedBlockState exState = (IExtendedBlockState)state;

      if(exState.getUnlistedNames().contains(Properties.AnimationProperty)) {
        final float time = Animation.getWorldTime(this.getWorld(), partialTick);
        final IAnimationStateMachine capability = te.getCapability(CapabilityAnimation.ANIMATION_CAPABILITY, null);

        if(capability != null) {
          final Pair<IModelState, Iterable<Event>> pair = capability.apply(time);
          this.handleEvents(te, time, pair.getRight());

          // TODO: caching?
          final IBakedModel model = blockRenderer.getBlockModelShapes().getModelForState(exState.getClean());
          exState = exState.withProperty(Properties.AnimationProperty, pair.getLeft());

          buffer.setTranslation(x - pos.getX(), y - pos.getY(), z - pos.getZ());

          blockRenderer.getBlockModelRenderer().renderModel(world, model, exState, pos, buffer, false);
        }
      }
    }

    buffer.setTranslation(0, 0, 0);

    tessellator.draw();

    RenderHelper.enableStandardItemLighting();
  }

  @Override
  public void handleEvents(final TileManualGrinder instance, final float time, final Iterable<Event> pastEvents) {

  }
}
