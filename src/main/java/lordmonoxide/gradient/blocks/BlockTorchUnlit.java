package lordmonoxide.gradient.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.TorchBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class BlockTorchUnlit extends TorchBlock {
  public final Supplier<Block> lit;

  public BlockTorchUnlit(final Supplier<Block> lit, final Properties properties) {
    super(properties.sound(SoundType.WOOD));
    this.lit = lit;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(final ItemStack stack, @Nullable final IBlockReader world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
    tooltip.add(new TranslationTextComponent(this.getTranslationKey() + ".tooltip"));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void animateTick(final BlockState state, final World world, final BlockPos pos, final Random rand) {
    // No particles
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult hit) {
    if(!world.isRemote || !player.isSneaking()) {
      if(this.isLitTorch(player.getHeldItemMainhand()) || this.isLitTorch(player.getHeldItemOffhand())) {
        world.setBlockState(pos, this.lit.get().getDefaultState());
        return true;
      }
    }

    return false;
  }

  private boolean isLitTorch(final ItemStack stack) {
    if(!(stack.getItem() instanceof BlockItem)) {
      return false;
    }

    final BlockItem itemBlock = (BlockItem)stack.getItem();

    return itemBlock.getBlock() instanceof TorchBlock && !(itemBlock.getBlock() instanceof BlockTorchUnlit);
  }
}
