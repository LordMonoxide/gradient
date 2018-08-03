package lordmonoxide.gradient.worldgen;

import com.google.common.base.Predicate;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class WorldOreGenerator extends WorldGenerator {
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
  private final int length;

  private WorldOreGenerator(final Stage[] stages,  final int length) {
    this.stages = stages;
    this.length = length;
  }

  @Override
  public boolean generate(final World world, final Random rand, final BlockPos position) {


    return true;
  }

  private static class Stage {
    private final Predicate<IBlockState> replace;
    private final IBlockState state;
    private final int minRadius;
    private final int maxRadius;
    private final float spawnChance;

    private Stage(final Predicate<IBlockState> replace, final IBlockState state, final int minRadius, final int maxRadius, final float spawnChance) {
      this.replace = replace;
      this.state = state;
      this.minRadius = minRadius;
      this.maxRadius = maxRadius;
      this.spawnChance = spawnChance;
    }
  }

  public static class WorldOreGeneratorBuilder {
    private final List<Stage> stages = new ArrayList<>();
    private int length;

    private WorldOreGeneratorBuilder() { }

    public WorldOreGeneratorBuilder addStage(final Consumer<StageBuilder> callback) {
      final StageBuilder builder = new StageBuilder();
      callback.accept(builder);
      this.stages.add(builder.build());
      return this;
    }

    public WorldOreGeneratorBuilder length(final int length) {
      this.length = length;
      return this;
    }

    private WorldOreGenerator build() {
      return new WorldOreGenerator(this.stages.toArray(new Stage[0]), this.length);
    }
  }

  public static class StageBuilder {
    private java.util.function.Predicate<IBlockState> replace = WorldOreGenerator::stonePredicate;
    private IBlockState state = Blocks.STONE.getDefaultState();
    private int minRadius;
    private int maxRadius = 5;
    private float spawnChance = 0.1f;

    private StageBuilder() { }

    public StageBuilder replace(final java.util.function.Predicate<IBlockState> replace) {
      this.replace = replace;
      return this;
    }

    public StageBuilder block(final IBlockState state) {
      this.state = state;
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
      return new Stage(this.replace, this.state, this.minRadius, this.maxRadius, this.spawnChance);
    }
  }
}
