package lordmonoxide.gradient.utils;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class WorldUtilsTest {
  @Test
  void testSerializeBlockPosAndFacing() {
    Assertions.assertEquals(0b0100000000000000000000000000000000000000000000000000000000000000L, WorldUtils.serializeBlockPosAndFacing(BlockPos.ORIGIN, EnumFacing.NORTH));
    Assertions.assertEquals(0b0100000000010000000000000000000000000100000000000000000000000001L, WorldUtils.serializeBlockPosAndFacing(new BlockPos(1, 1, 1), EnumFacing.NORTH));
    Assertions.assertEquals(0b1010000000000000000000000000000000000011111111111111111111111111L, WorldUtils.serializeBlockPosAndFacing(new BlockPos(0, 0, -1), EnumFacing.EAST));
    Assertions.assertEquals(0b1010111111111111111111111111111111111111111111111111111111111111L, WorldUtils.serializeBlockPosAndFacing(new BlockPos(-1, 255, -1), EnumFacing.EAST));
    Assertions.assertEquals(0b1011000000000111001001110000111000000001110010011100001110000000L, WorldUtils.serializeBlockPosAndFacing(new BlockPos(30000000, 256, 30000000), EnumFacing.EAST));
    Assertions.assertEquals(0b1011000000001000110110001111001000000010001101100011110010000000L, WorldUtils.serializeBlockPosAndFacing(new BlockPos(-30000000, 256, -30000000), EnumFacing.EAST));
  }

  @Test
  void testDeserializeBlockPos() {
    Assertions.assertEquals(BlockPos.ORIGIN, WorldUtils.getBlockPosFromSerialized(0b0100000000000000000000000000000000000000000000000000000000000000L));
    Assertions.assertEquals(new BlockPos(1, 1, 1), WorldUtils.getBlockPosFromSerialized(0b0100000000010000000000000000000000000100000000000000000000000001L));
    Assertions.assertEquals(new BlockPos(0, 0, -1), WorldUtils.getBlockPosFromSerialized(0b1010000000000000000000000000000000000011111111111111111111111111L));
    Assertions.assertEquals(new BlockPos(-1, 255, -1), WorldUtils.getBlockPosFromSerialized(0b1010111111111111111111111111111111111111111111111111111111111111L));
    Assertions.assertEquals(new BlockPos(30000000, 256, 30000000), WorldUtils.getBlockPosFromSerialized(0b1011000000000111001001110000111000000001110010011100001110000000L));
    Assertions.assertEquals(new BlockPos(-30000000, 256, -30000000), WorldUtils.getBlockPosFromSerialized(0b1011000000001000110110001111001000000010001101100011110010000000L));
  }

  @Test
  void testDeserializeFacing() {
    Assertions.assertEquals(EnumFacing.NORTH, WorldUtils.getFacingFromSerialized(0b0100000000000000000000000000000000000000000000000000000000000000L));
    Assertions.assertEquals(EnumFacing.NORTH, WorldUtils.getFacingFromSerialized(0b0100000000010000000000000000000000000100000000000000000000000001L));
    Assertions.assertEquals(EnumFacing.EAST, WorldUtils.getFacingFromSerialized(0b1010000000000000000000000000000000000011111111111111111111111111L));
    Assertions.assertEquals(EnumFacing.EAST, WorldUtils.getFacingFromSerialized(0b1010111111111111111111111111111111111111111111111111111111111111L));
    Assertions.assertEquals(EnumFacing.EAST, WorldUtils.getFacingFromSerialized(0b1011000000000111001001110000111000000001110010011100001110000000L));
    Assertions.assertEquals(EnumFacing.EAST, WorldUtils.getFacingFromSerialized(0b1011000000001000110110001111001000000010001101100011110010000000L));
  }
}
