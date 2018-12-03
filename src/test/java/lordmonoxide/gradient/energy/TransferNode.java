package lordmonoxide.gradient.energy;

import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.ArrayUtils;

import java.util.function.Predicate;

public class TransferNode implements IEnergyTransfer {
  private final Predicate<EnumFacing> canConnect;

  public TransferNode() {
    this.canConnect = facing -> true;
  }

  public TransferNode(final EnumFacing... validSides) {
    this.canConnect = facing -> ArrayUtils.contains(validSides, facing);
  }

  @Override
  public boolean canSink(final EnumFacing side) {
    return this.canConnect.test(side);
  }

  @Override
  public boolean canSource(final EnumFacing side) {
    return this.canConnect.test(side);
  }

  @Override
  public boolean canTransfer(final EnumFacing from, final EnumFacing to) {
    return true;
  }

  @Override
  public void transfer(final float amount, final EnumFacing from, final EnumFacing to) {

  }
}
