/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.util.buffer;

import com.epam.deltix.util.UnsafeAccess;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static com.epam.deltix.util.BitUtil.*;
import static com.epam.deltix.util.UnsafeAccess.UNSAFE;


public final class UnsafeBuffer implements AtomicBuffer {

    public static final int ALIGNMENT = SIZE_OF_LONG;

    public static final String DISABLE_BOUNDS_CHECKS_PROP_NAME = "deltix.buffer.disable.bounds.check";
    public static final boolean SHOULD_BOUNDS_CHECK = !Boolean.getBoolean(DISABLE_BOUNDS_CHECKS_PROP_NAME);

    private static final ByteOrder NATIVE_BYTE_ORDER = ByteOrder.nativeOrder();
    private static final long ARRAY_BASE_OFFSET = UnsafeAccess.UNSAFE.arrayBaseOffset(byte[].class);

    private byte[] byteArray;
    private ByteBuffer byteBuffer;
    private long addressOffset;

    private int capacity;

    public UnsafeBuffer(byte[] buffer) {
        wrap(buffer);
    }

    public UnsafeBuffer(byte[] buffer, int offset, int length) {
        wrap(buffer, offset, length);
    }

    public UnsafeBuffer(ByteBuffer buffer) {
        wrap(buffer);
    }

    public UnsafeBuffer(ByteBuffer buffer, int offset, int length) {
        wrap(buffer, offset, length);
    }

    public UnsafeBuffer(Buffer buffer) {
        wrap(buffer);
    }

    public UnsafeBuffer(Buffer buffer, int offset, int length) {
        wrap(buffer, offset, length);
    }

    public UnsafeBuffer(long address, int length) {
        wrap(address, length);
    }

    public void wrap(byte[] buffer) {
        addressOffset = ARRAY_BASE_OFFSET;
        capacity = buffer.length;
        byteArray = buffer;
        byteBuffer = null;
    }

    public void wrap(byte[] buffer, int offset, int length) {
        if (SHOULD_BOUNDS_CHECK) {
            int bufferLength = buffer.length;
            if (offset != 0 && (offset < 0 || offset > bufferLength - 1)) {
                throw new IllegalArgumentException("offset=" + offset + " not valid for buffer.length=" + bufferLength);
            }

            if (length < 0 || length > bufferLength - offset) {
                throw new IllegalArgumentException(
                        "offset=" + offset + " length=" + length + " not valid for buffer.length=" + bufferLength);
            }
        }

        addressOffset = ARRAY_BASE_OFFSET + offset;
        capacity = length;
        byteArray = buffer;
        byteBuffer = null;
    }

    public void wrap(ByteBuffer buffer) {
        byteBuffer = buffer;

        if (buffer.hasArray()) {
            byteArray = buffer.array();
            addressOffset = ARRAY_BASE_OFFSET + buffer.arrayOffset();
        } else {
            byteArray = null;
            addressOffset = ((sun.nio.ch.DirectBuffer) buffer).address();
        }

        capacity = buffer.capacity();
    }

    public void wrap(ByteBuffer buffer, int offset, int length) {
        if (SHOULD_BOUNDS_CHECK) {
            int bufferCapacity = buffer.capacity();
            if (offset != 0 && (offset < 0 || offset > bufferCapacity - 1)) {
                throw new IllegalArgumentException("offset=" + offset + " not valid for buffer.capacity()=" + bufferCapacity);
            }

            if (length < 0 || length > bufferCapacity - offset) {
                throw new IllegalArgumentException(
                        "offset=" + offset + " length=" + length + " not valid for buffer.capacity()=" + bufferCapacity);
            }
        }

        byteBuffer = buffer;

        if (buffer.hasArray()) {
            byteArray = buffer.array();
            addressOffset = ARRAY_BASE_OFFSET + buffer.arrayOffset() + offset;
        } else {
            byteArray = null;
            addressOffset = ((sun.nio.ch.DirectBuffer) buffer).address() + offset;
        }

        capacity = length;
    }

    public void wrap(Buffer buffer) {
        addressOffset = buffer.addressOffset();
        capacity = buffer.capacity();
        byteArray = buffer.byteArray();
        byteBuffer = buffer.byteBuffer();
    }

    public void wrap(Buffer buffer, int offset, int length) {
        if (SHOULD_BOUNDS_CHECK) {
            int bufferCapacity = buffer.capacity();
            if (offset != 0 && (offset < 0 || offset > bufferCapacity - 1)) {
                throw new IllegalArgumentException("offset=" + offset + " not valid for buffer.capacity()=" + bufferCapacity);
            }

            if (length < 0 || length > bufferCapacity - offset) {
                throw new IllegalArgumentException(
                        "offset=" + offset + " length=" + length + " not valid for buffer.capacity()=" + bufferCapacity);
            }
        }

        addressOffset = buffer.addressOffset() + offset;
        capacity = length;
        byteArray = buffer.byteArray();
        byteBuffer = buffer.byteBuffer();
    }

    public void wrap(long address, int length) {
        addressOffset = address;
        capacity = length;
        byteArray = null;
        byteBuffer = null;
    }

    public long addressOffset() {
        return addressOffset;
    }

    public byte[] byteArray() {
        return byteArray;
    }

    public ByteBuffer byteBuffer() {
        return byteBuffer;
    }

    public void setMemory(int index, int length, byte value) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, length);
        }

        UNSAFE.setMemory(byteArray, addressOffset + index, length, value);
    }

    public int capacity() {
        return capacity;
    }

    public void checkLimit(int limit) {
        if (limit > capacity) {
            String msg = String.format("limit=%d is beyond capacity=%d", limit, capacity);
            throw new IndexOutOfBoundsException(msg);
        }
    }

    public void verifyAlignment() {
        if (0 != (addressOffset & (ALIGNMENT - 1))) {
            throw new IllegalStateException(String.format(
                    "AtomicBuffer is not correctly aligned: addressOffset=%d in not divisible by %d",
                    addressOffset,
                    ALIGNMENT));
        }
    }

    public long getLong(int index, ByteOrder byteOrder) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_LONG);
        }

        long bits = UNSAFE.getLong(byteArray, addressOffset + index);
        if (NATIVE_BYTE_ORDER != byteOrder) {
            bits = Long.reverseBytes(bits);
        }

        return bits;
    }

    public void putLong(int index, long value, ByteOrder byteOrder) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_LONG);
        }

        long bits = value;
        if (NATIVE_BYTE_ORDER != byteOrder) {
            bits = Long.reverseBytes(bits);
        }

        UNSAFE.putLong(byteArray, addressOffset + index, bits);
    }

    public long getLong(int index) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_LONG);
        }

        return UNSAFE.getLong(byteArray, addressOffset + index);
    }

    public void putLong(int index, long value) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_LONG);
        }

        UNSAFE.putLong(byteArray, addressOffset + index, value);
    }

    public long getLongVolatile(int index) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_LONG);
        }

        return UNSAFE.getLongVolatile(byteArray, addressOffset + index);
    }

    public void putLongVolatile(int index, long value) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_LONG);
        }

        UNSAFE.putLongVolatile(byteArray, addressOffset + index, value);
    }

    public void putLongOrdered(int index, long value) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_LONG);
        }

        UNSAFE.putOrderedLong(byteArray, addressOffset + index, value);
    }

    public long addLongOrdered(int index, long increment) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_LONG);
        }

        long offset = addressOffset + index;
        byte[] byteArray = this.byteArray;
        long value = UNSAFE.getLong(byteArray, offset);
        UNSAFE.putOrderedLong(byteArray, offset, value + increment);

        return value;
    }

    public boolean compareAndSetLong(int index, long expectedValue, long updateValue) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_LONG);
        }

        return UNSAFE.compareAndSwapLong(byteArray, addressOffset + index, expectedValue, updateValue);
    }

    public long getAndSetLong(int index, long value) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_LONG);
        }

        return UNSAFE.getAndSetLong(byteArray, addressOffset + index, value);
    }

    public long getAndAddLong(int index, long delta) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_LONG);
        }

        return UNSAFE.getAndAddLong(byteArray, addressOffset + index, delta);
    }

    public int getInt(int index, ByteOrder byteOrder) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_INT);
        }

        int bits = UNSAFE.getInt(byteArray, addressOffset + index);
        if (NATIVE_BYTE_ORDER != byteOrder) {
            bits = Integer.reverseBytes(bits);
        }

        return bits;
    }

    public void putInt(int index, int value, ByteOrder byteOrder) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_INT);
        }

        int bits = value;
        if (NATIVE_BYTE_ORDER != byteOrder) {
            bits = Integer.reverseBytes(bits);
        }

        UNSAFE.putInt(byteArray, addressOffset + index, bits);
    }

    public int getInt(int index) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_INT);
        }

        return UNSAFE.getInt(byteArray, addressOffset + index);
    }

    public void putInt(int index, int value) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_INT);
        }

        UNSAFE.putInt(byteArray, addressOffset + index, value);
    }

    public int getIntVolatile(int index) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_INT);
        }

        return UNSAFE.getIntVolatile(byteArray, addressOffset + index);
    }

    public void putIntVolatile(int index, int value) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_INT);
        }

        UNSAFE.putIntVolatile(byteArray, addressOffset + index, value);
    }

    public void putIntOrdered(int index, int value) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_INT);
        }

        UNSAFE.putOrderedInt(byteArray, addressOffset + index, value);
    }

    public int addIntOrdered(int index, int increment) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_INT);
        }

        long offset = addressOffset + index;
        byte[] byteArray = this.byteArray;
        int value = UNSAFE.getInt(byteArray, offset);
        UNSAFE.putOrderedInt(byteArray, offset, value + increment);

        return value;
    }

    public boolean compareAndSetInt(int index, int expectedValue, int updateValue) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_INT);
        }

        return UNSAFE.compareAndSwapInt(byteArray, addressOffset + index, expectedValue, updateValue);
    }

    public int getAndSetInt(int index, int value) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_INT);
        }

        return UNSAFE.getAndSetInt(byteArray, addressOffset + index, value);
    }

    public int getAndAddInt(int index, int delta) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_INT);
        }

        return UNSAFE.getAndAddInt(byteArray, addressOffset + index, delta);
    }

    public double getDouble(int index, ByteOrder byteOrder) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_DOUBLE);
        }

        if (NATIVE_BYTE_ORDER != byteOrder) {
            long bits = UNSAFE.getLong(byteArray, addressOffset + index);
            return Double.longBitsToDouble(Long.reverseBytes(bits));
        } else {
            return UNSAFE.getDouble(byteArray, addressOffset + index);
        }
    }

    public void putDouble(int index, double value, ByteOrder byteOrder) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_DOUBLE);
        }

        if (NATIVE_BYTE_ORDER != byteOrder) {
            long bits = Long.reverseBytes(Double.doubleToRawLongBits(value));
            UNSAFE.putLong(byteArray, addressOffset + index, bits);
        } else {
            UNSAFE.putDouble(byteArray, addressOffset + index, value);
        }
    }

    public double getDouble(int index) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_DOUBLE);
        }

        return UNSAFE.getDouble(byteArray, addressOffset + index);
    }

    public void putDouble(int index, double value) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_DOUBLE);
        }

        UNSAFE.putDouble(byteArray, addressOffset + index, value);
    }

    public float getFloat(int index, ByteOrder byteOrder) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_FLOAT);
        }

        if (NATIVE_BYTE_ORDER != byteOrder) {
            int bits = UNSAFE.getInt(byteArray, addressOffset + index);
            return Float.intBitsToFloat(Integer.reverseBytes(bits));
        } else {
            return UNSAFE.getFloat(byteArray, addressOffset + index);
        }
    }

    public void putFloat(int index, float value, ByteOrder byteOrder) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_FLOAT);
        }

        if (NATIVE_BYTE_ORDER != byteOrder) {
            int bits = Integer.reverseBytes(Float.floatToRawIntBits(value));
            UNSAFE.putInt(byteArray, addressOffset + index, bits);
        } else {
            UNSAFE.putFloat(byteArray, addressOffset + index, value);
        }
    }

    public float getFloat(int index) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_FLOAT);
        }

        return UNSAFE.getFloat(byteArray, addressOffset + index);
    }

    public void putFloat(int index, float value) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_FLOAT);
        }

        UNSAFE.putFloat(byteArray, addressOffset + index, value);
    }

    public short getShort(int index, ByteOrder byteOrder) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_SHORT);
        }

        short bits = UNSAFE.getShort(byteArray, addressOffset + index);
        if (NATIVE_BYTE_ORDER != byteOrder) {
            bits = Short.reverseBytes(bits);
        }

        return bits;
    }

    public void putShort(int index, short value, ByteOrder byteOrder) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_SHORT);
        }

        short bits = value;
        if (NATIVE_BYTE_ORDER != byteOrder) {
            bits = Short.reverseBytes(bits);
        }

        UNSAFE.putShort(byteArray, addressOffset + index, bits);
    }

    public short getShort(int index) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_SHORT);
        }

        return UNSAFE.getShort(byteArray, addressOffset + index);
    }

    public void putShort(int index, short value) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_SHORT);
        }

        UNSAFE.putShort(byteArray, addressOffset + index, value);
    }

    public short getShortVolatile(int index) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_SHORT);
        }

        return UNSAFE.getShortVolatile(byteArray, addressOffset + index);
    }

    public void putShortVolatile(int index, short value) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_SHORT);
        }

        UNSAFE.putShortVolatile(byteArray, addressOffset + index, value);
    }

    @Override
    public boolean getBool(int index) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck(index);
        }

        return UNSAFE.getByte(byteArray, addressOffset + index) == 1;
    }

    @Override
    public void putBool(int index, boolean value) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck(index);
        }

        UNSAFE.putByte(byteArray, addressOffset + index, value ? (byte)1 : (byte)0);
    }

    public byte getByte(int index) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck(index);
        }

        return UNSAFE.getByte(byteArray, addressOffset + index);
    }

    public void putByte(int index, byte value) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck(index);
        }

        UNSAFE.putByte(byteArray, addressOffset + index, value);
    }

    public byte getByteVolatile(int index) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck(index);
        }

        return UNSAFE.getByteVolatile(byteArray, addressOffset + index);
    }

    public void putByteVolatile(int index, byte value) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck(index);
        }

        UNSAFE.putByteVolatile(byteArray, addressOffset + index, value);
    }

    public void getBytes(int index, byte[] dst) {
        getBytes(index, dst, 0, dst.length);
    }

    public void getBytes(int index, byte[] dst, int offset, int length) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, length);
            BufferUtil.boundsCheck(dst, offset, length);
        }

        UNSAFE.copyMemory(byteArray, addressOffset + index, dst, ARRAY_BASE_OFFSET + offset, length);
    }

    public void getBytes(int index, MutableBuffer dstBuffer, int dstIndex, int length) {
        dstBuffer.putBytes(dstIndex, this, index, length);
    }

    public void getBytes(int index, ByteBuffer dstBuffer, int length) {
        int dstOffset = dstBuffer.position();
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, length);
            BufferUtil.boundsCheck(dstBuffer, (long) dstOffset, length);
        }

        byte[] dstByteArray;
        long dstBaseOffset;
        if (dstBuffer.hasArray()) {
            dstByteArray = dstBuffer.array();
            dstBaseOffset = ARRAY_BASE_OFFSET + dstBuffer.arrayOffset();
        } else {
            dstByteArray = null;
            dstBaseOffset = ((sun.nio.ch.DirectBuffer) dstBuffer).address();
        }

        UNSAFE.copyMemory(byteArray, addressOffset + index, dstByteArray, dstBaseOffset + dstOffset, length);
        dstBuffer.position(dstBuffer.position() + length);
    }

    public void putBytes(int index, byte[] src) {
        putBytes(index, src, 0, src.length);
    }

    public void putBytes(int index, byte[] src, int offset, int length) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, length);
            BufferUtil.boundsCheck(src, offset, length);
        }

        UNSAFE.copyMemory(src, ARRAY_BASE_OFFSET + offset, byteArray, addressOffset + index, length);
    }

    public void putBytes(int index, ByteBuffer srcBuffer, int length) {
        int srcIndex = srcBuffer.position();
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, length);
            BufferUtil.boundsCheck(srcBuffer, (long) srcIndex, length);
        }

        putBytes(index, srcBuffer, srcIndex, length);
        srcBuffer.position(srcIndex + length);
    }

    public void putBytes(int index, ByteBuffer srcBuffer, int srcIndex, int length) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, length);
            BufferUtil.boundsCheck(srcBuffer, srcIndex, length);
        }

        byte[] srcByteArray;
        long srcBaseOffset;
        if (srcBuffer.hasArray()) {
            srcByteArray = srcBuffer.array();
            srcBaseOffset = ARRAY_BASE_OFFSET + srcBuffer.arrayOffset();
        } else {
            srcByteArray = null;
            srcBaseOffset = ((sun.nio.ch.DirectBuffer) srcBuffer).address();
        }

        UNSAFE.copyMemory(srcByteArray, srcBaseOffset + srcIndex, byteArray, addressOffset + index, length);
    }

    public void putBytes(int index, Buffer srcBuffer) {
        putBytes(index, srcBuffer, 0, srcBuffer.capacity());
    }

    public void putBytes(int index, Buffer srcBuffer, int srcIndex, int length) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, length);
            srcBuffer.boundsCheck(srcIndex, length);
        }

        UNSAFE.copyMemory(
                srcBuffer.byteArray(),
                srcBuffer.addressOffset() + srcIndex,
                byteArray,
                addressOffset + index,
                length);
    }

    public char getChar(int index, ByteOrder byteOrder) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_SHORT);
        }

        char bits = UNSAFE.getChar(byteArray, addressOffset + index);
        if (NATIVE_BYTE_ORDER != byteOrder) {
            bits = (char) Short.reverseBytes((short) bits);
        }

        return bits;
    }

    public void putChar(int index, char value, ByteOrder byteOrder) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_SHORT);
        }

        char bits = value;
        if (NATIVE_BYTE_ORDER != byteOrder) {
            bits = (char) Short.reverseBytes((short) bits);
        }

        UNSAFE.putChar(byteArray, addressOffset + index, bits);
    }

    public char getChar(int index) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_CHAR);
        }

        return UNSAFE.getChar(byteArray, addressOffset + index);
    }

    public void putChar(int index, char value) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_CHAR);
        }

        UNSAFE.putChar(byteArray, addressOffset + index, value);
    }

    public char getCharVolatile(int index) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_CHAR);
        }

        return UNSAFE.getCharVolatile(byteArray, addressOffset + index);
    }

    public void putCharVolatile(int index, char value) {
        if (SHOULD_BOUNDS_CHECK) {
            boundsCheck0(index, SIZE_OF_CHAR);
        }

        UNSAFE.putCharVolatile(byteArray, addressOffset + index, value);
    }

    public void boundsCheck(int index, int length) {
        boundsCheck0(index, length);
    }

    private void boundsCheck(int index) {
        if (index < 0 || index >= capacity) {
            throw new IndexOutOfBoundsException(String.format("index=%d, capacity=%d", index, capacity));
        }
    }

    private void boundsCheck0(int index, int length) {
        long resultingPosition = index + (long) length;
        if (index < 0 || resultingPosition > capacity) {
            throw new IndexOutOfBoundsException(String.format("index=%d, length=%d, capacity=%d", index, length, capacity));
        }
    }

    public static UnsafeBuffer allocateHeap(int capacity) {
        return new UnsafeBuffer(ByteBuffer.allocate(capacity));
    }

    public static UnsafeBuffer allocateDirect(int capacity) {
        return new UnsafeBuffer(ByteBuffer.allocateDirect(capacity));
    }

}