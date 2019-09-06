package lordmonoxide.gradient.tileentities;

import com.google.common.collect.ImmutableMap;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.heat.HeatSinker;
import lordmonoxide.gradient.network.PacketUpdateClayMetalMixerNeighbours;
import lordmonoxide.gradient.recipes.AlloyRecipe;
import lordmonoxide.gradient.utils.RecipeUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.animation.TimeValues;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TileClayMetalMixer extends HeatSinker {
  @CapabilityInject(IFluidHandler.class)
  private static Capability<IFluidHandler> FLUID_HANDLER_CAPABILITY;

  @CapabilityInject(IAnimationStateMachine.class)
  private static Capability<IAnimationStateMachine> ANIMATION_CAPABILITY;

  private static final int CYCLE_TICKS = 40;

  @Nullable
  private final IAnimationStateMachine asm;
  private final TimeValues.VariableValue ticksValue = new TimeValues.VariableValue(0.0f);
  private boolean isAnimating;
  private int animationTicks;

  private final Map<EnumFacing, IFluidHandler> inputs = new EnumMap<>(EnumFacing.class);
  private final Map<Fluid, List<EnumFacing>> fluidSideMap = new HashMap<>();

  @Nullable
  private IFluidHandler output;

  @Nullable
  private AlloyRecipe recipe;
  private int recipeTicks;

  private final Random rand = new Random();

  public TileClayMetalMixer() {
    this.asm = GradientMod.proxy.loadAsm(GradientMod.resource("asms/block/clay_metal_mixer.json"), ImmutableMap.of("spinning_cycle", this.ticksValue));
  }

  public boolean isConnected(final EnumFacing side) {
    return this.inputs.get(side) != null;
  }

  public void inputUpdated(final EnumFacing side) {
    if(this.inputs.containsKey(side)) {
      this.updateRecipe();
    }
  }

  public void outputUpdated() {
    if(this.world.isRemote) {
      if(this.output != null) {
        final FluidStack fluidStack = this.output.drain(1, false);

        if(fluidStack != null && fluidStack.amount != 0) {
          this.asm.transition("spinning");
          this.isAnimating = true;
          this.animationTicks = 0;
          return;
        }
      }

      this.asm.transition("idle");
      this.isAnimating = false;
    }
  }

  public void inputChanged(final EnumFacing side, @Nullable final IFluidHandler fluidHandler) {
    this.inputs.put(side, fluidHandler);

    PacketUpdateClayMetalMixerNeighbours.send(this.pos);
  }

  public void outputChanged(@Nullable final IFluidHandler fluidHandler) {
    this.output = fluidHandler;

    PacketUpdateClayMetalMixerNeighbours.send(this.pos);
  }

  public void updateAllSides() {
    this.output = this.getFluidHandler(this.world, this.pos.down());

    for(final EnumFacing side : EnumFacing.HORIZONTALS) {
      this.inputs.put(side, this.getFluidHandler(this.world, this.pos.offset(side)));
    }
  }

  private void updateRecipe() {
    this.fluidSideMap.clear();

    final NonNullList<FluidStack> fluids = NonNullList.create();

    for(final EnumFacing side : EnumFacing.HORIZONTALS) {
      final IFluidHandler handler = this.inputs.get(side);

      if(handler != null) {
        final FluidStack fluidStack = handler.drain(Integer.MAX_VALUE, false);

        if(fluidStack != null) {
          fluids.add(fluidStack);
          this.fluidSideMap.computeIfAbsent(fluidStack.getFluid(), key -> new ArrayList<>()).add(side);
        }
      }
    }

    this.recipe = RecipeUtils.findRecipe(AlloyRecipe.class, r -> r.matches(fluids));

    if(this.recipe != null) {
      this.recipeTicks = this.getTicksForRecipe(this.recipe);
    }
  }

  private int getTicksForRecipe(final AlloyRecipe recipe) {
    return recipe.getOutput().amount * 3;
  }

  @Override
  public void onLoad() {
    super.onLoad();
    this.updateAllSides();
    this.outputUpdated();

    if(!this.world.isRemote) {
      this.updateRecipe();
    }
  }

  @Override
  protected void tickBeforeCooldown() {

  }

  @Override
  protected void tickAfterCooldown() {
    if(this.world.isRemote) {
      if(this.isAnimating) {
        this.ticksValue.setValue((float)this.animationTicks / CYCLE_TICKS);
        this.animationTicks++;
        this.animationTicks %= CYCLE_TICKS;
      }

      return;
    }

    if(this.recipe != null && this.output != null) {
      if(this.recipeTicks == 0) {
        // Only mix if there's room
        if(this.output.fill(this.recipe.getOutput(), false) < this.recipe.getOutput().amount) {
          return;
        }

        // Copy ref to recipe - it can be updated inside of the for loop by block updates
        final AlloyRecipe recipe = this.recipe;

        for(final FluidStack recipeFluid : recipe.getInputs()) {
          int remaining = recipeFluid.amount;
          int failed = 0;

          final List<EnumFacing> sides = this.fluidSideMap.get(recipeFluid.getFluid());

          while(remaining > 0 || failed >= 10) {
            final int sideIndex = this.rand.nextInt(sides.size());
            final EnumFacing side = sides.get(sideIndex);

            final IFluidHandler fluidHandler = this.inputs.get(side);
            final FluidStack drained = fluidHandler.drain(1, true);

            if(drained != null && drained.amount > 0) {
              remaining--;
            } else {
              failed++;
            }
          }
        }

        this.output.fill(recipe.getOutput(), true);

        this.recipeTicks = this.getTicksForRecipe(recipe);
      } else {
        this.recipeTicks--;
      }
    }
  }

  @Override
  protected float calculateHeatLoss(final IBlockState state) {
    return (float)Math.max(0.5d, Math.pow(this.getHeat() / 800, 2));
  }

  @Override
  protected float heatTransferEfficiency() {
    return 0.6f;
  }

  @Nullable
  private IFluidHandler getFluidHandler(final IBlockAccess world, final BlockPos pos) {
    final TileEntity output = world.getTileEntity(pos);

    if(output == null) {
      return null;
    }

    return output.getCapability(FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
  }

  @Override
  public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
    return
      capability == ANIMATION_CAPABILITY ||
      super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    if(capability == ANIMATION_CAPABILITY) {
      return ANIMATION_CAPABILITY.cast(this.asm);
    }

    return super.getCapability(capability, facing);
  }
}
