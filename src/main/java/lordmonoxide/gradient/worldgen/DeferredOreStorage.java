package lordmonoxide.gradient.worldgen;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class DeferredOreStorage extends WorldSavedData {
  private static final String DATA_NAME = GradientMod.MODID + "_deferred_ore";

  public static DeferredOreStorage get(final World world) {
    final MapStorage storage = world.getPerWorldStorage();
    DeferredOreStorage instance = (DeferredOreStorage)storage.getOrLoadData(DeferredOreStorage.class, DATA_NAME);

    if(instance == null) {
      instance = new DeferredOreStorage();
      storage.setData(DATA_NAME, instance);
    }

    return instance;
  }

  private final Map<ChunkPos, Map<BlockPos, IBlockState>> deferredOres = new HashMap<>();

  public DeferredOreStorage() {
    this(DATA_NAME);
  }

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
  public void readFromNBT(final NBTTagCompound nbt) {
    this.deferredOres.clear();

    final NBTTagList chunkList = nbt.getTagList("chunks", Constants.NBT.TAG_COMPOUND);

    for(final NBTBase chunkBase : chunkList) {
      final NBTTagCompound chunkNbt = (NBTTagCompound)chunkBase;

      final ChunkPos chunkPos = new ChunkPos(chunkNbt.getInteger("x"), chunkNbt.getInteger("z"));

      final Map<BlockPos, IBlockState> ores = new HashMap<>();
      this.deferredOres.put(chunkPos, ores);

      final NBTTagList oreList = chunkNbt.getTagList("ores", Constants.NBT.TAG_COMPOUND);

      for(final NBTBase oreBase : oreList) {
        final NBTTagCompound oreNbt = (NBTTagCompound)oreBase;

        final BlockPos blockPos = NBTUtil.getPosFromTag(oreNbt.getCompoundTag("pos"));
        final IBlockState ore = NBTUtil.readBlockState(oreNbt.getCompoundTag("ore"));

        ores.put(blockPos, ore);
      }
    }
  }

  @Override
  public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
    final NBTTagList chunkList = new NBTTagList();
    compound.setTag("chunks", chunkList);

    this.deferredOres.forEach((chunkPos, ores) -> {
      final NBTTagCompound chunkNbt = new NBTTagCompound();
      chunkList.appendTag(chunkNbt);

      chunkNbt.setInteger("x", chunkPos.x);
      chunkNbt.setInteger("z", chunkPos.z);

      final NBTTagList oreList = new NBTTagList();
      chunkNbt.setTag("ores", oreList);

      ores.forEach((blockPos, ore) -> {
        final NBTTagCompound oreNbt = new NBTTagCompound();
        oreList.appendTag(oreNbt);

        oreNbt.setTag("pos", NBTUtil.createPosTag(blockPos));
        oreNbt.setTag("ore", NBTUtil.writeBlockState(new NBTTagCompound(), ore));
      });
    });

    return compound;
  }
}
