package lordmonoxide.gradient.tileentities;

import com.google.common.collect.ImmutableMap;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.GradientSounds;
import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.recipes.GrindingRecipe;
import lordmonoxide.gradient.utils.AgeUtils;
import lordmonoxide.gradient.utils.RecipeUtils;
import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.animation.TimeValues;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileManualGrinder extends TileEntity implements ITickable {
  @CapabilityInject(IItemHandler.class)
  private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY;

  @CapabilityInject(IAnimationStateMachine.class)
  private static Capability<IAnimationStateMachine> ANIMATION_CAPABILITY;

  private static final int INPUT_SLOT = 0;
  private static final int OUTPUT_SLOT = 1;

  private final ItemStackHandler inventory = new ItemStackHandler(2) {
    @Override
    public boolean isItemValid(final int slot, @Nonnull final ItemStack stack) {
      if(slot == INPUT_SLOT) {
        return RecipeUtils.findRecipe(GrindingRecipe.class, recipe -> recipe.matches(stack)) != null;
      }

      return false;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(final int slot, @Nonnull final ItemStack stack, final boolean simulate) {
      if(!this.isItemValid(slot, stack) && !TileManualGrinder.this.forceInsert) {
        return stack;
      }

      return super.insertItem(slot, stack, simulate);
    }

    @Override
    protected void onContentsChanged(final int slot) {
      if(slot == INPUT_SLOT) {
        final ItemStack stack = this.getStackInSlot(slot);

        if(!stack.isEmpty()) {
          if(TileManualGrinder.this.recipe == null) {
            TileManualGrinder.this.passes = 0;
            TileManualGrinder.this.ticks = 0;
            TileManualGrinder.this.updateRecipe();

            if(TileManualGrinder.this.recipe != null) {
              TileManualGrinder.this.ticks = TileManualGrinder.this.recipe.ticks;
            }
          }
        } else {
          TileManualGrinder.this.recipe = null;
          TileManualGrinder.this.passes = 0;
          TileManualGrinder.this.ticks = 0;
        }
      }

      TileManualGrinder.this.sync();
    }
  };

  @Nullable
  private final IAnimationStateMachine asm;
  private final TimeValues.VariableValue ticksValue = new TimeValues.VariableValue(0.0f);

  @Nullable
  private GrindingRecipe recipe;
  private Age age = Age.AGE1;
  private int passes;
  private int ticks;
  private boolean forceInsert;

  public TileManualGrinder() {
    this.asm = GradientMod.proxy.loadAsm(GradientMod.resource("asms/block/manual_grinder.json"), ImmutableMap.of("spinning_cycle", this.ticksValue));
  }

  public boolean hasInput() {
    return !this.inventory.getStackInSlot(INPUT_SLOT).isEmpty();
  }

  public boolean hasOutput() {
    return !this.inventory.getStackInSlot(OUTPUT_SLOT).isEmpty();
  }

  public ItemStack getInput() {
    return this.inventory.getStackInSlot(INPUT_SLOT);
  }

  public ItemStack getOutput() {
    return this.inventory.getStackInSlot(OUTPUT_SLOT);
  }

  public ItemStack takeInput() {
    return this.inventory.extractItem(INPUT_SLOT, this.inventory.getSlotLimit(0), false);
  }

  public ItemStack takeOutput() {
    return this.inventory.extractItem(OUTPUT_SLOT, this.inventory.getSlotLimit(1), false);
  }

  public ItemStack insertItem(final ItemStack stack, final EntityPlayer player) {
    if(!this.hasInput()) {
      this.age = AgeUtils.getPlayerAge(player);
      return this.inventory.insertItem(INPUT_SLOT, stack, false);
    }

    return this.inventory.insertItem(INPUT_SLOT, stack, false);
  }

  @Override
  public void update() {
    if(this.recipe == null) {
      return;
    }

    if(this.ticks < this.recipe.ticks) {
      this.ticks++;
      this.markDirty();
    }

    if(this.world.isRemote) {
      if(this.ticks < this.recipe.ticks) {
        if("idle".equals(this.asm.currentState())) {
          this.asm.transition("spinning");
        }

        this.ticksValue.setValue((float)this.ticks / this.recipe.ticks);
      } else {
        if("spinning".equals(this.asm.currentState())) {
          this.asm.transition("idle");
        }
      }

      return;
    }

    if(this.ticks >= this.recipe.ticks && this.passes >= this.recipe.passes) {
      this.passes = 0;

      this.inventory.extractItem(INPUT_SLOT, 1, false);
      this.forceInsert = true;
      this.inventory.insertItem(OUTPUT_SLOT, this.recipe.getRecipeOutput().copy(), false);
      this.forceInsert = false;
    }
  }

  public void crank() {
    if(this.recipe == null) {
      return;
    }

    if(this.ticks >= this.recipe.ticks) {
      this.world.playSound(null, this.pos, GradientSounds.GRINDSTONE, SoundCategory.NEUTRAL, 0.8f, this.world.rand.nextFloat() * 0.1f + 0.9f);
      ((WorldServer)this.world).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.pos.getX() + 0.5d, this.pos.getY() + 0.5d, this.pos.getZ() + 0.5d, 10, 0.1d, 0.1d, 0.1d, 0.01d);

      this.ticks = 0;
      this.passes++;

      this.sync();
    }
  }

  private void updateRecipe() {
    this.recipe = RecipeUtils.findRecipe(GrindingRecipe.class, recipe -> recipe.matches(this.getInput(), this.age));
  }

  @Override
  public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
    compound.setTag("inventory", this.inventory.serializeNBT());
    compound.setInteger("passes", this.passes);
    compound.setInteger("ticks", this.ticks);
    compound.setInteger("player_age", this.age.value());

    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(final NBTTagCompound compound) {
    final NBTTagCompound inv = compound.getCompoundTag("inventory");
    inv.removeTag("Size");
    this.inventory.deserializeNBT(inv);
    this.passes = compound.getInteger("passes");
    this.ticks = compound.getInteger("ticks");
    this.age = Age.get(compound.getInteger("player_age"));

    this.updateRecipe();

    super.readFromNBT(compound);
  }

  @Override
  public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
    return
      capability == ITEM_HANDLER_CAPABILITY ||
      capability == ANIMATION_CAPABILITY ||
      super.hasCapability(capability, facing);
  }

  @Nullable
  @Override
  public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
    if(capability == ITEM_HANDLER_CAPABILITY) {
      return ITEM_HANDLER_CAPABILITY.cast(this.inventory);
    }

    if(capability == ANIMATION_CAPABILITY) {
      return ANIMATION_CAPABILITY.cast(this.asm);
    }

    return super.getCapability(capability, facing);
  }

  private void sync() {
    if(!this.getWorld().isRemote) {
      WorldUtils.notifyUpdate(this.world, this.pos);
      this.markDirty();
    }
  }

  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
  }

  @Override
  public NBTTagCompound getUpdateTag() {
    return this.writeToNBT(new NBTTagCompound());
  }

  @Override
  public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity pkt) {
    this.readFromNBT(pkt.getNbtCompound());
  }
}
