package lordmonoxide.gradient.worldgen;

import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.pebble.BlockPebble;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
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
    if(state == null) {
      return false;
    }

    if(state.getBlock() == Blocks.STONE) {
      return state.getValue(BlockStone.VARIANT).isNatural();
    }

    if(state.getBlock() == Blocks.GRAVEL) {
      return true;
    }

    return false;
  }

  private final Stage[] stages;
  private final Pebble[] pebbles;
  private final Function<Integer, Integer> minLength;
  private final Function<Integer, Integer> maxLength;

  private WorldOreGenerator(final Stage[] stages, final Pebble[] pebbles, final Function<Integer, Integer> minLength, final Function<Integer, Integer> maxLength) {
    this.stages = stages;
    this.pebbles = pebbles;
    this.minLength = minLength;
    this.maxLength = maxLength;
  }

  @Override
  public boolean generate(final World world, final Random rand, final BlockPos start) {
    final int minLength = this.minLength.apply(start.getY());
    final int maxLength = this.maxLength.apply(start.getY());

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
      if(stage.stageSpawnChance.apply(start.getY()) >= rand.nextFloat()) {
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
        final int minRadius = stage.minRadius.apply(start.getY());
        final int maxRadius = stage.maxRadius.apply(start.getY());
        final int blockCount = Math.round((maxRadius * maxRadius - minRadius * minRadius) * stage.blockDensity.apply(start.getY()));

        for(int i = 0; i < blockCount; i++) {
          final int radius = rand.nextInt(maxRadius - minRadius + 1) + minRadius;
          final float angle = rand.nextFloat() * PI * 2;

          pos.set(segmentIndex, (float)Math.sin(angle) * radius, (float)Math.cos(angle) * radius);
          pos.mul(rotation);

          blockPos.setPos(root.x + pos.x, root.y + pos.y, root.z + pos.z);

          this.placeBlock(blocksToPlace, world, blockPos, stage.ore);
        }
      }

      for(final Pebble pebble : this.pebbles) {
        if(rand.nextFloat() <= pebble.density) {
          this.placePebble(blocksToPlace, world, pebble.pebble, (int)(root.x + pos.x), (int)(root.z + pos.z));
        }
      }
    }

    for(final Map.Entry<BlockPos, IBlockState> block : blocksToPlace.entrySet()) {
      if(block.getValue().getBlock() instanceof BlockPebble) {
        world.setBlockState(block.getKey(), block.getValue());
        continue;
      }

      final IBlockState state = world.getBlockState(block.getKey());
      if(state.getBlock().isReplaceableOreGen(state, world, block.getKey(), WorldOreGenerator::stonePredicate)) {
        world.setBlockState(block.getKey(), block.getValue(), 0x2 | 0x10);
      }
    }

    return true;
  }

  private void placePebble(final Map<BlockPos, IBlockState> blocksToPlace, final World world, final IBlockState pebble, final int x, final int z) {
    final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, 128, z);

    for(IBlockState iblockstate = world.getBlockState(pos); pos.getY() > 0 && (iblockstate.getBlock().isReplaceable(world, pos) || iblockstate.getBlock().isWood(world, pos)); iblockstate = world.getBlockState(pos)) {
      pos.move(EnumFacing.DOWN);
    }

    pos.move(EnumFacing.UP);

    if(pebble.getBlock().canPlaceBlockAt(world, pos)) {
      this.placeBlock(blocksToPlace, world, pos, pebble);
    }
  }

  private void placeBlock(final Map<BlockPos, IBlockState> blocksToPlace, final World world, final BlockPos pos, final IBlockState ore) {
    if(world.isOutsideBuildHeight(pos)) {
      return;
    }

    final ChunkPos chunkPos = new ChunkPos(pos);

    if(!world.isChunkGeneratedAt(chunkPos.x, chunkPos.z)) {
      final DeferredOreStorage deferredOres = DeferredOreStorage.get(world);
      deferredOres.get(chunkPos).put(pos.toImmutable(), ore);
      deferredOres.markDirty();
      return;
    }

    blocksToPlace.put(pos.toImmutable(), ore);
  }

  public void generateDeferredOres(final World world, final ChunkPos chunkPos) {
    final DeferredOreStorage deferredOres = DeferredOreStorage.get(world);

    if(deferredOres.has(chunkPos)) {
      deferredOres.get(chunkPos).forEach((pos, ore) -> {
        if(ore.getBlock() instanceof BlockPebble) {
          if(ore.getBlock().canPlaceBlockAt(world, pos)) {
            world.setBlockState(pos, ore);
            return;
          }
        }

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
    private final Function<Integer, Integer> minRadius;
    private final Function<Integer, Integer> maxRadius;
    private final Function<Integer, Float> blockDensity;
    private final Function<Integer, Float> stageSpawnChance;

    private Stage(final IBlockState ore, final Function<Integer, Integer> minRadius, final Function<Integer, Integer> maxRadius, final Function<Integer, Float> blockDensity, final Function<Integer, Float> stageSpawnChance) {
      this.ore = ore;
      this.minRadius = minRadius;
      this.maxRadius = maxRadius;
      this.blockDensity = blockDensity;
      this.stageSpawnChance = stageSpawnChance;
    }
  }

  private static final class Pebble {
    private final IBlockState pebble;
    private final float density;

    private Pebble(final IBlockState pebble, final float density) {
      this.pebble = pebble;
      this.density = density;
    }
  }

  public static final class WorldOreGeneratorBuilder {
    private final List<Stage> stages = new ArrayList<>();
    private final List<Pebble> pebbles = new ArrayList<>();
    private Function<Integer, Integer> minLength = depth -> 3;
    private Function<Integer, Integer> maxLength = depth -> 5;

    private WorldOreGeneratorBuilder() { }

    public WorldOreGeneratorBuilder addStage(final Consumer<StageBuilder> callback) {
      final StageBuilder builder = new StageBuilder();
      callback.accept(builder);
      this.stages.add(builder.build());
      return this;
    }

    public WorldOreGeneratorBuilder addPebble(final Consumer<PebbleBuilder> callback) {
      final PebbleBuilder builder = new PebbleBuilder();
      callback.accept(builder);
      this.pebbles.add(builder.build());
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

    private static final Stage[] ZERO_LENGTH_STAGE = new Stage[0];
    private static final Pebble[] ZERO_LENGTH_PEBBLE = new Pebble[0];

    private WorldOreGenerator build() {
      return new WorldOreGenerator(this.stages.toArray(ZERO_LENGTH_STAGE), this.pebbles.toArray(ZERO_LENGTH_PEBBLE), this.minLength, this.maxLength);
    }
  }

  public static final class StageBuilder {
    private IBlockState ore = Blocks.STONE.getDefaultState();
    private Function<Integer, Integer> minRadius = depth -> 0;
    private Function<Integer, Integer> maxRadius = depth -> 5;
    private Function<Integer, Float> blockDensity = depth -> 0.75f;
    private Function<Integer, Float> stageSpawnChance = depth -> 1.0f;

    private StageBuilder() { }

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

    public StageBuilder blockDensity(final float density) {
      return this.blockDensity(depth -> density);
    }

    public StageBuilder blockDensity(final Function<Integer, Float> density) {
      this.blockDensity = density;
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
      return new Stage(this.ore, this.minRadius, this.maxRadius, this.blockDensity, this.stageSpawnChance);
    }
  }

  public static final class PebbleBuilder {
    private IBlockState pebble = GradientBlocks.PEBBLE.getDefaultState();
    private float density = 0.5f;

    private PebbleBuilder() { }

    public PebbleBuilder pebble(final IBlockState pebble) {
      this.pebble = pebble;
      return this;
    }

    public PebbleBuilder density(final float density) {
      this.density = density;
      return this;
    }

    private Pebble build() {
      return new Pebble(this.pebble, this.density);
    }
  }
}
