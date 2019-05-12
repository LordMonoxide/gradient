package lordmonoxide.terra;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.BasePlacement;

import java.util.Random;

public class TerraConfig extends BasePlacement<TerraPlacementConfig> {
  public static final TerraConfig INSTANCE = new TerraConfig();

  @Override
  public <C extends IFeatureConfig> boolean generate(final IWorld world, final IChunkGenerator<? extends IChunkGenSettings> chunkGenerator, final Random random, final BlockPos pos, final TerraPlacementConfig placementConfig, final Feature<C> feature, final C featureConfig) {
    if(random.nextInt(placementConfig.chance) == 0) {
      TerraMod.logger.info("Generating vein in chunk {}", pos);

      for(int i = 0; i < placementConfig.attempts; i++) {
        final int x = random.nextInt(16);
        final int y = random.nextInt(placementConfig.top) + placementConfig.bottom;
        final int z = random.nextInt(16);

        if(feature.place(world, chunkGenerator, random, pos.add(x, y, z), featureConfig)) {
          TerraMod.logger.info("Success {}", pos.add(x, y, z));
          return true;
        }
      }

      TerraMod.logger.info("Failed");
    }

    return false;
  }
}
