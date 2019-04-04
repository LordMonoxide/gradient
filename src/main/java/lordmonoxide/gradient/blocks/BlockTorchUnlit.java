package lordmonoxide.gradient.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockTorchUnlit extends BlockTorch {
  public final Block lit;

  public BlockTorchUnlit(final String name, final Block lit, final Properties properties) {
    super(properties.sound(SoundType.WOOD));
    this.setRegistryName(name);

    this.lit = lit;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(final ItemStack stack, @Nullable final IBlockReader world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
    tooltip.add(new TextComponentTranslation(this.getTranslationKey() + ".tooltip"));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void animateTick(final IBlockState state, final World world, final BlockPos pos, final Random rand) {
    // No particles
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean onBlockActivated(final IBlockState state, final World world, final BlockPos pos, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
    if(!world.isRemote || !player.isSneaking()) {
      if(this.isLitTorch(player.getHeldItemMainhand()) || this.isLitTorch(player.getHeldItemOffhand())) {
        world.setBlockState(pos, this.lit.getDefaultState());
        return true;
      }
    }

    return false;
  }

  private boolean isLitTorch(final ItemStack stack) {
    if(!(stack.getItem() instanceof ItemBlock)) {
      return false;
    }

    final ItemBlock itemBlock = (ItemBlock)stack.getItem();

    return itemBlock.getBlock() instanceof BlockTorch && !(itemBlock.getBlock() instanceof BlockTorchUnlit);
  }
}
