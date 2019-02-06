package lordmonoxide.gradient.blocks;

import lordmonoxide.gradient.items.GradientItems;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import java.util.Random;

public class BlockSalt extends GradientBlock {
  public BlockSalt() {
    super("salt_block", CreativeTabs.FOOD, Material.SAND, MapColor.QUARTZ);
    this.setHardness(0.5f);
    this.setSoundType(SoundType.SAND);
  }

  @Override
  public int quantityDropped(final IBlockState state, final int fortune, final Random random) {
    return random.nextInt(4 + fortune * 2) + 1;
  }

  @Override
  public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
    return GradientItems.SALT;
  }
}
