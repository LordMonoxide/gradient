package lordmonoxide.gradient.client.tesr;

import lordmonoxide.gradient.blocks.BlockGrindstone;
import lordmonoxide.gradient.tileentities.TileManualGrinder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TileManualGrinderRenderer extends TileEntityRenderer<TileManualGrinder> {
  @Override
  public void render(final TileManualGrinder te, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
    GlStateManager.pushMatrix();
    GlStateManager.translated(x + 0.5d, y + 0.5d, z + 0.5d);

    final EnumFacing facing = te.getBlockState().get(BlockGrindstone.FACING);

    if(te.hasInput()) {
      GlStateManager.pushMatrix();

      switch(facing) {
        case NORTH:
          GlStateManager.translatef(0.25f, -0.25f, 0.0f);
          break;

        case SOUTH:
          GlStateManager.translatef(-0.25f, -0.25f, 0.0f);
          break;

        case EAST:
          GlStateManager.translatef(0.0f, -0.25f, 0.25f);
          break;

        case WEST:
          GlStateManager.translatef(0.0f, -0.25f, -0.25f);
          break;
      }

      final ItemStack input = te.getInput();

      if(input.getCount() > 1) {
        this.drawNameplate(te, Integer.toString(input.getCount()), -0.5d, -1.05d, -0.5d, 16);
      }

      GlStateManager.scalef(0.5f, 0.5f, 0.5f);
      GlStateManager.rotatef(-facing.getHorizontalAngle(), 0.0f, 1.0f, 0.0f);
      Minecraft.getInstance().getItemRenderer().renderItem(input, ItemCameraTransforms.TransformType.GROUND);
      GlStateManager.popMatrix();
    }

    if(te.hasOutput()) {
      GlStateManager.pushMatrix();

      switch(facing) {
        case NORTH:
          GlStateManager.translatef(-0.25f, -0.25f, 0.0f);
          break;

        case SOUTH:
          GlStateManager.translatef(0.25f, -0.25f, 0.0f);
          break;

        case EAST:
          GlStateManager.translatef(0.0f, -0.25f, -0.25f);
          break;

        case WEST:
          GlStateManager.translatef(0.0f, -0.25f, 0.25f);
          break;
      }

      final ItemStack output = te.getOutput();

      if(output.getCount() > 1) {
        this.drawNameplate(te, Integer.toString(output.getCount()), -0.5d, -1.05d, -0.5d, 16);
      }

      GlStateManager.scalef(0.5f, 0.5f, 0.5f);
      GlStateManager.rotatef(-facing.getHorizontalAngle(), 0.0f, 1.0f, 0.0f);
      Minecraft.getInstance().getItemRenderer().renderItem(te.getOutput(), ItemCameraTransforms.TransformType.GROUND);
      GlStateManager.popMatrix();
    }

    GlStateManager.popMatrix();
  }
}
