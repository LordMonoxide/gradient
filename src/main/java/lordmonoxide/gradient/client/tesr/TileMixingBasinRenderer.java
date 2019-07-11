package lordmonoxide.gradient.client.tesr;

import com.mojang.blaze3d.platform.GlStateManager;
import lordmonoxide.gradient.blocks.BlockMixingBasin;
import lordmonoxide.gradient.tileentities.TileMixingBasin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TileMixingBasinRenderer extends TileEntityRenderer<TileMixingBasin> {
  private final ItemStack water = new ItemStack(Items.WATER_BUCKET);

  @Override
  public void render(final TileMixingBasin te, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
    GlStateManager.pushMatrix();
    GlStateManager.translated(x + 0.5d, y + 0.5d, z + 0.5d);

    final Direction facing = te.getBlockState().get(BlockMixingBasin.FACING);
    final double facingAngle = Math.toRadians(facing.getHorizontalAngle());

    if(te.hasFluid()) {
      GlStateManager.pushMatrix();

      GlStateManager.translatef(0.0f, -0.25f, 0.0f);
      GlStateManager.rotatef(-facing.getHorizontalAngle(), 0.0f, 1.0f, 0.0f);
      GlStateManager.scalef(0.5f, 0.5f, 0.5f);
      Minecraft.getInstance().getItemRenderer().renderItem(this.water, ItemCameraTransforms.TransformType.GROUND);

      GlStateManager.popMatrix();
    }

    for(int slot = 0; slot < TileMixingBasin.INPUT_SIZE; slot++) {
      if(te.hasInput(slot)) {
        final ItemStack input = te.getInput(slot);

        GlStateManager.pushMatrix();

        final double angle = (6 - slot) * Math.PI / 4 + facingAngle;
        final double inputX = Math.cos(angle) * 0.25d;
        final double inputZ = Math.sin(angle) * 0.25d;

        GlStateManager.translated(inputX, -0.25d, inputZ);
        GlStateManager.rotatef(-facing.getHorizontalAngle(), 0.0f, 1.0f, 0.0f);
        GlStateManager.scalef(0.5f, 0.5f, 0.5f);
        Minecraft.getInstance().getItemRenderer().renderItem(input, ItemCameraTransforms.TransformType.GROUND);

        GlStateManager.popMatrix();
      }
    }

    if(te.hasOutput()) {
      final ItemStack output = te.getOutput();

      GlStateManager.pushMatrix();

      final double inputX = Math.cos(facingAngle) * 0.25d;
      final double inputZ = Math.sin(facingAngle) * 0.25d;

      GlStateManager.translated(inputX, -0.25d, inputZ);

      if(output.getCount() > 1) {
        this.drawNameplate(te, Integer.toString(output.getCount()), -0.5d, -1.05d, -0.5d, 16);
      }

      GlStateManager.rotatef(-facing.getHorizontalAngle(), 0.0f, 1.0f, 0.0f);
      GlStateManager.scalef(0.5f, 0.5f, 0.5f);
      Minecraft.getInstance().getItemRenderer().renderItem(output, ItemCameraTransforms.TransformType.GROUND);

      GlStateManager.popMatrix();
    }

    GlStateManager.popMatrix();
  }
}
