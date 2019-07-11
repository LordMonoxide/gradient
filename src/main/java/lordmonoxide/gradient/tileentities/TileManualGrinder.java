package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.progress.Age;
import lordmonoxide.gradient.recipes.GradientRecipeTypes;
import lordmonoxide.gradient.recipes.GrindingRecipe;
import lordmonoxide.gradient.utils.AgeUtils;
import lordmonoxide.gradient.utils.RecipeUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.world.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileManualGrinder extends TileEntity implements ITickableTileEntity {
  @CapabilityInject(IItemHandler.class)
  private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY;

  private final ItemStackHandler inventory = new ItemStackHandler(2);

  @Nullable
  private GrindingRecipe recipe;
  private Age age = Age.AGE1;
  private int passes;
  private int ticks;

  public TileManualGrinder() {
    super(GradientTileEntities.GRINDSTONE);
  }

  public boolean hasInput() {
    return !this.inventory.getStackInSlot(0).isEmpty();
  }

  public boolean hasOutput() {
    return !this.inventory.getStackInSlot(1).isEmpty();
  }

  public ItemStack getInput() {
    return this.inventory.getStackInSlot(0);
  }

  public ItemStack getOutput() {
    return this.inventory.getStackInSlot(1);
  }

  public ItemStack takeInput() {
    this.recipe = null;
    final ItemStack input = this.inventory.extractItem(0, this.inventory.getSlotLimit(0), false);
    this.sync();
    return input;
  }

  public ItemStack takeOutput() {
    final ItemStack output = this.inventory.extractItem(1, this.inventory.getSlotLimit(1), false);
    this.sync();
    return output;
  }

  public ItemStack insertItem(final ItemStack stack, final PlayerEntity player) {
    if(!this.hasInput()) {
      this.age = AgeUtils.getPlayerAge(player);

      final ItemStack remaining = this.inventory.insertItem(0, stack, false);

      this.updateRecipe();
      this.passes = 0;

      if(this.recipe != null) {
        this.ticks = this.recipe.ticks;
      }

      this.sync();
      return remaining;
    }

    final ItemStack remaining = this.inventory.insertItem(0, stack, false);
    this.sync();
    return remaining;
  }

  @Override
  public void tick() {
    if(this.world.isRemote) {
      return;
    }

    if(this.recipe == null) {
      return;
    }

    if(this.ticks < this.recipe.ticks) {
      this.ticks++;
      this.markDirty();
    }

    if(this.ticks >= this.recipe.ticks && this.passes >= this.recipe.passes) {
      this.passes = 0;

      this.inventory.extractItem(0, 1, false);
      this.inventory.insertItem(1, this.recipe.getRecipeOutput().copy(), false);

      if(!this.hasInput()) {
        this.recipe = null;
      }

      this.sync();
    }
  }

  public void crank() {
    if(this.recipe == null) {
      return;
    }

    if(this.ticks >= this.recipe.ticks) {
      ((ServerWorld)this.world).spawnParticle(ParticleTypes.SMOKE, this.pos.getX() + 0.5d, this.pos.getY() + 0.5d, this.pos.getZ() + 0.5d, 10, 0.1d, 0.1d, 0.1d, 0.01d);

      this.ticks = 0;
      this.passes++;

      this.markDirty();
    }
  }

  private void updateRecipe() {
    this.recipe = RecipeUtils.findRecipe(GradientRecipeTypes.GRINDING, recipe -> recipe.matches(this.inventory, this.age, 0, 0));
  }

  @Override
  public CompoundNBT write(final CompoundNBT compound) {
    compound.put("inventory", this.inventory.serializeNBT());
    compound.putInt("passes", this.passes);
    compound.putInt("ticks", this.ticks);
    compound.putInt("player_age", this.age.value());

    return super.write(compound);
  }

  @Override
  public void read(final CompoundNBT compound) {
    final CompoundNBT inv = compound.getCompound("inventory");
    inv.remove("Size");
    this.inventory.deserializeNBT(inv);
    this.passes = compound.getInt("passes");
    this.ticks = compound.getInt("ticks");
    this.age = Age.get(compound.getInt("player_age"));

    this.updateRecipe();

    super.read(compound);
  }

  @Override
  public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing) {
    if(capability == ITEM_HANDLER_CAPABILITY) {
      return LazyOptional.of(() -> (T)this.inventory);
    }

    return super.getCapability(capability, facing);
  }

  private void sync() {
    if(!this.world.isRemote) {
      final BlockState state = this.world.getBlockState(this.getPos());
      this.world.notifyBlockUpdate(this.getPos(), state, state, 3);
      this.markDirty();
    }
  }

  @Override
  public SUpdateTileEntityPacket getUpdatePacket() {
    return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
  }

  @Override
  public CompoundNBT getUpdateTag() {
    return this.write(new CompoundNBT());
  }

  @Override
  public void onDataPacket(final NetworkManager net, final SUpdateTileEntityPacket pkt) {
    this.read(pkt.getNbtCompound());
  }
}
