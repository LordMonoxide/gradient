package lordmonoxide.gradient;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import lordmonoxide.gradient.advancements.AdvancementTriggers;
import lordmonoxide.gradient.client.gui.BronzeBoilerScreen;
import lordmonoxide.gradient.client.gui.BronzeFurnaceScreen;
import lordmonoxide.gradient.client.gui.BronzeGrinderScreen;
import lordmonoxide.gradient.client.gui.BronzeOvenScreen;
import lordmonoxide.gradient.client.gui.ClayCastScreen;
import lordmonoxide.gradient.client.gui.ClayCrucibleScreen;
import lordmonoxide.gradient.containers.GradientContainers;
import lordmonoxide.gradient.energy.CapabilityEnergy;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyStorage;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyTransfer;
import lordmonoxide.gradient.energy.kinetic.KineticEnergyStorage;
import lordmonoxide.gradient.energy.kinetic.KineticEnergyTransfer;
import lordmonoxide.gradient.hacks.FixToolBreakingNotFiringHarvestDropEvents;
import lordmonoxide.gradient.init.ClientProxy;
import lordmonoxide.gradient.init.IProxy;
import lordmonoxide.gradient.init.ServerProxy;
import lordmonoxide.gradient.progress.CapabilityPlayerProgress;
import lordmonoxide.gradient.progress.SetAgeCommand;
import lordmonoxide.gradient.recipes.GradientRecipeSerializers;
import lordmonoxide.gradient.science.geology.Meltables;
import lordmonoxide.gradient.worldgen.OreGenerator;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Mod(GradientMod.MOD_ID)
public class GradientMod {
  public static final String MOD_ID = "gradient";

  public static final Logger logger = LogManager.getLogger();
  public static final IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

  private static RecipeManager recipeManager;

  public static RecipeManager getRecipeManager() {
    return recipeManager;
  }

  public GradientMod() {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupServer);

    MinecraftForge.EVENT_BUS.addListener(this::serverStarting);

    // Need to do this to make sure the class loads
    logger.info("Drying serializer {}", GradientRecipeSerializers.DRYING);
  }

  private void setup(final FMLCommonSetupEvent event) {
    logger.info("{} is loading!", MOD_ID);

    FluidRegistry.enableUniversalBucket();

    //TODO this.syncTriumphAdvancements(event.getModConfigurationDirectory());
    AdvancementTriggers.register();

    CapabilityPlayerProgress.register();
    FixToolBreakingNotFiringHarvestDropEvents.CapabilityPlayerItem.register();

    //TODO MinecraftForge.ORE_GEN_BUS.register(DisableVanillaOre.class);

    CapabilityEnergy.register(
      IKineticEnergyStorage.class,
      IKineticEnergyTransfer.class,
      () -> new KineticEnergyStorage(10000.0f),
      KineticEnergyTransfer::new
    );

    //TODO NetworkRegistry.INSTANCE.registerGuiHandler(GradientMod.instance, new GradientGuiHandler());

    //TODO GameRegistry.registerWorldGenerator(new GeneratePebbles(), 0);
    OreGenerator.registerGenerators();

    Meltables.registerMeltables();

    GradientNet.register();

    //TODO RecipeRemover.replacePlankRecipes((IForgeRegistryModifiable<IRecipe>)ForgeRegistries.RECIPES);
  }

  private void setupClient(final FMLClientSetupEvent event) {
    logger.info("------------------- SETUP CLIENT -------------------");

    ScreenManager.registerFactory(GradientContainers.CLAY_CAST, ClayCastScreen::new);
    ScreenManager.registerFactory(GradientContainers.CLAY_CRUCIBLE, ClayCrucibleScreen::new);
    ScreenManager.registerFactory(GradientContainers.BRONZE_BOILER, BronzeBoilerScreen::new);
    ScreenManager.registerFactory(GradientContainers.BRONZE_FURNACE, BronzeFurnaceScreen::new);
    ScreenManager.registerFactory(GradientContainers.BRONZE_GRINDER, BronzeGrinderScreen::new);
    ScreenManager.registerFactory(GradientContainers.BRONZE_OVEN, BronzeOvenScreen::new);

    MinecraftForge.EVENT_BUS.addListener(this::onWorldLoad);

    //TODO RenderingRegistry.registerEntityRenderingHandler(EntityPebble.class, manager -> new RenderSnowball<>(manager, ItemBlock.getItemFromBlock(GradientBlocks.PEBBLE), Minecraft.getMinecraft().getRenderItem()));
  }

  private void setupServer(final FMLDedicatedServerSetupEvent event) {
    logger.info("------------------- SETUP SERVER -------------------");

    recipeManager = event.getServerSupplier().get().getRecipeManager();
  }

  private void serverStarting(final FMLServerStartingEvent event) {
    logger.info("------------------- SERVER START -------------------");

    final LiteralArgumentBuilder<CommandSource> root = Commands.literal(MOD_ID)
      .then(SetAgeCommand.register());

    event.getCommandDispatcher().register(root);
  }

  private void onWorldLoad(final WorldEvent.Load event) {
    recipeManager = ((World)event.getWorld()).getRecipeManager();
  }

  public static ResourceLocation resource(final String path) {
    return new ResourceLocation(MOD_ID, path);
  }

  private void syncTriumphAdvancements(final File configDir) throws URISyntaxException, IOException {
    final Path destDir = configDir.toPath().resolve("triumph");

    logger.info("Copying triumphs");

    FileUtils.deleteDirectory(destDir.toFile());

    final URLConnection connection = this.getClass().getResource("").openConnection();

    if(connection instanceof JarURLConnection) {
      final JarFile jar = ((JarURLConnection)connection).getJarFile();

      final String dir = "assets/" + MOD_ID + "/triumph/";

      jar.stream().forEach(entry -> {
        if(!entry.isDirectory()) {
          if(entry.getName().startsWith(dir)) {
            this.copyJarDirectory(jar, entry, dir, destDir);
          }
        }
      });
    } else {
      this.copyDevDirectory("../../assets/" + MOD_ID + "/triumph", destDir);
    }
  }

  private void copyJarDirectory(final JarFile jar, final JarEntry entry, final String sourceDir, final Path destDir) {
    try(final InputStream stream = jar.getInputStream(entry)) {
      final Path path = destDir.resolve(entry.getName().substring(sourceDir.length()));
      Files.createDirectories(path.getParent());
      IOUtils.copy(stream, Files.newOutputStream(path));
    } catch(final IOException e) {
      GradientMod.logger.error("Error copying triumph", e);
    }
  }

  private void copyDevDirectory(final String sourceDir, final Path destDir) throws URISyntaxException, IOException {
    final File dir = Paths.get(this.getClass().getResource(sourceDir).toURI()).toFile();
    FileUtils.copyDirectory(dir, destDir.toFile());
  }
}
