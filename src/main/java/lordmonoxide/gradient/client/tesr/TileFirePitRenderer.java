package lordmonoxide.gradient.client.tesr;

import com.mojang.blaze3d.platform.GlStateManager;
import lordmonoxide.gradient.blocks.BlockFirePit;
import lordmonoxide.gradient.tileentities.TileFirePit;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TileFirePitRenderer extends TileEntityRenderer<TileFirePit> {
  @Override
  public void render(final TileFirePit firepit, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
    final BlockState state = firepit.getBlockState();

    GlStateManager.pushMatrix();
    GlStateManager.translated(x + 0.5d, y + 0.5d, z + 0.5d);

    final Direction facing = state.get(BlockFirePit.FACING);
    final double facingAngle = Math.toRadians(facing.getHorizontalAngle());

    final double fuelAngleOffset = firepit.hasFurnace(state) ? Math.PI / 2 : 0.0d;

    for(int slot = 0; slot < TileFirePit.FUEL_SLOTS_COUNT; slot++) {
      if(firepit.hasFuel(slot)) {
        final ItemStack fuel = firepit.getFuel(slot);

        final double angle = (5 - slot) * Math.PI / 4 + facingAngle - fuelAngleOffset;
        final double inputX = Math.cos(angle) * 0.25d;
        final double inputZ = Math.sin(angle) * 0.25d;

        GlStateManager.pushMatrix();
        GlStateManager.translated(inputX, -0.4375d, inputZ);
        GlStateManager.rotatef(-facing.getHorizontalAngle(), 0.0f, 1.0f, 0.0f);
        GlStateManager.scalef(0.5f, 0.5f, 0.5f);
        Minecraft.getInstance().getItemRenderer().renderItem(fuel, ItemCameraTransforms.TransformType.GROUND);
        GlStateManager.popMatrix();
      }
    }

    if(firepit.hasInput()) {
      final ItemStack input = firepit.getInput();

      GlStateManager.pushMatrix();

      GlStateManager.translated(0.0d, -0.3125d, 0.0d);
      GlStateManager.rotatef(-facing.getHorizontalAngle(), 0.0f, 1.0f, 0.0f);
      GlStateManager.scalef(0.5f, 0.5f, 0.5f);
      Minecraft.getInstance().getItemRenderer().renderItem(input, ItemCameraTransforms.TransformType.GROUND);

      GlStateManager.popMatrix();
    }

    if(firepit.hasOutput()) {
      final ItemStack output = firepit.getOutput();

      final double inputX = Math.cos(facingAngle) * 0.25f;
      final double inputZ = Math.sin(facingAngle) * 0.25f;

      GlStateManager.pushMatrix();
      GlStateManager.translated(inputX, -0.40625d, inputZ);

      if(output.getCount() > 1) {
        this.drawNameplate(firepit, Integer.toString(output.getCount()), -0.5d, -1.05d, -0.5d, 16);
      }

      GlStateManager.rotatef(-facing.getHorizontalAngle(), 0.0f, 1.0f, 0.0f);
      GlStateManager.scalef(0.5f, 0.5f, 0.5f);
      Minecraft.getInstance().getItemRenderer().renderItem(output, ItemCameraTransforms.TransformType.GROUND);

      GlStateManager.popMatrix();
    }

    GlStateManager.popMatrix();
  }
}
