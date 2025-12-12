package com.epam.deltix.util.buffer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public interface Buffer {

    long addressOffset();

    int capacity();

    byte[] byteArray();

    ByteBuffer byteBuffer();

    long getLong(int index);

    long getLong(int index, ByteOrder byteOrder);

    int getInt(int index, ByteOrder byteOrder);

    int getInt(int index);

    double getDouble(int index);

    double getDouble(int index, ByteOrder byteOrder);

    float getFloat(int index);

    float getFloat(int index, ByteOrder byteOrder);

    short getShort(int index);

    short getShort(int index, ByteOrder byteOrder);

    char getChar(int index);

    char getChar(int index, ByteOrder byteOrder);

    boolean getBool(int index);

    byte getByte(int index);

    void getBytes(int index, byte[] dst);

    void getBytes(int index, byte[] dst, int dstOffset, int length);

    void getBytes(int index, MutableBuffer dstBuffer, int dstIndex, int length);

    void getBytes(int index, ByteBuffer dstBuffer, int length);

    void boundsCheck(int offset, int length);

    void checkLimit(int limit);

}
