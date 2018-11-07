package lordmonoxide.gradient.blocks.firepit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TileFirePitRenderer extends TileEntitySpecialRenderer<TileFirePit> {
  @Override
  public void render(final TileFirePit firepit, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
    GlStateManager.pushMatrix();
    GlStateManager.translate((float)x + 0.5f, (float)y + 0.5f, (float)z + 0.5f);

    final EnumFacing facing = EnumFacing.byHorizontalIndex(firepit.getBlockMetadata());
    final double facingAngle = Math.toRadians(facing.getHorizontalAngle());

    for(int slot = 0; slot < TileFirePit.FUEL_SLOTS_COUNT; slot++) {
      if(firepit.hasFuel(slot)) {
        final ItemStack fuel = firepit.getFuel(slot);

        final double angle = (5 - slot) * Math.PI / 4 + facingAngle;
        final float inputX = (float)Math.cos(angle) * 0.25f;
        final float inputZ = (float)Math.sin(angle) * 0.25f;

        GlStateManager.pushMatrix();
        GlStateManager.translate(inputX, -0.25f, inputZ);
        GlStateManager.rotate(-facing.getHorizontalAngle(), 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        Minecraft.getMinecraft().getRenderItem().renderItem(fuel, ItemCameraTransforms.TransformType.GROUND);
        GlStateManager.popMatrix();
      }
    }

    if(firepit.hasInput()) {
      final ItemStack input = firepit.getInput();

      GlStateManager.pushMatrix();

      GlStateManager.translate(0.0f, -0.25f, 0.0f);
      GlStateManager.rotate(-facing.getHorizontalAngle(), 0.0f, 1.0f, 0.0f);
      GlStateManager.scale(0.5f, 0.5f, 0.5f);
      Minecraft.getMinecraft().getRenderItem().renderItem(input, ItemCameraTransforms.TransformType.GROUND);

      GlStateManager.popMatrix();
    }

    if(firepit.hasOutput()) {
      final ItemStack output = firepit.getOutput();

      final float inputX = (float)Math.cos(facingAngle) * 0.25f;
      final float inputZ = (float)Math.sin(facingAngle) * 0.25f;

      GlStateManager.pushMatrix();
      GlStateManager.translate(inputX, -0.25f, inputZ);

      if(output.getCount() > 1) {
        this.drawNameplate(firepit, Integer.toString(output.getCount()), -0.5d, -1.05d, -0.5d, 16);
      }

      GlStateManager.rotate(-facing.getHorizontalAngle(), 0.0f, 1.0f, 0.0f);
      GlStateManager.scale(0.5f, 0.5f, 0.5f);
      Minecraft.getMinecraft().getRenderItem().renderItem(output, ItemCameraTransforms.TransformType.GROUND);

      GlStateManager.popMatrix();
    }

    GlStateManager.popMatrix();
  }
}
