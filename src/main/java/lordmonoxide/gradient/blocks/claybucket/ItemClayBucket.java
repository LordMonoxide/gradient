package lordmonoxide.gradient.blocks.claybucket;

import lordmonoxide.gradient.ModelManager;
import lordmonoxide.gradient.items.GradientItem;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemClayBucket extends GradientItem implements ModelManager.CustomModel {
  private final ItemStack empty;
  private final int capacity;

  public ItemClayBucket() {
    super("clay_bucket_item", CreativeTabs.MISC);

    this.empty = new ItemStack(this);
    this.capacity = Fluid.BUCKET_VOLUME;

    this.setMaxStackSize(1);

    BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DispenseFluidContainer.getInstance());
  }

  /**
   * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
   */
  @Override
  public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> subItems) {
    if(!this.isInCreativeTab(tab)) {
      return;
    }

    subItems.add(this.empty);

    for(Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
      // add all fluids that the bucket can be filled  with
      FluidStack fs = new FluidStack(fluid, this.capacity);
      ItemStack stack = new ItemStack(this);
      IFluidHandlerItem fluidHandler = new FluidBucketWrapper(stack);

      if(fluidHandler.fill(fs, true) == fs.amount) {
        subItems.add(fluidHandler.getContainer());
      }
    }
  }

  @Override
  public String getItemStackDisplayName(final ItemStack stack) {
    FluidStack fluidStack = getFluid(stack);
    if(fluidStack == null) {
      return I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".empty.name").trim();
    }

    String unloc = this.getUnlocalizedNameInefficiently(stack);

    if(I18n.canTranslate(unloc + "." + fluidStack.getFluid().getName())) {
      return I18n.translateToLocal(unloc + "." + fluidStack.getFluid().getName());
    }

    return I18n.translateToLocalFormatted(unloc + ".name", fluidStack.getLocalizedName());
  }

  @Nullable
  public FluidStack getFluid(final ItemStack container) {
    return FluidStack.loadFluidStackFromNBT(container.getTagCompound());
  }

  @Override
  public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable NBTTagCompound nbt) {
    return new FluidBucketWrapper(stack);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
    ItemStack itemstack = player.getHeldItem(hand);
    FluidStack fluidStack = getFluid(itemstack);

    // clicked on a block?
    final RayTraceResult mop = this.rayTrace(world, player, fluidStack == null);

    if(fluidStack == null) {
      ActionResult<ItemStack> ret = ForgeEventFactory.onBucketUse(player, world, itemstack, mop);
      if(ret != null) return ret;
    }

    if(mop == null || mop.typeOfHit != RayTraceResult.Type.BLOCK) {
      return ActionResult.newResult(EnumActionResult.PASS, itemstack);
    }

    BlockPos clickPos = mop.getBlockPos();
    // can we place liquid there?
    if(world.isBlockModifiable(player, clickPos)) {
      // the block adjacent to the side we clicked on
      BlockPos targetPos = clickPos.offset(mop.sideHit);

      // can the player place there?
      if(player.canPlayerEdit(targetPos, mop.sideHit, itemstack)) {
        // try placing liquid
        FluidActionResult result = FluidUtil.tryPlaceFluid(player, world, targetPos, itemstack, fluidStack);
        if(result.isSuccess() && !player.capabilities.isCreativeMode) {
          itemstack.shrink(1);
          ItemStack drained = result.getResult();
          ItemStack emptyStack = !drained.isEmpty() ? drained.copy() : new ItemStack(this);

          // check whether we replace the item or add the empty one to the inventory
          if(itemstack.isEmpty()) {
            return ActionResult.newResult(EnumActionResult.SUCCESS, emptyStack);
          } else {
            // add empty bucket to player inventory
            ItemHandlerHelper.giveItemToPlayer(player, emptyStack);
            return ActionResult.newResult(EnumActionResult.SUCCESS, itemstack);
          }
        }
      }
    }

    // couldn't place liquid there2
    return ActionResult.newResult(EnumActionResult.FAIL, itemstack);
  }

  @SubscribeEvent(priority = EventPriority.LOW) // low priority so other mods can handle their stuff first
  public void onFillBucket(FillBucketEvent event) {
    if(event.getResult() != Event.Result.DEFAULT) {
      // event was already handled
      return;
    }

    // not for us to handle
    ItemStack emptyBucket = event.getEmptyBucket();
    if(emptyBucket.isEmpty() || !emptyBucket.isItemEqual(ItemClayBucket.this.empty)) {
      return;
    }

    // needs to target a block
    RayTraceResult target = event.getTarget();
    if(target == null || target.typeOfHit != RayTraceResult.Type.BLOCK) {
      return;
    }

    World world = event.getWorld();
    BlockPos pos = target.getBlockPos();

    ItemStack singleBucket = emptyBucket.copy();
    singleBucket.setCount(1);

    FluidActionResult filledResult = FluidUtil.tryPickUpFluid(singleBucket, event.getEntityPlayer(), world, pos, target.sideHit);
    if(filledResult.isSuccess()) {
      event.setResult(Event.Result.ALLOW);
      event.setFilledBucket(filledResult.getResult());
    } else {
      // cancel event, otherwise the vanilla minecraft ItemBucket would
      // convert it into a water/lava bucket depending on the blocks material
      event.setCanceled(true);
    }
  }

  @Override
  public void registerCustomModels() {
    ModelLoader.setBucketModelDefinition(this);
  }

  public class FluidBucketWrapper implements IFluidHandlerItem, ICapabilityProvider {
    protected ItemStack container;

    public FluidBucketWrapper(ItemStack container) {
      this.container = container;
    }

    @Override
    public ItemStack getContainer() {
      return container;
    }

    public boolean canFillFluidType(FluidStack fluid) {
      if(fluid.getFluid() == FluidRegistry.WATER || fluid.getFluid() == FluidRegistry.LAVA || fluid.getFluid().getName().equals("milk")) {
        return true;
      }

      return FluidRegistry.getBucketFluids().contains(fluid.getFluid());
    }

    @Nullable
    public FluidStack getFluid() {
      return ItemClayBucket.this.getFluid(this.container);
    }

    /**
     * @deprecated use the NBT-sensitive version {@link #setFluid(FluidStack)}
     */
    @Deprecated
    protected void setFluid(@Nullable Fluid fluid) {
      setFluid(new FluidStack(fluid, Fluid.BUCKET_VOLUME));
    }

    protected void setFluid(@Nullable FluidStack fluidStack) {
      if(fluidStack == null) {
        container.setTagCompound(new NBTTagCompound());
      } else {
        NBTTagCompound tag = new NBTTagCompound();
        fluidStack.writeToNBT(tag);
        container.setTagCompound(tag);
      }
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
      return new FluidTankProperties[] { new FluidTankProperties(getFluid(), Fluid.BUCKET_VOLUME) };
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
      if(container.getCount() != 1 || resource == null || resource.amount < Fluid.BUCKET_VOLUME || getFluid() != null || !canFillFluidType(resource)) {
        return 0;
      }

      if(doFill) {
        setFluid(resource);
      }

      return Fluid.BUCKET_VOLUME;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
      if(container.getCount() != 1 || resource == null || resource.amount < Fluid.BUCKET_VOLUME) {
        return null;
      }

      FluidStack fluidStack = getFluid();
      if(fluidStack != null && fluidStack.isFluidEqual(resource)) {
        if(doDrain) {
          setFluid((FluidStack) null);
        }

        return fluidStack;
      }

      return null;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
      if(container.getCount() != 1 || maxDrain < Fluid.BUCKET_VOLUME) {
        return null;
      }

      FluidStack fluidStack = getFluid();
      if(fluidStack != null) {
        if(doDrain) {
          setFluid((FluidStack) null);
        }

        return fluidStack;
      }

      return null;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
      return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
    }

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
      if(capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY) {
        return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.cast(this);
      }

      return null;
    }
  }
}
