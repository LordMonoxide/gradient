package lordmonoxide.gradient.worldgen;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Random;
import java.util.function.Function;

public class OreGenerator implements IWorldGenerator {
  @GameRegistry.ObjectHolder("gradient:ore.hematite")
  private static final Block HEMATITE_ORE = null;
  @GameRegistry.ObjectHolder("gradient:pebble.hematite")
  private static final Block HEMATITE_PEBBLE = null;

  @GameRegistry.ObjectHolder("gradient:ore.graphite")
  private static final Block GRAPHITE_ORE = null;
  @GameRegistry.ObjectHolder("gradient:pebble.graphite")
  private static final Block GRAPHITE_PEBBLE = null;

  @GameRegistry.ObjectHolder("gradient:ore.cassiterite")
  private static final Block CASSITERITE_ORE = null;
  @GameRegistry.ObjectHolder("gradient:pebble.cassiterite")
  private static final Block CASSITERITE_PEBBLE = null;

  @GameRegistry.ObjectHolder("gradient:ore.copper")
  private static final Block COPPER_ORE = null;
  @GameRegistry.ObjectHolder("gradient:pebble.copper")
  private static final Block COPPER_PEBBLE = null;

  private final WorldOreGenerator carbon = WorldOreGenerator.create(generator -> {
    generator.minLength(25);
    generator.maxLength(35);

    generator.addStage(stage -> {
      stage.ore(Blocks.COAL_ORE.getDefaultState());
      stage.minRadius(3);
      stage.maxRadius(6);
      stage.blockDensity(0.75f);
      stage.stageSpawnChance(0.90f);
    });

    generator.addStage(stage -> {
      stage.ore(Blocks.COAL_ORE.getDefaultState());
      stage.minRadius(7);
      stage.maxRadius(9);
      stage.blockDensity(0.33f);
      stage.stageSpawnChance(0.90f);
    });

    generator.addStage(stage -> {
      stage.ore(GRAPHITE_ORE.getDefaultState());
      stage.minRadius(0);
      stage.maxRadius(4);
      stage.blockDensity(0.75f);
      stage.stageSpawnChance(0.95f);
    });

    generator.addStage(stage -> {
      stage.ore(Blocks.DIAMOND_ORE.getDefaultState());
      stage.minRadius(0);
      stage.maxRadius(1);
      stage.blockDensity(0.75f);
      stage.stageSpawnChance(0.8f);
    });

    generator.addPebble(pebble -> {
      pebble.pebble(GRAPHITE_PEBBLE.getDefaultState());
      pebble.density(0.1f);
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
      stage.blockDensity(1.0f);
    });
  });

  private final WorldOreGenerator hematite = WorldOreGenerator.create(generator -> {
    generator.minLength(25);
    generator.maxLength(40);

    generator.addStage(stage -> {
      stage.ore(HEMATITE_ORE.getDefaultState());
      stage.minRadius(0);
      stage.maxRadius(8);
      stage.blockDensity(1.0f);
    });

    generator.addPebble(pebble -> {
      pebble.pebble(HEMATITE_PEBBLE.getDefaultState());
    });
  });

  private final WorldOreGenerator smallHematite = WorldOreGenerator.create(generator -> {
    generator.minLength(10);
    generator.maxLength(20);

    generator.addStage(stage -> {
      stage.ore(HEMATITE_ORE.getDefaultState());
      stage.minRadius(0);
      stage.maxRadius(3);
      stage.blockDensity(0.9f);
    });

    generator.addPebble(pebble -> {
      pebble.pebble(HEMATITE_PEBBLE.getDefaultState());
    });
  });

  private final WorldOreGenerator cassiterite = WorldOreGenerator.create(generator -> {
    final Function<Integer, Float> scale = depth -> 1.0f / ((depth + 64) / 64.0f);

    generator.minLength(depth -> (int)(scale.apply(depth) * 5));
    generator.maxLength(depth -> (int)(scale.apply(depth) * 20));

    generator.addStage(stage -> {
      stage.ore(CASSITERITE_ORE.getDefaultState());
      stage.minRadius(0);
      stage.maxRadius(depth -> (int)(scale.apply(depth) * 4));
      stage.blockDensity(0.5f);
    });

    generator.addPebble(pebble -> {
      pebble.pebble(CASSITERITE_PEBBLE.getDefaultState());
    });
  });

  private final WorldOreGenerator copper = WorldOreGenerator.create(generator -> {
    generator.minLength(4);
    generator.maxLength(7);

    generator.addStage(stage -> {
      stage.ore(COPPER_ORE.getDefaultState());
      stage.minRadius(0);
      stage.maxRadius(1);
      stage.blockDensity(3.0f);
    });

    generator.addPebble(pebble -> pebble.pebble(COPPER_PEBBLE.getDefaultState()));
  });

  @Override
  public void generate(final Random random, final int chunkX, final int chunkZ, final World world, final IChunkGenerator chunkGenerator, final IChunkProvider chunkProvider) {
    if(world.provider.getDimensionType() == DimensionType.OVERWORLD) {
      final ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);

      this.carbon.generateDeferredOres(world, chunkPos);
      this.coal.generateDeferredOres(world, chunkPos);
      this.smallHematite.generateDeferredOres(world, chunkPos);
      this.hematite.generateDeferredOres(world, chunkPos);
      this.cassiterite.generateDeferredOres(world, chunkPos);
      this.copper.generateDeferredOres(world, chunkPos);

      if(random.nextInt(256) == 0) {
        this.runGenerator(this.carbon, world, random, chunkX, chunkZ, 3, 0, 20);
      }

      if(random.nextInt(16) == 0) {
        this.runGenerator(this.coal, world, random, chunkX, chunkZ, 30, 0, 256);
      }

      this.generateHematite(random, chunkPos, world, chunkGenerator, chunkProvider);

      if(random.nextInt(64) == 0) {
        this.runGenerator(this.cassiterite, world, random, chunkX, chunkZ, 30, 0, 256);
      }

      if(random.nextInt(81) == 0) {
        this.runGenerator(this.copper, world, random, chunkX, chunkZ, 30, 0, 256);
      }
    }
  }

  private boolean generateHematite(final Random random, final ChunkPos chunkPos, final World world, final IChunkGenerator chunkGenerator, final IChunkProvider chunkProvider) {
    if(!BiomeDictionary.hasType(world.getBiome(chunkPos.getBlock(0, 0, 0)), BiomeDictionary.Type.WATER)) {
      if(random.nextInt(100) == 0) {
        return this.runGenerator(this.smallHematite, world, random, chunkPos.x, chunkPos.z, 2, 0, 128);
      }

      return false;
    }

    if(random.nextInt(100) == 0) {
      final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(chunkPos.getXStart() + random.nextInt(16), 64, chunkPos.getZStart() + random.nextInt(16));

      for(IBlockState iblockstate = world.getBlockState(pos); pos.getY() > 0 && iblockstate.getMaterial() != Material.ROCK; iblockstate = world.getBlockState(pos)) {
        pos.move(EnumFacing.DOWN);
      }

      pos.move(EnumFacing.UP, 4);

      return this.hematite.generate(world, random, pos);
    }

    return false;
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
