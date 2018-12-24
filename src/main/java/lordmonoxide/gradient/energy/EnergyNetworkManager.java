package lordmonoxide.gradient.energy;

import lordmonoxide.gradient.GradientMod;
import lordmonoxide.gradient.utils.BlockPosUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mod.EventBusSubscriber(modid = GradientMod.MODID)
public class EnergyNetworkManager<STORAGE extends IEnergyStorage, TRANSFER extends IEnergyTransfer> {
  private static final Map<World, List<EnergyNetworkManager<?, ?>>> clientManagers = new HashMap<>();
  private static final Map<World, List<EnergyNetworkManager<?, ?>>> serverManagers = new HashMap<>();

  public static <STORAGE extends IEnergyStorage, TRANSFER extends IEnergyTransfer> EnergyNetworkManager<STORAGE, TRANSFER> getManager(final World world, final Capability<STORAGE> storage, final Capability<TRANSFER> transfer) {
    final Map<World, List<EnergyNetworkManager<?, ?>>> managers = world.isRemote ? clientManagers : serverManagers;

    final List<EnergyNetworkManager<?, ?>> list = managers.computeIfAbsent(world, k -> new ArrayList<>());

    for(final EnergyNetworkManager<?, ?> manager : list) {
      if(manager.storage == storage && manager.transfer == transfer) {
        GradientMod.logger.info("Using manager {}", manager);
        return (EnergyNetworkManager<STORAGE, TRANSFER>)manager;
      }
    }

    final EnergyNetworkManager<STORAGE, TRANSFER> manager = new EnergyNetworkManager<>(world, storage, transfer);
    GradientMod.logger.info("New manager {}", manager);
    list.add(manager);
    return manager;
  }

  @SubscribeEvent
  public static void onServerTick(final TickEvent.ServerTickEvent event) {
    if(event.phase == TickEvent.Phase.START) {
      serverManagers.values().forEach(list -> list.forEach(EnergyNetworkManager::tick));
    }
  }

  private final Capability<STORAGE> storage;
  private final Capability<TRANSFER> transfer;

  private final List<EnergyNetwork<STORAGE, TRANSFER>> networks = new ArrayList<>();
  private final IBlockAccess world;

  private final Map<BlockPos, TileEntity> allNodes = new HashMap<>();

  public EnergyNetworkManager(final IBlockAccess world, final Capability<STORAGE> storage, final Capability<TRANSFER> transfer) {
    this.world = world;
    this.storage = storage;
    this.transfer = transfer;
  }

  private final Map<STORAGE, BlockPos> tickSinkNodes = new HashMap<>();
  private final Set<TRANSFER> tickTransferNodes = new HashSet<>();

  public void tick() {
    GradientMod.logger.info("Ticking {}", this);

    this.tickSinkNodes.clear();
    this.tickTransferNodes.clear();

    for(final TileEntity te : this.allNodes.values()) {
      GradientMod.logger.info("Checking {}", te);
      for(final EnumFacing facing : EnumFacing.VALUES) {
        if(te.hasCapability(this.storage, facing) && te.getCapability(this.storage, facing).canSink()) {
          this.tickSinkNodes.put(te.getCapability(this.storage, facing), te.getPos());
        } else if(te.hasCapability(this.transfer, facing)) {
          this.tickTransferNodes.add(te.getCapability(this.transfer, facing));
        }
      }
    }

    for(final TRANSFER transfer : this.tickTransferNodes) {
      GradientMod.logger.info("Resetting transfer {}", transfer);
      transfer.resetEnergyTransferred();
    }

    for(final Map.Entry<STORAGE, BlockPos> entry : this.tickSinkNodes.entrySet()) {
      final STORAGE sink = entry.getKey();
      final BlockPos pos = entry.getValue();

      GradientMod.logger.info("Ticking sink {} @ {}", sink, pos);

      final float requested = sink.getRequestedEnergy();

      GradientMod.logger.info("{} requesting {} energy", sink, requested);

      if(requested != 0.0f) {
        final float energy = this.requestEnergy(pos, requested);

        GradientMod.logger.info("{} got {} energy", sink, energy);

        if(energy != 0.0f) {
          sink.sinkEnergy(energy, false);
        }
      }
    }
  }

  public int size() {
    return this.networks.size();
  }

  public List<EnergyNetwork<STORAGE, TRANSFER>> getNetworksForBlock(final BlockPos pos) {
    final List<EnergyNetwork<STORAGE, TRANSFER>> networks = new ArrayList<>();

    for(final EnergyNetwork<STORAGE, TRANSFER> network : this.networks) {
      if(network.contains(pos)) {
        networks.add(network);
      }
    }

    return networks;
  }

  public Map<EnumFacing, EnergyNetwork<STORAGE, TRANSFER>> connect(final BlockPos newNodePos, final TileEntity newTe) {
    GradientMod.logger.info("Attempting to add {} @ {} to a network...", newTe, newNodePos);

    final Map<EnumFacing, EnergyNetwork<STORAGE, TRANSFER>> added = new HashMap<>();
    final Map<EnumFacing, EnergyNetwork<STORAGE, TRANSFER>> merge = new HashMap<>();

    for(final EnumFacing facing : EnumFacing.VALUES) {
      final BlockPos networkPos = newNodePos.offset(facing);
      final TileEntity worldTe = this.world.getTileEntity(networkPos);

      // If there's no TE, there's no network
      if(worldTe == null) {
        continue;
      }

      GradientMod.logger.info("Found TE {} @ {} ({})", worldTe, networkPos, facing);

      if(newTe.hasCapability(this.storage, facing)) {
        if(worldTe.hasCapability(this.storage, facing)) {
          // New network if we can't connect to the storage's network (we can only connect if it's the only block in the network)
          GradientMod.logger.info("Both TEs are storages");
          this.addToOrCreateNetwork(newNodePos, newTe, networkPos, worldTe, added);
        } else if(worldTe.hasCapability(this.transfer, facing)) {
          // Add to network, no merge
          GradientMod.logger.info("New TE is storage, world TE is transfer");
          this.addToOrCreateNetwork(newNodePos, newTe, networkPos, worldTe, added);
        }
      } else if(newTe.hasCapability(this.transfer, facing)) {
        if(worldTe.hasCapability(this.storage, facing)) {
          // If worldTe is in its own network, add (may have to merge)
          // If worldTe is in an existing network, new (may have to merge)
          GradientMod.logger.info("New TE is transfer, world TE is storage");
          final Map<EnumFacing, EnergyNetwork<STORAGE, TRANSFER>> networks = new HashMap<>();
          this.addToOrCreateNetwork(newNodePos, newTe, networkPos, worldTe, networks);
          added.putAll(networks);
          merge.putAll(networks);
        } else if(worldTe.hasCapability(this.transfer, facing)) {
          // Add to network (may have to merge)
          GradientMod.logger.info("Both TEs are transfers");
          final Map<EnumFacing, EnergyNetwork<STORAGE, TRANSFER>> networks = new HashMap<>();
          this.addToOrCreateNetwork(newNodePos, newTe, networkPos, worldTe, networks);
          added.putAll(networks);
          merge.putAll(networks);
        }
      }
    }

    while(merge.size() > 1) {
      GradientMod.logger.info("Merging networks");
      final Iterator<Map.Entry<EnumFacing, EnergyNetwork<STORAGE, TRANSFER>>> it = merge.entrySet().iterator();
      final Map.Entry<EnumFacing, EnergyNetwork<STORAGE, TRANSFER>> firstEntry = it.next();
      final EnumFacing firstFacing = firstEntry.getKey();
      final EnergyNetwork<STORAGE, TRANSFER> firstNetwork = firstEntry.getValue();
      it.remove();

      while(it.hasNext()) {
        final Map.Entry<EnumFacing, EnergyNetwork<STORAGE, TRANSFER>> otherEntry = it.next();
        final EnumFacing otherFacing = otherEntry.getKey();
        final EnergyNetwork<STORAGE, TRANSFER> otherNetwork = otherEntry.getValue();

        if(firstNetwork == otherNetwork) {
          it.remove();
          continue;
        }

        final EnergyNetwork.EnergyNode firstNode = firstNetwork.getNode(newNodePos);
        final EnergyNetwork.EnergyNode otherNode = otherNetwork.getNode(newNodePos);

        if(firstNode.te.getCapability(this.transfer, firstFacing) == otherNode.te.getCapability(this.transfer, otherFacing)) {
          firstNetwork.merge(otherNetwork);
          this.networks.remove(otherNetwork);
          added.put(otherFacing, firstNetwork);
          it.remove();
        }
      }
    }

    // There were no adjacent nodes, create a new network
    if(added.isEmpty()) {
      GradientMod.logger.info("No adjacent nodes, creating new network");
      final EnergyNetwork<STORAGE, TRANSFER> network = new EnergyNetwork<>(this.storage, this.transfer);
      network.connect(newNodePos, newTe);
      this.networks.add(network);
      added.put(null, network);
    }

    this.allNodes.put(newNodePos, newTe);

    return added;
  }

  private void addToOrCreateNetwork(final BlockPos newNodePos, final TileEntity newTe, final BlockPos networkPos, final TileEntity worldTe, final Map<EnumFacing, EnergyNetwork<STORAGE, TRANSFER>> added) {
    GradientMod.logger.info("Trying to add new TE {} @ {} to existing networks at {}", newTe, newNodePos, networkPos);

    boolean connected = false;
    for(final EnergyNetwork<STORAGE, TRANSFER> network : this.getNetworksForBlock(networkPos)) {
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
      final EnergyNetwork<STORAGE, TRANSFER> network = new EnergyNetwork<>(this.storage, this.transfer);
      this.allNodes.put(networkPos, worldTe);
      network.connect(networkPos, worldTe);
      network.connect(newNodePos, newTe);
      this.networks.add(network);
      added.put(BlockPosUtils.getBlockFacing(newNodePos, networkPos), network);
    }
  }

  public void disconnect(final BlockPos pos) {
    GradientMod.logger.info("Removing node {}", pos);

    this.allNodes.remove(pos);

    for(final EnergyNetwork<STORAGE, TRANSFER> network : this.getNetworksForBlock(pos)) {
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

  private final Map<EnergyNetwork<STORAGE, TRANSFER>, EnumFacing> extractNetworks = new HashMap<>();

  public float requestEnergy(final BlockPos requestPosition, final float amount) {
    for(final EnergyNetwork<STORAGE, TRANSFER> network : this.networks) {
      if(network.contains(requestPosition)) {
        final EnergyNetwork.EnergyNode node = network.getNode(requestPosition);

        for(final EnumFacing side : EnumFacing.VALUES) {
          final EnergyNetwork.EnergyNode connection = node.connection(side);

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
      return 0.0f;
    }

    float share = amount / this.extractNetworks.size();
    float deficit = 0.0f;
    float total = 0.0f;

    while(total < amount) {
      for(final Iterator<Map.Entry<EnergyNetwork<STORAGE, TRANSFER>, EnumFacing>> it = this.extractNetworks.entrySet().iterator(); it.hasNext(); ) {
        final Map.Entry<EnergyNetwork<STORAGE, TRANSFER>, EnumFacing> entry = it.next();
        final EnergyNetwork<STORAGE, TRANSFER> network = entry.getKey();
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
