package lordmonoxide.gradient.client.tesr;

import com.mojang.blaze3d.platform.GlStateManager;
import lordmonoxide.gradient.blocks.BlockClayOvenHardened;
import lordmonoxide.gradient.tileentities.TileClayOven;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TileClayOvenRenderer extends TileEntityRenderer<TileClayOven> {
  @Override
  public void render(final TileClayOven oven, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
    GlStateManager.pushMatrix();
    GlStateManager.translated(x + 0.5d, y + 0.5d, z + 0.5d);

    final Direction facing = oven.getBlockState().get(BlockClayOvenHardened.FACING);

    if(oven.hasInput()) {
      final ItemStack input = oven.getInput();

      GlStateManager.pushMatrix();

      GlStateManager.translated(0.0d, -0.40625d, 0.0d);
      GlStateManager.rotatef(-facing.getHorizontalAngle(), 0.0f, 1.0f, 0.0f);
      GlStateManager.scalef(0.5f, 0.5f, 0.5f);
      Minecraft.getInstance().getItemRenderer().renderItem(input, ItemCameraTransforms.TransformType.GROUND);

      GlStateManager.popMatrix();
    }

    if(oven.hasOutput()) {
      final ItemStack output = oven.getOutput();

      GlStateManager.pushMatrix();

      if(output.getCount() > 1) {
        this.drawNameplate(oven, Integer.toString(output.getCount()), -0.5d, -1.05d, -0.5d, 16);
      }

      GlStateManager.translated(0.0d, -0.0625d, 0.0d);
      GlStateManager.rotatef(-facing.getHorizontalAngle(), 0.0f, 1.0f, 0.0f);
      GlStateManager.scalef(0.5f, 0.5f, 0.5f);
      Minecraft.getInstance().getItemRenderer().renderItem(output, ItemCameraTransforms.TransformType.GROUND);

      GlStateManager.popMatrix();
    }

    GlStateManager.popMatrix();
  }
}
