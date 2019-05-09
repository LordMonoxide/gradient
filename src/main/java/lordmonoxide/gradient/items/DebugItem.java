package lordmonoxide.gradient.items;

import lordmonoxide.gradient.blocks.heat.HeatSinker;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IWorld;

public class DebugItem extends Item {
  public DebugItem() {
    super(new Properties().group(ItemGroup.TOOLS));
  }

  @Override
  public EnumActionResult onItemUse(final ItemUseContext context) {
    final IWorld world = context.getWorld();

    if(world.isRemote()) {
      final BlockPos pos = context.getPos();
      final IBlockState state = world.getBlockState(pos);
      final Block block = state.getBlock();

      this.addChat("Registry name: " + block.getRegistryName());

      this.addChat("Harvest tool: " + block.getHarvestTool(state));
      this.addChat("Harvest level: " + block.getHarvestLevel(state));

      state.getProperties().forEach(property -> this.addChat("Property " + property + ": " + state.get(property)));

      final TileEntity te = world.getTileEntity(pos);

      if(te instanceof HeatSinker) {
        this.addChat("Heat sinker heat: " + ((HeatSinker)te).getHeat());
      }
    }

    return EnumActionResult.SUCCESS;
  }

  private void addChat(final String text) {
    Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(text));
  }
}
