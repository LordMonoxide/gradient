package lordmonoxide.gradient.worldgen;

import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.config.GradientConfig;
import lordmonoxide.gradient.science.geology.Metals;
import lordmonoxide.gradient.science.geology.Ores;
import lordmonoxide.terra.TerraOreVein;
import lordmonoxide.terra.TerraOreVeinConfig;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.placement.ChanceRangeConfig;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Random;
import java.util.function.Function;

public final class OreGenerator {
  private OreGenerator() { }

  private static final TerraOreVeinConfig carbon = TerraOreVeinConfig.create(generator -> {
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
      stage.ore(GradientBlocks.ore(Ores.GRAPHITE).getDefaultState());
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
      pebble.pebble(GradientBlocks.pebble(Metals.GRAPHITE).getDefaultState());
      pebble.density(0.1f);
    });
  });

  private static final TerraOreVeinConfig coal = TerraOreVeinConfig.create(generator -> {
    final Function<Integer, Float> scale = depth -> 1.0f / ((depth + 64) / 64.0f);

    generator.minLength(state -> (int)(scale.apply(state.getDepth()) * 10));
    generator.maxLength(state -> (int)(scale.apply(state.getDepth()) * 30));

    generator.addStage(stage -> {
      stage.ore(Blocks.COAL_ORE.getDefaultState());
      stage.minRadius(0);
      stage.maxRadius(state -> (int)(scale.apply(state.getDepth()) * 4));
      stage.blockDensity(1.0f);
    });
  });

  private static final TerraOreVeinConfig hematite = TerraOreVeinConfig.create(generator -> {
    generator.minLength(25);
    generator.maxLength(40);

    generator.addStage(stage -> {
      stage.ore(GradientBlocks.ore(Ores.HEMATITE).getDefaultState());
      stage.minRadius(0);
      stage.maxRadius(8);
      stage.blockDensity(1.0f);
    });

    generator.addPebble(pebble -> pebble.pebble(GradientBlocks.pebble(Metals.HEMATITE).getDefaultState()));
  });

  private static final TerraOreVeinConfig smallHematite = TerraOreVeinConfig.create(generator -> {
    generator.minLength(10);
    generator.maxLength(20);

    generator.addStage(stage -> {
      stage.ore(GradientBlocks.ore(Ores.HEMATITE).getDefaultState());
      stage.minRadius(0);
      stage.maxRadius(3);
      stage.blockDensity(0.9f);
    });

    generator.addPebble(pebble -> pebble.pebble(GradientBlocks.pebble(Metals.HEMATITE).getDefaultState()));
  });

  private static final TerraOreVeinConfig cassiterite = TerraOreVeinConfig.create(generator -> {
    final Function<Integer, Float> scale = depth -> 1.0f / ((depth + 64) / 64.0f);

    generator.minLength(state -> (int)(scale.apply(state.getDepth()) * 5));
    generator.maxLength(state -> (int)(scale.apply(state.getDepth()) * 20));

    generator.addStage(stage -> {
      stage.ore(GradientBlocks.ore(Ores.CASSITERITE).getDefaultState());
      stage.minRadius(0);
      stage.maxRadius(state -> (int)(scale.apply(state.getDepth()) * 4));
      stage.blockDensity(0.5f);
    });

    generator.addPebble(pebble -> pebble.pebble(GradientBlocks.pebble(Metals.CASSITERITE).getDefaultState()));
  });

  private static final TerraOreVeinConfig copper = TerraOreVeinConfig.create(generator -> {
    generator.minLength(4);
    generator.maxLength(7);

    generator.addStage(stage -> {
      stage.ore(GradientBlocks.ore(Ores.COPPER).getDefaultState());
      stage.minRadius(0);
      stage.maxRadius(1);
      stage.blockDensity(3.0f);
    });

    generator.addPebble(pebble -> pebble.pebble(GradientBlocks.pebble(Metals.COPPER).getDefaultState()));
  });

  private static final TerraOreVeinConfig gold = TerraOreVeinConfig.create(generator -> {
    generator.minLength(7);
    generator.maxLength(12);

    generator.addStage(stage -> {
      stage.ore(GradientBlocks.ore(Ores.PYRITE).getDefaultState());
      stage.minRadius(1);
      stage.maxRadius(6);
      stage.blockDensity(0.1f);
      stage.stageSpawnChance(0.9f);
    });

    generator.addStage(stage -> {
      stage.ore(GradientBlocks.ore(Ores.GOLD).getDefaultState());
      stage.minRadius(0);
      stage.maxRadius(1);
      stage.blockDensity(1.0f);
      stage.stageSpawnChance(0.9f);
    });

    generator.addPebble(pebble -> {
      pebble.pebble(GradientBlocks.pebble(Metals.PYRITE).getDefaultState());
      pebble.density(0.25f);
    });

    generator.addPebble(pebble -> {
      pebble.pebble(GradientBlocks.pebble(Metals.GOLD).getDefaultState());
      pebble.density(0.25f);
    });
  });

  private static final TerraOreVeinConfig pyrite = TerraOreVeinConfig.create(generator -> {
    generator.minLength(7);
    generator.maxLength(16);

    generator.addStage(stage -> {
      stage.ore(GradientBlocks.ore(Ores.PYRITE).getDefaultState());
      stage.minRadius(0);
      stage.maxRadius(2);
      stage.blockDensity(3.0f);
    });

    generator.addPebble(pebble -> {
      pebble.pebble(GradientBlocks.pebble(Metals.PYRITE).getDefaultState());
      pebble.density(0.25f);
    });
  });

  //TODO
  //private final WorldGenerator salt = new WorldGenSand(GradientBlocks.SALT_BLOCK, 4);

  public static void registerGenerators() {
    if(!GradientConfig.worldgen.generateOres) {
      return;
    }

    for(final Biome biome : Biome.BIOMES) {
      biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createCompositeFeature(new TerraOreVein(), carbon,      Biome.CHANCE_RANGE, new ChanceRangeConfig(1.0f / 256.0f, 0, 0,  20)));
      biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createCompositeFeature(new TerraOreVein(), coal,        Biome.CHANCE_RANGE, new ChanceRangeConfig(1.0f /  16.0f, 0, 0, 256)));
      biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createCompositeFeature(new TerraOreVein(), cassiterite, Biome.CHANCE_RANGE, new ChanceRangeConfig(1.0f /  64.0f, 0, 0, 256)));
      biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createCompositeFeature(new TerraOreVein(), copper,      Biome.CHANCE_RANGE, new ChanceRangeConfig(1.0f /  81.0f, 0, 0, 256)));
      biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createCompositeFeature(new TerraOreVein(), gold,        Biome.CHANCE_RANGE, new ChanceRangeConfig(1.0f / 225.0f, 0, 0,  32)));
      biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createCompositeFeature(new TerraOreVein(), pyrite,      Biome.CHANCE_RANGE, new ChanceRangeConfig(1.0f /  81.0f, 0, 0, 256)));
    }

/*
    if(world.getDimension().getType() == DimensionType.OVERWORLD) {
      final ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);

      this.generateHematite(random, chunkPos, world, chunkGenerator, chunkProvider);

      final int x = random.nextInt(16) + 8;
      final int z = random.nextInt(16) + 8;
      //TODO this.salt.generate(world, random, world.getTopSolidOrLiquidBlock(chunkPos.getBlock(x, 0, z)));
    }
*/
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

      return false; //TODO this.hematite.generate(world, random, pos);
    }

    return false;
  }

  //TODO
  private boolean runGenerator(final /*WorldGenerator*/Object generator, final World world, final Random rand, final int chunkX, final int chunkZ, final int chancesToSpawn, final int minHeight, final int maxHeight) {
    assert minHeight >= 0 && maxHeight <= 256 && minHeight <= maxHeight : "Illegal Height Arguments for WorldGenerator";

    final int heightDiff = maxHeight - minHeight + 1;

    for(int i = 0; i < chancesToSpawn; i ++) {
      final int x = chunkX * 16 + rand.nextInt(16);
      final int y = minHeight + rand.nextInt(heightDiff);
      final int z = chunkZ * 16 + rand.nextInt(16);

      if(true /*generator.generate(world, rand, new BlockPos(x, y, z))*/) {
        return true;
      }
    }

    return false;
  }
}
