package lordmonoxide.gradient.worldgen;

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
import java.util.function.Predicate;

public final class WorldOreGenerator extends WorldGenerator {
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
  public boolean generate(final World world, final Random rand, final BlockPos root) {
    final int length = rand.nextInt(this.maxLength - this.minLength) + this.minLength;

    for(final Stage stage : this.stages) {
      final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(root);

      for(int x = 0; x < length; x++) {
        pos.setPos(pos.getX() + 1, pos.getY(), pos.getZ());

        final IBlockState state = world.getBlockState(pos);
        if(state.getBlock().isReplaceableOreGen(state, world, pos, stage.replace::test)) {
          world.setBlockState(pos, stage.ore, 2);
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
