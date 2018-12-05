package lordmonoxide.gradient.energy;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.utils.BlockPosUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import java.util.ArrayList;
import java.util.Arrays;
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
    return this.connect(newNodePos, te, false);
  }

  private boolean connect(final BlockPos newNodePos, final TileEntity te, boolean force) {
    GradientMod.logger.info("Adding node {} to enet {} @ {}", te, this, newNodePos);

    if(this.nodes.isEmpty()) {
      GradientMod.logger.info("First node, adding");
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

        GradientMod.logger.info("New node is adjacent to {} on facing {}", node, facing);

        final IEnergyNode teNode;

        if(te.hasCapability(STORAGE, facing)) {
          // Storage nodes can't connect to other storage nodes
          if(!force && node.te.hasCapability(STORAGE, facing.getOpposite())) {
            GradientMod.logger.info("Adjacent node is storage - moving on");
            continue;
          }

          teNode = te.getCapability(STORAGE, facing);
        } else if(te.hasCapability(TRANSFER, facing)) {
          // Networks are split by storage nodes (a transfer node can connect to a storage node if it is the only node)
          if(!force && node.te.hasCapability(STORAGE, facing.getOpposite()) && this.nodes.size() > 1) {
            GradientMod.logger.info("Adjacent node is storage - moving on");
            continue;
          }

          teNode = te.getCapability(TRANSFER, facing);
        } else {
          continue;
        }

        final boolean canConnect = this.canConnect(teNode, node, facing.getOpposite());

        if(!force && !canConnect) {
          GradientMod.logger.info("Adjacent node is not connectable");
          continue;
        }

        GradientMod.logger.info("Connecting!");

        if(newNode == null) {
          newNode = new EnergyNode(newNodePos, te);
        }

        if(canConnect) {
          newNode.connections.put(facing, node);
          node.connections.put(facing.getOpposite(), newNode);
        }
      }
    }

    if(force && newNode == null) {
      GradientMod.logger.info("No connections possible but force is true, connecting anyway");
      newNode = new EnergyNode(newNodePos, te);
    }

    if(newNode != null) {
      this.nodes.put(newNodePos, newNode);
      return true;
    }

    return false;
  }

  private boolean canConnect(final IEnergyNode newNode, final EnergyNode existingNode, final EnumFacing facing) {
    final IEnergyNode existing;

    if(existingNode.te.hasCapability(STORAGE, facing)) {
      existing = existingNode.te.getCapability(STORAGE, facing);
    } else if(existingNode.te.hasCapability(TRANSFER, facing)) {
      existing = existingNode.te.getCapability(TRANSFER, facing);
    } else {
      return false;
    }

    return newNode.canSink() && existing.canSource() || newNode.canSource() && existing.canSink();
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

  @Override
  public String toString() {
    return super.toString() + " (" + this.nodes.size() + " nodes)";
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

    @Override
    public String toString() {
      return "Node holder {" + this.te + "} @ " + this.pos + " connections {" + String.join(", ", this.connections.keySet().stream().map(EnumFacing::toString).toArray(String[]::new)) + '}';
    }
  }

  static EnergyNetwork merge(final EnergyNetwork... networks) {
    GradientMod.logger.info("Merging networks {}", Arrays.toString(networks));

    final EnergyNetwork newNet = new EnergyNetwork();

    for(final EnergyNetwork net : networks) {
      for(final Map.Entry<BlockPos, EnergyNode> node : net.nodes.entrySet()) {
        newNet.connect(node.getKey(), node.getValue().te, true);
      }
    }

    return newNet;
  }
}
