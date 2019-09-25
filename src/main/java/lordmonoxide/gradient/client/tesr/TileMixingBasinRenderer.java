package lordmonoxide.gradient.client.tesr;

import lordmonoxide.gradient.blocks.BlockMixingBasin;
import lordmonoxide.gradient.tileentities.TileMixingBasin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileMixingBasinRenderer extends TileEntitySpecialRenderer<TileMixingBasin> {
  private final ItemStack water = new ItemStack(Items.WATER_BUCKET);

  @Override
  public void render(final TileMixingBasin te, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
    final IBlockState state = te.getWorld().getBlockState(te.getPos());

    if(!(state.getBlock() instanceof BlockMixingBasin)) {
      return;
    }

    GlStateManager.pushMatrix();
    GlStateManager.translate((float)x + 0.5f, (float)y + 0.5f, (float)z + 0.5f);

    final EnumFacing facing = EnumFacing.byHorizontalIndex(te.getBlockMetadata());
    final double facingAngle = Math.toRadians(facing.getHorizontalAngle());

    for(int slot = 0; slot < TileMixingBasin.INPUT_SIZE; slot++) {
      if(te.hasInput(slot)) {
        final ItemStack input = te.getInput(slot);

        GlStateManager.pushMatrix();

        final double angle = (6 - slot) * Math.PI / 4 + facingAngle;
        final float inputX = (float)Math.cos(angle) * 0.2f;
        final float inputZ = (float)Math.sin(angle) * 0.2f;

        GlStateManager.translate(inputX, -0.15f, inputZ);
        GlStateManager.rotate(-facing.getHorizontalAngle(), 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        Minecraft.getMinecraft().getRenderItem().renderItem(input, ItemCameraTransforms.TransformType.GROUND);

        GlStateManager.popMatrix();
      }
    }

    if(te.hasOutput()) {
      final ItemStack output = te.getOutput();

      GlStateManager.pushMatrix();

      final float inputX = (float)Math.cos(facingAngle) * 0.2f;
      final float inputZ = (float)Math.sin(facingAngle) * 0.2f;

      GlStateManager.translate(inputX, -0.15f, inputZ);

      if(output.getCount() > 1) {
        this.drawNameplate(te, Integer.toString(output.getCount()), -0.5d, -1.05d, -0.5d, 16);
      }

      GlStateManager.rotate(-facing.getHorizontalAngle(), 0.0f, 1.0f, 0.0f);
      GlStateManager.scale(0.5f, 0.5f, 0.5f);
      Minecraft.getMinecraft().getRenderItem().renderItem(output, ItemCameraTransforms.TransformType.GROUND);

      GlStateManager.popMatrix();
    }

    GlStateManager.popMatrix();
  }
}
