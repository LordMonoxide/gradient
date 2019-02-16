package lordmonoxide.gradient.core.items;

import lordmonoxide.gradient.core.geology.ores.Ore;
import lordmonoxide.gradient.core.geology.ores.Ores;
import lordmonoxide.gradient.core.utils.NbtUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemOre extends ItemBlock {
  public ItemOre(final Block block, final Builder builder) {
    super(block, builder);
  }

  @Override
  public String getTranslationKey(final ItemStack stack) {
    final NBTTagCompound tile = stack.getChildTag("BlockEntityTag");

    if(tile == null || !tile.hasKey("ForgeData")) {
      return super.getTranslationKey(stack);
    }

    final NBTTagCompound ore = tile.getCompound("ForgeData");
    return "item." + ore.getString("namespace") + ".ore." + ore.getString("path").replace('/', '.');
  }

  @Override
  public void fillItemGroup(final ItemGroup group, final NonNullList<ItemStack> items) {
    if(this.isInGroup(group)) {
      for(final Ore ore : Ores.all()) {
        final ItemStack stack = new ItemStack(this);
        final NBTTagCompound tag = stack.getOrCreateChildTag("BlockEntityTag");
        tag.setTag("ForgeData", NbtUtil.setResourceLocation(new NBTTagCompound(), ore.name));

        items.add(stack);
      }
    }
  }

  @Override
  protected boolean onBlockPlaced(final BlockPos pos, final World world, @Nullable final EntityPlayer player, final ItemStack stack, final IBlockState state) {
    final boolean ret = setTileEntityNBT(world, player, pos, stack);
    world.notifyBlockUpdate(pos, state, state, 3);
    return ret;
  }
}
