package lordmonoxide.gradient.client.tesr;

import lordmonoxide.gradient.tileentities.TileClayOven;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileClayOvenRenderer extends TileEntitySpecialRenderer<TileClayOven> {
  @Override
  public void render(final TileClayOven oven, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
    GlStateManager.pushMatrix();
    GlStateManager.translate(x + 0.5d, y + 0.5d, z + 0.5d);

    final EnumFacing facing = EnumFacing.byHorizontalIndex(oven.getBlockMetadata());

    if(oven.hasInput()) {
      final ItemStack input = oven.getInput();

      GlStateManager.pushMatrix();

      GlStateManager.translate(0.0d, -0.40625d, -0.25d);
      GlStateManager.rotate(-facing.getHorizontalAngle(), 0.0f, 1.0f, 0.0f);

      GlStateManager.pushMatrix();
      GlStateManager.scale(0.5f, 0.5f, 0.5f);
      Minecraft.getMinecraft().getRenderItem().renderItem(input, ItemCameraTransforms.TransformType.GROUND);
      GlStateManager.popMatrix();

      if(oven.isCooking()) {
        GlStateManager.translate(-0.1f, 0.0f, 0.05f);
        GlStateManager.scale(0.2f * (1.0f - oven.getCookingPercent()), 0.025f, 1.0f);
        GlStateManager.disableCull();
        this.setLightmapDisabled(true);
        GlStateManager.disableLighting();
        Gui.drawRect(0, 0, 1, 1, 0xFF1AFF00);
        GlStateManager.enableLighting();
        this.setLightmapDisabled(false);
        GlStateManager.enableCull();
      }

      GlStateManager.popMatrix();
    }

    if(oven.hasOutput()) {
      final ItemStack output = oven.getOutput();

      GlStateManager.pushMatrix();

      if(output.getCount() > 1) {
        this.drawNameplate(oven, Integer.toString(output.getCount()), -0.5d, -1.05d, -0.5d, 16);
      }

      GlStateManager.translate(0.0d, -0.0625d, 0.0d);
      GlStateManager.rotate(-facing.getHorizontalAngle(), 0.0f, 1.0f, 0.0f);
      GlStateManager.scale(0.5f, 0.5f, 0.5f);
      Minecraft.getMinecraft().getRenderItem().renderItem(output, ItemCameraTransforms.TransformType.GROUND);

      GlStateManager.popMatrix();
    }

    GlStateManager.popMatrix();
  }
}
