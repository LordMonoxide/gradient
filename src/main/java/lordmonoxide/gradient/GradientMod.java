package lordmonoxide.gradient;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import lordmonoxide.gradient.advancements.AdvancementTriggers;
import lordmonoxide.gradient.energy.CapabilityEnergy;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyStorage;
import lordmonoxide.gradient.energy.kinetic.IKineticEnergyTransfer;
import lordmonoxide.gradient.energy.kinetic.KineticEnergyStorage;
import lordmonoxide.gradient.energy.kinetic.KineticEnergyTransfer;
import lordmonoxide.gradient.progress.CapabilityPlayerProgress;
import lordmonoxide.gradient.progress.SetAgeCommand;
import lordmonoxide.gradient.recipes.GradientRecipeSerializers;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
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
import java.util.jar.JarFile;

@Mod(GradientMod.MODID)
public class GradientMod {
  public static final String MODID = "gradient";

  public static final Logger logger = LogManager.getLogger();

  private static RecipeManager recipeManager;

  public static RecipeManager getRecipeManager() {
    return recipeManager;
  }

  public GradientMod() {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupServer);

    ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> GradientGuiHandler::openGui);

    MinecraftForge.EVENT_BUS.addListener(this::serverStarting);

    logger.info("Drying serializer {}", GradientRecipeSerializers.DRYING);
  }

  private void init(final FMLCommonSetupEvent event) {
    logger.info("{} is loading!", MODID);

    FluidRegistry.enableUniversalBucket();

    //TODO this.syncTriumphAdvancements(event.getModConfigurationDirectory());
    AdvancementTriggers.register();

    CapabilityPlayerProgress.register();

    //TODO MinecraftForge.ORE_GEN_BUS.register(DisableVanillaOre.class);

    CapabilityEnergy.register(
      IKineticEnergyStorage.class,
      IKineticEnergyTransfer.class,
      () -> new KineticEnergyStorage(10000.0f),
      KineticEnergyTransfer::new
    );

    //TODO NetworkRegistry.INSTANCE.registerGuiHandler(GradientMod.instance, new GradientGuiHandler());

    //TODO GameRegistry.registerWorldGenerator(new GeneratePebbles(), 0);
    //TODO GameRegistry.registerWorldGenerator(new OreGenerator(), 0);

    GradientMetals.registerMeltables();

    GradientNet.register();

    //TODO RecipeRemover.replacePlankRecipes((IForgeRegistryModifiable<IRecipe>)ForgeRegistries.RECIPES);
  }

  private void setupClient(final FMLClientSetupEvent event) {
    logger.info("------------------- SETUP CLIENT -------------------");

    MinecraftForge.EVENT_BUS.addListener(this::onWorldLoad);

    //TODO RenderingRegistry.registerEntityRenderingHandler(EntityPebble.class, manager -> new RenderSnowball<>(manager, ItemBlock.getItemFromBlock(GradientBlocks.PEBBLE), Minecraft.getMinecraft().getRenderItem()));
  }

  private void setupServer(final FMLDedicatedServerSetupEvent event) {
    logger.info("------------------- SETUP SERVER -------------------");

    recipeManager = event.getServerSupplier().get().getRecipeManager();
  }

  private void serverStarting(final FMLServerStartingEvent event) {
    logger.info("------------------- SERVER START -------------------");

    final LiteralArgumentBuilder<CommandSource> root = Commands.literal(MODID)
      .then(SetAgeCommand.register());

    event.getCommandDispatcher().register(root);
  }

  private void onWorldLoad(final WorldEvent.Load event) {
    recipeManager = ((World)event.getWorld()).getRecipeManager();
  }

  public static ResourceLocation resource(final String path) {
    return new ResourceLocation(MODID, path);
  }

  private void syncTriumphAdvancements(final File configDir) throws URISyntaxException, IOException {
    final Path destScriptDir = configDir.toPath().resolve("triumph/script/" + MODID);
    final Path destJsonDir = configDir.toPath().resolve("triumph/json/" + MODID);

    logger.info("Copying triumphs");

    FileUtils.deleteDirectory(destScriptDir.toFile());
    FileUtils.deleteDirectory(destJsonDir.toFile());

    final URLConnection connection = this.getClass().getResource("").openConnection();

    if(connection instanceof JarURLConnection) {
      final JarFile jar = ((JarURLConnection)connection).getJarFile();

      final String scriptsDir = "assets/" + MODID + "/triumph/script/";
      final String jsonDir = "assets/" + MODID + "/triumph/json/";

      jar.stream().forEach(entry -> {
        if(!entry.isDirectory() && entry.getName().startsWith(scriptsDir)) {
          this.copyJarDirectory(jar, entry.getName(), scriptsDir, destScriptDir);
          this.copyJarDirectory(jar, entry.getName(), jsonDir, destJsonDir);
        }
      });
    } else {
      this.copyDevDirectory("../../assets/" + MODID + "/triumph/script", destScriptDir);
      this.copyDevDirectory("../../assets/" + MODID + "/triumph/json", destJsonDir);
    }
  }

  private void copyJarDirectory(final JarFile jar, final String fileName, final String sourceDir, final Path destDir) {
    try(final InputStream stream = jar.getInputStream(jar.getEntry(fileName))) {
      final Path path = destDir.resolve(fileName.substring(sourceDir.length()));
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
