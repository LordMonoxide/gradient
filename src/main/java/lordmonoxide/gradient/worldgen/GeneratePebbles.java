package lordmonoxide.gradient.worldgen;

import lordmonoxide.gradient.blocks.GradientBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class GeneratePebbles implements IWorldGenerator {
  private static final BiomeDictionary.Type[] spawnIn = {
    BiomeDictionary.Type.SPARSE,
    BiomeDictionary.Type.DRY,
    BiomeDictionary.Type.SAVANNA,
    BiomeDictionary.Type.DEAD,
    BiomeDictionary.Type.RIVER,
    BiomeDictionary.Type.MESA,
    BiomeDictionary.Type.PLAINS,
    BiomeDictionary.Type.MOUNTAIN,
    BiomeDictionary.Type.HILLS,
    BiomeDictionary.Type.SANDY,
    BiomeDictionary.Type.WASTELAND,
    BiomeDictionary.Type.BEACH,
  };

  @Override
  public void generate(final Random random, final int chunkX, final int chunkZ, final World world, final IChunkGenerator chunkGenerator, final IChunkProvider chunkProvider) {
    if(random.nextInt(10) != 0) {
      return;
    }

    final int xChunk = chunkX * 16 + 8;
    final int zChunk = chunkZ * 16 + 8;
    final int xCh = chunkX * 16 + random.nextInt(16);
    final int yCh = random.nextInt(128);
    final int zCh = chunkZ * 16 + random.nextInt(16);

    final Biome biome = world.getBiome(new BlockPos(xChunk + 16, 0, zChunk + 16));

    if(this.isBiomeOfAnyType(biome, spawnIn)) {
      BlockPos pos = new BlockPos(xCh, yCh + 64, zCh);

      for(IBlockState iblockstate = world.getBlockState(pos); (iblockstate.getBlock().isAir(iblockstate, world, pos)) && pos.getY() > 0; iblockstate = world.getBlockState(pos)) {
        pos = pos.down();
      }

      for(int i = 0; i < 32; ++i) {
        final BlockPos blockpos = pos.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));

        if(GradientBlocks.PEBBLE.canPlaceBlockAt(world, blockpos)) {
          world.setBlockState(blockpos, GradientBlocks.PEBBLE.getDefaultState(), 2);
        }
      }
    }
  }

  private boolean isBiomeOfAnyType(final Biome biome, final BiomeDictionary.Type[] types) {
    for(final BiomeDictionary.Type type : types) {
      if(BiomeDictionary.hasType(biome, type)) {
        return true;
      }
    }

    return false;
  }
}
