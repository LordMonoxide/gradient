package lordmonoxide.gradient.recipes;

import lordmonoxide.gradient.GradientMod;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(GradientMod.MOD_ID)
public final class GradientRecipeSerializers {
  private GradientRecipeSerializers() { }

  public static final IRecipeSerializer<AgeGatedShapedToolRecipe>    SHAPED    = null;
  public static final IRecipeSerializer<AgeGatedShapelessToolRecipe> SHAPELESS = null;
  public static final IRecipeSerializer<DryingRecipe>                DRYING    = null;
  public static final IRecipeSerializer<FirePitRecipe>               FIREPIT   = null;
  public static final IRecipeSerializer<FuelRecipe>                  FUEL      = null;
  public static final IRecipeSerializer<GrindingRecipe>              GRINDING  = null;
  public static final IRecipeSerializer<HardeningRecipe>             HARDENING = null;
  public static final IRecipeSerializer<MixingRecipe>                MIXING    = null;

  @Mod.EventBusSubscriber(modid = GradientMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
  public static final class Registration {
    private Registration() { }

    @SubscribeEvent
    public static void onRegistration(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
      GradientMod.logger.info("Registering recipe serializers...");

      final IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();

      registry.register(new AgeGatedShapedToolRecipe.Serializer().setRegistryName(GradientMod.resource("shaped")));
      registry.register(new AgeGatedShapelessToolRecipe.Serializer().setRegistryName(GradientMod.resource("shapeless")));
      registry.register(new DryingRecipe.Serializer().setRegistryName(GradientMod.resource("drying")));
      registry.register(new FirePitRecipe.Serializer().setRegistryName(GradientMod.resource("firepit")));
      registry.register(new FuelRecipe.Serializer().setRegistryName(GradientMod.resource("fuel")));
      registry.register(new GrindingRecipe.Serializer().setRegistryName(GradientMod.resource("grinding")));
      registry.register(new HardeningRecipe.Serializer().setRegistryName(GradientMod.resource("hardening")));
      registry.register(new MixingRecipe.Serializer().setRegistryName(GradientMod.resource("mixing")));
    }
  }
}
