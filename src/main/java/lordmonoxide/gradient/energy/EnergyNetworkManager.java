package lordmonoxide.gradient.energy;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.utils.BlockPosUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EnergyNetworkManager {
  @CapabilityInject(IEnergyStorage.class)
  public static Capability<IEnergyStorage> STORAGE;

  @CapabilityInject(IEnergyTransfer.class)
  public static Capability<IEnergyTransfer> TRANSFER;

  private final List<EnergyNetwork> networks = new ArrayList<>();
  private final IBlockAccess world;

  private final Map<BlockPos, TileEntity> allNodes = new HashMap<>();

  public EnergyNetworkManager(final IBlockAccess world) {
    this.world = world;
  }

  private final Map<IEnergyStorage, BlockPos> tickStorageNodes = new HashMap<>();
  private final Set<IEnergyTransfer> tickTransferNodes = new HashSet<>();

  public void tick() {
    GradientMod.logger.info("Ticking {}", this);

    this.tickStorageNodes.clear();
    this.tickTransferNodes.clear();

    for(final TileEntity te : this.allNodes.values()) {
      GradientMod.logger.info("Checking {}", te);
      for(final EnumFacing facing : EnumFacing.VALUES) {
        if(te.hasCapability(STORAGE, facing)) {
          this.tickStorageNodes.put(te.getCapability(STORAGE, facing), te.getPos());
        } else if(te.hasCapability(TRANSFER, facing)) {
          this.tickTransferNodes.add(te.getCapability(TRANSFER, facing));
        }
      }
    }

    for(final IEnergyTransfer transfer : this.tickTransferNodes) {
      GradientMod.logger.info("Resetting transfer {}", transfer);
      transfer.resetEnergyTransferred();
    }

    for(final Map.Entry<IEnergyStorage, BlockPos> entry : this.tickStorageNodes.entrySet()) {
      final IEnergyStorage storage = entry.getKey();
      final BlockPos pos = entry.getValue();

      GradientMod.logger.info("Ticking storage {} @ {}", storage, pos);

      final float requested = storage.getRequestedEnergy();

      if(requested != 0.0f) {
        final float energy = this.requestEnergy(pos, requested);
        storage.sinkEnergy(energy, false);
      }
    }
  }

  public int size() {
    return this.networks.size();
  }

  public List<EnergyNetwork> getNetworksForBlock(final BlockPos pos) {
    final List<EnergyNetwork> networks = new ArrayList<>();

    for(final EnergyNetwork network : this.networks) {
      if(network.contains(pos)) {
        networks.add(network);
      }
    }

    return networks;
  }

  public Collection<EnergyNetwork> connect(final BlockPos newNodePos, final TileEntity newTe) {
    GradientMod.logger.info("Attempting to add {} @ {} to a network...", newTe, newNodePos);

    final Map<EnumFacing, EnergyNetwork> added = new EnumMap<>(EnumFacing.class);
    final Map<EnumFacing, EnergyNetwork> merge = new EnumMap<>(EnumFacing.class);

    for(final EnumFacing facing : EnumFacing.VALUES) {
      final BlockPos networkPos = newNodePos.offset(facing);
      final TileEntity worldTe = this.world.getTileEntity(networkPos);

      // If there's no TE, there's no network
      if(worldTe == null) {
        continue;
      }

      GradientMod.logger.info("Found TE {} @ {} ({})", worldTe, networkPos, facing);

      if(newTe.hasCapability(STORAGE, facing)) {
        if(worldTe.hasCapability(STORAGE, facing)) {
          // New network if we can't connect to the storage's network (we can only connect if it's the only block in the network)
          GradientMod.logger.info("Both TEs are storages");
          this.addToOrCreateNetwork(newNodePos, newTe, networkPos, worldTe, added);
        } else if(worldTe.hasCapability(TRANSFER, facing)) {
          // Add to network, no merge
          GradientMod.logger.info("New TE is storage, world TE is transfer");
          this.addToOrCreateNetwork(newNodePos, newTe, networkPos, worldTe, added);
        }
      } else if(newTe.hasCapability(TRANSFER, facing)) {
        if(worldTe.hasCapability(STORAGE, facing)) {
          // If worldTe is in its own network, add (may have to merge)
          // If worldTe is in an existing network, new (may have to merge)
          GradientMod.logger.info("New TE is transfer, world TE is storage");
          final Map<EnumFacing, EnergyNetwork> networks = new EnumMap<>(EnumFacing.class);
          this.addToOrCreateNetwork(newNodePos, newTe, networkPos, worldTe, networks);
          added.putAll(networks);
          merge.putAll(networks);
        } else if(worldTe.hasCapability(TRANSFER, facing)) {
          // Add to network (may have to merge)
          GradientMod.logger.info("Both TEs are transfers");
          final Map<EnumFacing, EnergyNetwork> networks = new EnumMap<>(EnumFacing.class);
          this.addToOrCreateNetwork(newNodePos, newTe, networkPos, worldTe, networks);
          added.putAll(networks);
          merge.putAll(networks);
        }
      }
    }

    while(merge.size() > 1) {
      GradientMod.logger.info("Merging networks");
      final Iterator<Map.Entry<EnumFacing, EnergyNetwork>> it = merge.entrySet().iterator();
      final Map.Entry<EnumFacing, EnergyNetwork> firstEntry = it.next();
      final EnumFacing firstFacing = firstEntry.getKey();
      final EnergyNetwork firstNetwork = firstEntry.getValue();
      it.remove();

      while(it.hasNext()) {
        final Map.Entry<EnumFacing, EnergyNetwork> otherEntry = it.next();
        final EnumFacing otherFacing = otherEntry.getKey();
        final EnergyNetwork otherNetwork = otherEntry.getValue();

        if(firstNetwork == otherNetwork) {
          added.remove(otherFacing);
          it.remove();
          continue;
        }

        final EnergyNetwork.EnergyNode firstNode = firstNetwork.getNode(newNodePos);
        final EnergyNetwork.EnergyNode otherNode = otherNetwork.getNode(newNodePos);

        if(firstNode.te.getCapability(TRANSFER, firstFacing) == otherNode.te.getCapability(TRANSFER, otherFacing)) {
          firstNetwork.merge(otherNetwork);
          this.networks.remove(otherNetwork);
          added.remove(otherFacing);
          it.remove();
        }
      }
    }

    // There were no adjacent nodes, create a new network
    if(added.isEmpty()) {
      GradientMod.logger.info("No adjacent nodes, creating new network");
      final EnergyNetwork network = new EnergyNetwork();
      network.connect(newNodePos, newTe);
      this.networks.add(network);
      added.put(EnumFacing.NORTH, network);
    }

    this.allNodes.put(newNodePos, newTe);

    return added.values();
  }

  private void addToOrCreateNetwork(final BlockPos newNodePos, final TileEntity newTe, final BlockPos networkPos, final TileEntity worldTe, final Map<EnumFacing, EnergyNetwork> added) {
    GradientMod.logger.info("Trying to add new TE {} @ {} to existing networks at {}", newTe, newNodePos, networkPos);

    boolean connected = false;
    for(final EnergyNetwork network : this.getNetworksForBlock(networkPos)) {
      GradientMod.logger.info("Trying network {}...", network);

      if(network.connect(newNodePos, newTe)) {
        GradientMod.logger.info("Success!");
        added.put(BlockPosUtils.getBlockFacing(newNodePos, networkPos), network);
        connected = true;
      }
    }

    if(!connected) {
      // Create a new network if we couldn't connect
      GradientMod.logger.info("Failed to find a network to connect to, creating a new one");
      final EnergyNetwork network = new EnergyNetwork();
      network.connect(networkPos, worldTe);
      network.connect(newNodePos, newTe);
      this.networks.add(network);
      added.put(EnumFacing.NORTH, network);
    }
  }

  public void disconnect(final BlockPos pos) {
    GradientMod.logger.info("Removing node {}", pos);

    this.allNodes.remove(pos);

    for(final EnergyNetwork network : this.getNetworksForBlock(pos)) {
      // Do we need to rebuild the network?
      if(network.disconnect(pos)) {
        GradientMod.logger.info("Rebuilding network {}", network);

        this.networks.remove(network);

        for(final EnergyNetwork.EnergyNode node : network.nodes.values()) {
          this.connect(node.pos, node.te);
        }
      }
    }
  }

  private final Map<EnergyNetwork, EnumFacing> extractNetworks = new HashMap<>();

  public float requestEnergy(final BlockPos requestPosition, final float amount) {
    for(final EnergyNetwork network : this.networks) {
      if(network.contains(requestPosition)) {
        final EnergyNetwork.EnergyNode node = network.getNode(requestPosition);

        for(final EnumFacing side : EnumFacing.VALUES) {
          final EnergyNetwork.EnergyNode connection = node.connection(side);

          if(connection != null) {
            final EnumFacing opposite = side.getOpposite();

            if(connection.te.hasCapability(STORAGE, opposite)) {
              if(connection.te.getCapability(STORAGE, opposite).canSource()) {
                this.extractNetworks.put(network, side);
                break;
              }
            } else if(connection.te.hasCapability(TRANSFER, opposite)) {
              if(connection.te.getCapability(TRANSFER, opposite).canSource()) {
                this.extractNetworks.put(network, side);
                break;
              }
            }
          }
        }
      }
    }

    if(this.extractNetworks.isEmpty()) {
      return 0.0f;
    }

    float share = amount / this.extractNetworks.size();
    float deficit = 0.0f;
    float total = 0.0f;

    while(total < amount) {
      for(final Iterator<Map.Entry<EnergyNetwork, EnumFacing>> it = this.extractNetworks.entrySet().iterator(); it.hasNext(); ) {
        final Map.Entry<EnergyNetwork, EnumFacing> entry = it.next();
        final EnergyNetwork network = entry.getKey();
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
}
