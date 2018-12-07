package lordmonoxide.gradient.energy;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.utils.BlockPosUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EnergyNetwork {
  @CapabilityInject(IEnergyStorage.class)
  public static Capability<IEnergyStorage> STORAGE;

  @CapabilityInject(IEnergyTransfer.class)
  public static Capability<IEnergyTransfer> TRANSFER;

  private final Map<BlockPos, EnergyNode> nodes = new HashMap<>();

  public int size() {
    return this.nodes.size();
  }

  public boolean contains(final BlockPos pos) {
    return this.nodes.containsKey(pos);
  }

  public boolean connect(final BlockPos newNodePos, final TileEntity te) {
    return this.connect(newNodePos, te, false);
  }

  private boolean connect(final BlockPos newNodePos, final TileEntity te, final boolean force) {
    GradientMod.logger.info("Adding node {} to enet {} @ {}", te, this, newNodePos);

    // First node is always accepted
    if(this.nodes.isEmpty()) {
      GradientMod.logger.info("First node, adding");
      this.nodes.put(newNodePos, new EnergyNode(newNodePos, te));
      return true;
    }

    // If we have a node here already, check to see if it's the same one
    if(this.contains(newNodePos)) {
      return this.getNode(newNodePos).te == te;
    }

    EnergyNode newNode = null;

    Map<EnumFacing, EnergyNode> conditionalConnections = null;

    for(final Map.Entry<BlockPos, EnergyNode> entry : this.nodes.entrySet()) {
      final BlockPos nodePos = entry.getKey();
      final EnergyNode node = entry.getValue();

      final EnumFacing facing = BlockPosUtils.areBlocksAdjacent(newNodePos, nodePos);

      if(facing != null) {
        GradientMod.logger.info("New node is adjacent to {} on facing {}", node, facing);

        final IEnergyNode teNode;

        if(te.hasCapability(STORAGE, facing)) {
          // Storage nodes can't connect to other storage nodes unless it's the only one
          if(!force && node.te.hasCapability(STORAGE, facing.getOpposite()) && this.nodes.size() > 1) {
            GradientMod.logger.info("Adjacent node is storage - moving on");
            continue;
          }

          teNode = te.getCapability(STORAGE, facing);
        } else if(te.hasCapability(TRANSFER, facing)) {
          // Networks are split by storage nodes (a transfer node can connect to a storage node if it is the only node)
          // Transfer nodes can also connect to storage nodes if the transfer node will be connecting to another transfer node
          if(!force && node.te.hasCapability(STORAGE, facing.getOpposite()) && this.nodes.size() > 1) {
            GradientMod.logger.info("Adjacent node is storage - deferring");

            if(conditionalConnections == null) {
              conditionalConnections = new EnumMap<>(EnumFacing.class);
            }

            conditionalConnections.put(facing, node);
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

    // If we made a connection, attempt to link up any deferred storages
    if(newNode != null && conditionalConnections != null) {
      for(final Map.Entry<EnumFacing, EnergyNode> entry : conditionalConnections.entrySet()) {
        final EnumFacing facing = entry.getKey();
        final EnergyNode node = entry.getValue();

        GradientMod.logger.info("Checking deferred connection {}", facing);

        if(!this.canConnect(te.getCapability(TRANSFER, facing), node, facing.getOpposite())) {
          GradientMod.logger.info("Adjacent node is not connectable");
          continue;
        }

        GradientMod.logger.info("Connecting!");

        newNode.connections.put(facing, node);
        node.connections.put(facing.getOpposite(), newNode);
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

  private final Set<IEnergyStorage> availableEnergySources = new HashSet<>();

  public float getAvailableEnergy() {
    float available = 0.0f;

    for(final EnergyNode node : this.nodes.values()) {
      for(final Map.Entry<EnumFacing, EnergyNode> connection : node.connections.entrySet()) {
        if(node.te.hasCapability(STORAGE, connection.getKey())) {
          final IEnergyStorage storage = node.te.getCapability(STORAGE, connection.getKey());

          if(storage.canSource() && connection.getValue() != null && !this.availableEnergySources.contains(storage)) {
            this.availableEnergySources.add(storage);
            available += storage.extractEnergy(storage.getEnergy(), true);
          }
        }
      }
    }

    this.availableEnergySources.clear();

    return available;
  }

  private final Set<IEnergyStorage> extractEnergySources = new HashSet<>();

  public float extractEnergy(final float amount) {
    // Find all of the energy sources
    for(final EnergyNode node : this.nodes.values()) {
      for(final Map.Entry<EnumFacing, EnergyNode> connection : node.connections.entrySet()) {
        if(node.te.hasCapability(STORAGE, connection.getKey())) {
          final IEnergyStorage storage = node.te.getCapability(STORAGE, connection.getKey());

          if(storage.canSource() && connection.getValue() != null && !this.extractEnergySources.contains(storage)) {
            this.extractEnergySources.add(storage);
          }
        }
      }
    }

    if(this.extractEnergySources.isEmpty()) {
      return 0.0f;
    }

    float share = amount / this.extractEnergySources.size();
    float deficit = 0.0f;
    float total = 0.0f;

    while(total < amount) {
      for(final Iterator<IEnergyStorage> it = this.extractEnergySources.iterator(); it.hasNext(); ) {
        final IEnergyStorage source = it.next();

        final float sourced = source.extractEnergy(share, false);

        if(sourced < share) {
          deficit += share - sourced;
          it.remove();
        }

        total += sourced;
      }

      if(deficit == 0.0f || this.extractEnergySources.isEmpty()) {
        break;
      }

      share = deficit / this.extractEnergySources.size();
      deficit = 0.0f;
    }

    this.extractEnergySources.clear();

    return total;
  }

  public List<BlockPos> pathFind(final BlockPos start, final BlockPos goal) {
    final Set<BlockPos> closed = new HashSet<>();
    final Set<BlockPos> open = new HashSet<>();

    final Map<BlockPos, BlockPos> cameFrom = new HashMap<>();
    final Object2IntMap<BlockPos> gScore = new Object2IntOpenHashMap<>();
    final Object2IntMap<BlockPos> fScore = new Object2IntLinkedOpenHashMap<>();

    gScore.defaultReturnValue(Integer.MAX_VALUE);
    fScore.defaultReturnValue(Integer.MAX_VALUE);

    open.add(start);
    gScore.put(start, 0);
    fScore.put(start, this.pathFindHeuristic(start, goal));

    while(!open.isEmpty()) {
      final BlockPos current = this.getLowest(fScore);

      GradientMod.logger.info("Current = " + current);

      if(current.equals(goal)) {
        GradientMod.logger.info("GOAL!");
        return this.reconstructPath(cameFrom, goal);
      }

      open.remove(current);
      fScore.removeInt(current);
      closed.add(current);

      final EnergyNode currentNode = this.getNode(current);

      for(final EnumFacing side : EnumFacing.VALUES) {
        GradientMod.logger.info("Checking side " + side);

        final EnergyNode neighbourNode = currentNode.connection(side);

        if(neighbourNode == null) {
          GradientMod.logger.info("No node, skipping");
          continue;
        }

        final BlockPos neighbour = current.offset(side);

        GradientMod.logger.info("Found " + neighbour);

        if(!neighbourNode.te.hasCapability(TRANSFER, side.getOpposite()) && !neighbour.equals(goal)) {
          GradientMod.logger.info("Not a transfer node, skipping");
          continue;
        }

        if(closed.contains(neighbour)) {
          GradientMod.logger.info("Already visited, skipping");
          continue;
        }

        final int g = gScore.get(current) + 1; // 1 = distance

        GradientMod.logger.info("New G = " + g + ", current G = " + gScore.getInt(neighbour));

        if(g >= gScore.getInt(neighbour)) {
          GradientMod.logger.info("G >= neighbour");
          continue;
        }

        open.add(neighbour);
        cameFrom.put(neighbour, current);
        gScore.put(neighbour, g);
        fScore.put(neighbour, g + this.pathFindHeuristic(neighbour, goal));

        GradientMod.logger.info("Adding node " + neighbour + " G = " + gScore.getInt(neighbour) + " F = " + fScore.getInt(neighbour));
      }
    }

    GradientMod.logger.info("Pathfinding failed");

    return new ArrayList<>();
  }

  private List<BlockPos> reconstructPath(final Map<BlockPos, BlockPos> cameFrom, final BlockPos goal) {
    final List<BlockPos> path = new ArrayList<>();
    path.add(goal);

    BlockPos current = goal;

    while(cameFrom.containsKey(current)) {
      current = cameFrom.get(current);
      path.add(current);
    }

    return path;
  }

  private int pathFindHeuristic(final BlockPos current, final BlockPos goal) {
    return (int)current.distanceSq(goal);
  }

  private BlockPos getLowest(final Object2IntMap<BlockPos> values) {
    int lowest = Integer.MAX_VALUE;
    BlockPos pos = null;

    for(final Object2IntMap.Entry<BlockPos> entry : values.object2IntEntrySet()) {
      if(entry.getIntValue() <= lowest) {
        lowest = entry.getIntValue();
        pos = entry.getKey();
      }
    }

    return pos;
  }

  @Override
  public String toString() {
    return super.toString() + " (" + this.nodes.size() + " nodes)";
  }

  public static final class EnergyNode {
    public final BlockPos pos;
    public final TileEntity te;
    private final Map<EnumFacing, EnergyNode> connections = new EnumMap<>(EnumFacing.class);

    private EnergyNode(final BlockPos pos, final TileEntity te) {
      this.pos = pos;
      this.te  = te;
    }

    @Nullable
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
