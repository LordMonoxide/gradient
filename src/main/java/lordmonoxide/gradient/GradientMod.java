package lordmonoxide.gradient;

import lordmonoxide.gradient.energy.CapabilityEnergy;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyStorage;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyTransfer;
import lordmonoxide.gradient.energy.kinetic.KineticEnergyStorage;
import lordmonoxide.gradient.energy.kinetic.KineticEnergyTransfer;
import lordmonoxide.gradient.init.IProxy;
import lordmonoxide.gradient.progress.CapabilityPlayerProgress;
import lordmonoxide.gradient.progress.SetAgeCommand;
import lordmonoxide.gradient.recipes.RecipeRemover;
import lordmonoxide.gradient.worldgen.DisableVanillaOre;
import lordmonoxide.gradient.worldgen.OreGenerator;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarFile;

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
  @SidedProxy(serverSide = "lordmonoxide.gradient.init.ServerProxy", clientSide = "lordmonoxide.gradient.init.ClientProxy")
  @Nonnull
  public static IProxy proxy;

  @SuppressWarnings({"StaticNonFinalField", "NullableProblems"})
  @Nonnull
  public static Logger logger;

  @Mod.EventHandler
  public void preInit(final FMLPreInitializationEvent event) throws URISyntaxException, IOException {
    //noinspection AssignmentToStaticFieldFromInstanceMethod
    logger = event.getModLog();

    logger.info("{} is loading!", NAME);
    logger.info("------------------- PREINIT -------------------");

    this.syncTriumphAdvancements(event.getModConfigurationDirectory());

    CapabilityPlayerProgress.register();

    MinecraftForge.ORE_GEN_BUS.register(DisableVanillaOre.class);

    CapabilityEnergy.register(
      IKineticEnergyStorage.class,
      IKineticEnergyTransfer.class,
      () -> new KineticEnergyStorage(10000.0f),
      KineticEnergyTransfer::new
    );

    NetworkRegistry.INSTANCE.registerGuiHandler(GradientMod.instance, new GradientGuiHandler());

    FluidRegistry.enableUniversalBucket();

    proxy.preInit(event);
  }

  @Mod.EventHandler
  public void init(final FMLInitializationEvent event) {
    logger.info("------------------- INIT -------------------");

    GameRegistry.registerWorldGenerator(new OreGenerator(), 0);

    GradientMetals.registerMeltables();

    GradientNet.register();

    proxy.init(event);
  }

  @Mod.EventHandler
  public void postInit(final FMLPostInitializationEvent event) {
    logger.info("------------------- POSTINIT -------------------");

    proxy.postInit(event);

    RecipeRemover.replacePlankRecipes((IForgeRegistryModifiable<IRecipe>)ForgeRegistries.RECIPES);
  }

  @Mod.EventHandler
  public void serverStarting(final FMLServerStartingEvent event) {
    logger.info("------------------- SERVER STARTING -------------------");

    event.registerServerCommand(new SetAgeCommand());
  }

  public static ResourceLocation resource(final String path) {
    return new ResourceLocation(MODID, path);
  }

  private void syncTriumphAdvancements(final File configDir) throws URISyntaxException, IOException {
    final Path destDir = configDir.toPath().resolve("triumph/script/" + MODID);

    logger.info("Copying triumphs to " + destDir);

    FileUtils.deleteDirectory(destDir.toFile());

    final URLConnection connection = this.getClass().getResource("").openConnection();

    if(connection instanceof JarURLConnection) {
      final JarFile jar = ((JarURLConnection)connection).getJarFile();

      jar.stream().forEach(entry -> {
        if(!entry.isDirectory() && entry.getName().startsWith("assets/gradient/triumph/")) {
          try(final InputStream stream = jar.getInputStream(jar.getEntry(entry.getName()))) {
            final Path path = destDir.resolve(entry.getName().substring(24));
            Files.createDirectories(path.getParent());
            IOUtils.copy(stream, Files.newOutputStream(path));
          } catch(final IOException e) {
            GradientMod.logger.error("Error copying triumph", e);
          }
        }
      });
    } else {
      final File sourceDir = Paths.get(this.getClass().getResource("../../assets/" + MODID + "/triumph").toURI()).toFile();
      FileUtils.copyDirectory(sourceDir, destDir.toFile());
    }
  }
}
