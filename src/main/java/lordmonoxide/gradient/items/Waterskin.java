package lordmonoxide.gradient.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.ItemFluidContainer;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class Waterskin extends ItemFluidContainer {
  public Waterskin() {
    super(new Properties().group(ItemGroup.TOOLS).maxStackSize(1), Fluid.BUCKET_VOLUME);
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public void addInformation(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
    if(stack.hasTag() && stack.getTag().contains("Fluid")) {
      final FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack.getTag().getCompound("Fluid"));

      if(fluid != null) {
        //TODO tooltip.add(TextFormatting.GREEN + "Contains: " + fluid.getFluid().getLocalizedName(fluid));
        //TODO tooltip.add(TextFormatting.BLUE.toString() + fluid.amount + " mB");
      }
    } else {
      //TODO tooltip.add(TextFormatting.RED + "Empty");
    }
  }

  @Override
  public boolean hasContainerItem(final ItemStack stack) {
    return true;
  }

  @Override
  public ItemStack getContainerItem(final ItemStack stack) {
    return new ItemStack(stack.getItem());
  }

  @Nullable
  public static FluidStack getFluid(@Nonnull final ItemStack stack) {
    if(stack.hasTag() && stack.getTag().contains("Fluid")) {
      return FluidStack.loadFluidStackFromNBT(stack.getTag().getCompound("Fluid"));
    }

    return FluidStack.loadFluidStackFromNBT(stack.getTag());
  }

  @Override
  @Nonnull
  public ActionResult<ItemStack> onItemRightClick(@Nonnull final World world, @Nonnull final EntityPlayer player, @Nonnull final EnumHand hand) {
    final ItemStack itemstack = player.getHeldItem(hand);
    final FluidStack fluidStack = getFluid(itemstack);

    if(fluidStack == null) {
      final RayTraceResult target = this.rayTrace(world, player, true);

      if(target == null || target.type != RayTraceResult.Type.BLOCK) {
        return ActionResult.newResult(EnumActionResult.PASS, itemstack);
      }

      final BlockPos pos = target.getBlockPos();
      final FluidActionResult filledResult = FluidUtil.tryPickUpFluid(itemstack, player, world, pos, target.sideHit);

      if(filledResult.isSuccess()) {
        final ItemStack filledStack = filledResult.getResult().copy();
        //TODO filledStack.setItemDamage(1);
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
        if(stack.hasTag() && stack.getTag().contains("Fluid")) {
          //TODO this.container.setItemDamage(1);
        } else {
          //TODO this.container.setItemDamage(0);
        }

        return this.container;
      }

      @Override
      protected void setContainerToEmpty() {
        if(stack.hasTag() && stack.getTag().contains("Fluid")) {
          this.container.getTag().remove("Fluid");
          //TODO this.container.setItemDamage(0);
        } else {
          //TODO this.container.setItemDamage(1);
        }
      }
    };
  }

  public ItemStack getFilled(final Fluid fluid) {
    final NBTTagCompound nbt = new NBTTagCompound();
    nbt.put("Fluid", new FluidStack(fluid, Fluid.BUCKET_VOLUME).writeToNBT(new NBTTagCompound()));
    final ItemStack filled = new ItemStack(this, 1);
    //TODO filled.setTagCompound(nbt);
    return filled;
  }
}
