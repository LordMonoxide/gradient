package lordmonoxide.terra;

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

public class DeferredGenerationStorage extends WorldSavedData {
  private static final String DATA_NAME = TerraMod.MOD_ID + "_deferred_ore";

  public static DeferredGenerationStorage get(final World world) {
    final DeferredGenerationStorage storage = world.getSavedData(world.getDimension().getType(), DeferredGenerationStorage::new, DATA_NAME);

    if(storage == null) {
      final DeferredGenerationStorage newStorage = new DeferredGenerationStorage(DATA_NAME);
      world.setSavedData(world.getDimension().getType(), DATA_NAME, newStorage);
      return newStorage;
    }

    return storage;
  }

  private final Map<ChunkPos, Deferred> deferred = new HashMap<>();

  public DeferredGenerationStorage(final String name) {
    super(name);
  }

  public boolean has(final ChunkPos pos) {
    return this.deferred.containsKey(pos);
  }

  public Map<BlockPos, IBlockState> getOres(final ChunkPos pos) {
    if(!this.deferred.containsKey(pos)) {
      final Deferred deferred = new Deferred();
      this.deferred.put(pos, deferred);
      return deferred.ores;
    }

    return this.deferred.get(pos).ores;
  }

  public Map<BlockPos, IBlockState> getPebbles(final ChunkPos pos) {
    if(!this.deferred.containsKey(pos)) {
      final Deferred deferred = new Deferred();
      this.deferred.put(pos, deferred);
      return deferred.pebbles;
    }

    return this.deferred.get(pos).pebbles;
  }

  public void remove(final ChunkPos pos) {
    this.deferred.remove(pos);
  }

  @Override
  public void read(final NBTTagCompound nbt) {
    this.deferred.clear();

    final NBTTagList chunkList = nbt.getList("chunks", Constants.NBT.TAG_COMPOUND);

    for(final INBTBase chunkBase : chunkList) {
      final NBTTagCompound chunkNbt = (NBTTagCompound)chunkBase;

      final ChunkPos chunkPos = new ChunkPos(chunkNbt.getInt("x"), chunkNbt.getInt("z"));

      final Map<BlockPos, IBlockState> ores = this.getOres(chunkPos);
      final NBTTagList oreList = chunkNbt.getList("ores", Constants.NBT.TAG_COMPOUND);

      for(final INBTBase oreBase : oreList) {
        final NBTTagCompound oreNbt = (NBTTagCompound)oreBase;

        final BlockPos blockPos = NBTUtil.readBlockPos(oreNbt.getCompound("pos"));
        final IBlockState ore = NBTUtil.readBlockState(oreNbt.getCompound("ore"));

        ores.put(blockPos, ore);
      }

      final Map<BlockPos, IBlockState> pebbles = this.getPebbles(chunkPos);
      final NBTTagList pebbleList = chunkNbt.getList("pebbles", Constants.NBT.TAG_COMPOUND);

      for(final INBTBase pebbleBase : pebbleList) {
        final NBTTagCompound pebbleNbt = (NBTTagCompound)pebbleBase;

        final BlockPos blockPos = NBTUtil.readBlockPos(pebbleNbt.getCompound("pos"));
        final IBlockState pebble = NBTUtil.readBlockState(pebbleNbt.getCompound("pebble"));

        pebbles.put(blockPos, pebble);
      }
    }
  }

  @Override
  public NBTTagCompound write(final NBTTagCompound compound) {
    final NBTTagList chunkList = new NBTTagList();
    compound.put("chunks", chunkList);

    this.deferred.forEach((chunkPos, deferred) -> {
      final NBTTagCompound chunkNbt = new NBTTagCompound();
      chunkList.add(chunkNbt);

      chunkNbt.putInt("x", chunkPos.x);
      chunkNbt.putInt("z", chunkPos.z);

      final NBTTagList oreList = new NBTTagList();
      chunkNbt.put("ores", oreList);

      deferred.ores.forEach((blockPos, ore) -> {
        final NBTTagCompound oreNbt = new NBTTagCompound();
        oreList.add(oreNbt);

        oreNbt.put("pos", NBTUtil.writeBlockPos(blockPos));
        oreNbt.put("ore", NBTUtil.writeBlockState(ore));
      });

      final NBTTagList pebbleList = new NBTTagList();
      chunkNbt.put("pebbles", pebbleList);

      deferred.pebbles.forEach((blockPos, pebble) -> {
        final NBTTagCompound pebbleNbt = new NBTTagCompound();
        pebbleList.add(pebbleNbt);

        pebbleNbt.put("pos", NBTUtil.writeBlockPos(blockPos));
        pebbleNbt.put("pebble", NBTUtil.writeBlockState(pebble));
      });
    });

    return compound;
  }

  private static class Deferred {
    private final Map<BlockPos, IBlockState> ores = new HashMap<>();
    private final Map<BlockPos, IBlockState> pebbles = new HashMap<>();
  }
}
