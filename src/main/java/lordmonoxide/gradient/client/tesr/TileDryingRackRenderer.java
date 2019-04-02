package lordmonoxide.gradient.client.tesr;

import lordmonoxide.gradient.blocks.BlockDryingRack;
import lordmonoxide.gradient.tileentities.TileDryingRack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileDryingRackRenderer extends TileEntitySpecialRenderer<TileDryingRack> {
  @Override
  public void render(final TileDryingRack rack, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
    GlStateManager.pushMatrix();
    GlStateManager.translate(x + 0.5d, y + 0.5d, z + 0.5d);

    final IBlockState state = rack.getWorld().getBlockState(rack.getPos());
    final EnumFacing facing = state.getValue(BlockDryingRack.FACING);
    final boolean roof = state.getValue(BlockDryingRack.ROOF);

    final float facingAngle = facing.getHorizontalAngle();

    if(rack.hasItem()) {
      final ItemStack input = rack.getItem();

      GlStateManager.pushMatrix();

      if(!roof) {
        final double amount = -7.0d / 16.0d;
        final double angle = Math.toRadians(facingAngle) - Math.PI / 2.0d;
        GlStateManager.translate(Math.cos(angle) * amount, 0.0d, Math.sin(angle) * amount);
      }

      GlStateManager.rotate(-facingAngle, 0.0f, 1.0f, 0.0f);
      Minecraft.getMinecraft().getRenderItem().renderItem(input, ItemCameraTransforms.TransformType.NONE);

      GlStateManager.popMatrix();
    }

    GlStateManager.popMatrix();
  }
}
