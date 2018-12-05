package lordmonoxide.gradient.energy;

import lordmonoxide.gradient.utils.BlockPosUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EnergyNetwork {
  @CapabilityInject(IEnergyStorage.class)
  public static Capability<IEnergyStorage> STORAGE;

  @CapabilityInject(IEnergyTransfer.class)
  public static Capability<IEnergyTransfer> TRANSFER;

  private final Map<BlockPos, EnergyNode> nodes = new HashMap<>();

  public boolean contains(final BlockPos pos) {
    return this.nodes.containsKey(pos);
  }

  public boolean connect(final BlockPos newNodePos, final TileEntity te) {
    if(this.nodes.isEmpty()) {
      this.nodes.put(newNodePos, new EnergyNode(newNodePos, te));
      return true;
    }

    if(this.contains(newNodePos)) {
      return false;
    }

    EnergyNode newNode = null;

    for(final Map.Entry<BlockPos, EnergyNode> entry : this.nodes.entrySet()) {
      final BlockPos nodePos = entry.getKey();
      final EnergyNode node = entry.getValue();

      if(BlockPosUtils.areBlocksAdjacent(newNodePos, nodePos)) {
        final EnumFacing facing = BlockPosUtils.getBlockFacing(newNodePos, nodePos);

        final IEnergyNode teNode;

        if(te.hasCapability(STORAGE, facing)) {
          // Storage nodes can't connect to other storage nodes
          if(node.te.hasCapability(STORAGE, facing.getOpposite())) {
            continue;
          }

          teNode = te.getCapability(STORAGE, facing);
        } else if(te.hasCapability(TRANSFER, facing)) {
          // Networks are split by storage nodes (a transfer node can connect to a storage node if it is the only node)
          if(node.te.hasCapability(STORAGE, facing.getOpposite()) && this.nodes.size() > 1) {
            continue;
          }

          teNode = te.getCapability(TRANSFER, facing);
        } else {
          continue;
        }

        if(!this.canConnect(teNode, node, facing.getOpposite())) {
          continue;
        }

        if(newNode == null) {
          newNode = new EnergyNode(newNodePos, te);
        }

        newNode.connections.put(facing, node);
        node.connections.put(facing.getOpposite(), newNode);
      }
    }

    if(newNode != null) {
      this.nodes.put(newNodePos, newNode);
      return true;
    }

    return false;
  }

  private boolean canConnect(final IEnergyNode node1, final EnergyNode node2, final EnumFacing facing) {
    final IEnergyNode n;

    if(node2.te.hasCapability(STORAGE, facing)) {
      n = node2.te.getCapability(STORAGE, facing);
    } else if(node2.te.hasCapability(TRANSFER, facing)) {
      n = node2.te.getCapability(TRANSFER, facing);
    } else {
      return false;
    }

    return node1.canSink() && n.canSource() || node1.canSource() && n.canSink();
  }

  public EnergyNode getNode(final BlockPos pos) {
    return this.nodes.get(pos);
  }

  public float getAvailableEnergy() {
    float available = 0.0f;

    for(final EnergyNode node : this.nodes.values()) {
      for(final Map.Entry<EnumFacing, EnergyNode> connection : node.connections.entrySet()) {
        if(node.te.hasCapability(STORAGE, connection.getKey())) {
          final IEnergyStorage storage = node.te.getCapability(STORAGE, connection.getKey());

          if(storage.canSource() && connection.getValue() != null) {
            available += storage.extractEnergy(storage.getEnergy(), true);
            break;
          }
        }
      }
    }

    return available;
  }

  private final List<IEnergyStorage> requestEnergySources = new ArrayList<>();

  public float extractEnergy(final float amount) {
    this.requestEnergySources.clear();

    // Find all of the energy sources
    // NOTE: the break prevents sources from getting added twice
    for(final EnergyNode node : this.nodes.values()) {
      for(final Map.Entry<EnumFacing, EnergyNode> connection : node.connections.entrySet()) {
        if(node.te.hasCapability(STORAGE, connection.getKey())) {
          final IEnergyStorage storage = node.te.getCapability(STORAGE, connection.getKey());

          if(storage.canSource() && connection.getValue() != null) {
            this.requestEnergySources.add(storage);
            break;
          }
        }
      }
    }

    if(this.requestEnergySources.isEmpty()) {
      return 0.0f;
    }

    float share = amount / this.requestEnergySources.size();
    float deficit = 0.0f;
    float total = 0.0f;

    while(total < amount) {
      for(final Iterator<IEnergyStorage> it = this.requestEnergySources.iterator(); it.hasNext(); ) {
        final IEnergyStorage source = it.next();

        final float sourced = source.extractEnergy(share, false);

        if(sourced < share) {
          deficit += share - sourced;
          it.remove();
        }

        total += sourced;
      }

      if(deficit == 0.0f || this.requestEnergySources.isEmpty()) {
        break;
      }

      share = deficit / this.requestEnergySources.size();
      deficit = 0.0f;
    }

    return total;
  }

  public static final class EnergyNode {
    public final BlockPos pos;
    public final TileEntity te;
    private final Map<EnumFacing, EnergyNode> connections = new EnumMap<>(EnumFacing.class);

    private EnergyNode(final BlockPos pos, final TileEntity te) {
      this.pos = pos;
      this.te  = te;
    }

    public EnergyNode connection(final EnumFacing side) {
      return this.connections.get(side);
    }
  }
}
