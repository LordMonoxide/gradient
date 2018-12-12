package lordmonoxide.gradient.energy;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.utils.BlockPosUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.List;
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
    EnergyNetworkManager.STORAGE = STORAGE;
    EnergyNetworkManager.TRANSFER = TRANSFER;

    GradientMod.logger = LogManager.getLogger(GradientMod.MODID);
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
    Assertions.assertTrue(checkNode(energyNode1, pos1, null, null, null, null, null, null), "Node 1 did not match expected");

    Assertions.assertTrue(this.net.connect(pos2, TileEntityWithCapabilities.transfer()), "Failed to connect pos2");
    final EnergyNetwork.EnergyNode energyNode2 = this.net.getNode(pos2);
    Assertions.assertTrue(checkNode(energyNode2, pos2, null, null, null, energyNode1, null, null), "Node 2 did not match expected");
    Assertions.assertTrue(checkNode(energyNode1, pos1, null, null, energyNode2, null, null, null), "Node 1 did not match expected");

    Assertions.assertTrue(this.net.connect(pos3, TileEntityWithCapabilities.transfer()), "Failed to connect pos3");
    final EnergyNetwork.EnergyNode energyNode3 = this.net.getNode(pos3);
    Assertions.assertTrue(checkNode(energyNode3, pos3, energyNode2, null, null, null, null, null), "Node 3 did not match expected");
    Assertions.assertTrue(checkNode(energyNode2, pos2, null, energyNode3, null, energyNode1, null, null), "Node 2 did not match expected");

    Assertions.assertTrue(this.net.connect(pos4, TileEntityWithCapabilities.transfer()), "Failed to connect pos4");
    final EnergyNetwork.EnergyNode energyNode4 = this.net.getNode(pos4);
    Assertions.assertTrue(checkNode(energyNode4, pos4, energyNode1, null, energyNode3, null, null, null), "Node 4 did not match expected");
    Assertions.assertTrue(checkNode(energyNode3, pos3, energyNode2, null, null, energyNode4, null, null), "Node 3 did not match expected");
    Assertions.assertTrue(checkNode(energyNode1, pos1, null, energyNode4, energyNode2, null, null, null), "Node 1 did not match expected");

    Assertions.assertTrue(this.net.connect(pos5, TileEntityWithCapabilities.transfer()), "Failed to connect pos5");
    final EnergyNetwork.EnergyNode energyNode5 = this.net.getNode(pos5);
    Assertions.assertTrue(checkNode(energyNode5, pos5, null, null, null, null, null, energyNode1), "Node 5 did not match expected");
    Assertions.assertTrue(checkNode(energyNode1, pos1, null, energyNode4, energyNode2, null, energyNode5, null), "Node 1 did not match expected");

    Assertions.assertTrue(this.net.connect(pos6, TileEntityWithCapabilities.transfer()), "Failed to connect pos6");
    final EnergyNetwork.EnergyNode energyNode6 = this.net.getNode(pos6);
    Assertions.assertTrue(checkNode(energyNode6, pos6, null, null, null, null, energyNode1, null), "Node 6 did not match expected");
    Assertions.assertTrue(checkNode(energyNode1, pos1, null, energyNode4, energyNode2, null, energyNode5, energyNode6), "Node 1 did not match expected");
  }

  static boolean checkNode(@Nullable final EnergyNetwork.EnergyNode node, final BlockPos pos, @Nullable final EnergyNetwork.EnergyNode north, @Nullable final EnergyNetwork.EnergyNode south, @Nullable final EnergyNetwork.EnergyNode east, @Nullable final EnergyNetwork.EnergyNode west, @Nullable final EnergyNetwork.EnergyNode up, @Nullable final EnergyNetwork.EnergyNode down) {
    return node != null && node.pos.equals(pos) && node.connection(EnumFacing.NORTH) == north && node.connection(EnumFacing.SOUTH) == south && node.connection(EnumFacing.EAST) == east && node.connection(EnumFacing.WEST) == west && node.connection(EnumFacing.UP) == up && node.connection(EnumFacing.DOWN) == down;
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
  void testStorageNodesCantConnectToOtherStorageNodesUnlessOnlyNode() {
    this.net.connect(BlockPos.ORIGIN, TileEntityWithCapabilities.storage());

    Assertions.assertTrue(this.net.connect(BlockPos.ORIGIN.north(), TileEntityWithCapabilities.storage()));
    Assertions.assertFalse(this.net.connect(BlockPos.ORIGIN.south(), TileEntityWithCapabilities.storage()));
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
  void testTileEntityWithMultipleStorages() {
    this.net.connect(BlockPos.ORIGIN, new TileEntityWithCapabilities().addCapability(STORAGE, new EnergyStorage(10000.0f, 32.0f, 32.0f, 10000.0f), EnumFacing.NORTH, EnumFacing.SOUTH).addCapability(STORAGE, new EnergyStorage(10000.0f, 16.0f, 16.0f, 10000.0f), EnumFacing.EAST, EnumFacing.WEST));

    Assertions.assertTrue(this.net.connect(BlockPos.ORIGIN.north(), TileEntityWithCapabilities.transfer()), "Failed to add north transfer node");
    Assertions.assertTrue(this.net.connect(BlockPos.ORIGIN.north().east(), TileEntityWithCapabilities.transfer()), "Failed to add north east transfer node");
    Assertions.assertTrue(this.net.connect(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.transfer()), "Failed to add east transfer node");

    Assertions.assertTrue(checkNode(this.net.getNode(BlockPos.ORIGIN), BlockPos.ORIGIN, this.net.getNode(BlockPos.ORIGIN.north()), null, this.net.getNode(BlockPos.ORIGIN.east()), null, null, null), () -> "Storage node did not match expected: " + this.net.getNode(BlockPos.ORIGIN));

    Assertions.assertEquals(48.0f, this.net.getAvailableEnergy(), 0.0001f, "Available energy did not match");
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

    Assertions.assertEquals(20.0f, this.net.requestEnergy(BlockPos.ORIGIN.north(), EnumFacing.SOUTH, 20.0f), 0.001f, "Extracted energy did not match");
    Assertions.assertEquals( 20.0f, s2.getEnergy(), 0.001f, "s2 remaining energy did not match");
    Assertions.assertEquals( 15.0f, s3.getEnergy(), 0.001f, "s3 remaining energy did not match");
    Assertions.assertEquals( 95.0f, s4.getEnergy(), 0.001f, "s4 remaining energy did not match");
  }

  @Test
  void testExtractFromTileEntityWithMultipleStorages() {
    final StorageNode s1 = new StorageNode(10000.0f, 32.0f, 32.0f, 10000.0f);
    final StorageNode s2 = new StorageNode(10000.0f, 16.0f, 16.0f, 10000.0f);

    this.net.connect(BlockPos.ORIGIN, new TileEntityWithCapabilities().addCapability(STORAGE, s1, EnumFacing.NORTH, EnumFacing.SOUTH).addCapability(STORAGE, s2, EnumFacing.EAST, EnumFacing.WEST));

    this.net.connect(BlockPos.ORIGIN.north(), TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN.north().east(), TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.transfer());

    this.net.connect(BlockPos.ORIGIN.north().north(), new TileEntityWithCapabilities().addCapability(STORAGE, new StorageNode(10000.0f, 100.0f, 0.0f, 0.0f)));

    Assertions.assertEquals(40.0f, this.net.requestEnergy(BlockPos.ORIGIN.north().north(), EnumFacing.SOUTH, 40.0f), 0.0001f, "Extracted energy did not match");
    Assertions.assertEquals(9976.0f, s1.getEnergy(), 0.001f, "s1 remaining energy did not match");
    Assertions.assertEquals(9984.0f, s2.getEnergy(), 0.001f, "s2 remaining energy did not match");
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

    this.net.connect(BlockPos.ORIGIN.up(), new TileEntityWithCapabilities().addCapability(STORAGE, new StorageNode(10000.0f, 100.0f, 0.0f, 0.0f)));

    Assertions.assertEquals( 50.0000f, this.net.requestEnergy(BlockPos.ORIGIN.up(), EnumFacing.DOWN, 50.0f), 0.001f, "Extracted energy did not match");
    Assertions.assertEquals(990.0000f, s1.getEnergy(), 0.001f, "s1 remaining energy did not match");
    Assertions.assertEquals( 11.6667f, s2.getEnergy(), 0.001f, "s2 remaining energy did not match");
    Assertions.assertEquals(  6.6667f, s3.getEnergy(), 0.001f, "s3 remaining energy did not match");
    Assertions.assertEquals( 86.6667f, s4.getEnergy(), 0.001f, "s4 remaining energy did not match");
  }

  @Test
  void testCantExtractFromSink() {
    final StorageNode s1 = new StorageNode(1000.0f, 10.0f,  0.0f, 1000.0f);

    this.net.connect(BlockPos.ORIGIN, new TileEntityWithCapabilities().addCapability(STORAGE, s1));
    this.net.connect(BlockPos.ORIGIN.south(), TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN.south().south(), new TileEntityWithCapabilities().addCapability(STORAGE, new StorageNode(10000.0f, 100.0f, 0.0f, 0.0f)));

    Assertions.assertEquals(0.0f, this.net.requestEnergy(BlockPos.ORIGIN.south().south(), EnumFacing.NORTH, 50.0f), 0.001f, "Extracted energy did not match");
    Assertions.assertEquals(1000.0f, s1.getEnergy(), 0.0001f, "Sink energy does not match");
  }

  @Test
  void testMergingNetworks() {
    final TileEntity origin = TileEntityWithCapabilities.transfer();
    final TileEntity north = TileEntityWithCapabilities.sink();
    final TileEntity south = TileEntityWithCapabilities.sink();
    final TileEntity east = TileEntityWithCapabilities.sink();
    final TileEntity west = TileEntityWithCapabilities.sink();
    final TileEntity source1 = TileEntityWithCapabilities.source();

    final EnergyNetwork net1 = new EnergyNetwork();
    Assertions.assertTrue(net1.connect(BlockPos.ORIGIN, origin));
    Assertions.assertTrue(net1.connect(BlockPos.ORIGIN.north(), north));
    Assertions.assertTrue(net1.connect(BlockPos.ORIGIN.south(), south));
    Assertions.assertTrue(net1.connect(BlockPos.ORIGIN.east(), east));
    Assertions.assertTrue(net1.connect(BlockPos.ORIGIN.west(), west));
    Assertions.assertTrue(net1.connect(BlockPos.ORIGIN.down(), source1));

    final TileEntity origin2 = TileEntityWithCapabilities.transfer();
    final TileEntity north2 = TileEntityWithCapabilities.sink();
    final TileEntity south2 = TileEntityWithCapabilities.sink();
    final TileEntity east2 = TileEntityWithCapabilities.sink();
    final TileEntity west2 = TileEntityWithCapabilities.sink();
    final TileEntity source2 = TileEntityWithCapabilities.source();

    final EnergyNetwork net2 = new EnergyNetwork();
    Assertions.assertTrue(net2.connect(BlockPos.ORIGIN.up(), origin2));
    Assertions.assertTrue(net2.connect(BlockPos.ORIGIN.up().north(), north2));
    Assertions.assertTrue(net2.connect(BlockPos.ORIGIN.up().south(), south2));
    Assertions.assertTrue(net2.connect(BlockPos.ORIGIN.up().east(), east2));
    Assertions.assertTrue(net2.connect(BlockPos.ORIGIN.up().west(), west2));
    Assertions.assertTrue(net2.connect(BlockPos.ORIGIN.up().up(), source2));

    final EnergyNetwork newNet = EnergyNetwork.merge(net1, net2);

    final EnergyNetwork.EnergyNode originNode = newNet.getNode(BlockPos.ORIGIN);
    final EnergyNetwork.EnergyNode northNode = newNet.getNode(BlockPos.ORIGIN.north());
    final EnergyNetwork.EnergyNode southNode = newNet.getNode(BlockPos.ORIGIN.south());
    final EnergyNetwork.EnergyNode eastNode = newNet.getNode(BlockPos.ORIGIN.east());
    final EnergyNetwork.EnergyNode westNode = newNet.getNode(BlockPos.ORIGIN.west());
    final EnergyNetwork.EnergyNode source1Node = newNet.getNode(BlockPos.ORIGIN.down());
    final EnergyNetwork.EnergyNode origin2Node = newNet.getNode(BlockPos.ORIGIN.up());
    final EnergyNetwork.EnergyNode north2Node = newNet.getNode(BlockPos.ORIGIN.up().north());
    final EnergyNetwork.EnergyNode south2Node = newNet.getNode(BlockPos.ORIGIN.up().south());
    final EnergyNetwork.EnergyNode east2Node = newNet.getNode(BlockPos.ORIGIN.up().east());
    final EnergyNetwork.EnergyNode west2Node = newNet.getNode(BlockPos.ORIGIN.up().west());
    final EnergyNetwork.EnergyNode source2Node = newNet.getNode(BlockPos.ORIGIN.up().up());

    Assertions.assertTrue(checkNode(originNode, BlockPos.ORIGIN, northNode, southNode, eastNode, westNode, origin2Node, source1Node), () -> "origin1 did not match: " + originNode);
    Assertions.assertTrue(checkNode(northNode, BlockPos.ORIGIN.north(), null, originNode, null, null, null, null), () -> "north1 did not match: " + northNode);
    Assertions.assertTrue(checkNode(southNode, BlockPos.ORIGIN.south(), originNode, null, null, null, null, null), () -> "south1 did not match: " + southNode);
    Assertions.assertTrue(checkNode(eastNode, BlockPos.ORIGIN.east(), null, null, null, originNode, null, null), () -> "east1 did not match: " + eastNode);
    Assertions.assertTrue(checkNode(westNode, BlockPos.ORIGIN.west(), null, null, originNode, null, null, null), () -> "west1 did not match: " + westNode);
    Assertions.assertTrue(checkNode(source1Node, BlockPos.ORIGIN.down(), null, null, null, null, originNode, null), () -> "source1 did not match: " + source1Node);
    Assertions.assertTrue(checkNode(origin2Node, BlockPos.ORIGIN.up(), north2Node, south2Node, east2Node, west2Node, source2Node, originNode), () -> "origin2 did not match: " + origin2Node);
    Assertions.assertTrue(checkNode(north2Node, BlockPos.ORIGIN.up().north(), null, origin2Node, null, null, null, null), () -> "north2 did not match: " + north2Node);
    Assertions.assertTrue(checkNode(south2Node, BlockPos.ORIGIN.up().south(), origin2Node, null, null, null, null, null), () -> "south2 did not match: " + south2Node);
    Assertions.assertTrue(checkNode(east2Node, BlockPos.ORIGIN.up().east(), null, null, null, origin2Node, null, null), () -> "east2 did not match: " + east2Node);
    Assertions.assertTrue(checkNode(west2Node, BlockPos.ORIGIN.up().west(), null, null, origin2Node, null, null, null), () -> "west2 did not match: " + west2Node);
    Assertions.assertTrue(checkNode(source2Node, BlockPos.ORIGIN.up().up(), null, null, null, null, null, origin2Node), () -> "source2 did not match: " + source2Node);

    Assertions.assertEquals(64.0f, newNet.getAvailableEnergy(), 0.0001f, "Available energy did not match");
  }

  @Test
  void testBasicPathfinding() {
    Assertions.assertTrue(this.net.connect(BlockPos.ORIGIN.south(), TileEntityWithCapabilities.storage()));
    Assertions.assertTrue(this.net.connect(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer()));
    Assertions.assertTrue(this.net.connect(BlockPos.ORIGIN.north(), TileEntityWithCapabilities.storage()));

    this.verifyPath(this.net.pathFind(BlockPos.ORIGIN.south(), EnumFacing.NORTH, BlockPos.ORIGIN.north(), EnumFacing.SOUTH));
  }

  @Test
  void testPathfindingMultiplePaths() {
    Assertions.assertTrue(this.net.connect(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer()));
    Assertions.assertTrue(this.net.connect(BlockPos.ORIGIN.north(), TileEntityWithCapabilities.transfer()));
    Assertions.assertTrue(this.net.connect(BlockPos.ORIGIN.north().west(), TileEntityWithCapabilities.transfer()));
    Assertions.assertTrue(this.net.connect(BlockPos.ORIGIN.south(), TileEntityWithCapabilities.transfer()));
    Assertions.assertTrue(this.net.connect(BlockPos.ORIGIN.south().east(), TileEntityWithCapabilities.transfer()));
    Assertions.assertTrue(this.net.connect(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.transfer()));
    Assertions.assertTrue(this.net.connect(BlockPos.ORIGIN.west(), TileEntityWithCapabilities.transfer()));
    Assertions.assertTrue(this.net.connect(BlockPos.ORIGIN.north().east(), TileEntityWithCapabilities.storage()));
    Assertions.assertTrue(this.net.connect(BlockPos.ORIGIN.south().west(), TileEntityWithCapabilities.storage()));

    this.verifyPath(this.net.pathFind(BlockPos.ORIGIN.north().east(), EnumFacing.SOUTH, BlockPos.ORIGIN.south().west(), EnumFacing.NORTH));
    this.verifyPath(this.net.pathFind(BlockPos.ORIGIN.north().east(), EnumFacing.WEST, BlockPos.ORIGIN.south().west(), EnumFacing.EAST));
  }

  @Test
  void testPathfindingTwoBranches() {
    Assertions.assertTrue(this.net.connect(new BlockPos( 0, 0, -1), TileEntityWithCapabilities.storage()));
    Assertions.assertTrue(this.net.connect(new BlockPos( 0, 0,  0), TileEntityWithCapabilities.transfer()));
    Assertions.assertTrue(this.net.connect(new BlockPos(-1, 0,  0), TileEntityWithCapabilities.transfer()));
    Assertions.assertTrue(this.net.connect(new BlockPos(-1, 0,  1), TileEntityWithCapabilities.transfer()));
    Assertions.assertTrue(this.net.connect(new BlockPos(-1, 0,  2), TileEntityWithCapabilities.transfer()));
    Assertions.assertTrue(this.net.connect(new BlockPos(-1, 0,  3), TileEntityWithCapabilities.transfer()));
    Assertions.assertTrue(this.net.connect(new BlockPos( 1, 0,  0), TileEntityWithCapabilities.transfer()));
    Assertions.assertTrue(this.net.connect(new BlockPos( 1, 0,  1), TileEntityWithCapabilities.transfer()));
    Assertions.assertTrue(this.net.connect(new BlockPos( 1, 0,  2), TileEntityWithCapabilities.transfer()));
    Assertions.assertTrue(this.net.connect(new BlockPos( 1, 0,  3), TileEntityWithCapabilities.transfer()));
    Assertions.assertTrue(this.net.connect(new BlockPos( 0, 0,  3), TileEntityWithCapabilities.storage()));

    this.verifyPath(this.net.pathFind(new BlockPos(0, 0, -1), EnumFacing.SOUTH, new BlockPos(0, 0, 3), EnumFacing.EAST));
    this.verifyPath(this.net.pathFind(new BlockPos(0, 0, -1), EnumFacing.SOUTH, new BlockPos(0, 0, 3), EnumFacing.WEST));
  }

  @Test
  void testPathWithLoop() {
    final TransferNode transferX = new TransferNode();
    final TransferNode transferZ = new TransferNode();

    this.net.connect(BlockPos.ORIGIN.south().south(), TileEntityWithCapabilities.storage());
    this.net.connect(BlockPos.ORIGIN.south(), TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN, new TileEntityWithCapabilities().addCapability(TRANSFER, transferX, EnumFacing.EAST, EnumFacing.WEST).addCapability(TRANSFER, transferZ, EnumFacing.NORTH, EnumFacing.SOUTH));
    this.net.connect(BlockPos.ORIGIN.north(), TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN.north().east(), TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN.west(), TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN.west().west(), TileEntityWithCapabilities.storage());

    final List<BlockPos> path = this.net.pathFind(BlockPos.ORIGIN.south().south(), EnumFacing.NORTH, BlockPos.ORIGIN.west().west(), EnumFacing.EAST);
    this.verifyPath(path);
    Assertions.assertEquals(9, path.size(), "Path did not loop");
  }

  @Test
  void testSeeminglyShorterPathDoesNotReachGoal() {
    this.net.connect(new BlockPos(0, 0,  1), TileEntityWithCapabilities.storage());

    this.net.connect(new BlockPos(0, 0,  0), TileEntityWithCapabilities.transfer());
    this.net.connect(new BlockPos(0, 0, -1), TileEntityWithCapabilities.transfer());
    this.net.connect(new BlockPos(0, 0, -2), TileEntityWithCapabilities.transfer());
    this.net.connect(new BlockPos(0, 0, -3), TileEntityWithCapabilities.transfer());
    this.net.connect(new BlockPos(0, 0, -4), TileEntityWithCapabilities.transfer());
    this.net.connect(new BlockPos(0, 0, -5), TileEntityWithCapabilities.transfer());
    this.net.connect(new BlockPos(0, 0, -6), TileEntityWithCapabilities.transfer());
    this.net.connect(new BlockPos(0, 0, -7), TileEntityWithCapabilities.transfer());

    this.net.connect(new BlockPos(-1, 0, 0), TileEntityWithCapabilities.transfer());
    this.net.connect(new BlockPos(-2, 0, 0), TileEntityWithCapabilities.transfer());
    this.net.connect(new BlockPos(-2, 0, -1), TileEntityWithCapabilities.transfer());
    this.net.connect(new BlockPos(-2, 0, -2), TileEntityWithCapabilities.transfer());
    this.net.connect(new BlockPos(-2, 0, -3), TileEntityWithCapabilities.transfer());
    this.net.connect(new BlockPos(-2, 0, -4), TileEntityWithCapabilities.transfer());
    this.net.connect(new BlockPos(-2, 0, -5), TileEntityWithCapabilities.transfer());
    this.net.connect(new BlockPos(-2, 0, -6), TileEntityWithCapabilities.transfer());
    this.net.connect(new BlockPos(-2, 0, -7), TileEntityWithCapabilities.transfer());
    this.net.connect(new BlockPos(-2, 0, -8), TileEntityWithCapabilities.transfer());
    this.net.connect(new BlockPos(-2, 0, -9), TileEntityWithCapabilities.transfer());
    this.net.connect(new BlockPos(-1, 0, -9), TileEntityWithCapabilities.transfer());

    this.net.connect(new BlockPos(0, 0,  -9), TileEntityWithCapabilities.storage());

    final List<BlockPos> path = this.net.pathFind(new BlockPos(0, 0, 1), EnumFacing.NORTH, new BlockPos(0, 0, -9), EnumFacing.WEST);
    this.verifyPath(path);
    Assertions.assertEquals(15, path.size(), "Path was the wrong size");
  }

  @Test
  void testEnergyTransferredThroughCorrectTransferNodes() {
    final StorageNode sourceEast = new StorageNode(10000.0f, 0.0f, 10.0f, 10000.0f);
    final StorageNode sourceWest = new StorageNode(10000.0f, 0.0f, 10.0f, 10000.0f);
    final TransferNode transferOrigin = new TransferNode();
    final TransferNode transferEast = new TransferNode();
    final TransferNode transferWest = new TransferNode();
    final TransferNode transferOrigin2 = new TransferNode();
    final TransferNode transferEast2 = new TransferNode();
    final TransferNode transferWest2 = new TransferNode();
    final TransferNode transferEast3 = new TransferNode();
    final TransferNode transferWest3 = new TransferNode();
    final TransferNode transferWest4 = new TransferNode();
    final StorageNode sink = new StorageNode(10000.0f, 32.0f, 0.0f, 0.0f);

    this.net.connect(BlockPos.ORIGIN, new TileEntityWithCapabilities().addCapability(TRANSFER, transferOrigin));
    this.net.connect(BlockPos.ORIGIN.east(), new TileEntityWithCapabilities().addCapability(TRANSFER, transferEast));
    this.net.connect(BlockPos.ORIGIN.west(), new TileEntityWithCapabilities().addCapability(TRANSFER, transferWest));
    this.net.connect(BlockPos.ORIGIN.north(), new TileEntityWithCapabilities().addCapability(TRANSFER, transferOrigin2));
    this.net.connect(BlockPos.ORIGIN.north().east(), new TileEntityWithCapabilities().addCapability(TRANSFER, transferEast2));
    this.net.connect(BlockPos.ORIGIN.north().west(), new TileEntityWithCapabilities().addCapability(TRANSFER, transferWest2));
    this.net.connect(BlockPos.ORIGIN.north().north().east(), new TileEntityWithCapabilities().addCapability(TRANSFER, transferEast3));
    this.net.connect(BlockPos.ORIGIN.north().north().west(), new TileEntityWithCapabilities().addCapability(TRANSFER, transferWest3));
    this.net.connect(BlockPos.ORIGIN.north().north().north().west(), new TileEntityWithCapabilities().addCapability(TRANSFER, transferWest4));
    this.net.connect(BlockPos.ORIGIN.south().east(), new TileEntityWithCapabilities().addCapability(STORAGE, sourceEast));
    this.net.connect(BlockPos.ORIGIN.south().west(), new TileEntityWithCapabilities().addCapability(STORAGE, sourceWest));
    this.net.connect(BlockPos.ORIGIN.north().north().north(), new TileEntityWithCapabilities().addCapability(STORAGE, sink));

    Assertions.assertEquals(20.0f, this.net.requestEnergy(BlockPos.ORIGIN.north().north().north(), EnumFacing.WEST, 32.0f), 0.0001f, "Extracted energy did not match");

    Assertions.assertEquals(10.0f, transferOrigin.getTransferred(), 0.0001f);
    Assertions.assertEquals(10.0f, transferEast.getTransferred(), 0.0001f);
    Assertions.assertEquals(10.0f, transferWest.getTransferred(), 0.0001f);
    Assertions.assertEquals(10.0f, transferOrigin2.getTransferred(), 0.0001f);
    Assertions.assertEquals( 0.0f, transferEast2.getTransferred(), 0.0001f);
    Assertions.assertEquals(20.0f, transferWest2.getTransferred(), 0.0001f);
    Assertions.assertEquals( 0.0f, transferEast3.getTransferred(), 0.0001f);
    Assertions.assertEquals(20.0f, transferWest3.getTransferred(), 0.0001f);
    Assertions.assertEquals(20.0f, transferWest4.getTransferred(), 0.0001f);
  }

  @Test
  void testRemoveBasic() {
    this.net.connect(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN.north(), TileEntityWithCapabilities.storage());
    this.net.connect(BlockPos.ORIGIN.south(), TileEntityWithCapabilities.storage());

    Assertions.assertEquals(3, this.net.size());

    Assertions.assertFalse(this.net.disconnect(BlockPos.ORIGIN.north()));
    Assertions.assertEquals(2, this.net.size());

    Assertions.assertFalse(this.net.disconnect(BlockPos.ORIGIN.south()));
    Assertions.assertEquals(1, this.net.size());
  }

  @Test
  void testRemoveLastNode() {
    this.net.connect(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer());

    Assertions.assertTrue(this.net.disconnect(BlockPos.ORIGIN));
    Assertions.assertEquals(0, this.net.size());
  }

  @Test
  void testRemoveNodeWouldSplitNetwork() {
    this.net.connect(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN.north(), TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN.south(), TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN.west(), TileEntityWithCapabilities.transfer());

    Assertions.assertTrue(this.net.disconnect(BlockPos.ORIGIN));
    Assertions.assertEquals(4, this.net.size());
  }

  @Test
  void testRemoveNodeWouldNotSplitNetwork() {
    this.net.connect(BlockPos.ORIGIN, TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN.north(), TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN.north().east(), TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN.north().west(), TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN.south(), TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN.south().east(), TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN.south().west(), TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN.east(), TileEntityWithCapabilities.transfer());
    this.net.connect(BlockPos.ORIGIN.west(), TileEntityWithCapabilities.transfer());

    Assertions.assertFalse(this.net.disconnect(BlockPos.ORIGIN));
    Assertions.assertEquals(8, this.net.size());
  }

  private void verifyPath(final List<BlockPos> path) {
    Assertions.assertTrue(this.net.getNode(path.get(0)).te.hasCapability(STORAGE, BlockPosUtils.getBlockFacing(path.get(0), path.get(1))), "Start node was not storage");
    Assertions.assertTrue(this.net.getNode(path.get(path.size() - 1)).te.hasCapability(STORAGE, BlockPosUtils.getBlockFacing(path.get(path.size() - 1), path.get(path.size() - 2))), "End node was not storage");

    for(int i = 0; i < path.size() - 1; i++) {
      Assertions.assertNotNull(BlockPosUtils.areBlocksAdjacent(path.get(i), path.get(i + 1)), "Positions were not adjacent");
    }

    for(int i = 1; i < path.size() - 1; i++) {
      Assertions.assertTrue(this.net.getNode(path.get(i)).te.hasCapability(TRANSFER, BlockPosUtils.getBlockFacing(path.get(i), path.get(i + 1))), "Intermediate node was not transfer");
      Assertions.assertTrue(this.net.getNode(path.get(i)).te.hasCapability(TRANSFER, BlockPosUtils.getBlockFacing(path.get(i), path.get(i - 1))), "Intermediate node was not transfer");
    }
  }

  static <T> Capability<T> newCap(final String name) throws RuntimeException {
    try {
      final Constructor<Capability> constructor = Capability.class.getDeclaredConstructor(String.class, Capability.IStorage.class, Callable.class);
      constructor.setAccessible(true);
      return constructor.newInstance(name, null, null);
    } catch(final Exception e) {
      throw new RuntimeException(e);
    }
  }
}
