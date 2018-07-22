package lordmonoxide.gradient.blocks.heat;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class HeatSinker extends TileEntity implements ITickable {
  private final Map<BlockPos, HeatSinker> sinks = new HashMap<>();

  private boolean firstTick = true;

  private float heat = 0;

  private IBlockState state;

  protected IBlockState getBlockState() {
    return this.state;
  }

  public boolean hasHeat() {
    return this.heat != 0;
  }

  public float getHeat() {
    return this.heat;
  }

  protected void setHeat(final float heat) {
    this.heat = heat;
  }

  protected void addHeat(final float heat) {
    this.heat += heat;
  }

  protected void removeHeat(final float heat) {
    this.heat = Math.max(0, this.heat - heat);
  }

  public void updateSink(final BlockPos pos) {
    final TileEntity te = this.getWorld().getTileEntity(pos);

    if(te instanceof HeatSinker) {
      this.sinks.put(pos, (HeatSinker)te);
    }
  }

  private void findSurroundingSinks() {
    this.updateSink(this.pos.north());
    this.updateSink(this.pos.south());
    this.updateSink(this.pos.east());
    this.updateSink(this.pos.west());
    this.updateSink(this.pos.up());
    this.updateSink(this.pos.down());
  }

  @Override
  public void update() {
    if(!this.hasHeat()) {
      return;
    }

    this.state = this.getWorld().getBlockState(this.getPos());

    this.tickBeforeCooldown();
    this.coolDown();
    this.tickAfterCooldown();

    if(this.firstTick) {
      this.findSurroundingSinks();
      this.firstTick = false;
    }

    this.transferHeat();
  }

  private void transferHeat() {
    if(this.sinks.isEmpty()) {
      return;
    }

    final Iterator<Map.Entry<BlockPos, HeatSinker>> iterator = this.sinks.entrySet().iterator();

    while(iterator.hasNext()) {
      final Map.Entry<BlockPos, HeatSinker> entry = iterator.next();

      final TileEntity worldEntity = this.getWorld().getTileEntity(entry.getKey());

      if(!(worldEntity instanceof HeatSinker)) {
        iterator.remove();
        return;
      }

      if(worldEntity != entry.getValue()) {
        entry.setValue((HeatSinker)worldEntity);
      }

      final HeatSinker sink = entry.getValue();

      if(sink.getHeat() < this.getHeat()) {
        final float mod = Math.max(1.0f, 5.0f - sink.getHeat() / this.getHeat() * 4);
        final float heat = this.calculateHeatLoss(this.state) * sink.heatTransferEfficiency() * mod / 20.0f;
        this.removeHeat(heat);
        sink.addHeat(heat);
      }
    }
  }

  protected abstract void tickBeforeCooldown();
  protected abstract void tickAfterCooldown();

  private void coolDown() {
    this.removeHeat(this.calculateHeatLoss(this.state) / 20.0f);
  }

  protected abstract float calculateHeatLoss(IBlockState state);

  /**
   * @return  The percentage of heat that is maintained when this sink absorbs heat. 0.0 = 0%, 1.0 = 100%
   */
  protected abstract float heatTransferEfficiency();

  @Override
  public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
    compound.setFloat("heat", this.getHeat());

    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(final NBTTagCompound compound) {
    this.setHeat(compound.getFloat("heat"));

    super.readFromNBT(compound);
  }

  protected void sync() {
    if(!this.getWorld().isRemote) {
      final IBlockState state = this.getWorld().getBlockState(this.getPos());
      this.getWorld().notifyBlockUpdate(this.getPos(), state, state, 3);
      this.markDirty();
    }
  }

  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
  }

  @Override
  public NBTTagCompound getUpdateTag() {
    return this.writeToNBT(new NBTTagCompound());
  }

  @Override
  public void onDataPacket(final NetworkManager net, final SPacketUpdateTileEntity pkt) {
    this.readFromNBT(pkt.getNbtCompound());
  }
}
