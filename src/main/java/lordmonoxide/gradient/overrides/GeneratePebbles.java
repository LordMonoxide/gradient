package lordmonoxide.gradient.overrides;

import lordmonoxide.gradient.blocks.GradientBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class GeneratePebbles implements IWorldGenerator {
  private static BiomeDictionary.Type[] spawn_in = {
      BiomeDictionary.Type.SPARSE,
      BiomeDictionary.Type.DRY,
      BiomeDictionary.Type.SAVANNA,
      BiomeDictionary.Type.DEAD,
      BiomeDictionary.Type.RIVER,
      BiomeDictionary.Type.MESA,
      BiomeDictionary.Type.PLAINS,
      BiomeDictionary.Type.MOUNTAIN,
      BiomeDictionary.Type.HILLS,
      BiomeDictionary.Type.SANDY,
      BiomeDictionary.Type.WASTELAND,
      BiomeDictionary.Type.BEACH,
  };
  
  @Override
  public void generate(Random random, int chunk_x, int chunk_z, World world, IChunkGenerator chunk_generator, IChunkProvider chunk_provider) {
    if(random.nextInt(10) != 0) {
      return;
    }
    
    int x_chunk = chunk_x * 16 + 8;
    int z_chunk = chunk_z * 16 + 8;
    int x_ch = chunk_x * 16 + random.nextInt(16);
    int y_ch = random.nextInt(128);
    int z_ch = chunk_z * 16 + random.nextInt(16);
    
    Biome biome = world.getBiome(new BlockPos(x_chunk + 16, 0, z_chunk + 16));
    BlockPos pos = new BlockPos(x_ch, y_ch + 64, z_ch);
    
    if(this.isBiomeOfAnyType(biome, spawn_in)) {
      for(IBlockState iblockstate = world.getBlockState(pos); (iblockstate.getBlock().isAir(iblockstate, world, pos)) && pos.getY() > 0; iblockstate = world.getBlockState(pos)) {
        pos = pos.down();
      }
      
      for(int i = 0; i < 32; ++i) {
        BlockPos blockpos = pos.add(random.nextInt(8) - random.nextInt(8), random.nextInt(4) - random.nextInt(4), random.nextInt(8) - random.nextInt(8));
        
        if(GradientBlocks.PEBBLE.canPlaceBlockAt(world, blockpos)) {
          world.setBlockState(blockpos, GradientBlocks.PEBBLE.getDefaultState(), 2);
        }
      }
    }
  }
  
  private boolean isBiomeOfAnyType(Biome biome, BiomeDictionary.Type[] types) {
    for(BiomeDictionary.Type type : types) {
      if(BiomeDictionary.hasType(biome, type)) {
        return true;
      }
    }
    
    return false;
  }
}
