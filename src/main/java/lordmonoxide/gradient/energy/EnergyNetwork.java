package lordmonoxide.gradient.energy;

import lordmonoxide.gradient.utils.BlockPosUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.*;

public class EnergyNetwork {
  private final Map<BlockPos, EnergyNode> nodes = new HashMap<>();

  public boolean contains(final BlockPos pos) {
    return this.nodes.containsKey(pos);
  }

  public boolean connect(final BlockPos pos, final IEnergyNode energyNode) {
    if(this.nodes.isEmpty()) {
      this.nodes.put(pos, new EnergyNode(pos, energyNode));
      return true;
    }

    if(this.contains(pos)) {
      return false;
    }

    EnergyNode newNode = null;

    for(final Map.Entry<BlockPos, EnergyNode> entry : this.nodes.entrySet()) {
      final BlockPos nodePos = entry.getKey();
      final EnergyNode node = entry.getValue();

      if(BlockPosUtils.areBlocksAdjacent(pos, nodePos)) {
        // Networks are split by storage nodes (a transfer node can connect to a storage node if it is the only node)
        if(node.node instanceof IEnergyStorage && (energyNode instanceof IEnergyStorage || this.nodes.size() > 1)) {
          continue;
        }

        final EnumFacing facing = BlockPosUtils.getBlockFacing(pos, nodePos);

        if(!this.canConnect(energyNode, facing, node.node, facing.getOpposite())) {
          continue;
        }

        if(newNode == null) {
          newNode = new EnergyNode(pos, energyNode);
        }

        newNode.connections.put(facing, node);
        node.connections.put(facing.getOpposite(), newNode);
      }
    }

    if(newNode != null) {
      this.nodes.put(pos, newNode);
      return true;
    }

    return false;
  }

  private boolean canConnect(final IEnergyNode node1, final EnumFacing facing1, final IEnergyNode node2, final EnumFacing facing2) {
    return node1.canSink(facing1) && node2.canSource(facing2) || node1.canSource(facing1) && node2.canSink(facing2);
  }

  public EnergyNode getNode(final BlockPos pos) {
    return this.nodes.get(pos);
  }

  public float getAvailableEnergy() {
    float available = 0.0f;

    for(final EnergyNode node : this.nodes.values()) {
      if(!(node.node instanceof IEnergyStorage)) {
        continue;
      }

      final IEnergyStorage storage = (IEnergyStorage)node.node;

      for(final Map.Entry<EnumFacing, EnergyNode> connection : node.connections.entrySet()) {
        if(storage.canSource(connection.getKey()) && connection.getValue() != null) {
          available += storage.extractEnergy(storage.getEnergy(), true);
          break;
        }
      }
    }

    return available;
  }

  private final List<IEnergyStorage> requestEnergySources = new ArrayList<>();

  public float extractEnergy(final float amount) {
    this.requestEnergySources.clear();

    for(final EnergyNode node : this.nodes.values()) {
      if(node.node instanceof IEnergyStorage) {
        final IEnergyStorage storage = (IEnergyStorage)node.node;

        for(final Map.Entry<EnumFacing, EnergyNode> connection : node.connections.entrySet()) {
          if(storage.canSource(connection.getKey()) && connection.getValue() != null) {
            this.requestEnergySources.add((IEnergyStorage)node.node);
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
    public final IEnergyNode node;
    private final Map<EnumFacing, EnergyNode> connections = new EnumMap<>(EnumFacing.class);

    private EnergyNode(final BlockPos pos, final IEnergyNode node) {
      this.pos  = pos;
      this.node = node;
    }

    public EnergyNode connection(final EnumFacing side) {
      return this.connections.get(side);
    }
  }
}
