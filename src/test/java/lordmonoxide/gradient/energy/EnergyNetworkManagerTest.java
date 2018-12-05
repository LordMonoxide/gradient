package lordmonoxide.gradient.energy;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class EnergyNetworkManagerTest {
  public static final EnergyNetwork[] EMPTY_ENERGY_NETWORKS = new EnergyNetwork[0];
  private World world;
  private EnergyNetworkManager manager;

  @BeforeAll
  static void setUpFirst() {
    EnergyNetworkTest.setUpFirst();
  }

  @BeforeEach
  void setUp() {
    this.world = new World();
    this.manager = new EnergyNetworkManager(this.world);
  }

  @Test
  void testAddingOneTransferNode() {
    final Set<EnergyNetwork> added = this.manager.connect(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer());

    Assertions.assertEquals(1, added.size(), "There should only be one network");

    for(final EnergyNetwork network : added) {
      Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN), BlockPos.ORIGIN, null, null, null, null, null, null), "Node did not match");
    }
  }

  @Test
  void testAddingOneStorageNode() {
    final Set<EnergyNetwork> added = this.manager.connect(BlockPos.ORIGIN, TileEntityWithCapabilities.storage());

    Assertions.assertEquals(1, added.size(), "There should only be one network");

    for(final EnergyNetwork network : added) {
      Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN), BlockPos.ORIGIN, null, null, null, null, null, null), "Node did not match");
    }
  }

  @Test
  void testMergingStorageNetworks() {
    final TileEntity teEast = TileEntityWithCapabilities.storage();
    final TileEntity teWest = TileEntityWithCapabilities.storage();

    this.world.addTileEntity(BlockPos.ORIGIN.east(), teEast);
    this.world.addTileEntity(BlockPos.ORIGIN.west(), teWest);

    final Set<EnergyNetwork> east = this.manager.connect(BlockPos.ORIGIN.east(), teEast);
    final Set<EnergyNetwork> west = this.manager.connect(BlockPos.ORIGIN.west(), teWest);

    Assertions.assertEquals(1, east.size(), "There should only be one network");
    Assertions.assertEquals(1, west.size(), "There should only be one network");
    Assertions.assertEquals(2, this.manager.size(), "There should be two networks total");

    for(final EnergyNetwork network : east) {
      Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.east()), BlockPos.ORIGIN.east(), null, null, null, null, null, null), "Node did not match");
    }

    for(final EnergyNetwork network : west) {
      Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.west()), BlockPos.ORIGIN.west(), null, null, null, null, null, null), "Node did not match");
    }

    final TileEntity teOrigin = TileEntityWithCapabilities.storage();

    this.world.addTileEntity(BlockPos.ORIGIN, teOrigin);

    final Set<EnergyNetwork> origin = this.manager.connect(BlockPos.ORIGIN, teOrigin);

    Assertions.assertEquals(2, origin.size(), "There should be two networks");
    Assertions.assertEquals(2, this.manager.size(), "There should be two networks total");

    final EnergyNetwork[] originNetworks = origin.toArray(EMPTY_ENERGY_NETWORKS);

    if(originNetworks[0].contains(BlockPos.ORIGIN.east())) {
      Assertions.assertTrue(EnergyNetworkTest.checkNode(originNetworks[0].getNode(BlockPos.ORIGIN), BlockPos.ORIGIN, null, null, originNetworks[0].getNode(BlockPos.ORIGIN.east()), null, null, null), () -> "Node did not match: " + originNetworks[0].getNode(BlockPos.ORIGIN));
      Assertions.assertTrue(EnergyNetworkTest.checkNode(originNetworks[0].getNode(BlockPos.ORIGIN.east()), BlockPos.ORIGIN.east(), null, null, null, originNetworks[0].getNode(BlockPos.ORIGIN), null, null), () -> "Node did not match: " + originNetworks[0].getNode(BlockPos.ORIGIN.east()));
      Assertions.assertTrue(EnergyNetworkTest.checkNode(originNetworks[1].getNode(BlockPos.ORIGIN), BlockPos.ORIGIN, null, null, null, originNetworks[1].getNode(BlockPos.ORIGIN.west()), null, null), () -> "Node did not match: " + originNetworks[1].getNode(BlockPos.ORIGIN));
      Assertions.assertTrue(EnergyNetworkTest.checkNode(originNetworks[1].getNode(BlockPos.ORIGIN.west()), BlockPos.ORIGIN.west(), null, null, originNetworks[1].getNode(BlockPos.ORIGIN), null, null, null), () -> "Node did not match: " + originNetworks[1].getNode(BlockPos.ORIGIN.west()));
    } else {
      Assertions.assertTrue(EnergyNetworkTest.checkNode(originNetworks[0].getNode(BlockPos.ORIGIN), BlockPos.ORIGIN, null, null, null, originNetworks[0].getNode(BlockPos.ORIGIN.west()), null, null), () -> "Node did not match: " + originNetworks[0].getNode(BlockPos.ORIGIN));
      Assertions.assertTrue(EnergyNetworkTest.checkNode(originNetworks[0].getNode(BlockPos.ORIGIN.west()), BlockPos.ORIGIN.west(), null, null, originNetworks[0].getNode(BlockPos.ORIGIN), null, null, null), () -> "Node did not match: " + originNetworks[0].getNode(BlockPos.ORIGIN.west()));
      Assertions.assertTrue(EnergyNetworkTest.checkNode(originNetworks[1].getNode(BlockPos.ORIGIN), BlockPos.ORIGIN, null, null, originNetworks[1].getNode(BlockPos.ORIGIN.east()), null, null, null), () -> "Node did not match: " + originNetworks[1].getNode(BlockPos.ORIGIN));
      Assertions.assertTrue(EnergyNetworkTest.checkNode(originNetworks[1].getNode(BlockPos.ORIGIN.east()), BlockPos.ORIGIN.east(), null, null, null, originNetworks[1].getNode(BlockPos.ORIGIN), null, null), () -> "Node did not match: " + originNetworks[1].getNode(BlockPos.ORIGIN.east()));
    }
  }
}
