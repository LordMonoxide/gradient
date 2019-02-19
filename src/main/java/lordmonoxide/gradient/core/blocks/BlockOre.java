package lordmonoxide.gradient.core.blocks;

import lordmonoxide.gradient.core.geology.ores.Ore;
import lordmonoxide.gradient.core.tileentities.TileOre;
import lordmonoxide.gradient.core.utils.NbtUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

//TODO:
// Silk touch?
// Pick block?

public class BlockOre extends Block {
  public BlockOre() {
    super(Properties.create(Material.ROCK).hardnessAndResistance(3.0f));
  }

  @Override
  @Deprecated
  @SuppressWarnings("deprecation")
  public float getBlockHardness(final IBlockState blockState, final IBlockReader world, final BlockPos pos) {
    //TODO: dynamic
    return this.blockHardness;
  }

  private final Map<IBlockState, Ore> oreMap = new HashMap<>();

  @SuppressWarnings("deprecation")
  @Deprecated
  @Override
  public void onReplaced(final IBlockState state, final World world, final BlockPos pos, final IBlockState newState, final boolean isMoving) {
    final TileEntity tile = world.getTileEntity(pos);

    if(tile instanceof TileOre) {
      this.oreMap.put(state, ((TileOre)tile).getOre());
    }

    super.onReplaced(state, world, pos, newState, isMoving);
  }

  @Override
  public void getDrops(final IBlockState state, final NonNullList<ItemStack> drops, final World world, final BlockPos pos, final int fortune) {
    final ItemStack stack = new ItemStack(this);
    final int count = this.getItemsToDropCount(state, fortune, world, pos, world.rand);

    final Ore ore = this.oreMap.remove(state);

    if(ore != null) {
      final NBTTagCompound tag = stack.getOrCreateChildTag("BlockEntityTag");
      tag.setTag("ForgeData", NbtUtil.setResourceLocation(new NBTTagCompound(), ore.name));
    }

    for(int i = 0; i < count; i++) {
      drops.add(stack);
    }
  }


  @Override
  public boolean hasTileEntity(final IBlockState state) {
    return true;
  }

  @Override
  @Nullable
  public TileEntity createTileEntity(final IBlockState state, final IBlockReader world) {
    return new TileOre();
  }
}
