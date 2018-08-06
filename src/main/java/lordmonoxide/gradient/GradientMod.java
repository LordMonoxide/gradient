package lordmonoxide.gradient;

import lordmonoxide.gradient.init.IProxy;
import lordmonoxide.gradient.overrides.*;
import lordmonoxide.gradient.worldgen.DisableVanillaOre;
import lordmonoxide.gradient.worldgen.GeneratePebbles;
import lordmonoxide.gradient.worldgen.OreGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

@Mod(modid = GradientMod.MODID, name = GradientMod.NAME, version = GradientMod.VERSION, dependencies = "after:ic2")
public class GradientMod {
  public static final String MODID = "gradient";
  public static final String NAME = "Gradient";
  public static final String VERSION = "${version}";

  @SuppressWarnings({"StaticNonFinalField", "NullableProblems"})
  @Mod.Instance(MODID)
  @Nonnull
  public static GradientMod instance;

  @SuppressWarnings({"StaticNonFinalField", "NullableProblems"})
  @SidedProxy(serverSide = "lordmonoxide.gradient.ServerProxy", clientSide = "lordmonoxide.gradient.init.ClientProxy")
  @Nonnull
  public static IProxy proxy;

  @SuppressWarnings({"StaticNonFinalField", "NullableProblems"})
  @Nonnull
  public static Logger logger;

  static {
    FluidRegistry.enableUniversalBucket();
  }

  @Mod.EventHandler
  public void preInit(final FMLPreInitializationEvent event) {
    //noinspection AssignmentToStaticFieldFromInstanceMethod
    logger = event.getModLog();

    logger.info("{} is loading!", NAME);
    logger.info("------------------- PREINIT -------------------");

    MinecraftForge.EVENT_BUS.register(OverrideInventory.instance);
    MinecraftForge.ORE_GEN_BUS.register(DisableVanillaOre.class);
    NetworkRegistry.INSTANCE.registerGuiHandler(GradientMod.instance, new GradientGuiHandler());

    proxy.preInit(event);
  }

  @Mod.EventHandler
  public void init(final FMLInitializationEvent event) {
    logger.info("------------------- INIT -------------------");

    MinecraftForge.EVENT_BUS.register(DisableVanillaTools.instance);
    MinecraftForge.EVENT_BUS.register(DisableBreakingBlocksWithoutTools.instance);
    MinecraftForge.EVENT_BUS.register(AddExtraDrops.instance);

    GameRegistry.registerWorldGenerator(new GeneratePebbles(), 0);
    GameRegistry.registerWorldGenerator(new OreGenerator(), 0);

    GradientMetals.registerMeltables();

    GradientNet.register();

    proxy.init(event);
  }

  @Mod.EventHandler
  public void postInit(final FMLPostInitializationEvent event) {
    logger.info("------------------- POSTINIT -------------------");

    proxy.postInit(event);
  }

  public static ResourceLocation resource(final String path) {
    return new ResourceLocation(MODID, path);
  }
}
