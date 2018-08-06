package lordmonoxide.gradient.worldgen;

import lordmonoxide.gradient.blocks.GradientBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;
import java.util.function.Function;

public class OreGenerator implements IWorldGenerator {
  private final WorldGenerator magnesium = new WorldGenMinable(GradientBlocks.ORE_MAGNESIUM.getDefaultState(), 4);
  private final WorldOreGenerator carbon = WorldOreGenerator.create(generator -> {
    generator.minLength(25);
    generator.maxLength(35);

    generator.addStage(stage -> {
      stage.ore(Blocks.COAL_ORE.getDefaultState());
      stage.minRadius(8);
      stage.maxRadius(18);
      stage.blockSpawnChance(0.75f);
      stage.stageSpawnChance(0.90f);
    });

    generator.addStage(stage -> {
      stage.ore(Blocks.GLASS.getDefaultState());
      stage.minRadius(0);
      stage.maxRadius(12);
      stage.blockSpawnChance(0.75f);
      stage.stageSpawnChance(0.95f);
    });

    generator.addStage(stage -> {
      stage.ore(Blocks.DIAMOND_ORE.getDefaultState());
      stage.minRadius(0);
      stage.maxRadius(1);
      stage.blockSpawnChance(0.75f);
      stage.stageSpawnChance(0.8f);
    });
  });

  private final WorldOreGenerator coal = WorldOreGenerator.create(generator -> {
    final Function<Integer, Float> scale = depth -> 1.0f / ((depth + 64) / 64.0f);

    generator.minLength(depth -> (int)(scale.apply(depth) * 10));
    generator.maxLength(depth -> (int)(scale.apply(depth) * 30));

    generator.addStage(stage -> {
      stage.ore(Blocks.COAL_ORE.getDefaultState());
      stage.minRadius(0);
      stage.maxRadius(depth -> (int)(scale.apply(depth) * 4));
      stage.blockSpawnChance(1.0f);
    });
  });

  @Override
  public void generate(final Random random, final int chunkX, final int chunkZ, final World world, final IChunkGenerator chunkGenerator, final IChunkProvider chunkProvider) {
    final ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);

    this.carbon.generateDeferredOres(world, chunkPos);
    this.coal.generateDeferredOres(world, chunkPos);

    if(world.provider.getDimensionType() == DimensionType.OVERWORLD) {
      this.runGenerator(this.magnesium, world, random, chunkX, chunkZ, 4, 0, 128);

      if(random.nextInt(64) == 0) {
        this.runGenerator(this.carbon, world, random, chunkX, chunkZ, 3, 0, 32);
      }

      if(random.nextInt(9) == 0) {
        this.runGenerator(this.coal, world, random, chunkX, chunkZ, 15, 0, 128);
      }
    }
  }

  private boolean runGenerator(final WorldGenerator generator, final World world, final Random rand, final int chunkX, final int chunkZ, final int chancesToSpawn, final int minHeight, final int maxHeight) {
    assert minHeight >= 0 && maxHeight <= 256 && minHeight <= maxHeight : "Illegal Height Arguments for WorldGenerator";

    final int heightDiff = maxHeight - minHeight + 1;

    for(int i = 0; i < chancesToSpawn; i ++) {
      final int x = chunkX * 16 + rand.nextInt(16);
      final int y = minHeight + rand.nextInt(heightDiff);
      final int z = chunkZ * 16 + rand.nextInt(16);

      if(generator.generate(world, rand, new BlockPos(x, y, z))) {
        return true;
      }
    }

    return false;
  }
}
