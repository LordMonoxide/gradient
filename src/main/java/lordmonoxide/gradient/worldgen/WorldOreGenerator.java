package lordmonoxide.gradient.worldgen;

import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.joml.Matrix3f;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class WorldOreGenerator extends WorldGenerator {
  private static final float PI = (float)Math.PI;

  public static WorldOreGenerator create(final Consumer<WorldOreGeneratorBuilder> callback) {
    final WorldOreGeneratorBuilder builder = new WorldOreGeneratorBuilder();
    callback.accept(builder);
    return builder.build();
  }

  private static boolean stonePredicate(@Nullable final IBlockState state) {
    if(state != null && state.getBlock() == Blocks.STONE) {
      return state.getValue(BlockStone.VARIANT).isNatural();
    }

    return false;
  }

  private final Stage[] stages;
  private final Function<Integer, Integer> minLength;
  private final Function<Integer, Integer> maxLength;

  private WorldOreGenerator(final Stage[] stages, final Function<Integer, Integer> minLength, final Function<Integer, Integer> maxLength) {
    this.stages = stages;
    this.minLength = minLength;
    this.maxLength = maxLength;
  }

  @Override
  public boolean generate(final World world, final Random rand, final BlockPos start) {
    final int minLength = this.minLength.apply(start.getY());
    final int maxLength = this.maxLength.apply(start.getY());

    final int length = rand.nextInt(maxLength - minLength + 1) + minLength;

    // Initial position
    final Matrix3f rotation = new Matrix3f();
    final Vector3f pos = new Vector3f();
    final Vector3f root = new Vector3f(start.getX(), start.getY(), start.getZ());

    // Initial rotation
    float xRotation = rand.nextFloat() * PI * 2;
    float yRotation = rand.nextFloat() * PI * 2;
    float zRotation = rand.nextFloat() * PI * 2;
    rotation.rotateXYZ(xRotation, yRotation, zRotation);

    final BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();

    final Map<Stage, Boolean> shouldSpawn = new HashMap<>();

    for(final Stage stage : this.stages) {
      shouldSpawn.put(stage, stage.stageSpawnChance.apply(start.getY()) >= rand.nextFloat());
    }

    // 1/x chance for a vein to change direction by up to 45 degrees total (across all axes).
    // Each block that is generated will decrease this value, making it more likely that the
    // vein will change directions.  If it changes directions, the divisor is incremented by 30.
    int changeDirectionDivisor = 30;
    int segmentIndex = 0;

    // Return false if we weren't able to place more than 1/3 of the blocks
    int blocksPlaced = 0;
    int blocksTotal = 0;

    for(int blockIndex = 0; blockIndex < length; blockIndex++, segmentIndex++) {
      // Change direction?
      if(rand.nextInt(changeDirectionDivisor) == 0) {
        changeDirectionDivisor += 30;

        float rotationChangeBudget = PI / 2;
        float rotationChange = rand.nextFloat() * rotationChangeBudget;
        rotationChangeBudget -= rotationChange;
        xRotation += rand.nextFloat() * rotationChange - rotationChange / 2;
        rotationChange = rand.nextFloat() * rotationChangeBudget;
        rotationChangeBudget -= rotationChange;
        yRotation += rand.nextFloat() * rotationChange - rotationChange / 2;
        rotationChange = rand.nextFloat() * rotationChangeBudget;
        zRotation += rand.nextFloat() * rotationChange - rotationChange / 2;

        rotation.rotateXYZ(xRotation, yRotation, zRotation);
        root.add(pos);
        segmentIndex = 0;
      }

      // More likely to change direction the longer we go without doing so
      changeDirectionDivisor--;

      for(final Stage stage : this.stages) {
        if(shouldSpawn.get(stage)) {
          final int minRadius = stage.minRadius.apply(start.getY());
          final int maxRadius = stage.maxRadius.apply(start.getY());
          final float blockSpawnChance = stage.blockSpawnChance.apply(start.getY());

          final int radius = rand.nextInt(maxRadius - minRadius + 1) + minRadius;

          for(int i = 0; i < radius - minRadius; i++) {
            if(blockSpawnChance >= rand.nextFloat()) {
              final float angle = rand.nextFloat() * PI * 2;
              pos.set(segmentIndex, (float)Math.sin(angle) * radius, (float)Math.cos(angle) * radius);
              pos.mul(rotation);

              blockPos.setPos(root.x + pos.x + 8, root.y + pos.y, root.z + pos.z + 8);

              final IBlockState state = world.getBlockState(blockPos);
              if(state.getBlock().isReplaceableOreGen(state, world, blockPos, stage.replace::test)) {
                world.setBlockState(blockPos, stage.ore, 2);
                blocksPlaced++;
              }

              blocksTotal++;
            }
          }
        }
      }
    }

    return (float)blocksPlaced / blocksTotal >= 1.0f / 3.0f;
  }

  private static final class Stage {
    private final Predicate<IBlockState> replace;
    private final IBlockState ore;
    private final Function<Integer, Integer> minRadius;
    private final Function<Integer, Integer> maxRadius;
    private final Function<Integer, Float> blockSpawnChance;
    private final Function<Integer, Float> stageSpawnChance;

    private Stage(final Predicate<IBlockState> replace, final IBlockState ore, final Function<Integer, Integer> minRadius, final Function<Integer, Integer> maxRadius, final Function<Integer, Float> blockSpawnChance, final Function<Integer, Float> stageSpawnChance) {
      this.replace = replace;
      this.ore = ore;
      this.minRadius = minRadius;
      this.maxRadius = maxRadius;
      this.blockSpawnChance = blockSpawnChance;
      this.stageSpawnChance = stageSpawnChance;
    }
  }

  public static final class WorldOreGeneratorBuilder {
    private final List<Stage> stages = new ArrayList<>();
    private Function<Integer, Integer> minLength = depth -> 3;
    private Function<Integer, Integer> maxLength = depth -> 5;

    private WorldOreGeneratorBuilder() { }

    public WorldOreGeneratorBuilder addStage(final Consumer<StageBuilder> callback) {
      final StageBuilder builder = new StageBuilder();
      callback.accept(builder);
      this.stages.add(builder.build());
      return this;
    }

    public WorldOreGeneratorBuilder minLength(final int length) {
      return this.minLength(depth -> length);
    }

    public WorldOreGeneratorBuilder minLength(final Function<Integer, Integer> length) {
      this.minLength = length;
      return this;
    }

    public WorldOreGeneratorBuilder maxLength(final int length) {
      return this.maxLength(depth -> length);
    }

    public WorldOreGeneratorBuilder maxLength(final Function<Integer, Integer> length) {
      this.maxLength = length;
      return this;
    }

    private WorldOreGenerator build() {
      return new WorldOreGenerator(this.stages.toArray(new Stage[0]), this.minLength, this.maxLength);
    }
  }

  public static final class StageBuilder {
    private Predicate<IBlockState> replace = WorldOreGenerator::stonePredicate;
    private IBlockState ore = Blocks.STONE.getDefaultState();
    private Function<Integer, Integer> minRadius = depth -> 0;
    private Function<Integer, Integer> maxRadius = depth -> 5;
    private Function<Integer, Float> blockSpawnChance = depth -> 0.75f;
    private Function<Integer, Float> stageSpawnChance = depth -> 1.0f;

    private StageBuilder() { }

    public StageBuilder replace(final Predicate<IBlockState> replace) {
      this.replace = replace;
      return this;
    }

    public StageBuilder ore(final IBlockState ore) {
      this.ore = ore;
      return this;
    }

    public StageBuilder minRadius(final int minRadius) {
      return this.minRadius(depth -> minRadius);
    }

    public StageBuilder minRadius(final Function<Integer, Integer> minRadius) {
      this.minRadius = minRadius;
      return this;
    }

    public StageBuilder maxRadius(final int maxRadius) {
      return this.maxRadius(depth -> maxRadius);
    }

    public StageBuilder maxRadius(final Function<Integer, Integer> maxRadius) {
      this.maxRadius = maxRadius;
      return this;
    }

    public StageBuilder blockSpawnChance(final float spawnChance) {
      return this.blockSpawnChance(depth -> spawnChance);
    }

    public StageBuilder blockSpawnChance(final Function<Integer, Float> spawnChance) {
      this.blockSpawnChance = spawnChance;
      return this;
    }

    public StageBuilder stageSpawnChance(final float spawnChance) {
      return this.stageSpawnChance(depth -> spawnChance);
    }

    public StageBuilder stageSpawnChance(final Function<Integer, Float> spawnChance) {
      this.stageSpawnChance = spawnChance;
      return this;
    }

    private Stage build() {
      return new Stage(this.replace, this.ore, this.minRadius, this.maxRadius, this.blockSpawnChance, this.stageSpawnChance);
    }
  }
}
