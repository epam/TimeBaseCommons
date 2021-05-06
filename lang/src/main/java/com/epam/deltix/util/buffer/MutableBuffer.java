package com.epam.deltix.util.buffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public interface MutableBuffer extends Buffer {

    void wrap(byte[] buffer);

    void wrap(byte[] buffer, int offset, int length);

    void wrap(ByteBuffer buffer);

    void wrap(ByteBuffer buffer, int offset, int length);

    void wrap(Buffer buffer);

    void wrap(Buffer buffer, int offset, int length);

    void wrap(long address, int length);

    void setMemory(int offset, int length, byte value);

    void putLong(int index, long value);

    void putLong(int index, long value, ByteOrder byteOrder);

    void putInt(int index, int value);

    void putInt(int index, int value, ByteOrder byteOrder);

    void putDouble(int index, double value);

    void putDouble(int index, double value, ByteOrder byteOrder);

    void putFloat(int index, float value);

    void putFloat(int index, float value, ByteOrder byteOrder);

    void putShort(int index, short value);

    void putShort(int index, short value, ByteOrder byteOrder);

    void putChar(int index, char value);

    void putChar(int index, char value, ByteOrder byteOrder);

    void putBool(int index, boolean value);

    void putByte(int index, byte value);

    void putBytes(int index, byte[] src);

    void putBytes(int index, byte[] src, int srcOffset, int length);

    void putBytes(int index, ByteBuffer srcBuffer, int length);

    void putBytes(int index, ByteBuffer srcBuffer, int srcOffset, int length);

    void putBytes(int index, Buffer srcBuffer);

    void putBytes(int index, Buffer srcBuffer, int srcOffset, int length);

}
