package lordmonoxide.gradient.core.tileentities;

import lordmonoxide.gradient.core.geology.ores.Ore;
import lordmonoxide.gradient.core.geology.ores.Ores;
import lordmonoxide.gradient.core.utils.NbtUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileOre extends TileEntity {
  private Ore ore;

  public TileOre() {
    super(CoreTiles.TILE_ORE_TYPE);
  }

  public Ore getOre() {
    return this.ore;
  }

  @Override
  public void read(final NBTTagCompound compound) {
    super.read(compound);
    this.ore = Ores.get(NbtUtil.getResourceLocation(this.getTileData()));
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
