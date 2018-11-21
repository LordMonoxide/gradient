package lordmonoxide.gradient.blocks;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumFacing;

public class BlockHardenedLog extends BlockRotatedPillar {
  public BlockHardenedLog() {
    super(Material.WOOD);
    this.setRegistryName("hardened_log");
    this.setTranslationKey("hardened_log");
    this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.Y));
    this.setHarvestLevel("axe", 0);
    this.setHardness(2.0f);
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, AXIS);
  }
}
