package deltix.util.memory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.LongToIntFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Alexei Osipov
 */
public class Test_MemoryDataOutput {
    private final MemoryDataOutput mdo = new MemoryDataOutput();

    // For easier testing of alternative implementations.
    private final LongToIntFunction testMethod = mdo::writeLongBytes;

    @Test
    public void writeLongBytes0() {

        mdo.seek(11); // This is to make write position not aligned

        int pos = mdo.getPosition();
        int result = testMethod.applyAsInt(0);
        assertEquals(0, result);
        assertEquals(pos, mdo.getPosition());
        assertEquals(pos, mdo.getSize());
    }

    @Test
    public void writeLongBytes1() {
        mdo.seek(11); // This is to make write position not aligned

        int pos = mdo.getPosition();
        int result = testMethod.applyAsInt(0xFF_00_FFL);
        assertEquals(3, result);
        assertEquals(pos + result, mdo.getPosition());
        assertEquals(pos + result, mdo.getSize());
    }

    @Test
    public void writeLongBytes2() {
        mdo.seek(11); // This is to make write position not aligned

        int pos = mdo.getPosition();
        int result = testMethod.applyAsInt(0xCAFE_BABEL);
        assertEquals(4, result);
        assertEquals(pos + result, mdo.getPosition());
        assertEquals(pos + result, mdo.getSize());
    }

    // Ensure that the bytes are written in little-endian order.
    @Test
    public void writeLongBytes3() {
        mdo.seek(11); // This is to make write position not aligned

        int pos = mdo.getPosition();
        int result = testMethod.applyAsInt(0x0807060504030201L);
        assertEquals(8, result);
        assertEquals(pos + result, mdo.getPosition());
        assertEquals(pos + result, mdo.getSize());
        byte[] mdoBuffer = mdo.getBuffer();
        byte[] actual = new byte[8];
        System.arraycopy(mdoBuffer, pos, actual, 0, 8);
        Assertions.assertArrayEquals(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08}, actual);
    }

    // Ensure that there are no problem with negative numbers.
    @Test
    public void writeLongBytes4() {
        mdo.seek(11); // This is to make write position not aligned

        int pos = mdo.getPosition();
        int result = testMethod.applyAsInt(0xF8F7F6F5F4F3F2F1L);
        assertEquals(8, result);
        assertEquals(pos + result, mdo.getPosition());
        assertEquals(pos + result, mdo.getSize());
        byte[] mdoBuffer = mdo.getBuffer();
        byte[] actual = new byte[8];
        System.arraycopy(mdoBuffer, pos, actual, 0, 8);
        Assertions.assertArrayEquals(new byte[]{
                (byte) 0xF1, (byte) 0xF2, (byte) 0xF3, (byte) 0xF4,
                (byte) 0xF5, (byte) 0xF6, (byte) 0xF7, (byte) 0xF8}, actual);
    }

}
