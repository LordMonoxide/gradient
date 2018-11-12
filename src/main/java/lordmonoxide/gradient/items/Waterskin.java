package lordmonoxide.gradient.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.ItemFluidContainer;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class Waterskin extends ItemFluidContainer {
  public Waterskin() {
    super(Fluid.BUCKET_VOLUME);
    this.setTranslationKey("waterskin");
    this.setRegistryName("waterskin");
    this.setCreativeTab(CreativeTabs.TOOLS);
    this.setHasSubtypes(true);
    this.setMaxStackSize(1);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(final ItemStack stack, @Nullable final World world, final List<String> tooltip, final ITooltipFlag flag) {
    if(stack.hasTagCompound() && stack.getTagCompound().hasKey("Fluid")) {
      final FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTagCompound().getCompoundTag("Fluid"));

      if(fluid != null) {
        tooltip.add(TextFormatting.GREEN + "Contains: " + fluid.getFluid().getLocalizedName(fluid));
        tooltip.add(TextFormatting.BLUE.toString() + fluid.amount + " mB");
      }
    } else {
      tooltip.add(TextFormatting.RED + "Empty");
    }
  }

  @Override
  public boolean hasContainerItem(final ItemStack stack) {
    return true;
  }

  @Override
  public ItemStack getContainerItem(final ItemStack stack) {
    return new ItemStack(stack.getItem(), 1, 0);
  }

  @Nullable
  public static FluidStack getFluid(@Nonnull final ItemStack stack) {
    if(stack.hasTagCompound() && stack.getTagCompound().hasKey("Fluid")) {
      return FluidStack.loadFluidStackFromNBT(stack.getTagCompound().getCompoundTag("Fluid"));
    }

    return FluidStack.loadFluidStackFromNBT(stack.getTagCompound());
  }

  @Override
  @Nonnull
  public ActionResult<ItemStack> onItemRightClick(@Nonnull final World world, @Nonnull final EntityPlayer player, @Nonnull final EnumHand hand) {
    final ItemStack itemstack = player.getHeldItem(hand);
    final FluidStack fluidStack = getFluid(itemstack);
    if(fluidStack == null) {
      final RayTraceResult target = this.rayTrace(world, player, true);

      if(target.typeOfHit != RayTraceResult.Type.BLOCK) {
        return ActionResult.newResult(EnumActionResult.PASS, itemstack);
      }

      final BlockPos pos = target.getBlockPos();
      final FluidActionResult filledResult = FluidUtil.tryPickUpFluid(itemstack, player, world, pos, target.sideHit);

      if(filledResult.isSuccess()) {
        final ItemStack filledStack = filledResult.getResult().copy();
        filledStack.setItemDamage(1);
        return ActionResult.newResult(EnumActionResult.SUCCESS, filledStack);
      }

      return ActionResult.newResult(EnumActionResult.PASS, itemstack);
    }

    return ActionResult.newResult(EnumActionResult.FAIL, itemstack);
  }

  @Override
  public ICapabilityProvider initCapabilities(@Nonnull final ItemStack stack, final @Nullable NBTTagCompound nbt) {
    return new FluidHandlerItemStack.SwapEmpty(stack, stack, this.capacity) {
      @Nonnull
      @Override
      public ItemStack getContainer() {
        if(stack.hasTagCompound() && stack.getTagCompound().hasKey("Fluid")) {
          this.container.setItemDamage(1);
        } else {
          this.container.setItemDamage(0);
        }

        return this.container;
      }

      @Override
      protected void setContainerToEmpty() {
        if(stack.hasTagCompound() && stack.getTagCompound().hasKey("Fluid")) {
          this.container.getTagCompound().removeTag("Fluid");
          this.container.setItemDamage(0);
        } else {
          this.container.setItemDamage(1);
        }
      }
    };
  }

  @Override
  public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> subItems) {
    if(!this.isInCreativeTab(tab)) {
      return;
    }

    subItems.add(new ItemStack(this, 1, 0));

    final NBTTagCompound nbt = new NBTTagCompound();
    nbt.setTag("Fluid", new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME).writeToNBT(new NBTTagCompound()));
    final ItemStack filled = new ItemStack(this, 1, 1);
    filled.setTagCompound(nbt);
    subItems.add(filled);
  }
}
