package lordmonoxide.gradient.blocks.manualgrinder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TileManualGrinderRenderer extends TileEntitySpecialRenderer<TileManualGrinder> {
  @Override
  public void render(final TileManualGrinder te, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
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
}
