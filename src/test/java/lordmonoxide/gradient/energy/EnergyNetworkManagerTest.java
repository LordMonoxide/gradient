package lordmonoxide.gradient.energy;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
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

  //TODO: run tests with sided blocks

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
    final TileEntity teEast = this.world.addTileEntity(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.storage());
    final Set<EnergyNetwork> east = this.manager.connect(BlockPos.ORIGIN.east(), teEast);

    final TileEntity teWest = this.world.addTileEntity(BlockPos.ORIGIN.west(), TileEntityWithCapabilities.storage());
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

  @Test
  void testAddingStorageToTransfer() {
    final TileEntity transfer = this.world.addTileEntity(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN, transfer).size());

    final TileEntity storage1 = this.world.addTileEntity(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.east(), storage1).size());

    final TileEntity storage2 = this.world.addTileEntity(BlockPos.ORIGIN.west(), TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.west(), storage2).size());

    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");
    Assertions.assertEquals(3, this.manager.getNetworksForBlock(BlockPos.ORIGIN).get(0).size(), "Network should have 3 nodes");
  }

  @Test
  void testAddingTransferBetweenStoragesMerges() {
    final TileEntity storage1 = this.world.addTileEntity(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.east(), storage1).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final TileEntity storage2 = this.world.addTileEntity(BlockPos.ORIGIN.west(), TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.west(), storage2).size());
    Assertions.assertEquals(2, this.manager.size(), "Manager should have two networks");

    final TileEntity transfer = this.world.addTileEntity(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN, transfer).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final EnergyNetwork network = this.manager.getNetworksForBlock(BlockPos.ORIGIN).get(0);
    Assertions.assertEquals(3, network.size(), "Network should have 3 nodes");

    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN), BlockPos.ORIGIN, null, null, network.getNode(BlockPos.ORIGIN.east()), network.getNode(BlockPos.ORIGIN.west()), null, null), () -> "Node did not match: " + network.getNode(BlockPos.ORIGIN));
  }

  @Test
  void testAddingTransferToStorage() {
    final TileEntity storage = this.world.addTileEntity(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.east(), storage).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final TileEntity transfer = this.world.addTileEntity(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN, transfer).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final EnergyNetwork network = this.manager.getNetworksForBlock(BlockPos.ORIGIN).get(0);
    Assertions.assertEquals(2, network.size(), "Network should have 3 nodes");

    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN), BlockPos.ORIGIN, null, null, network.getNode(BlockPos.ORIGIN.east()), null, null, null), () -> "Node did not match: " + network.getNode(BlockPos.ORIGIN));
  }

  @Test
  void testTransferStarWithStorageTipsIsMergedByCentralTransfer() {
    final TileEntity transferNorth = this.world.addTileEntity(BlockPos.ORIGIN.north(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.north(), transferNorth).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final TileEntity transferSouth = this.world.addTileEntity(BlockPos.ORIGIN.south(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.south(), transferSouth).size());
    Assertions.assertEquals(2, this.manager.size(), "Manager should have two networks");

    final TileEntity transferEast = this.world.addTileEntity(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.east(), transferEast).size());
    Assertions.assertEquals(3, this.manager.size(), "Manager should have three networks");

    final TileEntity transferWest = this.world.addTileEntity(BlockPos.ORIGIN.west(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.west(), transferWest).size());
    Assertions.assertEquals(4, this.manager.size(), "Manager should have four networks");

    final TileEntity storageNorth = this.world.addTileEntity(BlockPos.ORIGIN.north().north(), TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.north().north(), storageNorth).size());
    Assertions.assertEquals(4, this.manager.size(), "Manager should have four networks");

    final TileEntity storageSouth = this.world.addTileEntity(BlockPos.ORIGIN.south().south(), TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.south().south(), storageSouth).size());
    Assertions.assertEquals(4, this.manager.size(), "Manager should have four networks");

    final TileEntity storageEast = this.world.addTileEntity(BlockPos.ORIGIN.east().east(), TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.east().east(), storageEast).size());
    Assertions.assertEquals(4, this.manager.size(), "Manager should have four networks");

    final TileEntity storageWest = this.world.addTileEntity(BlockPos.ORIGIN.west().west(), TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.west().west(), storageWest).size());
    Assertions.assertEquals(4, this.manager.size(), "Manager should have four networks");

    final TileEntity origin = this.world.addTileEntity(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN, origin).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final EnergyNetwork network = this.manager.getNetworksForBlock(BlockPos.ORIGIN).get(0);

    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN), BlockPos.ORIGIN, network.getNode(BlockPos.ORIGIN.north()), network.getNode(BlockPos.ORIGIN.south()), network.getNode(BlockPos.ORIGIN.east()), network.getNode(BlockPos.ORIGIN.west()), null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.north()), BlockPos.ORIGIN.north(), network.getNode(BlockPos.ORIGIN.north().north()), network.getNode(BlockPos.ORIGIN), null, null, null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.south()), BlockPos.ORIGIN.south(), network.getNode(BlockPos.ORIGIN), network.getNode(BlockPos.ORIGIN.south().south()), null, null, null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.east()), BlockPos.ORIGIN.east(), null, null, network.getNode(BlockPos.ORIGIN.east().east()), network.getNode(BlockPos.ORIGIN), null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.west()), BlockPos.ORIGIN.west(), null, null, network.getNode(BlockPos.ORIGIN), network.getNode(BlockPos.ORIGIN.west().west()), null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.north().north()), BlockPos.ORIGIN.north().north(), null, network.getNode(BlockPos.ORIGIN.north()), null, null, null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.south().south()), BlockPos.ORIGIN.south().south(), network.getNode(BlockPos.ORIGIN.south()), null, null, null, null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.east().east()), BlockPos.ORIGIN.east().east(), null, null, null, network.getNode(BlockPos.ORIGIN.east()), null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.west().west()), BlockPos.ORIGIN.west().west(), null, null, network.getNode(BlockPos.ORIGIN.west()), null, null, null));
  }

  @Test
  void testTransferStarWithStorageTipsIsSplitByCentralStorage() {
    final TileEntity transferNorth = this.world.addTileEntity(BlockPos.ORIGIN.north(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.north(), transferNorth).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final TileEntity transferSouth = this.world.addTileEntity(BlockPos.ORIGIN.south(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.south(), transferSouth).size());
    Assertions.assertEquals(2, this.manager.size(), "Manager should have two networks");

    final TileEntity transferEast = this.world.addTileEntity(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.east(), transferEast).size());
    Assertions.assertEquals(3, this.manager.size(), "Manager should have three networks");

    final TileEntity transferWest = this.world.addTileEntity(BlockPos.ORIGIN.west(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.west(), transferWest).size());
    Assertions.assertEquals(4, this.manager.size(), "Manager should have four networks");

    final TileEntity storageNorth = this.world.addTileEntity(BlockPos.ORIGIN.north().north(), TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.north().north(), storageNorth).size());
    Assertions.assertEquals(4, this.manager.size(), "Manager should have four networks");

    final TileEntity storageSouth = this.world.addTileEntity(BlockPos.ORIGIN.south().south(), TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.south().south(), storageSouth).size());
    Assertions.assertEquals(4, this.manager.size(), "Manager should have four networks");

    final TileEntity storageEast = this.world.addTileEntity(BlockPos.ORIGIN.east().east(), TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.east().east(), storageEast).size());
    Assertions.assertEquals(4, this.manager.size(), "Manager should have four networks");

    final TileEntity storageWest = this.world.addTileEntity(BlockPos.ORIGIN.west().west(), TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.west().west(), storageWest).size());
    Assertions.assertEquals(4, this.manager.size(), "Manager should have four networks");

    final TileEntity origin = this.world.addTileEntity(BlockPos.ORIGIN, TileEntityWithCapabilities.storage());
    Assertions.assertEquals(4, this.manager.connect(BlockPos.ORIGIN, origin).size());
    Assertions.assertEquals(4, this.manager.size(), "Manager should have one network");

    final EnergyNetwork networkNorth = this.manager.getNetworksForBlock(BlockPos.ORIGIN.north()).get(0);
    final EnergyNetwork networkSouth = this.manager.getNetworksForBlock(BlockPos.ORIGIN.south()).get(0);
    final EnergyNetwork networkEast = this.manager.getNetworksForBlock(BlockPos.ORIGIN.east()).get(0);
    final EnergyNetwork networkWest = this.manager.getNetworksForBlock(BlockPos.ORIGIN.west()).get(0);

    Assertions.assertTrue(EnergyNetworkTest.checkNode(networkNorth.getNode(BlockPos.ORIGIN), BlockPos.ORIGIN, networkNorth.getNode(BlockPos.ORIGIN.north()), networkNorth.getNode(BlockPos.ORIGIN.south()), networkNorth.getNode(BlockPos.ORIGIN.east()), networkNorth.getNode(BlockPos.ORIGIN.west()), null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(networkNorth.getNode(BlockPos.ORIGIN.north()), BlockPos.ORIGIN.north(), networkNorth.getNode(BlockPos.ORIGIN.north().north()), networkNorth.getNode(BlockPos.ORIGIN), null, null, null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(networkNorth.getNode(BlockPos.ORIGIN.north().north()), BlockPos.ORIGIN.north().north(), null, networkNorth.getNode(BlockPos.ORIGIN.north()), null, null, null, null));

    Assertions.assertTrue(EnergyNetworkTest.checkNode(networkSouth.getNode(BlockPos.ORIGIN), BlockPos.ORIGIN, networkSouth.getNode(BlockPos.ORIGIN.north()), networkSouth.getNode(BlockPos.ORIGIN.south()), networkSouth.getNode(BlockPos.ORIGIN.east()), networkSouth.getNode(BlockPos.ORIGIN.west()), null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(networkSouth.getNode(BlockPos.ORIGIN.south()), BlockPos.ORIGIN.south(), networkSouth.getNode(BlockPos.ORIGIN), networkSouth.getNode(BlockPos.ORIGIN.south().south()), null, null, null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(networkSouth.getNode(BlockPos.ORIGIN.south().south()), BlockPos.ORIGIN.south().south(), networkSouth.getNode(BlockPos.ORIGIN.south()), null, null, null, null, null));

    Assertions.assertTrue(EnergyNetworkTest.checkNode(networkEast.getNode(BlockPos.ORIGIN), BlockPos.ORIGIN, networkEast.getNode(BlockPos.ORIGIN.north()), networkEast.getNode(BlockPos.ORIGIN.south()), networkEast.getNode(BlockPos.ORIGIN.east()), networkEast.getNode(BlockPos.ORIGIN.west()), null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(networkEast.getNode(BlockPos.ORIGIN.east()), BlockPos.ORIGIN.east(), null, null, networkEast.getNode(BlockPos.ORIGIN.east().east()), networkEast.getNode(BlockPos.ORIGIN), null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(networkEast.getNode(BlockPos.ORIGIN.east().east()), BlockPos.ORIGIN.east().east(), null, null, null, networkEast.getNode(BlockPos.ORIGIN.east()), null, null));

    Assertions.assertTrue(EnergyNetworkTest.checkNode(networkWest.getNode(BlockPos.ORIGIN), BlockPos.ORIGIN, networkWest.getNode(BlockPos.ORIGIN.north()), networkWest.getNode(BlockPos.ORIGIN.south()), networkWest.getNode(BlockPos.ORIGIN.east()), networkWest.getNode(BlockPos.ORIGIN.west()), null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(networkWest.getNode(BlockPos.ORIGIN.west()), BlockPos.ORIGIN.west(), null, null, networkWest.getNode(BlockPos.ORIGIN), networkWest.getNode(BlockPos.ORIGIN.west().west()), null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(networkWest.getNode(BlockPos.ORIGIN.west().west()), BlockPos.ORIGIN.west().west(), null, null, networkWest.getNode(BlockPos.ORIGIN.west()), null, null, null));
  }

  @Test
  void testStorageAtCornerOfTransferSquareDoesNotCreateNewNetwork() {
    final TileEntity transfer1 = this.world.addTileEntity(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN, transfer1).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final TileEntity transfer2 = this.world.addTileEntity(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.east(), transfer2).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final TileEntity transfer3 = this.world.addTileEntity(BlockPos.ORIGIN.south(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.south(), transfer3).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final TileEntity storage = this.world.addTileEntity(BlockPos.ORIGIN.south().east(), TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.south().east(), storage).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final EnergyNetwork network = this.manager.getNetworksForBlock(BlockPos.ORIGIN).get(0);

    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN), BlockPos.ORIGIN, null, network.getNode(BlockPos.ORIGIN.south()), network.getNode(BlockPos.ORIGIN.east()), null, null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.south()), BlockPos.ORIGIN.south(), network.getNode(BlockPos.ORIGIN), null, network.getNode(BlockPos.ORIGIN.south().east()), null, null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.east()), BlockPos.ORIGIN.east(), null, network.getNode(BlockPos.ORIGIN.south().east()), null, network.getNode(BlockPos.ORIGIN), null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.south().east()), BlockPos.ORIGIN.south().east(), network.getNode(BlockPos.ORIGIN.east()), null, null, network.getNode(BlockPos.ORIGIN.south()), null, null));
  }

  @Test
  void testStorageAtCornerOfTransferSquareWithAdjacentTransferNetworkDoesNotCreateNewNetworkOrMergeNetworks() {
    final TileEntity transfer1 = this.world.addTileEntity(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN, transfer1).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final TileEntity transfer2 = this.world.addTileEntity(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.east(), transfer2).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final TileEntity transfer3 = this.world.addTileEntity(BlockPos.ORIGIN.south(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.south(), transfer3).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final TileEntity transfer4 = this.world.addTileEntity(BlockPos.ORIGIN.south().east().east(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.south().east().east(), transfer4).size());
    Assertions.assertEquals(2, this.manager.size(), "Manager should have two networks");

    final TileEntity storage = this.world.addTileEntity(BlockPos.ORIGIN.south().east(), TileEntityWithCapabilities.storage());
    Assertions.assertEquals(2, this.manager.connect(BlockPos.ORIGIN.south().east(), storage).size(), "Storage should have been added to two networks");
    Assertions.assertEquals(2, this.manager.size(), "Manager should have two networks");

    final EnergyNetwork network1 = this.manager.getNetworksForBlock(BlockPos.ORIGIN).get(0);
    final EnergyNetwork network2 = this.manager.getNetworksForBlock(BlockPos.ORIGIN.south().east().east()).get(0);

    Assertions.assertTrue(EnergyNetworkTest.checkNode(network1.getNode(BlockPos.ORIGIN), BlockPos.ORIGIN, null, network1.getNode(BlockPos.ORIGIN.south()), network1.getNode(BlockPos.ORIGIN.east()), null, null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network1.getNode(BlockPos.ORIGIN.south()), BlockPos.ORIGIN.south(), network1.getNode(BlockPos.ORIGIN), null, network1.getNode(BlockPos.ORIGIN.south().east()), null, null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network1.getNode(BlockPos.ORIGIN.east()), BlockPos.ORIGIN.east(), null, network1.getNode(BlockPos.ORIGIN.south().east()), null, network1.getNode(BlockPos.ORIGIN), null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network1.getNode(BlockPos.ORIGIN.south().east()), BlockPos.ORIGIN.south().east(), network1.getNode(BlockPos.ORIGIN.east()), null, null, network1.getNode(BlockPos.ORIGIN.south()), null, null));

    Assertions.assertTrue(EnergyNetworkTest.checkNode(network2.getNode(BlockPos.ORIGIN.south().east()), BlockPos.ORIGIN.south().east(), null, null, network2.getNode(BlockPos.ORIGIN.south().east().east()), null, null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network2.getNode(BlockPos.ORIGIN.south().east().east()), BlockPos.ORIGIN.south().east().east(), null, null, null, network2.getNode(BlockPos.ORIGIN.south().east()), null, null));
  }

  @Test
  void testTransferAtCornerOfTransferSquareDoesNotCreateNewNetwork() {
    final TileEntity transfer1 = this.world.addTileEntity(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN, transfer1).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final TileEntity transfer2 = this.world.addTileEntity(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.east(), transfer2).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final TileEntity transfer3 = this.world.addTileEntity(BlockPos.ORIGIN.south(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.south(), transfer3).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final TileEntity transfer4 = this.world.addTileEntity(BlockPos.ORIGIN.south().east(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.south().east(), transfer4).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final EnergyNetwork network = this.manager.getNetworksForBlock(BlockPos.ORIGIN).get(0);

    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN), BlockPos.ORIGIN, null, network.getNode(BlockPos.ORIGIN.south()), network.getNode(BlockPos.ORIGIN.east()), null, null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.south()), BlockPos.ORIGIN.south(), network.getNode(BlockPos.ORIGIN), null, network.getNode(BlockPos.ORIGIN.south().east()), null, null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.east()), BlockPos.ORIGIN.east(), null, network.getNode(BlockPos.ORIGIN.south().east()), null, network.getNode(BlockPos.ORIGIN), null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.south().east()), BlockPos.ORIGIN.south().east(), network.getNode(BlockPos.ORIGIN.east()), null, null, network.getNode(BlockPos.ORIGIN.south()), null, null));
  }

  @Test
  void testTransferAtCornerOfTransferSquareWithAdjacentTransferNetworkMergesNetworks() {
    final TileEntity transfer1 = this.world.addTileEntity(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN, transfer1).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final TileEntity transfer2 = this.world.addTileEntity(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.east(), transfer2).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final TileEntity transfer3 = this.world.addTileEntity(BlockPos.ORIGIN.south(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.south(), transfer3).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final TileEntity transfer4 = this.world.addTileEntity(BlockPos.ORIGIN.south().east().east(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.south().east().east(), transfer4).size());
    Assertions.assertEquals(2, this.manager.size(), "Manager should have two networks");

    final TileEntity transferMerge = this.world.addTileEntity(BlockPos.ORIGIN.south().east(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(BlockPos.ORIGIN.south().east(), transferMerge).size(), "transferMerge should have been added to one network");
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final EnergyNetwork network = this.manager.getNetworksForBlock(BlockPos.ORIGIN).get(0);

    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN), BlockPos.ORIGIN, null, network.getNode(BlockPos.ORIGIN.south()), network.getNode(BlockPos.ORIGIN.east()), null, null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.south()), BlockPos.ORIGIN.south(), network.getNode(BlockPos.ORIGIN), null, network.getNode(BlockPos.ORIGIN.south().east()), null, null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.east()), BlockPos.ORIGIN.east(), null, network.getNode(BlockPos.ORIGIN.south().east()), null, network.getNode(BlockPos.ORIGIN), null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.south().east()), BlockPos.ORIGIN.south().east(), network.getNode(BlockPos.ORIGIN.east()), null, network.getNode(BlockPos.ORIGIN.south().east().east()), network.getNode(BlockPos.ORIGIN.south()), null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.south().east().east()), BlockPos.ORIGIN.south().east().east(), null, null, null, network.getNode(BlockPos.ORIGIN.south().east()), null, null));
  }

  @Test
  void testTwoWayTransferDoesntConnectInvalidSides() {
    final TileEntity north = this.world.addTileEntity(BlockPos.ORIGIN.north(), TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(north.getPos(), north).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final TileEntity south = this.world.addTileEntity(BlockPos.ORIGIN.south(), TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(south.getPos(), south).size());
    Assertions.assertEquals(2, this.manager.size(), "Manager should have two networks");

    final TileEntity east = this.world.addTileEntity(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(east.getPos(), east).size());
    Assertions.assertEquals(3, this.manager.size(), "Manager should have three networks");

    final TileEntity west = this.world.addTileEntity(BlockPos.ORIGIN.west(), TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(west.getPos(), west).size());
    Assertions.assertEquals(4, this.manager.size(), "Manager should have four networks");

    final TileEntity transfer = this.world.addTileEntity(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer(EnumFacing.NORTH, EnumFacing.SOUTH));
    Assertions.assertEquals(1, this.manager.connect(transfer.getPos(), transfer).size());
    Assertions.assertEquals(3, this.manager.size(), "Manager should have three networks");

    final EnergyNetwork netMiddle = this.manager.getNetworksForBlock(BlockPos.ORIGIN).get(0);
    final EnergyNetwork netEast = this.manager.getNetworksForBlock(BlockPos.ORIGIN.east()).get(0);
    final EnergyNetwork netWest = this.manager.getNetworksForBlock(BlockPos.ORIGIN.west()).get(0);

    Assertions.assertTrue(EnergyNetworkTest.checkNode(netMiddle.getNode(transfer.getPos()), transfer.getPos(), netMiddle.getNode(north.getPos()), netMiddle.getNode(south.getPos()), null, null, null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(netMiddle.getNode(north.getPos()), north.getPos(), null, netMiddle.getNode(transfer.getPos()), null, null, null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(netMiddle.getNode(south.getPos()), south.getPos(), netMiddle.getNode(transfer.getPos()), null, null, null, null, null));

    Assertions.assertTrue(EnergyNetworkTest.checkNode(netEast.getNode(east.getPos()), east.getPos(), null, null, null, null, null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(netWest.getNode(west.getPos()), west.getPos(), null, null, null, null, null, null));
  }
}
