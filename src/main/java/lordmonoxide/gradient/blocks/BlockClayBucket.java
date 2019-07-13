package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.GradientMaterials;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BlockClayBucket extends Block {
  private static final VoxelShape SHAPE = Block.makeCuboidShape(3.0d, 0.0d, 3.0d, 13.0d, 8.0d, 13.0d);

  public static BlockClayBucket hardened() {
    return new BlockClayBucket(true);
  }

  public static BlockClayBucket unhardened() {
    return new BlockClayBucket(false);
  }

  private final boolean hardened;

  protected BlockClayBucket(final boolean hardened) {
    super(Properties.create(hardened ? GradientMaterials.MATERIAL_CLAY_MACHINE : Material.CLAY).hardnessAndResistance(1.0f, hardened ? 5.0f : 2.0f));
    this.hardened = hardened;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(final ItemStack stack, @Nullable final IBlockReader world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
    super.addInformation(stack, world, tooltip, flag);

    if(!this.hardened) {
      tooltip.add(new TranslationTextComponent("unhardened_clay.tooltip"));
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  @Deprecated
  public VoxelShape getShape(final BlockState state, final IBlockReader world, final BlockPos pos, final ISelectionContext context) {
    return SHAPE;
  }
}
