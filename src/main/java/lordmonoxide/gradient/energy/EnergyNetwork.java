package lordmonoxide.gradient.energy;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.config.GradientConfig;
import lordmonoxide.gradient.utils.Tuple;
import lordmonoxide.gradient.utils.WorldUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLLog;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;

public class EnergyNetwork<STORAGE extends IEnergyStorage, TRANSFER extends IEnergyTransfer> {
  final Capability<STORAGE> storage;
  final Capability<TRANSFER> transfer;

  private final List<EnergyNetworkSegment<STORAGE, TRANSFER>> networks = new ArrayList<>();
  public final int dimension;
  private final IBlockAccess world;

  private final ConcurrentLinkedDeque<QueuedAction> nodeQueue = new ConcurrentLinkedDeque<>();

  private final Map<BlockPos, TileEntity> allNodes = new HashMap<>();

  private final EnergyNetworkState state = new EnergyNetworkState();

  public EnergyNetwork(final int dimension, final IBlockAccess world, final Capability<STORAGE> storage, final Capability<TRANSFER> transfer) {
    this.dimension = dimension;
    this.world = world;
    this.storage = storage;
    this.transfer = transfer;

    this.state.setCapabilities(storage, transfer);
  }

  private final Map<STORAGE, Tuple<BlockPos, EnumFacing>> tickSinkNodes = new HashMap<>();
  private final Set<TRANSFER> tickTransferNodes = new HashSet<>();

  public EnergyNetworkState tick() {
    QueuedAction queue;
    while((queue = this.nodeQueue.pollFirst()) != null) {
      switch(queue.action) {
        case CONNECT:
          this.connect(queue.pos, queue.te);
        break;

        case DISCONNECT:
          this.disconnect(queue.pos);
        break;
      }
    }

    if(GradientConfig.enet.enableTickDebug) {
      GradientMod.logger.info("Ticking {}", this);
    }

    this.state.reset();

    this.tickSinkNodes.clear();
    this.tickTransferNodes.clear();

    for(final TileEntity te : this.allNodes.values()) {
      if(GradientConfig.enet.enableTickDebug) {
        GradientMod.logger.info("Checking {}", te);
      }

      for(final EnumFacing facing : EnumFacing.VALUES) {
        if(te.hasCapability(this.storage, facing) && te.getCapability(this.storage, facing).canSink()) {
          this.tickSinkNodes.put(te.getCapability(this.storage, facing), new Tuple<>(te.getPos(), facing));
        } else if(te.hasCapability(this.transfer, facing)) {
          this.tickTransferNodes.add(te.getCapability(this.transfer, facing));
        }
      }
    }

    for(final TRANSFER transfer : this.tickTransferNodes) {
      if(GradientConfig.enet.enableTickDebug) {
        GradientMod.logger.info("Resetting transfer {}", transfer);
      }

      transfer.resetEnergyTransferred();
    }

    for(final Map.Entry<STORAGE, Tuple<BlockPos, EnumFacing>> entry : this.tickSinkNodes.entrySet()) {
      final STORAGE sink = entry.getKey();
      final BlockPos pos = entry.getValue().a;
      final EnumFacing facing = entry.getValue().b;

      if(GradientConfig.enet.enableTickDebug) {
        GradientMod.logger.info("Ticking sink {} @ {}", sink, pos);
      }

      final float requested = sink.getRequestedEnergy();

      if(GradientConfig.enet.enableTickDebug) {
        GradientMod.logger.info("{} requesting {} energy", sink, requested);
      }

      if(requested != 0.0f) {
        final float energy = this.requestEnergy(pos, requested);

        if(GradientConfig.enet.enableTickDebug) {
          GradientMod.logger.info("{} got {} energy", sink, energy);
        }

        if(energy != 0.0f) {
          sink.sinkEnergy(energy, false);
          this.state.add(pos, facing, sink.getEnergy());
        }
      }
    }

    return this.state;
  }

  public int size() {
    return this.networks.size();
  }

  public List<EnergyNetworkSegment<STORAGE, TRANSFER>> getNetworksForBlock(final BlockPos pos) {
    final List<EnergyNetworkSegment<STORAGE, TRANSFER>> networks = new ArrayList<>();

    for(final EnergyNetworkSegment<STORAGE, TRANSFER> network : this.networks) {
      if(network.contains(pos)) {
        networks.add(network);
      }
    }

    return networks;
  }

  private boolean inConnectMethod;

  public void queueConnection(final BlockPos newNodePos, final TileEntity newTe) {
    if(GradientConfig.enet.enableNodeDebug) {
      GradientMod.logger.info("Adding {} @ {} to connection queue", newTe, newNodePos);
    }

    this.nodeQueue.addLast(new QueuedAction(Action.CONNECT, newNodePos, newTe));
  }

  public void queueDisconnection(final BlockPos newNodePos) {
    if(GradientConfig.enet.enableNodeDebug) {
      GradientMod.logger.info("Adding {} to disconnection queue", newNodePos);
    }

    this.nodeQueue.addLast(new QueuedAction(Action.DISCONNECT, newNodePos));
  }

  public Map<EnumFacing, EnergyNetworkSegment<STORAGE, TRANSFER>> connect(final BlockPos newNodePos, final TileEntity newTe) {
    if(this.inConnectMethod) {
      throw new RuntimeException("Attempting to re-enter connect method! " + newTe + " @ " + newNodePos);
    }

    this.inConnectMethod = true;

    if(GradientConfig.enet.enableNodeDebug) {
      GradientMod.logger.info("Attempting to add {} @ {} to a network...", newTe, newNodePos);
    }

    final Map<EnumFacing, EnergyNetworkSegment<STORAGE, TRANSFER>> added = new HashMap<>();
    final Map<EnumFacing, EnergyNetworkSegment<STORAGE, TRANSFER>> merge = new HashMap<>();

    for(final EnumFacing facing : EnumFacing.VALUES) {
      final BlockPos networkPos = newNodePos.offset(facing);
      final TileEntity worldTe = this.world.getTileEntity(networkPos);

      // If there's no TE, there's no network
      if(worldTe == null) {
        continue;
      }

      if(GradientConfig.enet.enableNodeDebug) {
        GradientMod.logger.info("Found adjacent TE {} @ {} ({})", worldTe, networkPos, facing);
      }

      final EnumFacing opposite = facing.getOpposite();

      if(newTe.hasCapability(this.storage, facing)) {
        if(worldTe.hasCapability(this.storage, opposite)) {
          // New network if we can't connect to the storage's network (we can only connect if it's the only block in the network)
          if(GradientConfig.enet.enableNodeDebug) {
            GradientMod.logger.info("Both TEs are storages");
          }

          this.addToOrCreateNetwork(newNodePos, newTe, networkPos, worldTe, added);
        } else if(worldTe.hasCapability(this.transfer, opposite)) {
          // Add to network, no merge
          if(GradientConfig.enet.enableNodeDebug) {
            GradientMod.logger.info("New TE is storage, world TE is transfer");
          }

          this.addToOrCreateNetwork(newNodePos, newTe, networkPos, worldTe, added);
        } else {
          if(GradientConfig.enet.enableNodeDebug) {
            GradientMod.logger.info("Unconnectable");
          }
        }
      } else if(newTe.hasCapability(this.transfer, facing)) {
        if(worldTe.hasCapability(this.storage, opposite)) {
          // If worldTe is in its own network, add (may have to merge)
          // If worldTe is in an existing network, new (may have to merge)
          if(GradientConfig.enet.enableNodeDebug) {
            GradientMod.logger.info("New TE is transfer, world TE is storage");
          }

          final Map<EnumFacing, EnergyNetworkSegment<STORAGE, TRANSFER>> networks = new HashMap<>();
          this.addToOrCreateNetwork(newNodePos, newTe, networkPos, worldTe, networks);
          added.putAll(networks);
          merge.putAll(networks);
        } else if(worldTe.hasCapability(this.transfer, opposite)) {
          // Add to network (may have to merge)
          if(GradientConfig.enet.enableNodeDebug) {
            GradientMod.logger.info("Both TEs are transfers");
          }

          final Map<EnumFacing, EnergyNetworkSegment<STORAGE, TRANSFER>> networks = new HashMap<>();
          this.addToOrCreateNetwork(newNodePos, newTe, networkPos, worldTe, networks);
          added.putAll(networks);
          merge.putAll(networks);
        } else {
          if(GradientConfig.enet.enableNodeDebug) {
            GradientMod.logger.info("Unconnectable");
          }
        }
      }
    }

    while(merge.size() > 1) {
      if(GradientConfig.enet.enableNodeDebug) {
        GradientMod.logger.info("Merging networks");
      }

      final Iterator<Map.Entry<EnumFacing, EnergyNetworkSegment<STORAGE, TRANSFER>>> it = merge.entrySet().iterator();
      final Map.Entry<EnumFacing, EnergyNetworkSegment<STORAGE, TRANSFER>> firstEntry = it.next();
      final EnumFacing firstFacing = firstEntry.getKey();
      final EnergyNetworkSegment<STORAGE, TRANSFER> firstNetwork = firstEntry.getValue();
      it.remove();

      while(it.hasNext()) {
        final Map.Entry<EnumFacing, EnergyNetworkSegment<STORAGE, TRANSFER>> otherEntry = it.next();
        final EnumFacing otherFacing = otherEntry.getKey();
        final EnergyNetworkSegment<STORAGE, TRANSFER> otherNetwork = otherEntry.getValue();

        if(firstNetwork == otherNetwork) {
          if(GradientConfig.enet.enableNodeDebug) {
            GradientMod.logger.info("Skipping merge - same network");
          }

          it.remove();
          continue;
        }

        final EnergyNetworkSegment.EnergyNode firstNode = firstNetwork.getNode(newNodePos);
        final EnergyNetworkSegment.EnergyNode otherNode = otherNetwork.getNode(newNodePos);

        if(firstNode.te.getCapability(this.transfer, firstFacing) == otherNode.te.getCapability(this.transfer, otherFacing)) {
          this.networks.remove(otherNetwork);
          otherNetwork.invalidate();

          firstNetwork.merge(otherNetwork);
          added.put(otherFacing, firstNetwork);
          it.remove();

          if(GradientConfig.enet.enableNodeDebug) {
            GradientMod.logger.info("There are now {} networks", this.size());
          }
        }
      }
    }

    // There were no adjacent nodes, create a new network
    if(added.isEmpty()) {
      if(GradientConfig.enet.enableNodeDebug) {
        GradientMod.logger.info("No adjacent nodes, creating new network");
      }

      final EnergyNetworkSegment<STORAGE, TRANSFER> network = new EnergyNetworkSegment<>(this.storage, this.transfer);
      network.connect(newNodePos, newTe);
      this.networks.add(network);
      added.put(null, network);

      if(GradientConfig.enet.enableNodeDebug) {
        GradientMod.logger.info("There are now {} networks", this.size());
      }
    }

    this.allNodes.put(newNodePos, newTe);

    if(this.getNetworksForBlock(newNodePos).isEmpty()) {
      FMLLog.bigWarning("MISSING ADD {}", newNodePos);
    }

    if(GradientConfig.enet.enableNodeDebug) {
      GradientMod.logger.info("Finished adding {} @ {}", newTe, newNodePos);
    }

    this.inConnectMethod = false;

    return added;
  }

  private void addToOrCreateNetwork(final BlockPos newNodePos, final TileEntity newTe, final BlockPos networkPos, final TileEntity worldTe, final Map<EnumFacing, EnergyNetworkSegment<STORAGE, TRANSFER>> added) {
    if(GradientConfig.enet.enableNodeDebug) {
      GradientMod.logger.info("Trying to add new TE {} @ {} to existing networks at {} ({})", newTe, newNodePos, networkPos, this.getNetworksForBlock(networkPos));
    }

    boolean connected = false;
    for(final EnergyNetworkSegment<STORAGE, TRANSFER> network : this.getNetworksForBlock(networkPos)) {
      if(GradientConfig.enet.enableNodeDebug) {
        GradientMod.logger.info("Trying network {}...", network);
      }

      if(network.connect(newNodePos, newTe)) {
        if(GradientConfig.enet.enableNodeDebug) {
          GradientMod.logger.info("Success!");
        }

        added.put(WorldUtils.getBlockFacing(newNodePos, networkPos), network);
        connected = true;
      }
    }

    if(!connected) {
      // Create a new network if we couldn't connect
      if(GradientConfig.enet.enableNodeDebug) {
        GradientMod.logger.info("Failed to find a network to connect to, creating a new one");
      }

      final EnergyNetworkSegment<STORAGE, TRANSFER> network = new EnergyNetworkSegment<>(this.storage, this.transfer);
      this.allNodes.put(networkPos, worldTe);
      network.connect(networkPos, worldTe);
      network.connect(newNodePos, newTe);
      this.networks.add(network);

      if(this.getNetworksForBlock(networkPos).isEmpty()) {
        FMLLog.bigWarning("MISSING NETWORKPOS {}", networkPos);
      }

      if(this.getNetworksForBlock(newNodePos).isEmpty()) {
        FMLLog.bigWarning("MISSING NEWNODEPOS {}", newNodePos);
      }

      added.put(WorldUtils.getBlockFacing(newNodePos, networkPos), network);

      if(GradientConfig.enet.enableNodeDebug) {
        GradientMod.logger.info("There are now {} networks", this.size());
      }
    }
  }

  public void disconnect(final BlockPos pos) {
    if(GradientConfig.enet.enableNodeDebug) {
      GradientMod.logger.info("Removing node {}", pos);
    }

    this.allNodes.remove(pos);

    for(final EnergyNetworkSegment<STORAGE, TRANSFER> network : this.getNetworksForBlock(pos)) {
      // Do we need to rebuild the network?
      if(network.disconnect(pos)) {
        if(GradientConfig.enet.enableNodeDebug) {
          GradientMod.logger.info("Rebuilding network {}", network);
        }

        this.networks.remove(network);
        network.invalidate();

        for(final EnergyNetworkSegment.EnergyNode node : network.nodes.values()) {
          this.connect(node.pos, node.te);
        }
      }
    }
  }

  private final Map<EnergyNetworkSegment<STORAGE, TRANSFER>, EnumFacing> extractNetworks = new HashMap<>();

  public float requestEnergy(final BlockPos requestPosition, final float amount) {
    for(final EnergyNetworkSegment<STORAGE, TRANSFER> network : this.networks) {
      if(network.contains(requestPosition)) {
        final EnergyNetworkSegment.EnergyNode node = network.getNode(requestPosition);

        for(final EnumFacing side : EnumFacing.VALUES) {
          final EnergyNetworkSegment.EnergyNode connection = node.connection(side);

          if(connection != null) {
            final EnumFacing opposite = side.getOpposite();

            if(connection.te.hasCapability(this.storage, opposite)) {
              if(connection.te.getCapability(this.storage, opposite).canSource()) {
                this.extractNetworks.put(network, side);
                break;
              }
            } else if(connection.te.hasCapability(this.transfer, opposite)) {
              if(connection.te.getCapability(this.transfer, opposite).canSource()) {
                this.extractNetworks.put(network, side);
                break;
              }
            }
          }
        }
      }
    }

    if(this.extractNetworks.isEmpty()) {
      if(GradientConfig.enet.enableTickDebug) {
        GradientMod.logger.info("Failed to get energy from any network");
      }

      return 0.0f;
    }

    float share = amount / this.extractNetworks.size();
    float deficit = 0.0f;
    float total = 0.0f;

    while(total < amount) {
      for(final Iterator<Map.Entry<EnergyNetworkSegment<STORAGE, TRANSFER>, EnumFacing>> it = this.extractNetworks.entrySet().iterator(); it.hasNext(); ) {
        final Map.Entry<EnergyNetworkSegment<STORAGE, TRANSFER>, EnumFacing> entry = it.next();
        final EnergyNetworkSegment<STORAGE, TRANSFER> network = entry.getKey();
        final EnumFacing requestSide = entry.getValue();

        final float sourced = network.requestEnergy(requestPosition, requestSide, share);

        if(sourced < share) {
          deficit += share - sourced;
          it.remove();
        }

        total += sourced;
      }

      if(deficit == 0.0f || this.extractNetworks.isEmpty()) {
        break;
      }

      share = deficit / this.extractNetworks.size();
      deficit = 0.0f;
    }

    this.extractNetworks.clear();

    return total;
  }

  private enum Action {
    CONNECT, DISCONNECT
  }

  private static final class QueuedAction {
    private final Action action;
    private final BlockPos pos;
    @Nullable
    private final TileEntity te;

    private QueuedAction(final Action action, final BlockPos pos, final TileEntity te) {
      this.action = action;
      this.pos = pos;
      this.te = te;
    }

    private QueuedAction(final Action action, final BlockPos pos) {
      this.action = action;
      this.pos = pos;
      this.te = null;
    }
  }
}
