package lordmonoxide.gradient.energy;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.utils.BlockPosUtils;
import lordmonoxide.gradient.utils.Tuple;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nullable;
import java.util.ArrayList;
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

  final Map<BlockPos, EnergyNode> nodes = new HashMap<>();

  public int size() {
    return this.nodes.size();
  }

  public boolean isEmpty() {
    return this.nodes.isEmpty();
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

  /**
   * @return true if this network needs to be rebuild or deleted (i.e. empty) by the manager
   */
  public boolean disconnect(final BlockPos pos) {
    if(!this.nodes.containsKey(pos)) {
      return false;
    }

    final EnergyNode node = this.getNode(pos);
    this.nodes.remove(pos);

    if(node.connections.isEmpty()) {
      return true;
    }

    // Remove the neighbour's connection to this node
    for(final Map.Entry<EnumFacing, EnergyNode> connection : node.connections.entrySet()) {
      connection.getValue().connections.remove(connection.getKey().getOpposite());
    }

    final EnergyNode firstNeighbour = node.connections.values().iterator().next();

    // See if we can still access the other nodes
    connectionLoop:
    for(final Map.Entry<EnumFacing, EnergyNode> connection : node.connections.entrySet()) {
      if(connection.getValue() != firstNeighbour) {
        for(final EnumFacing startFacing : EnumFacing.VALUES) {
          if(!firstNeighbour.connections.containsKey(startFacing)) {
            continue;
          }

          for(final EnumFacing goalFacing : EnumFacing.VALUES) {
            if(!connection.getValue().connections.containsKey(goalFacing)) {
              continue;
            }

            // If we path successfully, it's connected
            if(!this.pathFind(firstNeighbour.pos, startFacing, connection.getValue().pos, goalFacing).isEmpty()) {
              // Continue on to the next connection
              continue connectionLoop;
            }
          }
        }

        return true;
      }
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
            available += storage.sourceEnergy(storage.getEnergy(), true);
          }
        }
      }
    }

    this.availableEnergySources.clear();

    return available;
  }

  private final Map<IEnergyStorage, List<BlockPos>> extractEnergySources = new HashMap<>();

  public float requestEnergy(final BlockPos sink, final EnumFacing sinkSide, final float amount) {
    // Find all of the energy sources
    for(final EnergyNode node : this.nodes.values()) {
      for(final Map.Entry<EnumFacing, EnergyNode> connection : node.connections.entrySet()) {
        if(node.te.hasCapability(STORAGE, connection.getKey())) {
          final IEnergyStorage storage = node.te.getCapability(STORAGE, connection.getKey());

          if(storage.canSource() && connection.getValue() != null && !this.extractEnergySources.containsKey(storage)) {
            final List<BlockPos> path = this.pathFind(sink, sinkSide, node.pos, connection.getKey());
            this.extractEnergySources.put(storage, path);
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
      for(final Iterator<Map.Entry<IEnergyStorage, List<BlockPos>>> it = this.extractEnergySources.entrySet().iterator(); it.hasNext(); ) {
        final Map.Entry<IEnergyStorage, List<BlockPos>> entry = it.next();

        final IEnergyStorage source = entry.getKey();
        final List<BlockPos> path = entry.getValue();

        final float sourced = source.sourceEnergy(share, false);

        if(sourced < share) {
          deficit += share - sourced;
          it.remove();
        }

        for(int i = 1; i < path.size() - 1; i++) {
          final BlockPos pathPos = path.get(i);
          final EnumFacing facingFrom = BlockPosUtils.getBlockFacing(pathPos, path.get(i - 1));
          final EnumFacing facingTo = BlockPosUtils.getBlockFacing(pathPos, path.get(i + 1));
          final TileEntity transferEntity = this.getNode(pathPos).te;

          if(transferEntity.hasCapability(TRANSFER, facingFrom)) {
            final IEnergyTransfer transfer = transferEntity.getCapability(TRANSFER, facingFrom);
            GradientMod.logger.info("Routing {} through {}", sourced, pathPos);
            transfer.transfer(sourced, facingFrom, facingTo);
          }
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

  private final Set<Tuple<BlockPos, EnumFacing>> closed = new HashSet<>();
  private final Set<Tuple<BlockPos, EnumFacing>> open = new HashSet<>();

  private final Map<Tuple<BlockPos, EnumFacing>, Tuple<BlockPos, EnumFacing>> cameFrom = new HashMap<>();
  private final Object2IntMap<Tuple<BlockPos, EnumFacing>> gScore = new Object2IntOpenHashMap<>();
  private final Object2IntMap<Tuple<BlockPos, EnumFacing>> fScore = new Object2IntLinkedOpenHashMap<>();

  public List<BlockPos> pathFind(final BlockPos start, final EnumFacing startFacing, final BlockPos goal, final EnumFacing goalFacing) {
    this.closed.clear();
    this.open.clear();
    this.cameFrom.clear();
    this.gScore.clear();
    this.fScore.clear();

    this.gScore.defaultReturnValue(Integer.MAX_VALUE);
    this.fScore.defaultReturnValue(Integer.MAX_VALUE);

    final Tuple<BlockPos, EnumFacing> startTuple = new Tuple<>(start, startFacing);
    final Tuple<BlockPos, EnumFacing> goalTuple = new Tuple<>(goal, goalFacing);

    GradientMod.logger.info("Starting pathfind at {} {}, goal {} {}", start, startFacing, goal, goalFacing);

    this.closed.add(startTuple);
    this.gScore.put(startTuple, 0);
    this.pathFindSide(startFacing, this.getNode(start), startTuple, goalTuple);

    while(!this.open.isEmpty()) {
      final Tuple<BlockPos, EnumFacing> current = this.getLowest(this.fScore);

      GradientMod.logger.info("Current = {} {}", current.a, current.b);

      if(current.equals(goalTuple)) {
        GradientMod.logger.info("GOAL!");
        return this.reconstructPath(this.cameFrom, goalTuple);
      }

      this.open.remove(current);
      this.fScore.removeInt(current);
      this.closed.add(current);

      final EnergyNode currentNode = this.getNode(current.a);

      for(final EnumFacing side : EnumFacing.VALUES) {
        this.pathFindSide(side, currentNode, current, goalTuple);
      }
    }

    GradientMod.logger.info("Pathfinding failed");

    return new ArrayList<>();
  }

  private void pathFindSide(final EnumFacing side, final EnergyNode currentNode, final Tuple<BlockPos, EnumFacing> currentTuple, final Tuple<BlockPos, EnumFacing> goalTuple) {
    GradientMod.logger.info("Checking side {}, came from {} {}", side, currentTuple.a, currentTuple.b);

    final EnergyNode neighbourNode = currentNode.connection(side);

    if(neighbourNode == null) {
      GradientMod.logger.info("No node, skipping");
      return;
    }

    final BlockPos neighbour = currentTuple.a.offset(side);

    GradientMod.logger.info("Found {}", neighbour);

    final EnumFacing opposite = side.getOpposite();
    final Tuple<BlockPos, EnumFacing> neighbourTuple = new Tuple<>(neighbour, opposite);

    if(!neighbourNode.te.hasCapability(TRANSFER, opposite) && !neighbourTuple.equals(goalTuple)) {
      GradientMod.logger.info("Not a transfer node, skipping");
      return;
    }

    if(this.closed.contains(neighbourTuple)) {
      GradientMod.logger.info("Already visited, skipping");
      return;
    }

    // Make sure the side we're trying to leave from is the same transfer node that we entered from
    if(currentNode.te.getCapability(TRANSFER, currentTuple.b) != currentNode.te.getCapability(TRANSFER, side)) {
      GradientMod.logger.info("Sides have different transfer nodes, skipping");
      return;
    }

    final int g = this.gScore.getInt(currentTuple) + 1; // 1 = distance

    GradientMod.logger.info("New G = {}, current G = {}", g, this.gScore.getInt(neighbourTuple));

    if(g >= this.gScore.getInt(neighbourTuple)) {
      GradientMod.logger.info("G >= neighbour");
      return;
    }

    this.open.add(neighbourTuple);
    this.closed.add(new Tuple<>(currentTuple.a, side));
    this.cameFrom.put(neighbourTuple, currentTuple);
    this.gScore.put(neighbourTuple, g);
    this.fScore.put(neighbourTuple, g + this.pathFindHeuristic(neighbour, goalTuple.a));

    GradientMod.logger.info("Adding node {} G = {} F = {}", neighbour, this.gScore.getInt(neighbourTuple), this.fScore.getInt(neighbourTuple));
  }

  private List<BlockPos> reconstructPath(final Map<Tuple<BlockPos, EnumFacing>, Tuple<BlockPos, EnumFacing>> cameFrom, final Tuple<BlockPos, EnumFacing> goal) {
    GradientMod.logger.info("Path:");

    final List<BlockPos> path = new ArrayList<>();
    path.add(goal.a);
    GradientMod.logger.info("{} {}", goal.a, goal.b);

    Tuple<BlockPos, EnumFacing> current = goal;

    while(cameFrom.containsKey(current)) {
      current = cameFrom.get(current);
      path.add(current.a);
      GradientMod.logger.info("{} {}", current.a, current.b);
    }

    return path;
  }

  private int pathFindHeuristic(final BlockPos current, final BlockPos goal) {
    return (int)current.distanceSq(goal);
  }

  private Tuple<BlockPos, EnumFacing> getLowest(final Object2IntMap<Tuple<BlockPos, EnumFacing>> values) {
    int lowest = Integer.MAX_VALUE;
    Tuple<BlockPos, EnumFacing> pos = null;

    for(final Object2IntMap.Entry<Tuple<BlockPos, EnumFacing>> entry : values.object2IntEntrySet()) {
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

  void merge(final EnergyNetwork other) {
    if(this == other) {
      GradientMod.logger.info("Skipping merge - same network");
      return;
    }

    GradientMod.logger.info("Merging networks {}, {}", this, other);

    for(final Map.Entry<BlockPos, EnergyNode> node : other.nodes.entrySet()) {
      this.connect(node.getKey(), node.getValue().te, true);
    }
  }
}
