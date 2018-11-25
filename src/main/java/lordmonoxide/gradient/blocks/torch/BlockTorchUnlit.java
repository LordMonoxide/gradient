package lordmonoxide.gradient.blocks.torch;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockTorchUnlit extends BlockTorch {
  public final Block lit;

  public BlockTorchUnlit(final String name, final Block lit) {
    this.setRegistryName(name);
    this.setTranslationKey(name);
    this.setHardness(0.0f);
    this.setSoundType(SoundType.WOOD);

    this.lit = lit;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(final ItemStack stack, @Nullable final World world, final List<String> tooltip, final ITooltipFlag flag) {
    tooltip.add(I18n.format(this.getTranslationKey() + ".tooltip"));
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void randomDisplayTick(final IBlockState state, final World world, final BlockPos pos, final Random rand) {
    // No particles
  }

  @Override
  public boolean onBlockActivated(final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
    if(!world.isRemote || !player.isSneaking()) {
      if(this.isLitTorch(player.getHeldItemMainhand()) || this.isLitTorch(player.getHeldItemOffhand())) {
        world.setBlockState(pos, this.lit.getStateForPlacement(world, pos, state.getValue(FACING), 0.0f, 0.0f, 0.0f, 0, player, hand));
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
