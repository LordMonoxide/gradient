package lordmonoxide.gradient.worldgen;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class DeferredOreStorage extends WorldSavedData {
  private static final String DATA_NAME = GradientMod.MODID + "_deferred_ore";

  public static DeferredOreStorage get(final World world) {
    return world.getSavedDataStorage().get(world.getDimension().getType(), DeferredOreStorage::new, DATA_NAME);
  }

  private final Map<ChunkPos, Map<BlockPos, IBlockState>> deferredOres = new HashMap<>();

  public DeferredOreStorage(final String name) {
    super(name);
  }

  public boolean has(final ChunkPos pos) {
    return this.deferredOres.containsKey(pos);
  }

  public Map<BlockPos, IBlockState> get(final ChunkPos pos) {
    if(!this.deferredOres.containsKey(pos)) {
      final Map<BlockPos, IBlockState> deferred = new HashMap<>();
      this.deferredOres.put(pos, deferred);
      return deferred;
    }

    return this.deferredOres.get(pos);
  }

  public void remove(final ChunkPos pos) {
    this.deferredOres.remove(pos);
  }

  @Override
  public void read(final NBTTagCompound nbt) {
    this.deferredOres.clear();

    final NBTTagList chunkList = nbt.getList("chunks", Constants.NBT.TAG_COMPOUND);

    for(final INBTBase chunkBase : chunkList) {
      final NBTTagCompound chunkNbt = (NBTTagCompound)chunkBase;

      final ChunkPos chunkPos = new ChunkPos(chunkNbt.getInt("x"), chunkNbt.getInt("z"));

      final Map<BlockPos, IBlockState> ores = new HashMap<>();
      this.deferredOres.put(chunkPos, ores);

      final NBTTagList oreList = chunkNbt.getList("ores", Constants.NBT.TAG_COMPOUND);

      for(final INBTBase oreBase : oreList) {
        final NBTTagCompound oreNbt = (NBTTagCompound)oreBase;

        final BlockPos blockPos = NBTUtil.readBlockPos(oreNbt.getCompound("pos"));
        final IBlockState ore = NBTUtil.readBlockState(oreNbt.getCompound("ore"));

        ores.put(blockPos, ore);
      }
    }
  }

  @Override
  public NBTTagCompound write(final NBTTagCompound compound) {
    final NBTTagList chunkList = new NBTTagList();
    compound.put("chunks", chunkList);

    this.deferredOres.forEach((chunkPos, ores) -> {
      final NBTTagCompound chunkNbt = new NBTTagCompound();
      chunkList.add(chunkNbt);

      chunkNbt.putInt("x", chunkPos.x);
      chunkNbt.putInt("z", chunkPos.z);

      final NBTTagList oreList = new NBTTagList();
      chunkNbt.put("ores", oreList);

      ores.forEach((blockPos, ore) -> {
        final NBTTagCompound oreNbt = new NBTTagCompound();
        oreList.add(oreNbt);

        oreNbt.put("pos", NBTUtil.writeBlockPos(blockPos));
        oreNbt.put("ore", NBTUtil.writeBlockState(ore));
      });
    });

    return compound;
  }
}
