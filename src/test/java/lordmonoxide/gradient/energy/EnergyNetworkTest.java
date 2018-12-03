package lordmonoxide.gradient.energy;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.annotation.Nullable;

@DisplayName("Energy Network")
class EnergyNetworkTest {
  @Test
  void testContainsEmptyNetwork() {
    final EnergyNetwork net = new EnergyNetwork();

    final boolean contains = net.contains(BlockPos.ORIGIN);

    Assertions.assertFalse(contains, "Contains didn't return false for empty net");
  }

  @Test
  void testAddingTransferNodeToEmptyNetwork() {
    final EnergyNetwork net = new EnergyNetwork();

    final BlockPos pos = BlockPos.ORIGIN;
    final boolean success = net.connect(pos, new TransferNode());

    Assertions.assertTrue(success, "Failed to add node to empty net");
    Assertions.assertTrue(net.contains(pos), "Net doesn't contain new node");
  }

  @Test
  void testAddingStorageNodeToEmptyNetwork() {
    final EnergyNetwork net = new EnergyNetwork();

    final BlockPos pos = BlockPos.ORIGIN;
    final boolean success = net.connect(pos, new StorageNode());

    Assertions.assertTrue(success, "Failed to add node to empty net");
    Assertions.assertTrue(net.contains(pos), "Net doesn't contain new node");
  }

  @Test
  void testNodeLinkage() {
    final EnergyNetwork net = new EnergyNetwork();

    final BlockPos pos1 = BlockPos.ORIGIN;
    final BlockPos pos2 = pos1.east();
    final BlockPos pos3 = pos2.south();
    final BlockPos pos4 = pos3.west();
    final BlockPos pos5 = pos1.up();
    final BlockPos pos6 = pos1.down();

    Assertions.assertTrue(net.connect(pos1, new TransferNode()), "Failed to connect pos1");
    final EnergyNetwork.EnergyNode energyNode1 = net.getNode(pos1);
    Assertions.assertTrue(this.checkNode(energyNode1, pos1, null, null, null, null, null, null), "Node 1 did not match expected");

    Assertions.assertTrue(net.connect(pos2, new TransferNode()), "Failed to connect pos2");
    final EnergyNetwork.EnergyNode energyNode2 = net.getNode(pos2);
    Assertions.assertTrue(this.checkNode(energyNode2, pos2, null, null, null, energyNode1, null, null), "Node 2 did not match expected");
    Assertions.assertTrue(this.checkNode(energyNode1, pos1, null, null, energyNode2, null, null, null), "Node 1 did not match expected");

    Assertions.assertTrue(net.connect(pos3, new TransferNode()), "Failed to connect pos3");
    final EnergyNetwork.EnergyNode energyNode3 = net.getNode(pos3);
    Assertions.assertTrue(this.checkNode(energyNode3, pos3, energyNode2, null, null, null, null, null), "Node 3 did not match expected");
    Assertions.assertTrue(this.checkNode(energyNode2, pos2, null, energyNode3, null, energyNode1, null, null), "Node 2 did not match expected");

    Assertions.assertTrue(net.connect(pos4, new TransferNode()), "Failed to connect pos4");
    final EnergyNetwork.EnergyNode energyNode4 = net.getNode(pos4);
    Assertions.assertTrue(this.checkNode(energyNode4, pos4, energyNode1, null, energyNode3, null, null, null), "Node 4 did not match expected");
    Assertions.assertTrue(this.checkNode(energyNode3, pos3, energyNode2, null, null, energyNode4, null, null), "Node 3 did not match expected");
    Assertions.assertTrue(this.checkNode(energyNode1, pos1, null, energyNode4, energyNode2, null, null, null), "Node 1 did not match expected");

    Assertions.assertTrue(net.connect(pos5, new TransferNode()), "Failed to connect pos5");
    final EnergyNetwork.EnergyNode energyNode5 = net.getNode(pos5);
    Assertions.assertTrue(this.checkNode(energyNode5, pos5, null, null, null, null, null, energyNode1), "Node 5 did not match expected");
    Assertions.assertTrue(this.checkNode(energyNode1, pos1, null, energyNode4, energyNode2, null, energyNode5, null), "Node 1 did not match expected");

    Assertions.assertTrue(net.connect(pos6, new TransferNode()), "Failed to connect pos6");
    final EnergyNetwork.EnergyNode energyNode6 = net.getNode(pos6);
    Assertions.assertTrue(this.checkNode(energyNode6, pos6, null, null, null, null, energyNode1, null), "Node 6 did not match expected");
    Assertions.assertTrue(this.checkNode(energyNode1, pos1, null, energyNode4, energyNode2, null, energyNode5, energyNode6), "Node 1 did not match expected");
  }

  private boolean checkNode(final EnergyNetwork.EnergyNode node, final BlockPos pos, @Nullable final EnergyNetwork.EnergyNode north, @Nullable final EnergyNetwork.EnergyNode south, @Nullable final EnergyNetwork.EnergyNode east, @Nullable final EnergyNetwork.EnergyNode west, @Nullable final EnergyNetwork.EnergyNode up, @Nullable final EnergyNetwork.EnergyNode down) {
    return node.pos == pos && node.connection(EnumFacing.NORTH) == north && node.connection(EnumFacing.SOUTH) == south && node.connection(EnumFacing.EAST) == east && node.connection(EnumFacing.WEST) == west && node.connection(EnumFacing.UP) == up && node.connection(EnumFacing.DOWN) == down;
  }

  @Test
  void testInvalidConnection() {
    final EnergyNetwork net = new EnergyNetwork();

    net.connect(BlockPos.ORIGIN, new TransferNode());

    Assertions.assertFalse(net.connect(new BlockPos(100.0f, 100.0f, 100.0f), new TransferNode()), "Invalid connection didn't return false");
  }

  @Test
  void testDuplicateConnection() {
    final EnergyNetwork net = new EnergyNetwork();

    net.connect(BlockPos.ORIGIN, new TransferNode());

    Assertions.assertFalse(net.connect(BlockPos.ORIGIN, new TransferNode()), "Duplicate connection didn't return false");
  }
}
