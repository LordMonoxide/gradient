package lordmonoxide.gradient.items;

import lordmonoxide.gradient.blocks.CastBlock;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

public class ItemCastBlock extends ItemBlock {
  private final CastBlock block;

  public ItemCastBlock(final CastBlock block) {
    super(block);
    this.block = block;
  }

  @Override
  public String getItemStackDisplayName(final ItemStack stack) {
    return I18n.translateToLocalFormatted("tile.cast_block.name", I18n.translateToLocal("metal." + this.block.metal.name));
  }
}
