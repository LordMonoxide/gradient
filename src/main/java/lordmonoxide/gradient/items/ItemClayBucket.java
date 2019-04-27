package lordmonoxide.gradient.items;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.DispenseFluidContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public class ItemClayBucket extends GradientItem {
  private final ItemStack empty;

  public ItemClayBucket() {
    super("clay_bucket_item", new Properties().group(ItemGroup.TOOLS).maxStackSize(1));

    this.empty = new ItemStack(this);

    //TODO this.setContainerItem(this);

    BlockDispenser.registerDispenseBehavior(this, DispenseFluidContainer.getInstance());
  }

  public static ItemStack getFilledBucket(final Fluid fluid) {
    final ItemStack filledBucket = new ItemStack(GradientItems.CLAY_BUCKET);
    final FluidStack fluidContents = new FluidStack(fluid, Fluid.BUCKET_VOLUME);

    final NBTTagCompound tag = new NBTTagCompound();
    fluidContents.writeToNBT(tag);
    //TODO filledBucket.setTagCompound(tag);

    return filledBucket;
  }

  @Nullable
  public FluidStack getFluid(final ItemStack container) {
    return null; //TODO FluidStack.loadFluidStackFromNBT(container.getTagCompound());
  }

  @Override
  public ICapabilityProvider initCapabilities(@Nonnull final ItemStack stack, @Nullable final NBTTagCompound nbt) {
    return new FluidBucketWrapper(stack);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
    final ItemStack itemstack = player.getHeldItem(hand);
    final FluidStack fluidStack = this.getFluid(itemstack);

    // clicked on a block?
    final RayTraceResult mop = this.rayTrace(world, player, fluidStack == null);

    if(fluidStack == null) {
      final ActionResult<ItemStack> ret = ForgeEventFactory.onBucketUse(player, world, itemstack, mop);

      if(ret != null) {
        return ret;
      }
    }

    if(mop == null || mop.type != RayTraceResult.Type.BLOCK) {
      return ActionResult.newResult(EnumActionResult.PASS, itemstack);
    }

    final BlockPos clickPos = mop.getBlockPos();
    // can we place liquid there?
    if(world.isBlockModifiable(player, clickPos)) {
      // the block adjacent to the side we clicked on
      final BlockPos targetPos = clickPos.offset(mop.sideHit);

      // can the player place there?
      if(player.canPlayerEdit(targetPos, mop.sideHit, itemstack)) {
        // try placing liquid
        final FluidActionResult result = FluidUtil.tryPlaceFluid(player, world, targetPos, itemstack, fluidStack);
        if(result.isSuccess() && !player.isCreative()) {
          itemstack.shrink(1);
          final ItemStack drained = result.getResult();
          final ItemStack emptyStack = !drained.isEmpty() ? drained.copy() : new ItemStack(this);

          // check whether we replace the item or add the empty one to the inventory
          if(itemstack.isEmpty()) {
            return ActionResult.newResult(EnumActionResult.SUCCESS, emptyStack);
          }

          // add empty bucket to player inventory
          ItemHandlerHelper.giveItemToPlayer(player, emptyStack);
          return ActionResult.newResult(EnumActionResult.SUCCESS, itemstack);
        }
      }
    }

    // couldn't place liquid there2
    return ActionResult.newResult(EnumActionResult.FAIL, itemstack);
  }

  @SubscribeEvent(priority = EventPriority.LOW) // low priority so other mods can handle their stuff first
  public static void onFillBucket(final FillBucketEvent event) {
    if(event.getResult() != Event.Result.DEFAULT) {
      // event was already handled
      return;
    }

    // not for us to handle
    final ItemStack emptyBucket = event.getEmptyBucket();
    if(emptyBucket.isEmpty() || emptyBucket.getItem() != GradientItems.CLAY_BUCKET) {
      return;
    }

    // needs to target a block
    final RayTraceResult target = event.getTarget();
    if(target == null || target.type != RayTraceResult.Type.BLOCK) {
      return;
    }

    final World world = event.getWorld();
    final BlockPos pos = target.getBlockPos();

    final ItemStack singleBucket = emptyBucket.copy();
    singleBucket.setCount(1);

    final FluidActionResult filledResult = FluidUtil.tryPickUpFluid(singleBucket, event.getEntityPlayer(), world, pos, target.sideHit);
    if(filledResult.isSuccess()) {
      event.setResult(Event.Result.ALLOW);
      event.setFilledBucket(filledResult.getResult());
    } else {
      // cancel event, otherwise the vanilla minecraft ItemBucket would
      // convert it into a water/lava bucket depending on the blocks material
      event.setCanceled(true);
    }
  }

  public class FluidBucketWrapper implements IFluidHandlerItem, ICapabilityProvider {
    protected final ItemStack container;

    public FluidBucketWrapper(final ItemStack container) {
      this.container = container;
    }

    @Override
    public ItemStack getContainer() {
      return this.container;
    }

    public boolean canFillFluidType(final FluidStack fluid) {
      //TODO
      return false;
/*
      if(fluid.getFluid() == FluidRegistry.WATER || fluid.getFluid() == FluidRegistry.LAVA || "milk".equals(fluid.getFluid().getName())) {
        return true;
      }

      return FluidRegistry.getBucketFluids().contains(fluid.getFluid());
*/
    }

    @Nullable
    public FluidStack getFluid() {
      return ItemClayBucket.this.getFluid(this.container);
    }

    protected void setFluid(@Nullable final FluidStack fluidStack) {
      if(fluidStack == null) {
        this.container.setTag(new NBTTagCompound());
      } else {
        final NBTTagCompound tag = new NBTTagCompound();
        fluidStack.writeToNBT(tag);
        this.container.setTag(tag);
      }
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
      return new FluidTankProperties[] { new FluidTankProperties(this.getFluid(), Fluid.BUCKET_VOLUME) };
    }

    @Override
    public int fill(final FluidStack resource, final boolean doFill) {
      if(this.container.getCount() != 1 || resource == null || resource.amount < Fluid.BUCKET_VOLUME || this.getFluid() != null || !this.canFillFluidType(resource)) {
        return 0;
      }

      if(doFill) {
        this.setFluid(resource);
      }

      return Fluid.BUCKET_VOLUME;
    }

    @Nullable
    @Override
    public FluidStack drain(final FluidStack resource, final boolean doDrain) {
      if(this.container.getCount() != 1 || resource == null || resource.amount < Fluid.BUCKET_VOLUME) {
        return null;
      }

      final FluidStack fluidStack = this.getFluid();
      if(fluidStack != null && fluidStack.isFluidEqual(resource)) {
        if(doDrain) {
          this.setFluid(null);
        }

        return fluidStack;
      }

      return null;
    }

    @Nullable
    @Override
    public FluidStack drain(final int maxDrain, final boolean doDrain) {
      if(this.container.getCount() != 1 || maxDrain < Fluid.BUCKET_VOLUME) {
        return null;
      }

      final FluidStack fluidStack = this.getFluid();
      if(fluidStack != null) {
        if(doDrain) {
          this.setFluid(null);
        }

        return fluidStack;
      }

      return null;
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> capability, @Nullable final EnumFacing facing) {
      if(capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY) {
        return LazyOptional.of(() -> (T)this);
      }

      return LazyOptional.empty();
    }
  }
}
