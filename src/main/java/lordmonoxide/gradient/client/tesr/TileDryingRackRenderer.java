package lordmonoxide.gradient.client.tesr;

import com.mojang.blaze3d.platform.GlStateManager;
import lordmonoxide.gradient.blocks.BlockDryingRack;
import lordmonoxide.gradient.tileentities.TileDryingRack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TileDryingRackRenderer extends TileEntityRenderer<TileDryingRack> {
  @Override
  public void render(final TileDryingRack rack, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
    GlStateManager.pushMatrix();
    GlStateManager.translated(x + 0.5d, y + 0.5d, z + 0.5d);

    final BlockState state = rack.getBlockState();
    final Direction facing = state.get(BlockDryingRack.FACING);
    final boolean roof = state.get(BlockDryingRack.ROOF);

    final float facingAngle = facing.getHorizontalAngle();

    if(rack.hasItem()) {
      final ItemStack input = rack.getItem();

      GlStateManager.pushMatrix();

      if(!roof) {
        final double amount = -7.0d / 16.0d;
        final double angle = Math.toRadians(facingAngle) - Math.PI / 2.0d;
        GlStateManager.translated(Math.cos(angle) * amount, 0.0d, Math.sin(angle) * amount);
      }

      GlStateManager.rotatef(-facingAngle, 0.0f, 1.0f, 0.0f);
      Minecraft.getInstance().getItemRenderer().renderItem(input, ItemCameraTransforms.TransformType.NONE);

      GlStateManager.popMatrix();
    }

    GlStateManager.popMatrix();
  }
}
