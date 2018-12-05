package lordmonoxide.gradient.energy;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;

@DisplayName("Energy Network")
class EnergyNetworkTest {
  static final Capability<IEnergyStorage> STORAGE = newCap("STORAGE");
  static final Capability<IEnergyTransfer> TRANSFER = newCap("TRANSFER");

  private EnergyNetwork net;

  @BeforeAll
  static void setUpFirst() {
    EnergyNetwork.STORAGE = STORAGE;
    EnergyNetwork.TRANSFER = TRANSFER;
  }

  @BeforeEach
  void setUp() {
    this.net = new EnergyNetwork();
  }

  @Test
  void testContainsEmptyNetwork() {
    final boolean contains = this.net.contains(BlockPos.ORIGIN);

    Assertions.assertFalse(contains, "Contains didn't return false for empty net");
  }

  @Test
  void testAddingTransferNodeToEmptyNetwork() {
    final BlockPos pos = BlockPos.ORIGIN;
    final boolean success = this.net.connect(pos, TileEntityWithCapabilities.transfer());

    Assertions.assertTrue(success, "Failed to add node to empty net");
    Assertions.assertTrue(this.net.contains(pos), "Net doesn't contain new node");
  }

  @Test
  void testAddingStorageNodeToEmptyNetwork() {
    final BlockPos pos = BlockPos.ORIGIN;
    final boolean success = this.net.connect(pos, TileEntityWithCapabilities.storage());

    Assertions.assertTrue(success, "Failed to add node to empty net");
    Assertions.assertTrue(this.net.contains(pos), "Net doesn't contain new node");
  }

  @Test
  void testNodeLinkage() {
    final BlockPos pos1 = BlockPos.ORIGIN;
    final BlockPos pos2 = pos1.east();
    final BlockPos pos3 = pos2.south();
    final BlockPos pos4 = pos3.west();
    final BlockPos pos5 = pos1.up();
    final BlockPos pos6 = pos1.down();

    Assertions.assertTrue(this.net.connect(pos1, TileEntityWithCapabilities.transfer()), "Failed to connect pos1");
    final EnergyNetwork.EnergyNode energyNode1 = this.net.getNode(pos1);
    Assertions.assertTrue(this.checkNode(energyNode1, pos1, null, null, null, null, null, null), "Node 1 did not match expected");

    Assertions.assertTrue(this.net.connect(pos2, TileEntityWithCapabilities.transfer()), "Failed to connect pos2");
    final EnergyNetwork.EnergyNode energyNode2 = this.net.getNode(pos2);
    Assertions.assertTrue(this.checkNode(energyNode2, pos2, null, null, null, energyNode1, null, null), "Node 2 did not match expected");
    Assertions.assertTrue(this.checkNode(energyNode1, pos1, null, null, energyNode2, null, null, null), "Node 1 did not match expected");

    Assertions.assertTrue(this.net.connect(pos3, TileEntityWithCapabilities.transfer()), "Failed to connect pos3");
    final EnergyNetwork.EnergyNode energyNode3 = this.net.getNode(pos3);
    Assertions.assertTrue(this.checkNode(energyNode3, pos3, energyNode2, null, null, null, null, null), "Node 3 did not match expected");
    Assertions.assertTrue(this.checkNode(energyNode2, pos2, null, energyNode3, null, energyNode1, null, null), "Node 2 did not match expected");

    Assertions.assertTrue(this.net.connect(pos4, TileEntityWithCapabilities.transfer()), "Failed to connect pos4");
    final EnergyNetwork.EnergyNode energyNode4 = this.net.getNode(pos4);
    Assertions.assertTrue(this.checkNode(energyNode4, pos4, energyNode1, null, energyNode3, null, null, null), "Node 4 did not match expected");
    Assertions.assertTrue(this.checkNode(energyNode3, pos3, energyNode2, null, null, energyNode4, null, null), "Node 3 did not match expected");
    Assertions.assertTrue(this.checkNode(energyNode1, pos1, null, energyNode4, energyNode2, null, null, null), "Node 1 did not match expected");

    Assertions.assertTrue(this.net.connect(pos5, TileEntityWithCapabilities.transfer()), "Failed to connect pos5");
    final EnergyNetwork.EnergyNode energyNode5 = this.net.getNode(pos5);
    Assertions.assertTrue(this.checkNode(energyNode5, pos5, null, null, null, null, null, energyNode1), "Node 5 did not match expected");
    Assertions.assertTrue(this.checkNode(energyNode1, pos1, null, energyNode4, energyNode2, null, energyNode5, null), "Node 1 did not match expected");

    Assertions.assertTrue(this.net.connect(pos6, TileEntityWithCapabilities.transfer()), "Failed to connect pos6");
    final EnergyNetwork.EnergyNode energyNode6 = this.net.getNode(pos6);
    Assertions.assertTrue(this.checkNode(energyNode6, pos6, null, null, null, null, energyNode1, null), "Node 6 did not match expected");
    Assertions.assertTrue(this.checkNode(energyNode1, pos1, null, energyNode4, energyNode2, null, energyNode5, energyNode6), "Node 1 did not match expected");
  }

  private boolean checkNode(final EnergyNetwork.EnergyNode node, final BlockPos pos, @Nullable final EnergyNetwork.EnergyNode north, @Nullable final EnergyNetwork.EnergyNode south, @Nullable final EnergyNetwork.EnergyNode east, @Nullable final EnergyNetwork.EnergyNode west, @Nullable final EnergyNetwork.EnergyNode up, @Nullable final EnergyNetwork.EnergyNode down) {
    return node.pos == pos && node.connection(EnumFacing.NORTH) == north && node.connection(EnumFacing.SOUTH) == south && node.connection(EnumFacing.EAST) == east && node.connection(EnumFacing.WEST) == west && node.connection(EnumFacing.UP) == up && node.connection(EnumFacing.DOWN) == down;
  }

  @Test
  void testInvalidConnection() {
    this.net.connect(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer());

    Assertions.assertFalse(this.net.connect(new BlockPos(100.0f, 100.0f, 100.0f), TileEntityWithCapabilities.transfer()), "Invalid connection didn't return false");
  }

  @Test
  void testDuplicateConnection() {
    this.net.connect(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer());

    Assertions.assertFalse(this.net.connect(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer()), "Duplicate connection didn't return false");
  }

  @Test
  void testStorageNodesCantConnectToOtherStorageNodesOnlyNode() {
    this.net.connect(BlockPos.ORIGIN, TileEntityWithCapabilities.storage());

    Assertions.assertFalse(this.net.connect(BlockPos.ORIGIN.north(), TileEntityWithCapabilities.storage()), "Transfer nodes should not be able to connect to each other");
  }

  @Test
  void testStorageNodesSplitNetwork() {
    this.net.connect(BlockPos.ORIGIN, TileEntityWithCapabilities.storage());

    Assertions.assertTrue(this.net.connect(BlockPos.ORIGIN.north(), TileEntityWithCapabilities.transfer()), "First transfer node should connect to storage node");
    Assertions.assertTrue(this.net.connect(BlockPos.ORIGIN.north().north(), TileEntityWithCapabilities.transfer()), "Second transfer node should connect to first transfer node");
    Assertions.assertFalse(this.net.connect(BlockPos.ORIGIN.south(), TileEntityWithCapabilities.transfer()), "Third transfer node should not connect to storage node");
  }

  @Test
  void testConnectionsRestrictedBySides() {
    this.net.connect(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer(EnumFacing.NORTH, EnumFacing.SOUTH));

    Assertions.assertTrue(this.net.connect(BlockPos.ORIGIN.north(), TileEntityWithCapabilities.transfer(EnumFacing.SOUTH)), "Failed to add north transfer node");
    Assertions.assertFalse(this.net.connect(BlockPos.ORIGIN.north().north(), TileEntityWithCapabilities.transfer()), "North transfer node 2 should not have been added");
    Assertions.assertFalse(this.net.connect(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.transfer()), "East transfer node should not have been added");
    Assertions.assertFalse(this.net.connect(BlockPos.ORIGIN.south(), TileEntityWithCapabilities.transfer(EnumFacing.SOUTH)), "South transfer node should not have been added");
  }

  @Test
  void testEnergyContained() {
    this.net.connect(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN.north(), new TileEntityWithCapabilities().addCapability(STORAGE, new StorageNode(1000.0f, 10.0f, 10.0f, 1000.0f)));
    this.net.connect(BlockPos.ORIGIN.south(), new TileEntityWithCapabilities().addCapability(STORAGE, new StorageNode(1000.0f, 10.0f, 50.0f, 25.0f)));
    this.net.connect(BlockPos.ORIGIN.east(), new TileEntityWithCapabilities().addCapability(STORAGE, new StorageNode(1000.0f, 10.0f, 15.0f, 20.0f)));
    this.net.connect(BlockPos.ORIGIN.west(), new TileEntityWithCapabilities().addCapability(STORAGE, new StorageNode(1000.0f, 10.0f, 100.0f, 100.0f)));

    Assertions.assertEquals(150.0f, this.net.getAvailableEnergy(), 0.001f, "Available energy did not match");
  }

  @Test
  void testExtractEnergyBalanced() {
    final StorageNode s1 = new StorageNode(1000.0f, 10.0f,  10.0f, 1000.0f);
    final StorageNode s2 = new StorageNode(1000.0f, 10.0f,  50.0f,   25.0f);
    final StorageNode s3 = new StorageNode(1000.0f, 10.0f,  15.0f,   20.0f);
    final StorageNode s4 = new StorageNode(1000.0f, 10.0f, 100.0f,  100.0f);

    this.net.connect(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN.north(), new TileEntityWithCapabilities().addCapability(STORAGE, s1));
    this.net.connect(BlockPos.ORIGIN.south(), new TileEntityWithCapabilities().addCapability(STORAGE, s2));
    this.net.connect(BlockPos.ORIGIN.east(), new TileEntityWithCapabilities().addCapability(STORAGE, s3));
    this.net.connect(BlockPos.ORIGIN.west(), new TileEntityWithCapabilities().addCapability(STORAGE, s4));

    Assertions.assertEquals(20.0f, this.net.extractEnergy(20.0f), 0.001f, "Extracted energy did not match");
    Assertions.assertEquals(995.0f, s1.getEnergy(), 0.001f, "s1 remaining energy did not match");
    Assertions.assertEquals( 20.0f, s2.getEnergy(), 0.001f, "s2 remaining energy did not match");
    Assertions.assertEquals( 15.0f, s3.getEnergy(), 0.001f, "s3 remaining energy did not match");
    Assertions.assertEquals( 95.0f, s4.getEnergy(), 0.001f, "s4 remaining energy did not match");
  }

  @Test
  void testExtractEnergyImbalanced() {
    final StorageNode s1 = new StorageNode(1000.0f, 10.0f,  10.0f, 1000.0f);
    final StorageNode s2 = new StorageNode(1000.0f, 10.0f,  50.0f,   25.0f);
    final StorageNode s3 = new StorageNode(1000.0f, 10.0f,  15.0f,   20.0f);
    final StorageNode s4 = new StorageNode(1000.0f, 10.0f, 100.0f,  100.0f);

    this.net.connect(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN.north(), new TileEntityWithCapabilities().addCapability(STORAGE, s1));
    this.net.connect(BlockPos.ORIGIN.south(), new TileEntityWithCapabilities().addCapability(STORAGE, s2));
    this.net.connect(BlockPos.ORIGIN.east(), new TileEntityWithCapabilities().addCapability(STORAGE, s3));
    this.net.connect(BlockPos.ORIGIN.west(), new TileEntityWithCapabilities().addCapability(STORAGE, s4));

    Assertions.assertEquals(050.0000f, this.net.extractEnergy(50.0f), 0.001f, "Extracted energy did not match");
    Assertions.assertEquals(990.0000f, s1.getEnergy(), 0.001f, "s1 remaining energy did not match");
    Assertions.assertEquals( 11.6667f, s2.getEnergy(), 0.001f, "s2 remaining energy did not match");
    Assertions.assertEquals(  6.6667f, s3.getEnergy(), 0.001f, "s3 remaining energy did not match");
    Assertions.assertEquals( 86.6667f, s4.getEnergy(), 0.001f, "s4 remaining energy did not match");
  }

  private static <T> Capability<T> newCap(final String name) throws RuntimeException {
    try {
      final Constructor<Capability> constructor = Capability.class.getDeclaredConstructor(String.class, Capability.IStorage.class, Callable.class);
      constructor.setAccessible(true);
      return constructor.newInstance(name, null, null);
    } catch(final Exception e) {
      throw new RuntimeException(e);
    }
  }
}
