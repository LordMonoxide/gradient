package lordmonoxide.gradient.energy;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class EnergyNetworkManagerTest {
  private static final EnergyNetwork[] EMPTY_ENERGY_NETWORKS = new EnergyNetwork[0];
  private World world;
  private EnergyNetworkManager<IEnergyStorage, IEnergyTransfer> manager;

  @BeforeAll
  static void setUpFirst() {
    EnergyNetworkTest.setUpFirst();
  }

  @BeforeEach
  void setUp() {
    this.world = new World();
    this.manager = new EnergyNetworkManager<>(this.world, EnergyNetworkTest.STORAGE, EnergyNetworkTest.TRANSFER);
  }

  @Test
  void testAddingOneTransferNode() {
    final Map<EnumFacing, EnergyNetwork<IEnergyStorage, IEnergyTransfer>> added = this.manager.connect(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer());

    Assertions.assertEquals(1, added.size(), "There should only be one network");

    for(final Map.Entry<EnumFacing, EnergyNetwork<IEnergyStorage, IEnergyTransfer>> network : added.entrySet()) {
      Assertions.assertNull(network.getKey());
      Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getValue().getNode(BlockPos.ORIGIN), BlockPos.ORIGIN, null, null, null, null, null, null), "Node did not match");
    }
  }

  @Test
  void testAddingOneStorageNode() {
    final Map<EnumFacing, EnergyNetwork<IEnergyStorage, IEnergyTransfer>> added = this.manager.connect(BlockPos.ORIGIN, TileEntityWithCapabilities.storage());

    Assertions.assertEquals(1, added.size(), "There should only be one network");

    for(final Map.Entry<EnumFacing, EnergyNetwork<IEnergyStorage, IEnergyTransfer>> network : added.entrySet()) {
      Assertions.assertNull(network.getKey());
      Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getValue().getNode(BlockPos.ORIGIN), BlockPos.ORIGIN, null, null, null, null, null, null), "Node did not match");
    }
  }

  @Test
  void testMergingStorageNetworks() {
    final TileEntity teEast = this.world.addTileEntity(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.storage());
    final Map<EnumFacing, EnergyNetwork<IEnergyStorage, IEnergyTransfer>> east = this.manager.connect(BlockPos.ORIGIN.east(), teEast);

    final TileEntity teWest = this.world.addTileEntity(BlockPos.ORIGIN.west(), TileEntityWithCapabilities.storage());
    final Map<EnumFacing, EnergyNetwork<IEnergyStorage, IEnergyTransfer>> west = this.manager.connect(BlockPos.ORIGIN.west(), teWest);

    Assertions.assertEquals(1, east.size(), "There should only be one network");
    Assertions.assertEquals(1, west.size(), "There should only be one network");
    Assertions.assertEquals(2, this.manager.size(), "There should be two networks total");

    for(final Map.Entry<EnumFacing, EnergyNetwork<IEnergyStorage, IEnergyTransfer>> network : east.entrySet()) {
      Assertions.assertNull(network.getKey());
      Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getValue().getNode(BlockPos.ORIGIN.east()), BlockPos.ORIGIN.east(), null, null, null, null, null, null), "Node did not match");
    }

    for(final Map.Entry<EnumFacing, EnergyNetwork<IEnergyStorage, IEnergyTransfer>> network : west.entrySet()) {
      Assertions.assertNull(network.getKey());
      Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getValue().getNode(BlockPos.ORIGIN.west()), BlockPos.ORIGIN.west(), null, null, null, null, null, null), "Node did not match");
    }

    final TileEntity teOrigin = TileEntityWithCapabilities.storage();

    this.world.addTileEntity(BlockPos.ORIGIN, teOrigin);

    final Map<EnumFacing, EnergyNetwork<IEnergyStorage, IEnergyTransfer>> origin = this.manager.connect(BlockPos.ORIGIN, teOrigin);

    Assertions.assertEquals(2, origin.size(), "There should be two networks");
    Assertions.assertEquals(2, this.manager.size(), "There should be two networks total");

    final EnergyNetwork[] originNetworks = origin.values().toArray(EMPTY_ENERGY_NETWORKS);

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
    final Map<EnumFacing, EnergyNetwork<IEnergyStorage, IEnergyTransfer>> transferNetworks = this.manager.connect(BlockPos.ORIGIN, transfer);
    Assertions.assertEquals(2, transferNetworks.size());
    Assertions.assertTrue(transferNetworks.containsKey(EnumFacing.EAST));
    Assertions.assertTrue(transferNetworks.containsKey(EnumFacing.WEST));
    Assertions.assertFalse(transferNetworks.containsKey(null));
    Assertions.assertEquals(transferNetworks.get(EnumFacing.EAST), transferNetworks.get(EnumFacing.WEST));
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final EnergyNetwork<IEnergyStorage, IEnergyTransfer> network = this.manager.getNetworksForBlock(BlockPos.ORIGIN).get(0);
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

    final EnergyNetwork<IEnergyStorage, IEnergyTransfer> network = this.manager.getNetworksForBlock(BlockPos.ORIGIN).get(0);
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
    final Map<EnumFacing, EnergyNetwork<IEnergyStorage, IEnergyTransfer>> networks = this.manager.connect(BlockPos.ORIGIN, origin);
    Assertions.assertEquals(4, networks.size());
    Assertions.assertTrue(networks.containsKey(EnumFacing.NORTH));
    Assertions.assertTrue(networks.containsKey(EnumFacing.SOUTH));
    Assertions.assertTrue(networks.containsKey(EnumFacing.EAST));
    Assertions.assertTrue(networks.containsKey(EnumFacing.WEST));
    Assertions.assertFalse(networks.containsKey(null));
    Assertions.assertEquals(networks.get(EnumFacing.NORTH), networks.get(EnumFacing.EAST));
    Assertions.assertEquals(networks.get(EnumFacing.EAST), networks.get(EnumFacing.SOUTH));
    Assertions.assertEquals(networks.get(EnumFacing.SOUTH), networks.get(EnumFacing.WEST));
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final EnergyNetwork<IEnergyStorage, IEnergyTransfer> network = this.manager.getNetworksForBlock(BlockPos.ORIGIN).get(0);

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

    final EnergyNetwork<IEnergyStorage, IEnergyTransfer> networkNorth = this.manager.getNetworksForBlock(BlockPos.ORIGIN.north()).get(0);
    final EnergyNetwork<IEnergyStorage, IEnergyTransfer> networkSouth = this.manager.getNetworksForBlock(BlockPos.ORIGIN.south()).get(0);
    final EnergyNetwork<IEnergyStorage, IEnergyTransfer> networkEast = this.manager.getNetworksForBlock(BlockPos.ORIGIN.east()).get(0);
    final EnergyNetwork<IEnergyStorage, IEnergyTransfer> networkWest = this.manager.getNetworksForBlock(BlockPos.ORIGIN.west()).get(0);

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
    Assertions.assertEquals(2, this.manager.connect(BlockPos.ORIGIN.south().east(), storage).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final EnergyNetwork<IEnergyStorage, IEnergyTransfer> network = this.manager.getNetworksForBlock(BlockPos.ORIGIN).get(0);

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
    Assertions.assertEquals(3, this.manager.connect(BlockPos.ORIGIN.south().east(), storage).size(), "Storage should have been added to two networks");
    Assertions.assertEquals(2, this.manager.size(), "Manager should have two networks");

    final EnergyNetwork<IEnergyStorage, IEnergyTransfer> network1 = this.manager.getNetworksForBlock(BlockPos.ORIGIN).get(0);
    final EnergyNetwork<IEnergyStorage, IEnergyTransfer> network2 = this.manager.getNetworksForBlock(BlockPos.ORIGIN.south().east().east()).get(0);

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
    Assertions.assertEquals(2, this.manager.connect(BlockPos.ORIGIN.south().east(), transfer4).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final EnergyNetwork<IEnergyStorage, IEnergyTransfer> network = this.manager.getNetworksForBlock(BlockPos.ORIGIN).get(0);

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
    final Map<EnumFacing, EnergyNetwork<IEnergyStorage, IEnergyTransfer>> networks = this.manager.connect(BlockPos.ORIGIN.south().east(), transferMerge);
    Assertions.assertEquals(3, networks.size(), "transferMerge should have been added to one network");
    Assertions.assertFalse(networks.containsKey(EnumFacing.UP));
    Assertions.assertFalse(networks.containsKey(EnumFacing.DOWN));
    Assertions.assertTrue(networks.containsKey(EnumFacing.NORTH));
    Assertions.assertFalse(networks.containsKey(EnumFacing.SOUTH));
    Assertions.assertTrue(networks.containsKey(EnumFacing.EAST));
    Assertions.assertTrue(networks.containsKey(EnumFacing.WEST));
    Assertions.assertFalse(networks.containsKey(null));
    Assertions.assertEquals(networks.get(EnumFacing.NORTH), networks.get(EnumFacing.WEST));
    Assertions.assertEquals(networks.get(EnumFacing.EAST), networks.get(EnumFacing.WEST));
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final EnergyNetwork<IEnergyStorage, IEnergyTransfer> network = this.manager.getNetworksForBlock(BlockPos.ORIGIN).get(0);

    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN), BlockPos.ORIGIN, null, network.getNode(BlockPos.ORIGIN.south()), network.getNode(BlockPos.ORIGIN.east()), null, null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.south()), BlockPos.ORIGIN.south(), network.getNode(BlockPos.ORIGIN), null, network.getNode(BlockPos.ORIGIN.south().east()), null, null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.east()), BlockPos.ORIGIN.east(), null, network.getNode(BlockPos.ORIGIN.south().east()), null, network.getNode(BlockPos.ORIGIN), null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.south().east()), BlockPos.ORIGIN.south().east(), network.getNode(BlockPos.ORIGIN.east()), null, network.getNode(BlockPos.ORIGIN.south().east().east()), network.getNode(BlockPos.ORIGIN.south()), null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(BlockPos.ORIGIN.south().east().east()), BlockPos.ORIGIN.south().east().east(), null, null, null, network.getNode(BlockPos.ORIGIN.south().east()), null, null));
  }

  @Test
  void testTwoWayTransferDoesNotConnectInvalidSides() {
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
    final Map<EnumFacing, EnergyNetwork<IEnergyStorage, IEnergyTransfer>> networks = this.manager.connect(transfer.getPos(), transfer);
    Assertions.assertEquals(2, networks.size());
    Assertions.assertFalse(networks.containsKey(EnumFacing.UP));
    Assertions.assertFalse(networks.containsKey(EnumFacing.DOWN));
    Assertions.assertTrue(networks.containsKey(EnumFacing.NORTH));
    Assertions.assertTrue(networks.containsKey(EnumFacing.SOUTH));
    Assertions.assertFalse(networks.containsKey(EnumFacing.EAST));
    Assertions.assertFalse(networks.containsKey(EnumFacing.WEST));
    Assertions.assertFalse(networks.containsKey(null));
    Assertions.assertEquals(networks.get(EnumFacing.NORTH), networks.get(EnumFacing.SOUTH));
    Assertions.assertEquals(3, this.manager.size(), "Manager should have three networks");

    final EnergyNetwork<IEnergyStorage, IEnergyTransfer> netMiddle = this.manager.getNetworksForBlock(BlockPos.ORIGIN).get(0);
    final EnergyNetwork<IEnergyStorage, IEnergyTransfer> netEast = this.manager.getNetworksForBlock(BlockPos.ORIGIN.east()).get(0);
    final EnergyNetwork<IEnergyStorage, IEnergyTransfer> netWest = this.manager.getNetworksForBlock(BlockPos.ORIGIN.west()).get(0);

    Assertions.assertTrue(EnergyNetworkTest.checkNode(netMiddle.getNode(transfer.getPos()), transfer.getPos(), netMiddle.getNode(north.getPos()), netMiddle.getNode(south.getPos()), null, null, null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(netMiddle.getNode(north.getPos()), north.getPos(), null, netMiddle.getNode(transfer.getPos()), null, null, null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(netMiddle.getNode(south.getPos()), south.getPos(), netMiddle.getNode(transfer.getPos()), null, null, null, null, null));

    Assertions.assertTrue(EnergyNetworkTest.checkNode(netEast.getNode(east.getPos()), east.getPos(), null, null, null, null, null, null));
    Assertions.assertTrue(EnergyNetworkTest.checkNode(netWest.getNode(west.getPos()), west.getPos(), null, null, null, null, null, null));
  }

  @Test
  void testTileEntityWithMultipleStorages() {
    final TileEntity origin = this.world.addTileEntity(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(origin.getPos(), origin).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final TileEntity east = this.world.addTileEntity(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(east.getPos(), east).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final TileEntity southEast = this.world.addTileEntity(BlockPos.ORIGIN.south().east(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(southEast.getPos(), southEast).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final TileEntity west = this.world.addTileEntity(BlockPos.ORIGIN.west(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(west.getPos(), west).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final TileEntity southWest = this.world.addTileEntity(BlockPos.ORIGIN.south().west(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(southWest.getPos(), southWest).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final TileEntity storage = this.world.addTileEntity(BlockPos.ORIGIN.south(), new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.STORAGE, new StorageNode(), EnumFacing.NORTH).addCapability(EnergyNetworkTest.STORAGE, new StorageNode(), EnumFacing.EAST).addCapability(EnergyNetworkTest.STORAGE, new StorageNode(), EnumFacing.WEST));
    Assertions.assertEquals(3, this.manager.connect(storage.getPos(), storage).size());
    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    final EnergyNetwork<IEnergyStorage, IEnergyTransfer> network = this.manager.getNetworksForBlock(BlockPos.ORIGIN).get(0);

    Assertions.assertTrue(EnergyNetworkTest.checkNode(network.getNode(storage.getPos()), storage.getPos(), network.getNode(origin.getPos()), null, network.getNode(southEast.getPos()), network.getNode(southWest.getPos()), null, null));
  }

  @Test
  void testDirectionalTransferNodeCreatesTwoNetworks() {
    final TileEntity north = this.world.addTileEntity(BlockPos.ORIGIN.north(), TileEntityWithCapabilities.transfer());
    this.manager.connect(north.getPos(), north);

    final TileEntity south = this.world.addTileEntity(BlockPos.ORIGIN.south(), TileEntityWithCapabilities.transfer());
    this.manager.connect(south.getPos(), south);

    final TileEntity east = this.world.addTileEntity(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.transfer());
    this.manager.connect(east.getPos(), east);

    final TileEntity west = this.world.addTileEntity(BlockPos.ORIGIN.west(), TileEntityWithCapabilities.transfer());
    this.manager.connect(west.getPos(), west);

    Assertions.assertEquals(4, this.manager.size());

    final TransferNode x = new TransferNode();
    final TransferNode z = new TransferNode();
    final TileEntity origin = this.world.addTileEntity(BlockPos.ORIGIN, new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.TRANSFER, x, EnumFacing.EAST, EnumFacing.WEST).addCapability(EnergyNetworkTest.TRANSFER, z, EnumFacing.NORTH, EnumFacing.SOUTH));
    final Map<EnumFacing, EnergyNetwork<IEnergyStorage, IEnergyTransfer>> networks = this.manager.connect(origin.getPos(), origin);
    Assertions.assertEquals(4, networks.size());
    Assertions.assertTrue(networks.containsKey(EnumFacing.NORTH));
    Assertions.assertTrue(networks.containsKey(EnumFacing.SOUTH));
    Assertions.assertTrue(networks.containsKey(EnumFacing.EAST));
    Assertions.assertTrue(networks.containsKey(EnumFacing.WEST));
    Assertions.assertFalse(networks.containsKey(null));
    Assertions.assertEquals(networks.get(EnumFacing.NORTH), networks.get(EnumFacing.SOUTH));
    Assertions.assertEquals(networks.get(EnumFacing.EAST), networks.get(EnumFacing.WEST));
    Assertions.assertNotEquals(networks.get(EnumFacing.NORTH), networks.get(EnumFacing.EAST));

    Assertions.assertEquals(2, this.manager.size());
  }

  @Test
  void testParallelOneWayTransfersDoNotMerge() {

  }

  @Test
  void testExtractingEnergySingleNetwork() {
    final IEnergyStorage sourceStorage = new StorageNode(10000.0f, 0.0f, 32.0f, 1000.0f);
    final TileEntity sourceTile = this.world.addTileEntity(BlockPos.ORIGIN.west(), new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.STORAGE, sourceStorage));
    this.manager.connect(sourceTile.getPos(), sourceTile);

    final TileEntity transfer = this.world.addTileEntity(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer());
    this.manager.connect(transfer.getPos(), transfer);

    final IEnergyStorage sinkStorage = new StorageNode(10000.0f, 32.0f, 0.0f, 100.0f);
    final TileEntity sinkTile = this.world.addTileEntity(BlockPos.ORIGIN.east(), new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.STORAGE, sinkStorage));
    this.manager.connect(sinkTile.getPos(), sinkTile);

    Assertions.assertEquals(1, this.manager.size(), "Manager should have one network");

    Assertions.assertEquals(32.0f, this.manager.requestEnergy(sinkTile.getPos(), 100.0f), 0.0001f, "Extracted energy did not match");
    Assertions.assertEquals(968.0f, sourceStorage.getEnergy(), 0.0001f, "Source energy does not match");
  }

  @Test
  void testExtractingEnergyMultipleNetworksBalanced() {
    final IEnergyStorage sourceStorageNorth = new StorageNode(10000.0f, 0.0f, 32.0f, 1000.0f);
    final TileEntity sourceNorth = this.world.addTileEntity(BlockPos.ORIGIN.north().north(), new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.STORAGE, sourceStorageNorth));
    this.manager.connect(sourceNorth.getPos(), sourceNorth);

    final TileEntity transferNorth = this.world.addTileEntity(BlockPos.ORIGIN.north(), TileEntityWithCapabilities.transfer());
    this.manager.connect(transferNorth.getPos(), transferNorth);

    final IEnergyStorage sourceStorageSouth = new StorageNode(10000.0f, 0.0f, 32.0f, 1000.0f);
    final TileEntity sourceSouth = this.world.addTileEntity(BlockPos.ORIGIN.south().south(), new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.STORAGE, sourceStorageSouth));
    this.manager.connect(sourceSouth.getPos(), sourceSouth);

    final TileEntity transferSouth = this.world.addTileEntity(BlockPos.ORIGIN.south(), TileEntityWithCapabilities.transfer());
    this.manager.connect(transferSouth.getPos(), transferSouth);

    final IEnergyStorage sourceStorageEast = new StorageNode(10000.0f, 0.0f, 32.0f, 1000.0f);
    final TileEntity sourceEast = this.world.addTileEntity(BlockPos.ORIGIN.east().east(), new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.STORAGE, sourceStorageEast));
    this.manager.connect(sourceEast.getPos(), sourceEast);

    final TileEntity transferEast = this.world.addTileEntity(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.transfer());
    this.manager.connect(transferEast.getPos(), transferEast);

    final IEnergyStorage sourceStorageWest = new StorageNode(10000.0f, 0.0f, 32.0f, 1000.0f);
    final TileEntity sourceWest = this.world.addTileEntity(BlockPos.ORIGIN.west().west(), new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.STORAGE, sourceStorageWest));
    this.manager.connect(sourceWest.getPos(), sourceWest);

    final TileEntity transferWest = this.world.addTileEntity(BlockPos.ORIGIN.west(), TileEntityWithCapabilities.transfer());
    this.manager.connect(transferWest.getPos(), transferWest);

    final IEnergyStorage sinkStorage = new StorageNode(10000.0f, 32.0f, 0.0f, 100.0f);
    final TileEntity sink = this.world.addTileEntity(BlockPos.ORIGIN, new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.STORAGE, sinkStorage));
    this.manager.connect(sink.getPos(), sink);

    Assertions.assertEquals(4, this.manager.size(), "Manager should have one network");

    Assertions.assertEquals(128.0f, this.manager.requestEnergy(sink.getPos(), 1000.0f), 0.0001f, "Extracted energy did not match");
    Assertions.assertEquals(968.0f, sourceStorageNorth.getEnergy(), 0.0001f, "Source north energy does not match");
    Assertions.assertEquals(968.0f, sourceStorageSouth.getEnergy(), 0.0001f, "Source south energy does not match");
    Assertions.assertEquals(968.0f, sourceStorageEast.getEnergy(), 0.0001f, "Source east energy does not match");
    Assertions.assertEquals(968.0f, sourceStorageWest.getEnergy(), 0.0001f, "Source west energy does not match");
  }

  @Test
  void testExtractingEnergyMultipleNetworksImbalanced() {
    final IEnergyStorage sourceStorageNorth = new StorageNode(10000.0f, 0.0f, 20.0f, 1000.0f);
    final TileEntity sourceNorth = this.world.addTileEntity(BlockPos.ORIGIN.north().north(), new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.STORAGE, sourceStorageNorth));
    this.manager.connect(sourceNorth.getPos(), sourceNorth);

    final TileEntity transferNorth = this.world.addTileEntity(BlockPos.ORIGIN.north(), TileEntityWithCapabilities.transfer());
    this.manager.connect(transferNorth.getPos(), transferNorth);

    final IEnergyStorage sourceStorageSouth = new StorageNode(10000.0f, 0.0f, 64.0f, 1000.0f);
    final TileEntity sourceSouth = this.world.addTileEntity(BlockPos.ORIGIN.south().south(), new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.STORAGE, sourceStorageSouth));
    this.manager.connect(sourceSouth.getPos(), sourceSouth);

    final TileEntity transferSouth = this.world.addTileEntity(BlockPos.ORIGIN.south(), TileEntityWithCapabilities.transfer());
    this.manager.connect(transferSouth.getPos(), transferSouth);

    final IEnergyStorage sourceStorageEast = new StorageNode(10000.0f, 0.0f, 64.0f, 1000.0f);
    final TileEntity sourceEast = this.world.addTileEntity(BlockPos.ORIGIN.east().east(), new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.STORAGE, sourceStorageEast));
    this.manager.connect(sourceEast.getPos(), sourceEast);

    final TileEntity transferEast = this.world.addTileEntity(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.transfer());
    this.manager.connect(transferEast.getPos(), transferEast);

    final IEnergyStorage sourceStorageWest = new StorageNode(10000.0f, 0.0f, 64.0f, 1000.0f);
    final TileEntity sourceWest = this.world.addTileEntity(BlockPos.ORIGIN.west().west(), new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.STORAGE, sourceStorageWest));
    this.manager.connect(sourceWest.getPos(), sourceWest);

    final TileEntity transferWest = this.world.addTileEntity(BlockPos.ORIGIN.west(), TileEntityWithCapabilities.transfer());
    this.manager.connect(transferWest.getPos(), transferWest);

    final IEnergyStorage sinkStorage = new StorageNode(10000.0f, 32.0f, 0.0f, 100.0f);
    final TileEntity sink = this.world.addTileEntity(BlockPos.ORIGIN, new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.STORAGE, sinkStorage));
    this.manager.connect(sink.getPos(), sink);

    Assertions.assertEquals(4, this.manager.size(), "Manager should have four networks");

    Assertions.assertEquals(128.0f, this.manager.requestEnergy(sink.getPos(), 128.0f), 0.0001f, "Extracted energy did not match");
    Assertions.assertEquals(980.0f, sourceStorageNorth.getEnergy(), 0.0001f, "Source north energy does not match");
    Assertions.assertEquals(964.0f, sourceStorageSouth.getEnergy(), 0.0001f, "Source south energy does not match");
    Assertions.assertEquals(964.0f, sourceStorageEast.getEnergy(), 0.0001f, "Source east energy does not match");
    Assertions.assertEquals(964.0f, sourceStorageWest.getEnergy(), 0.0001f, "Source west energy does not match");
  }

  @Test
  void testTick() {
    final IEnergyTransfer transfer = new TransferNode();
    final IEnergyStorage source1 = new StorageNode(10000.0f, 0.0f, 10.0f, 10000.0f);
    final IEnergyStorage source2 = new StorageNode(10000.0f, 0.0f, 10.0f, 20.0f);
    final IEnergyStorage sink = new StorageNode(10000.0f, 32.0f, 0.0f, 0.0f);

    final TileEntity teTransfer = this.world.addTileEntity(BlockPos.ORIGIN, new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.TRANSFER, transfer));
    this.manager.connect(teTransfer.getPos(), teTransfer);

    final TileEntity teSource1 = this.world.addTileEntity(BlockPos.ORIGIN.east(), new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.STORAGE, source1));
    this.manager.connect(teSource1.getPos(), teSource1);

    final TileEntity teSource2 = this.world.addTileEntity(BlockPos.ORIGIN.west(), new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.STORAGE, source2));
    this.manager.connect(teSource2.getPos(), teSource2);

    final TileEntity teSink = this.world.addTileEntity(BlockPos.ORIGIN.north(), new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.STORAGE, sink));
    this.manager.connect(teSink.getPos(), teSink);

    Assertions.assertEquals(this.manager.size(), 1);

    this.manager.tick();

    Assertions.assertEquals(9990.0f, source1.getEnergy(), 0.0001f);
    Assertions.assertEquals(  10.0f, source2.getEnergy(), 0.0001f);
    Assertions.assertEquals(  20.0f, sink.getEnergy(), 0.0001f);

    Assertions.assertEquals(20.0f, transfer.getEnergyTransferred(), 0.0001f);

    this.manager.tick();

    Assertions.assertEquals(9980.0f, source1.getEnergy(), 0.0001f);
    Assertions.assertEquals(   0.0f, source2.getEnergy(), 0.0001f);
    Assertions.assertEquals(  40.0f, sink.getEnergy(), 0.0001f);

    Assertions.assertEquals(20.0f, transfer.getEnergyTransferred(), 0.0001f);

    this.manager.tick();

    Assertions.assertEquals(9970.0f, source1.getEnergy(), 0.0001f);
    Assertions.assertEquals(   0.0f, source2.getEnergy(), 0.0001f);
    Assertions.assertEquals(  50.0f, sink.getEnergy(), 0.0001f);

    Assertions.assertEquals(10.0f, transfer.getEnergyTransferred(), 0.0001f);
  }

  @Test
  void testPowerIsNotExtractedFromEmptyStorage() {
    final IEnergyTransfer transfer = new TransferNode() {
      @Override
      public void transfer(final float amount, final EnumFacing from, final EnumFacing to) {
        Assertions.fail("Power should not have been routed");
        super.transfer(amount, from, to);
      }
    };
    final IEnergyStorage source = new StorageNode(10000.0f, 0.0f, 10.0f, 0.0f);
    final IEnergyStorage sink = new StorageNode(10000.0f, 32.0f, 0.0f, 0.0f) {
      @Override
      public float sinkEnergy(final float maxSink, final boolean simulate) {
        Assertions.fail("Should not have received energy");
        return super.sinkEnergy(maxSink, simulate);
      }
    };

    final TileEntity teTransfer = this.world.addTileEntity(BlockPos.ORIGIN, new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.TRANSFER, transfer));
    this.manager.connect(teTransfer.getPos(), teTransfer);

    final TileEntity teSource = this.world.addTileEntity(BlockPos.ORIGIN.east(), new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.STORAGE, source));
    this.manager.connect(teSource.getPos(), teSource);

    final TileEntity teSink = this.world.addTileEntity(BlockPos.ORIGIN.north(), new TileEntityWithCapabilities().addCapability(EnergyNetworkTest.STORAGE, sink));
    this.manager.connect(teSink.getPos(), teSink);

    Assertions.assertEquals(this.manager.size(), 1);

    this.manager.tick();

    Assertions.assertEquals(0.0f, source.getEnergy(), 0.0001f);
    Assertions.assertEquals(0.0f, sink.getEnergy(), 0.0001f);

    Assertions.assertEquals(0.0f, transfer.getEnergyTransferred(), 0.0001f);
  }

  @Test
  void testRemoveBasic() {
    final TileEntity transfer = this.world.addTileEntity(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(transfer.getPos(), transfer).size());

    final TileEntity north = this.world.addTileEntity(BlockPos.ORIGIN.north(), TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(north.getPos(), north).size());

    final TileEntity south = this.world.addTileEntity(BlockPos.ORIGIN.south(), TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(south.getPos(), south).size());

    Assertions.assertEquals(1, this.manager.size());

    this.world.removeTileEntity(north.getPos());
    this.manager.disconnect(north.getPos());
    Assertions.assertEquals(1, this.manager.size());
    Assertions.assertEquals(0, this.manager.getNetworksForBlock(north.getPos()).size());
    Assertions.assertEquals(1, this.manager.getNetworksForBlock(south.getPos()).size());
    Assertions.assertEquals(1, this.manager.getNetworksForBlock(transfer.getPos()).size());

    this.world.removeTileEntity(south.getPos());
    this.manager.disconnect(south.getPos());
    Assertions.assertEquals(1, this.manager.size());
    Assertions.assertEquals(0, this.manager.getNetworksForBlock(south.getPos()).size());
    Assertions.assertEquals(1, this.manager.getNetworksForBlock(transfer.getPos()).size());

    this.world.removeTileEntity(transfer.getPos());
    this.manager.disconnect(transfer.getPos());
    Assertions.assertEquals(0, this.manager.size());
    Assertions.assertEquals(0, this.manager.getNetworksForBlock(transfer.getPos()).size());
  }

  @Test
  void testRemoveSplitsNetwork() {
    final TileEntity transfer = this.world.addTileEntity(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(transfer.getPos(), transfer).size());

    final TileEntity north = this.world.addTileEntity(BlockPos.ORIGIN.north(), TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(north.getPos(), north).size());

    final TileEntity south = this.world.addTileEntity(BlockPos.ORIGIN.south(), TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(south.getPos(), south).size());

    Assertions.assertEquals(1, this.manager.size());

    this.world.removeTileEntity(transfer.getPos());
    this.manager.disconnect(transfer.getPos());
    Assertions.assertEquals(2, this.manager.size());
    Assertions.assertEquals(0, this.manager.getNetworksForBlock(transfer.getPos()).size());
    Assertions.assertEquals(1, this.manager.getNetworksForBlock(north.getPos()).size());
    Assertions.assertEquals(1, this.manager.getNetworksForBlock(south.getPos()).size());
    Assertions.assertNotEquals(this.manager.getNetworksForBlock(north.getPos()), this.manager.getNetworksForBlock(south.getPos()));
  }

  @Test
  void testRemoveStorageNode() {
    final TileEntity storage = this.world.addTileEntity(BlockPos.ORIGIN, TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(storage.getPos(), storage).size());

    final TileEntity north = this.world.addTileEntity(BlockPos.ORIGIN.north(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(north.getPos(), north).size());

    final TileEntity south = this.world.addTileEntity(BlockPos.ORIGIN.south(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(south.getPos(), south).size());

    Assertions.assertEquals(2, this.manager.size());

    this.world.removeTileEntity(storage.getPos());
    this.manager.disconnect(storage.getPos());
    Assertions.assertEquals(2, this.manager.size());
    Assertions.assertEquals(0, this.manager.getNetworksForBlock(storage.getPos()).size());
    Assertions.assertEquals(1, this.manager.getNetworksForBlock(north.getPos()).size());
    Assertions.assertEquals(1, this.manager.getNetworksForBlock(south.getPos()).size());
    Assertions.assertNotEquals(this.manager.getNetworksForBlock(north.getPos()), this.manager.getNetworksForBlock(south.getPos()));
  }

  @Test
  void testRemovePartOfCircularNetwork() {
    final TileEntity storage = this.world.addTileEntity(BlockPos.ORIGIN, TileEntityWithCapabilities.storage());
    Assertions.assertEquals(1, this.manager.connect(storage.getPos(), storage).size());

    final TileEntity north = this.world.addTileEntity(BlockPos.ORIGIN.north(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(north.getPos(), north).size());

    final TileEntity east = this.world.addTileEntity(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(1, this.manager.connect(east.getPos(), east).size());

    Assertions.assertEquals(2, this.manager.size());

    final TileEntity ne = this.world.addTileEntity(BlockPos.ORIGIN.north().east(), TileEntityWithCapabilities.transfer());
    Assertions.assertEquals(2, this.manager.connect(ne.getPos(), ne).size());

    Assertions.assertEquals(1, this.manager.size());

    this.world.removeTileEntity(ne.getPos());
    this.manager.disconnect(ne.getPos());
    Assertions.assertEquals(2, this.manager.size());
    Assertions.assertEquals(0, this.manager.getNetworksForBlock(ne.getPos()).size());
    Assertions.assertEquals(1, this.manager.getNetworksForBlock(north.getPos()).size());
    Assertions.assertEquals(1, this.manager.getNetworksForBlock(east.getPos()).size());
    Assertions.assertEquals(2, this.manager.getNetworksForBlock(storage.getPos()).size());
    Assertions.assertNotEquals(this.manager.getNetworksForBlock(north.getPos()), this.manager.getNetworksForBlock(east.getPos()));
    Assertions.assertTrue(this.manager.getNetworksForBlock(storage.getPos()).contains(this.manager.getNetworksForBlock(north.getPos()).get(0)));
    Assertions.assertTrue(this.manager.getNetworksForBlock(storage.getPos()).contains(this.manager.getNetworksForBlock(east.getPos()).get(0)));
  }
}
