package lordmonoxide.gradient.blocks.heat;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public abstract class HeatSinker extends TileEntity implements ITickable {
  private final Map<BlockPos, HeatSinker> sinks = new HashMap<>();
  
  private boolean firstTick = true;
  private long nextSync;
  
  private float heat;
  
  public boolean hasHeat() {
    return this.heat != 0;
  }
  
  public float getHeat() {
    return this.heat;
  }
  
  protected void setHeat(float heat) {
    this.heat = heat;
  }
  
  protected void addHeat(float heat) {
    this.heat += heat;
  }
  
  protected void removeHeat(float heat) {
    this.heat = Math.max(0, this.heat - heat);
  }
  
  public void updateSink(BlockPos pos) {
    TileEntity te = this.getWorld().getTileEntity(pos);
    
    if(!(te instanceof HeatSinker)) {
      this.sinks.remove(pos);
      return;
    }
    
    this.sinks.put(pos, (HeatSinker)te);
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
    
    this.tickBeforeCooldown();
    this.coolDown();
    this.tickAfterCooldown();
    
    if(this.firstTick) {
      this.findSurroundingSinks();
      this.firstTick = false;
    }
    
    this.transferHeat();
    
    if(!this.getWorld().isRemote) {
      if(Minecraft.getSystemTime() >= this.nextSync) {
        this.nextSync = Minecraft.getSystemTime() + 10000;
        this.sync();
      }
    }
  }
  
  private void transferHeat() {
    if(this.sinks.isEmpty()) {
      return;
    }
    
    this.sinks.keySet().removeIf(pos -> !(this.getWorld().getTileEntity(pos) instanceof HeatSinker));
    this.sinks.forEach((pos, sink) -> {
      if(sink.getHeat() < this.getHeat()) {
        float heat = this.calculateHeatLoss() * sink.heatTransferEfficiency() / 20.0f;
        this.removeHeat(heat);
        sink.addHeat(heat);
      }
    });
  }
  
  protected abstract void tickBeforeCooldown();
  protected abstract void tickAfterCooldown();
  
  private void coolDown() {
    this.removeHeat(this.calculateHeatLoss() / 20.0f);
  }
  
  protected abstract float calculateHeatLoss();
  
  /**
   * @return  The percentage of heat that is maintained when this sink absorbs heat. 0.0 = 0%, 1.0 = 100%
   */
  protected abstract float heatTransferEfficiency();
  
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setFloat("heat", this.getHeat());
    
    return super.writeToNBT(compound);
  }
  
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    this.setHeat(compound.getFloat("heat"));
    
    super.readFromNBT(compound);
  }
  
  protected void sync() {
    if(!this.getWorld().isRemote) {
      IBlockState state = this.getWorld().getBlockState(this.getPos());
      this.getWorld().notifyBlockUpdate(this.getPos(), state, state, 3);
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
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    this.readFromNBT(pkt.getNbtCompound());
  }
}
