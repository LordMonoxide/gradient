package lordmonoxide.terra;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;
import java.util.stream.Stream;

public class TerraConfig extends Placement<TerraPlacementConfig> {
  public static final TerraConfig INSTANCE = new TerraConfig();

  @Override
  public <C extends IFeatureConfig> boolean generate(final IWorld world, final ChunkGenerator<? extends GenerationSettings> chunkGenerator, final Random random, final BlockPos pos, final TerraPlacementConfig placementConfig, final Feature<C> feature, final C featureConfig) {
    if(random.nextInt(placementConfig.chance) == 0) {
      for(int i = 0; i < placementConfig.attempts; i++) {
        final int x = random.nextInt(16);
        final int y = random.nextInt(placementConfig.top) + placementConfig.bottom;
        final int z = random.nextInt(16);

        if(feature.place(world, chunkGenerator, random, pos.add(x, y, z), featureConfig)) {
          return true;
        }
      }
    }

    return false;
  }
}
