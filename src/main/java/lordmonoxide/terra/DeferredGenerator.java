package lordmonoxide.terra;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class DeferredGenerator extends Feature<NoFeatureConfig> {
  @Override
  public boolean place(final IWorld world, final IChunkGenerator<? extends IChunkGenSettings> generator, final Random rand, final BlockPos start, final NoFeatureConfig config) {
    final DeferredGenerationStorage deferred = DeferredGenerationStorage.get(world.getWorld());

    final ChunkPos chunkPos = new ChunkPos(start);

    if(deferred.has(chunkPos)) {
      deferred.getOres(chunkPos).forEach((pos, ore) -> {
        final IBlockState oldState = world.getBlockState(pos);
        if(oldState.getBlock().isReplaceableOreGen(oldState, world.getWorld(), pos, TerraOreVein::stonePredicate)) {
          this.setBlockState(world, pos, ore);
        }
      });

      deferred.getPebbles(chunkPos).forEach((pos, pebble) -> {
        final BlockPos.MutableBlockPos pebblePos = new BlockPos.MutableBlockPos(pos.getX(), 128, pos.getZ());

        for(IBlockState iblockstate = world.getBlockState(pebblePos); pebblePos.getY() > 0 && (iblockstate.getMaterial().isReplaceable() || iblockstate.isIn(BlockTags.LOGS)); iblockstate = world.getBlockState(pebblePos)) {
          pebblePos.move(EnumFacing.DOWN);
        }

        pebblePos.move(EnumFacing.UP);

        if(pebble.isValidPosition(world, pebblePos)) {
          this.setBlockState(world, pebblePos, pebble);
        }
      });

      deferred.remove(chunkPos);
      deferred.markDirty();
    }

    return true;
  }
}
