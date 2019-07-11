package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockSalt extends Block {
  public BlockSalt() {
    super(Properties.create(Material.SAND, MaterialColor.QUARTZ).hardnessAndResistance(0.5f).sound(SoundType.SAND));
  }

  @Override
  public int getItemsToDropCount(final BlockState state, final int fortune, final World world, final BlockPos pos, final Random random) {
    return random.nextInt(4 + fortune * 2) + 1;
  }

  @Override
  public IItemProvider getItemDropped(final BlockState state, final World world, final BlockPos pos, final int fortune) {
    return GradientItems.SALT;
  }
}
