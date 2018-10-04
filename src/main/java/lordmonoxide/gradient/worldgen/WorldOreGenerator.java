package lordmonoxide.gradient.worldgen;

import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
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
  private final StateFunction<Integer> minLength;
  private final StateFunction<Integer> maxLength;

  private final OreGenState state = new OreGenState();

  private WorldOreGenerator(final Stage[] stages, final StateFunction<Integer> minLength, final StateFunction<Integer> maxLength) {
    this.stages = stages;
    this.minLength = minLength;
    this.maxLength = maxLength;
  }

  @Override
  public boolean generate(final World world, final Random rand, final BlockPos start) {
    this.state.depth = start.getY();

    final int minLength = this.minLength.apply(this.state);
    final int maxLength = this.maxLength.apply(this.state);

    final int length = rand.nextInt(maxLength - minLength + 1) + minLength;

    // Initial position (offset by 8 to help prevent cascading)
    final Matrix3f rotation = new Matrix3f();
    final Vector3f pos = new Vector3f();
    final Vector3f root = new Vector3f(start.getX() + 8, start.getY(), start.getZ() + 8);

    // Initial rotation
    float xRotation = rand.nextFloat() * PI * 2;
    float yRotation = rand.nextFloat() * PI * 2;
    float zRotation = rand.nextFloat() * PI * 2;
    rotation.rotateXYZ(xRotation, yRotation, zRotation);

    final BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();

    final List<Stage> stages = new ArrayList<>();

    for(final Stage stage : this.stages) {
      if(stage.stageSpawnChance.apply(this.state) >= rand.nextFloat()) {
        stages.add(stage);
      }
    }

    final Map<BlockPos, IBlockState> blocksToPlace = new HashMap<>();

    // 1/x chance for a vein to change direction by up to 45 degrees total (across all axes).
    // Each block that is generated will decrease this value, making it more likely that the
    // vein will change directions.  If it changes directions, the divisor is incremented by 30.
    int changeDirectionDivisor = 30;
    int segmentIndex = 0;

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

      for(final Stage stage : stages) {
        final int minRadius = stage.minRadius.apply(this.state);
        final int maxRadius = stage.maxRadius.apply(this.state);
        final int blockCount = maxRadius * maxRadius - minRadius * minRadius;
        final float blockSpawnChance = stage.blockSpawnChance.apply(this.state);

        for(int i = 0; i < blockCount; i++) {
          if(blockSpawnChance >= rand.nextFloat()) {
            final int radius = rand.nextInt(maxRadius - minRadius + 1) + minRadius;
            final float angle = rand.nextFloat() * PI * 2;

            pos.set(segmentIndex, (float)Math.sin(angle) * radius, (float)Math.cos(angle) * radius);
            pos.mul(rotation);

            blockPos.setPos(root.x + pos.x, root.y + pos.y, root.z + pos.z);

            this.placeBlock(blocksToPlace, world, blockPos, stage);
          }
        }
      }
    }

    for(final Map.Entry<BlockPos, IBlockState> block : blocksToPlace.entrySet()) {
      final IBlockState state = world.getBlockState(block.getKey());
      if(state.getBlock().isReplaceableOreGen(state, world, block.getKey(), WorldOreGenerator::stonePredicate)) {
        world.setBlockState(block.getKey(), block.getValue(), 0x2 | 0x10);
      }
    }

    return true;
  }

  private void placeBlock(final Map<BlockPos, IBlockState> blocksToPlace, final World world, final BlockPos pos, final Stage stage) {
    if(world.isOutsideBuildHeight(pos)) {
      return;
    }

    final ChunkPos chunkPos = new ChunkPos(pos);

    if(!world.isChunkGeneratedAt(chunkPos.x, chunkPos.z)) {
      final DeferredOreStorage deferredOres = DeferredOreStorage.get(world);
      deferredOres.get(chunkPos).put(pos.toImmutable(), stage.ore);
      deferredOres.markDirty();
      return;
    }

    blocksToPlace.put(pos.toImmutable(), stage.ore);
  }

  public void generateDeferredOres(final World world, final ChunkPos chunkPos) {
    final DeferredOreStorage deferredOres = DeferredOreStorage.get(world);

    if(deferredOres.has(chunkPos)) {
      deferredOres.get(chunkPos).forEach((pos, ore) -> {
        final IBlockState oldState = world.getBlockState(pos);
        if(oldState.getBlock().isReplaceableOreGen(oldState, world, pos, WorldOreGenerator::stonePredicate)) {
          world.setBlockState(pos, ore, 2);
        }
      });

      deferredOres.remove(chunkPos);
      deferredOres.markDirty();
    }
  }

  private static final class Stage {
    private final IBlockState ore;
    private final StateFunction<Integer> minRadius;
    private final StateFunction<Integer> maxRadius;
    private final StateFunction<Float> blockSpawnChance;
    private final StateFunction<Float> stageSpawnChance;

    private Stage(final IBlockState ore, final StateFunction<Integer> minRadius, final StateFunction<Integer> maxRadius, final StateFunction<Float> blockSpawnChance, final StateFunction<Float> stageSpawnChance) {
      this.ore = ore;
      this.minRadius = minRadius;
      this.maxRadius = maxRadius;
      this.blockSpawnChance = blockSpawnChance;
      this.stageSpawnChance = stageSpawnChance;
    }
  }

  public static final class WorldOreGeneratorBuilder {
    private final List<Stage> stages = new ArrayList<>();
    private StateFunction<Integer> minLength = state -> 3;
    private StateFunction<Integer> maxLength = state -> 5;

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

    public WorldOreGeneratorBuilder minLength(final StateFunction<Integer> length) {
      this.minLength = length;
      return this;
    }

    public WorldOreGeneratorBuilder maxLength(final int length) {
      return this.maxLength(depth -> length);
    }

    public WorldOreGeneratorBuilder maxLength(final StateFunction<Integer> length) {
      this.maxLength = length;
      return this;
    }

    private static final Stage[] ZERO_LENGTH_STAGE = new Stage[0];

    private WorldOreGenerator build() {
      return new WorldOreGenerator(this.stages.toArray(ZERO_LENGTH_STAGE), this.minLength, this.maxLength);
    }
  }

  public static final class StageBuilder {
    private IBlockState ore = Blocks.STONE.getDefaultState();
    private StateFunction<Integer> minRadius = state -> 0;
    private StateFunction<Integer> maxRadius = state -> 5;
    private StateFunction<Float> blockSpawnChance = state -> 0.75f;
    private StateFunction<Float> stageSpawnChance = state -> 1.0f;

    private StageBuilder() { }

    public StageBuilder ore(final IBlockState ore) {
      this.ore = ore;
      return this;
    }

    public StageBuilder minRadius(final int minRadius) {
      return this.minRadius(depth -> minRadius);
    }

    public StageBuilder minRadius(final StateFunction<Integer> minRadius) {
      this.minRadius = minRadius;
      return this;
    }

    public StageBuilder maxRadius(final int maxRadius) {
      return this.maxRadius(depth -> maxRadius);
    }

    public StageBuilder maxRadius(final StateFunction<Integer> maxRadius) {
      this.maxRadius = maxRadius;
      return this;
    }

    public StageBuilder blockSpawnChance(final float spawnChance) {
      return this.blockSpawnChance(depth -> spawnChance);
    }

    public StageBuilder blockSpawnChance(final StateFunction<Float> spawnChance) {
      this.blockSpawnChance = spawnChance;
      return this;
    }

    public StageBuilder stageSpawnChance(final float spawnChance) {
      return this.stageSpawnChance(depth -> spawnChance);
    }

    public StageBuilder stageSpawnChance(final StateFunction<Float> spawnChance) {
      this.stageSpawnChance = spawnChance;
      return this;
    }

    private Stage build() {
      return new Stage(this.ore, this.minRadius, this.maxRadius, this.blockSpawnChance, this.stageSpawnChance);
    }
  }

  public static class OreGenState {
    private int depth;

    public int getDepth() {
      return this.depth;
    }

    private void setDepth(final int depth) {
      this.depth = depth;
    }
  }

  public interface StateFunction<RETURN> extends Function<OreGenState, RETURN> {

  }
}
