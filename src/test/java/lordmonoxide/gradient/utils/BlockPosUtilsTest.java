package lordmonoxide.gradient.utils;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BlockPosUtilsTest {
  @Test
  void testSerializeBlockPosAndFacing() {
    Assertions.assertEquals(0b0100000000000000000000000000000000000000000000000000000000000000L, BlockPosUtils.serializeBlockPosAndFacing(BlockPos.ORIGIN, EnumFacing.NORTH));
    Assertions.assertEquals(0b0100000000010000000000000000000000000100000000000000000000000001L, BlockPosUtils.serializeBlockPosAndFacing(new BlockPos(1, 1, 1), EnumFacing.NORTH));
    Assertions.assertEquals(0b1010000000000000000000000000000000000011111111111111111111111111L, BlockPosUtils.serializeBlockPosAndFacing(new BlockPos(0, 0, -1), EnumFacing.EAST));
    Assertions.assertEquals(0b1010111111111111111111111111111111111111111111111111111111111111L, BlockPosUtils.serializeBlockPosAndFacing(new BlockPos(-1, 255, -1), EnumFacing.EAST));
    Assertions.assertEquals(0b1011000000000111001001110000111000000001110010011100001110000000L, BlockPosUtils.serializeBlockPosAndFacing(new BlockPos(30000000, 256, 30000000), EnumFacing.EAST));
    Assertions.assertEquals(0b1011000000001000110110001111001000000010001101100011110010000000L, BlockPosUtils.serializeBlockPosAndFacing(new BlockPos(-30000000, 256, -30000000), EnumFacing.EAST));
  }

  @Test
  void testDeserializeBlockPos() {
    Assertions.assertEquals(BlockPos.ORIGIN, BlockPosUtils.getBlockPosFromSerialized(0b0100000000000000000000000000000000000000000000000000000000000000L));
    Assertions.assertEquals(new BlockPos(1, 1, 1), BlockPosUtils.getBlockPosFromSerialized(0b0100000000010000000000000000000000000100000000000000000000000001L));
    Assertions.assertEquals(new BlockPos(0, 0, -1), BlockPosUtils.getBlockPosFromSerialized(0b1010000000000000000000000000000000000011111111111111111111111111L));
    Assertions.assertEquals(new BlockPos(-1, 255, -1), BlockPosUtils.getBlockPosFromSerialized(0b1010111111111111111111111111111111111111111111111111111111111111L));
    Assertions.assertEquals(new BlockPos(30000000, 256, 30000000), BlockPosUtils.getBlockPosFromSerialized(0b1011000000000111001001110000111000000001110010011100001110000000L));
    Assertions.assertEquals(new BlockPos(-30000000, 256, -30000000), BlockPosUtils.getBlockPosFromSerialized(0b1011000000001000110110001111001000000010001101100011110010000000L));
  }

  @Test
  void testDeserializeFacing() {
    Assertions.assertEquals(EnumFacing.NORTH, BlockPosUtils.getFacingFromSerialized(0b0100000000000000000000000000000000000000000000000000000000000000L));
    Assertions.assertEquals(EnumFacing.NORTH, BlockPosUtils.getFacingFromSerialized(0b0100000000010000000000000000000000000100000000000000000000000001L));
    Assertions.assertEquals(EnumFacing.EAST, BlockPosUtils.getFacingFromSerialized(0b1010000000000000000000000000000000000011111111111111111111111111L));
    Assertions.assertEquals(EnumFacing.EAST, BlockPosUtils.getFacingFromSerialized(0b1010111111111111111111111111111111111111111111111111111111111111L));
    Assertions.assertEquals(EnumFacing.EAST, BlockPosUtils.getFacingFromSerialized(0b1011000000000111001001110000111000000001110010011100001110000000L));
    Assertions.assertEquals(EnumFacing.EAST, BlockPosUtils.getFacingFromSerialized(0b1011000000001000110110001111001000000010001101100011110010000000L));
  }
}
