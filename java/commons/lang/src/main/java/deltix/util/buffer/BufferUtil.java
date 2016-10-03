package deltix.util.buffer;

import deltix.util.ByteSequenceWrapper;
import deltix.util.StringUtil;

import java.nio.ByteBuffer;


public class BufferUtil {

    public static UnsafeBuffer fromString(String string) {
        return new UnsafeBuffer(StringUtil.asciiBytes(string));
    }

    public static String toString(Buffer buffer) {
        return new ByteSequenceWrapper(buffer).toString();
    }

    static void boundsCheck(byte[] buffer, long index, int length) {
        int capacity = buffer.length;
        long resultingPosition = index + (long) length;
        if (index < 0 || resultingPosition > capacity) {
            throw new IndexOutOfBoundsException(String.format("index=%d, length=%d, capacity=%d", index, length, capacity));
        }
    }

    static void boundsCheck(ByteBuffer buffer, long index, int length) {
        int capacity = buffer.capacity();
        long resultingPosition = index + (long) length;
        if (index < 0 || resultingPosition > capacity) {
            throw new IndexOutOfBoundsException(String.format("index=%d, length=%d, capacity=%d", index, length, capacity));
        }
    }

}
