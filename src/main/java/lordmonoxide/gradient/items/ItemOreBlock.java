package lordmonoxide.gradient.items;

import lordmonoxide.gradient.blocks.BlockOre;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class ItemOreBlock extends ItemBlock {
  private final BlockOre block;

  public ItemOreBlock(final BlockOre block) {
    super(block);
    this.block = block;
  }

  @Override
  public String getItemStackDisplayName(final ItemStack stack) {
    return I18n.translateToLocalFormatted("tile.ore.name", I18n.translateToLocal("metal." + this.block.ore.metal.name));
  }
}
