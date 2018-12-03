package lordmonoxide.gradient.energy;

import lordmonoxide.gradient.utils.BlockPosUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

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
        final EnumFacing facing = BlockPosUtils.getBlockFacing(pos, nodePos);

        if(energyNode.canConnect(facing) && node.node.canConnect(facing.getOpposite())) {
          if(newNode == null) {
            newNode = new EnergyNode(pos, energyNode);
          }

          newNode.connections.put(facing, node);
          node.connections.put(facing.getOpposite(), newNode);
        }
      }
    }

    if(newNode != null) {
      this.nodes.put(pos, newNode);
      return true;
    }

    return false;
  }

  public EnergyNode getNode(final BlockPos pos) {
    return this.nodes.get(pos);
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
