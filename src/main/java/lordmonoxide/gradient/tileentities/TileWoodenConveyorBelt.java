package lordmonoxide.gradient.tileentities;

import lordmonoxide.gradient.blocks.BlockWoodenConveyorBelt;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import java.util.HashSet;
import java.util.Set;

public class TileWoodenConveyorBelt extends TileEntity {
  private final Set<TileWoodenConveyorBeltDriver> drivers = new HashSet<>();
  private EnumFacing facing;

  public EnumFacing getFacing() {
    return this.facing;
  }

  public void addDriver(final TileWoodenConveyorBeltDriver driver) {
    this.drivers.add(driver);
  }

  public void removeDriver(final TileWoodenConveyorBeltDriver driver) {
    this.drivers.remove(driver);
  }

  @Override
  public void onLoad() {
    super.onLoad();

    this.facing = this.world.getBlockState(this.pos).getValue(BlockWoodenConveyorBelt.FACING);
  }
}
