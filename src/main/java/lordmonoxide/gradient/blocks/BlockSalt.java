package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockSalt extends GradientBlock {
  public BlockSalt() {
    super("salt_block", Properties.create(Material.SAND, MaterialColor.QUARTZ).hardnessAndResistance(0.5f).sound(SoundType.SAND));
  }

  @Override
  public int getItemsToDropCount(final IBlockState state, final int fortune, final World world, final BlockPos pos, final Random random) {
    return random.nextInt(4 + fortune * 2) + 1;
  }

  @Override
  public Item asItem() {
    return GradientItems.SALT;
  }
}
