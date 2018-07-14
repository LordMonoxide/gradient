package lordmonoxide.gradient.blocks.firepit;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.blocks.GradientBlocks;
import lordmonoxide.gradient.blocks.clayfurnace.BlockClayFurnace;
import lordmonoxide.gradient.containers.GradientContainer;
import lordmonoxide.gradient.containers.GradientGuiContainer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiFirePit extends GradientGuiContainer {
  private static final ResourceLocation BG_TEXTURE = new ResourceLocation(GradientMod.MODID, "textures/gui/fire_pit.png");

  private final TileFirePit firePit;
  private final IBlockState state;
  private final InventoryPlayer playerInv;

  public GuiFirePit(final ContainerFirePit container, final TileFirePit firePit, final IBlockState state, final InventoryPlayer playerInv) {
    super(container);
    this.firePit = firePit;
    this.state = state;
    this.playerInv = playerInv;
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
    GlStateManager.color(1, 1, 1, 1);
    this.mc.getTextureManager().bindTexture(BG_TEXTURE);
    final int x = (this.width  - this.xSize) / 2;
    final int y = (this.height - this.ySize) / 2;
    this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

    if(this.firePit.isCooking()) {
      final float percent = this.firePit.getCookingFood().cookPercent();

      this.drawTexturedModalRect(x + 122, y + 35, 176, 0, (int)(16 * percent), 14);
    }
  }

  @Override
  protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
    for(int slot = 0; slot < TileFirePit.FUEL_SLOTS_COUNT; slot++) {
      if(this.firePit.isBurning(slot)) {
        final int x = ContainerFirePit.FUEL_SLOTS_X + (slot % 3) * (GradientContainer.SLOT_X_SPACING + 8) + 20;
        final int y = ContainerFirePit.FUEL_SLOTS_Y + (slot / 3) * (GradientContainer.SLOT_Y_SPACING + 8);
        final float percent = this.firePit.getBurningFuel(slot).burnPercent();

        drawRect(x, (int)(y + percent * 16), x + 2, y + 16, 0xFF01FE00);
      }
    }

    final String name;

    if(this.firePit.hasFurnace(this.state)) {
      final Block furnace = GradientBlocks.CLAY_FURNACE;
      name = I18n.format(new ItemStack(furnace, 1, furnace.getMetaFromState(furnace.getDefaultState().withProperty(BlockClayFurnace.HARDENED, true))).getTranslationKey() + ".name");
    } else {
      name = I18n.format(GradientBlocks.FIRE_PIT.getTranslationKey() + ".name");
    }

    final String fuel = I18n.format(GradientBlocks.FIRE_PIT.getTranslationKey() + ".fuel");
    final String food = I18n.format(GradientBlocks.FIRE_PIT.getTranslationKey() + ".input");

    final String heat = I18n.format(GradientBlocks.FIRE_PIT.getTranslationKey() + ".heat", (int)this.firePit.getHeat());

    this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
    this.fontRenderer.drawString(fuel, ContainerFirePit.FUEL_SLOTS_X, ContainerFirePit.FUEL_SLOTS_Y - this.fontRenderer.FONT_HEIGHT - 2, 0x404040);
    this.fontRenderer.drawString(food, ContainerFirePit.INPUT_SLOTS_X, ContainerFirePit.INPUT_SLOTS_Y - this.fontRenderer.FONT_HEIGHT - 2, 0x404040);
    this.fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, this.ySize - 94, 0x404040);

    this.fontRenderer.drawString(heat, ContainerFirePit.FUEL_SLOTS_X, 55, 0x404040);
  }
}
