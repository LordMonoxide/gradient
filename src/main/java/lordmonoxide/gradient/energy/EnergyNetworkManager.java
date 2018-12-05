package lordmonoxide.gradient.energy;

import com.google.common.collect.Sets;
import lordmonoxide.gradient.GradientMod;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

  public EnergyNetworkManager(final IBlockAccess world) {
    this.world = world;
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

  public Set<EnergyNetwork> connect(final BlockPos newNodePos, final TileEntity newTe) {
    GradientMod.logger.info("Attempting to add {} @ {} to a network...", newTe, newNodePos);

    final Set<EnergyNetwork> added = new HashSet<>();

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
        final IEnergyTransfer newTransfer = newTe.getCapability(TRANSFER, facing);

        if(worldTe.hasCapability(STORAGE, facing)) {
          // If worldTe is in its own network, add (may have to merge)
          // If worldTe is in an existing network, new (may have to merge)
          GradientMod.logger.info("New TE is transfer, world TE is storage");
        } else if(worldTe.hasCapability(TRANSFER, facing)) {
          // Add to network (may have to merge)
          GradientMod.logger.info("Both TEs are transfers");
        }
      }
    }

    // There were no adjacent nodes, create a new network
    if(added.isEmpty()) {
      GradientMod.logger.info("No adjacent nodes, creating new network");
      final EnergyNetwork network = new EnergyNetwork();
      network.connect(newNodePos, newTe);
      this.networks.add(network);
      added.add(network);
    }

    return added;
  }

  private void addToOrCreateNetwork(final BlockPos newNodePos, final TileEntity newTe, final BlockPos networkPos, final TileEntity worldTe, final Set<EnergyNetwork> added) {
    GradientMod.logger.info("Trying to add new TE {} @ {} to existing networks at {}", newTe, newNodePos, networkPos);

    boolean connected = false;
    for(final EnergyNetwork network : this.getNetworksForBlock(networkPos)) {
      GradientMod.logger.info("Trying network {}...", network);

      if(network.connect(newNodePos, newTe)) {
        GradientMod.logger.info("Success!");
        added.add(network);
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
      added.add(network);
    }
  }

  public Set<EnergyNetwork> connect2(final BlockPos newNodePos, final TileEntity te) {
    final Map<EnergyNetwork, List<EnumFacing>> add = new HashMap<>();

    for(final EnergyNetwork net : this.networks) {
      final List<EnumFacing> facing = net.canConnect(newNodePos, te);

      if(!facing.isEmpty()) {
        add.put(net, facing);
      }
    }

    if(add.isEmpty()) {
      // Can't connect to any network
      final EnergyNetwork net = new EnergyNetwork();
      net.connect(newNodePos, te);
      this.networks.add(net);
      return Sets.newHashSet(net);
    }

    if(add.size() == 1) {
      // Can connect to one network
      for(final EnergyNetwork net : add.keySet()) {
        net.connect(newNodePos, te);
      }

      return add.keySet();
    }

    // There are multiple networks to connect to


    for(final Map.Entry<EnergyNetwork, List<EnumFacing>> entry : add.entrySet()) {
      for(final EnumFacing facing : entry.getValue()) {

      }
    }

    return add.keySet();
  }

  private EnergyNetwork attemptToCreateNetwork(final BlockPos newNodePos, final TileEntity te) {
    return null;
  }
}
