package lordmonoxide.gradient.core.tileentities;

import lordmonoxide.gradient.core.geology.ores.Ore;
import lordmonoxide.gradient.core.geology.ores.Ores;
import lordmonoxide.gradient.core.utils.NbtUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TileOre extends TileEntity {
  private ResourceLocation oreTexture;
  private Ore ore;

  public TileOre() {
    super(CoreTiles.TILE_ORE_TYPE);
  }

  public Ore getOre() {
    return this.ore;
  }

  public ResourceLocation getOreTexture() {
    return this.oreTexture;
  }

  private void setOre(final Ore ore) {
    this.ore = ore;
    this.oreTexture = new ResourceLocation(ore.name.getNamespace(), "textures/blocks/ore." + ore.name.getPath() + ".png");
  }

  @Override
  public void read(final NBTTagCompound compound) {
    super.read(compound);
    this.setOre(Ores.get(NbtUtil.getResourceLocation(this.getTileData())));
  }

  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
  }

  @Override
  public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity pkt) {
    this.handleUpdateTag(pkt.getNbtCompound());
  }
}
