package lordmonoxide.gradient.core.blocks;

import lordmonoxide.gradient.core.items.CoreItems;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockSalt extends Block {
  public BlockSalt() {
    super(Builder.create(Material.SAND, MapColor.QUARTZ).hardnessAndResistance(0.5f).sound(SoundType.SAND));
  }

  @Override
  public int getItemsToDropCount(final IBlockState state, final int fortune, final World world, final BlockPos pos, final Random random) {
    return random.nextInt(4 + fortune * 2) + 1;
  }

  @Override
  public IItemProvider getItemDropped(final IBlockState state, final World world, final BlockPos pos, final int fortune) {
    return CoreItems.SALT;
  }
}
