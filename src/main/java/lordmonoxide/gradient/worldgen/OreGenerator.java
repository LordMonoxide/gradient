package lordmonoxide.gradient.worldgen;

import lordmonoxide.gradient.blocks.GradientBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenSand;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class OreGenerator implements IWorldGenerator {
  private final WorldGenerator magnesium = new WorldGenMinable(GradientBlocks.ORE_MAGNESIUM.getDefaultState(), 4);
  private final WorldGenerator salt = new WorldGenSand(GradientBlocks.SALT_BLOCK, 4);

  @Override
  public void generate(final Random random, final int chunkX, final int chunkZ, final World world, final IChunkGenerator chunkGenerator, final IChunkProvider chunkProvider) {
    if(world.provider.getDimensionType() == DimensionType.OVERWORLD) {
      this.runGenerator(this.magnesium, world, random, chunkX, chunkZ, 4, 0, 128);

      final ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
      final int x = random.nextInt(16) + 8;
      final int z = random.nextInt(16) + 8;
      this.salt.generate(world, random, world.getTopSolidOrLiquidBlock(chunkPos.getBlock(x, 0, z)));
    }
  }

  private void runGenerator(final WorldGenerator generator, final World world, final Random rand, final int chunkX, final int chunkZ, final int chancesToSpawn, final int minHeight, final int maxHeight) {
    assert minHeight >= 0 && maxHeight <= 256 && minHeight <= maxHeight : "Illegal Height Arguments for WorldGenerator";

    final int heightDiff = maxHeight - minHeight + 1;

    for(int i = 0; i < chancesToSpawn; i ++) {
      final int x = chunkX * 16 + rand.nextInt(16);
      final int y = minHeight + rand.nextInt(heightDiff);
      final int z = chunkZ * 16 + rand.nextInt(16);

      generator.generate(world, rand, new BlockPos(x, y, z));
    }
  }
}
