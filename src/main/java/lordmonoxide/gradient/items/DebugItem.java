package lordmonoxide.gradient.items;

import lordmonoxide.gradient.blocks.heat.HeatSinker;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class DebugItem extends GradientItem {
  public DebugItem() {
    super("debug", CreativeTabs.TOOLS);
  }

  @Override
  public EnumActionResult onItemUse(final EntityPlayer player, final World world, final BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ) {
    if(world.isRemote) {
      final IBlockState state = world.getBlockState(pos);
      final Block block = state.getBlock();

      this.addChat("Registry name: " + block.getRegistryName());

      this.addChat("Harvest tool: " + block.getHarvestTool(state));
      this.addChat("Harvest level: " + block.getHarvestLevel(state));

      final int meta = block.getMetaFromState(state);
      this.addChat("Meta: " + meta + " (" + Integer.toString(meta, 2) + ')');

      state.getProperties().forEach((property, value) -> this.addChat("Property " + property + ": " + value));

      final TileEntity te = world.getTileEntity(pos);

      if(te instanceof HeatSinker) {
        this.addChat("Heat sinker heat: " + ((HeatSinker)te).getHeat());
      }
    }

    return EnumActionResult.SUCCESS;
  }

  private void addChat(final String text) {
    Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(text));
  }
}
