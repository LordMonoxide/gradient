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
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
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
  private final int minLength;
  private final int maxLength;

  private WorldOreGenerator(final Stage[] stages, final int minLength, final int maxLength) {
    this.stages = stages;
    this.minLength = minLength;
    this.maxLength = maxLength;
  }

  @Override
  public boolean generate(final World world, final Random rand, final BlockPos start) {
    final int length = rand.nextInt(this.maxLength - this.minLength + 1) + this.minLength;

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

    // 1/x chance for a vein to change direction by up to 45 degrees total (across all axes).
    // Each block that is generated will decrease this value, making it more likely that the
    // vein will change directions.  If it changes directions, the divisor is incremented by 30.
    int changeDirectionDivisor = 30;
    int directionIndex = 0;

    for(int blockIndex = 0; blockIndex < length; blockIndex++, directionIndex++) {
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
        directionIndex = 0;
      }

      // More likely to change direction the longer we go without doing so
      changeDirectionDivisor--;

      for(final Stage stage : this.stages) {
        final int radius = rand.nextInt(stage.maxRadius - stage.minRadius + 1) + stage.minRadius;

        for(int i = 0; i < radius - stage.minRadius; i++) {
          if(stage.spawnChance >= rand.nextFloat()) {
            final float angle = rand.nextFloat() * PI * 2;
            pos.set(directionIndex, (float)Math.sin(angle) * radius, (float)Math.cos(angle) * radius);
            pos.mul(rotation);

            blockPos.setPos(root.x + pos.x + 8, root.y + pos.y, root.z + pos.z + 8);

            final IBlockState state = world.getBlockState(blockPos);
            if(state.getBlock().isReplaceableOreGen(state, world, blockPos, stage.replace::test)) {
              world.setBlockState(blockPos, stage.ore, 2);
            }
          }
        }
      }
    }

    return true;
  }

  private static final class Stage {
    private final Predicate<IBlockState> replace;
    private final IBlockState ore;
    private final int minRadius;
    private final int maxRadius;
    private final float spawnChance;

    private Stage(final Predicate<IBlockState> replace, final IBlockState ore, final int minRadius, final int maxRadius, final float spawnChance) {
      this.replace = replace;
      this.ore = ore;
      this.minRadius = minRadius;
      this.maxRadius = maxRadius;
      this.spawnChance = spawnChance;
    }
  }

  public static final class WorldOreGeneratorBuilder {
    private final List<Stage> stages = new ArrayList<>();
    private int minLength = 3;
    private int maxLength = 5;

    private WorldOreGeneratorBuilder() { }

    public WorldOreGeneratorBuilder addStage(final Consumer<StageBuilder> callback) {
      final StageBuilder builder = new StageBuilder();
      callback.accept(builder);
      this.stages.add(builder.build());
      return this;
    }

    public WorldOreGeneratorBuilder minLength(final int length) {
      this.minLength = length;
      return this;
    }

    public WorldOreGeneratorBuilder maxLength(final int length) {
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
    private int minRadius;
    private int maxRadius = 5;
    private float spawnChance = 0.1f;

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
      this.minRadius = minRadius;
      return this;
    }

    public StageBuilder maxRadius(final int maxRadius) {
      this.maxRadius = maxRadius;
      return this;
    }

    public StageBuilder spawnChance(final float spawnChance) {
      this.spawnChance = spawnChance;
      return this;
    }

    private Stage build() {
      return new Stage(this.replace, this.ore, this.minRadius, this.maxRadius, this.spawnChance);
    }
  }
}
