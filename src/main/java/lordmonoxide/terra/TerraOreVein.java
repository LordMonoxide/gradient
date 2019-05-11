package lordmonoxide.terra;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.Tags;
import org.joml.Matrix3f;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TerraOreVein extends Feature<TerraOreVeinConfig> {
  private static final float PI = (float)Math.PI;

  //TODO: handle this predicate better... needs to be configurable, but still work with deferred gen
  static boolean stonePredicate(@Nullable final IBlockState state) {
    if(state == null) {
      return false;
    }

    return state.isIn(Tags.Blocks.STONE) || state.getBlock() == Blocks.GRAVEL;
  }

  private final OreGenState state = new OreGenState();

  @Override
  public boolean place(final IWorld world, final IChunkGenerator<? extends IChunkGenSettings> generator, final Random rand, final BlockPos start, final TerraOreVeinConfig config) {
    TerraMod.logger.info("Generating vein at {}", start);

    this.state.setDepth(start.getY());

    final int minLength = config.minLength.apply(this.state);
    final int maxLength = config.maxLength.apply(this.state);

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

    final List<TerraOreVeinConfig.Stage> stages = new ArrayList<>();

    for(final TerraOreVeinConfig.Stage stage : config.stages) {
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

      for(final TerraOreVeinConfig.Stage stage : stages) {
        final int minRadius = stage.minRadius.apply(this.state);
        final int maxRadius = stage.maxRadius.apply(this.state);
        final int blockCount = Math.round((maxRadius * maxRadius - minRadius * minRadius) * stage.blockDensity.apply(this.state));

        for(int i = 0; i < blockCount; i++) {
          final int radius = rand.nextInt(maxRadius - minRadius + 1) + minRadius;
          final float angle = rand.nextFloat() * PI * 2;

          pos.set(segmentIndex, (float)Math.sin(angle) * radius, (float)Math.cos(angle) * radius);
          pos.mul(rotation);

          blockPos.setPos(root.x + pos.x, root.y + pos.y, root.z + pos.z);

          this.placeBlock(blocksToPlace, world, blockPos, stage.ore);
        }
      }

      for(final TerraOreVeinConfig.Pebble pebble : config.pebbles) {
        if(rand.nextFloat() <= pebble.density) {
          this.placePebble(world, pebble.pebble, (int)(root.x + pos.x), (int)(root.z + pos.z));
        }
      }
    }

    for(final Map.Entry<BlockPos, IBlockState> block : blocksToPlace.entrySet()) {
      final IBlockState state = world.getBlockState(block.getKey());
      if(state.isReplaceableOreGen(world.getWorld(), block.getKey(), TerraOreVein::stonePredicate)) {
        this.setBlockState(world, block.getKey(), block.getValue());
      }
    }

    return true;
  }

  private void placePebble(final IWorld world, final IBlockState pebble, final int x, final int z) {
    final int chunkX = x >> 4;
    final int chunkZ = z >> 4;

    if(!world.isChunkLoaded(chunkX, chunkZ, false)) {
      final ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
      final DeferredGenerationStorage deferredOres = DeferredGenerationStorage.get(world.getWorld());
      deferredOres.getOres(chunkPos).put(new BlockPos(x, 0, z), pebble);
      deferredOres.markDirty();
      return;
    }

    final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, 128, z);

    for(IBlockState iblockstate = world.getBlockState(pos); pos.getY() > 0 && (iblockstate.getMaterial().isReplaceable() || iblockstate.isIn(BlockTags.LOGS)); iblockstate = world.getBlockState(pos)) {
      pos.move(EnumFacing.DOWN);
    }

    pos.move(EnumFacing.UP);

    if(pebble.isValidPosition(world, pos)) {
      this.setBlockState(world, pos, pebble);
    }
  }

  private void placeBlock(final Map<BlockPos, IBlockState> blocksToPlace, final IWorld world, final BlockPos pos, final IBlockState ore) {
    if(World.isOutsideBuildHeight(pos)) {
      return;
    }

    final ChunkPos chunkPos = new ChunkPos(pos);

    if(!world.isChunkLoaded(chunkPos.x, chunkPos.z, false)) {
      final DeferredGenerationStorage deferredOres = DeferredGenerationStorage.get(world.getWorld());
      deferredOres.getOres(chunkPos).put(pos.toImmutable(), ore);
      deferredOres.markDirty();
      return;
    }

    blocksToPlace.put(pos.toImmutable(), ore);
  }
}
