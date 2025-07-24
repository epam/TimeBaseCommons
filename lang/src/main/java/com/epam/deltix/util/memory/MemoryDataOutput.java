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
package com.epam.deltix.util.memory;

import com.epam.deltix.dfp.Decimal64;
import com.epam.deltix.dfp.Decimal64Utils;
import com.epam.deltix.util.BitUtil;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;

/**
 *  Equivalent of DataOutputStream wrapped around
 *  ByteArrayOutputStream optimized for extreme performance. This class uses
 *  no virtual method calls and presents a non-virtual public API.
 */
public final class MemoryDataOutput {
    private static final VarHandle LE_INT_HANDLE = MethodHandles.byteArrayViewVarHandle(int[].class, ByteOrder.LITTLE_ENDIAN);

    private byte []             mBuffer;
    private int                 mPos = 0;
    private int                 mSize = 0;
    
    public MemoryDataOutput () {
        this (4096);
    }
    
    public MemoryDataOutput (int capacity) {
        mBuffer = new byte [capacity];
    }
    
    public void          makeRoom (int space) {
        ensureSize (mPos + space);
    }

    /**
     * Extends buffer capacity to accommodate the specified space after the current position.
     * Unlike {@link #makeRoom(int)} this method does not change the logical size of the buffer.
     */
    public void ensureSpace(int space) {
        if (mPos + space > mBuffer.length) {
            extendBuffer(mSize, mPos + space);
        }
    }

    /**
     * Increases "logical" size up to specified value.
     */
    public void          ensureSize (int minSize) {
        if (mSize < minSize) {
            // Increase "logical" size (value of "mSize")
            final int   oldSize = mSize;
            
            mSize = minSize;
        
            int         currentSize = mBuffer.length;

            if (currentSize < minSize) {
                extendBuffer(oldSize, minSize);
            }
        }
    }

    /**
     * Increases "physical" size (size of "mBuffer") up to specified value.
     * Does not change "logical" buffer size (mSize).
     */
    private void ensureBufferSize(int minSize) {
        int currentSize = mBuffer.length;
        if (currentSize < minSize) {
            extendBuffer(mSize, minSize);
        }
    }

    private void extendBuffer(int oldSize, int minSize) {
        assert oldSize < minSize;
        int newSize = BitUtil.nextPowerOfTwo(minSize);

        byte [] newBuffer = new byte [newSize];
        System.arraycopy (mBuffer, 0, newBuffer, 0, oldSize);
        mBuffer = newBuffer;
    }

    /**
     *  Reset buffer with the size of 0.
     */
    public void             reset () {
        mPos = 0;
        mSize = 0;
    }
    
    /**
     *  Reset buffer with initial size.
     */
    public void             reset (int size) {
        mPos = 0;
        mSize = 0;
        makeRoom (size);
    }
    
    /**
     *  Seek to absolute position
     *  @param offset
     */
    public void           seek (int offset) {
        mPos = offset;
        makeRoom (0);
    }
    
    public void             insertSpace (int offset, int length) {
        final int   oldSize = mSize;
        ensureSize (oldSize + length);
        System.arraycopy (mBuffer, offset, mBuffer, offset + length, oldSize - offset);
    }
    
    public int            getPosition () {
        return (mPos);
    }
    
    public void           skip (int numBytes) {
        makeRoom (numBytes);
        mPos += numBytes;
    }

    /**
     * Advances current position without checking if there is enough space in the buffer.
     * <p>Should be used only after {@link #makeRoom(int)} call to ensure that there is enough space.
     */
    public void           skipUnsafe(int numBytes) {
        mPos += numBytes;
    }

    public byte []        getBuffer () {
        return (mBuffer);
    }
    
    public int            getSize () {
        return (mSize);
    }
    
    public void           writeString (CharSequence str) {
        if (str == null)
            writeNullString ();
        else
            writeStringNonNull (str, 0, str.length ());
    }
    
    public void           writeString (CharSequence str, int start, int strlen) {
        if (str == null) 
            writeNullString ();
        else
            writeStringNonNull (str, start, strlen);
    }

    public void           writeNullString () {
        writeUnsignedShort (0xFFFF);
    }
    
    public void           writeStringNonNull (CharSequence str) {
        writeStringNonNull (str, 0, str.length ());
    }

    /**
     * @deprecated Use {@link #writeStringNonNull(CharSequence, int, int)} instead.
     */
    @Deprecated(forRemoval = true)
    public void           writeStringNonNullOld (CharSequence str, int start, int endIndex) {
        int     utflen = 0;
        int     c, count = 0;

        /* use charAt instead of copying String to char array */
        for (int i = start; i < endIndex; i++) {
            c = str.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F)) 
                utflen++;
            else if (c > 0x07FF) 
                utflen += 3;
            else 
                utflen += 2;
        }

        if (utflen >= 0xFFFF)
            throw new RuntimeException ("Encoded string too long: " + utflen + " bytes");

        makeRoom (utflen + 2);
        writeUnsignedShort (utflen);
	        
        int i=0;
        
        for (i=start; i<endIndex; i++) {
           c = str.charAt (i);
           
           if (!((c >= 0x0001) && (c <= 0x007F))) 
               break;
           
           mBuffer [mPos++] = (byte) c;
        }

        for (; i < endIndex; i++) {
            c = str.charAt(i);
            
            if ((c >= 0x0001) && (c <= 0x007F)) 
                mBuffer [mPos++] = (byte) c;
            else if (c > 0x07FF) {
                mBuffer [mPos++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
                mBuffer [mPos++] = (byte) (0x80 | ((c >>  6) & 0x3F));
                mBuffer [mPos++] = (byte) (0x80 | (c & 0x3F));
            }
            else {
                mBuffer [mPos++] = (byte) (0xC0 | ((c >>  6) & 0x1F));
                mBuffer [mPos++] = (byte) (0x80 | (c & 0x3F));
            }
        }
    }

    public void writeStringNonNull(CharSequence str, int startIndex, int endIndex) {
        // Ensure enough space for the worst case
        ensureSpace(2 + (endIndex - startIndex) * 3);
        int startPos = mPos;

        int pos = startPos + 2; // reserve space for length

        for (int i = startIndex; i < endIndex; i++) {
            int c = str.charAt(i);

            if ((c >= 0x0001) && (c <= 0x007F)) {
                mBuffer[pos] = (byte) c;
                pos++;
            } else if (c > 0x07FF) {
                mBuffer[pos] = (byte) (0xE0 | ((c >> 12) & 0x0F));
                mBuffer[pos + 1] = (byte) (0x80 | ((c >> 6) & 0x3F));
                mBuffer[pos + 2] = (byte) (0x80 | (c & 0x3F));
                pos += 3;
            } else {
                mBuffer[pos] = (byte) (0xC0 | ((c >> 6) & 0x1F));
                mBuffer[pos + 1] = (byte) (0x80 | (c & 0x3F));
                pos += 2;
            }
        }

        int utfLen = pos - startPos - 2;
        if (utfLen >= 0xFFFF) {
            throw new RuntimeException("Encoded string too long: " + utfLen + " bytes");
        }
        DataExchangeUtils.writeUnsignedShort(mBuffer, startPos, utfLen);

        mPos = pos;
        mSize = Math.max(mSize, mPos);
    }

    public void           write (byte[] b, int off, int len) {
        makeRoom (len);
        System.arraycopy (b, off, mBuffer, mPos, len);
        mPos += len;
    }

    public void           write (byte[] b) {
        write (b, 0, b.length);
    }

    public void           writeByte (int v) {
        writeByte ((byte) v);
    }
    
    public void           writeByte (long v) {
        writeByte ((byte) v);
    }
    
    public void           writeByte (byte v) {
        makeRoom (1);
        writeByteUnsafe(v);
    }

    /**
     * Writes byte to buffer without checking if there space for this byte available.
     * This call *must* be prepended with corresponding {@link #makeRoom(int)} call.
     */
    private void           writeByteUnsafe (byte v) {
        mBuffer [mPos] = v;
        mPos++;
    }

    /**
     * Writes byte to buffer without checking if there space for this byte available.
     * This call *must* be prepended with corresponding {@link #makeRoom(int)} call.
     *
     * @deprecated Use direct access to buffer via {@link #getBuffer()} and advance position using {@link #skipUnsafe} instead.
     */
    @Deprecated
    public void           writeByteUnsafe (long v) {
        writeByteUnsafe((byte) v);
    }

    public void           writeUnsignedByte (int v) {
        writeByte (v);
    }

    public void           writeChar (char v) {
        makeRoom (2);
        DataExchangeUtils.writeChar (mBuffer, mPos, v);
        mPos += 2;
    }

    public void           writeCharInverted (char v) {
        makeRoom (2);
        DataExchangeUtils.writeCharInvertBytes (mBuffer, mPos, v);
        mPos += 2;
    }

    public void           writeInt (int v) {
        makeRoom (4);
        DataExchangeUtils.writeInt (mBuffer, mPos, v);
        mPos += 4;
    }

    public void           writeIntInverted (int v) {
        makeRoom (4);
        DataExchangeUtils.writeIntInvertBytes (mBuffer, mPos, v);
        mPos += 4;
    }

    public void           writeShort (short v) {
        makeRoom (2);
        DataExchangeUtils.writeShort (mBuffer, mPos, v);
        mPos += 2;
    }

    public void           writeShortInverted (short v) {
        makeRoom (2);
        DataExchangeUtils.writeShortInvertBytes (mBuffer, mPos, v);
        mPos += 2;
    }

    public void           writeShort (int v) {
        writeShort ((short) v);
    }

    public void           writeUnsignedShort (int v) {
        makeRoom (2);
        DataExchangeUtils.writeUnsignedShort (mBuffer, mPos, v);
        mPos += 2;
    }

    public void           writeBoolean (boolean v) {
        makeRoom (1);
        mBuffer [mPos] = v ? (byte) 1 : 0;
        mPos += 1;
    }

    public void           writeLong (long v) {
        makeRoom (8);
        DataExchangeUtils.writeLong (mBuffer, mPos, v);
        mPos += 8;
    }

    public void           writeLong (Decimal64 v) {
        writeDecimal64(v);
    }

    public void           writeLongInverted (long v) {
        makeRoom (8);
        DataExchangeUtils.writeLongInvertBytes (mBuffer, mPos, v);
        mPos += 8;
    }

    public void           writeLong48 (long v) {
        makeRoom (6);
        DataExchangeUtils.writeLong48 (mBuffer, mPos, v);
        mPos += 6;
    }

    /**
     *  Writes an unsigned int in the smallest possible number of bytes.
     */
    public void           writePackedUnsignedInt (int v) {
        if ((v & 0xC0000000) != 0)
            throw new IllegalArgumentException ("High 2 bits must be 0; v=" + v);
        
        makeRoom (1);
        
        int         pos = mPos++;
        int         addlPos = mPos;
        int         low6bits = v & 0x3F;
        
        v = v >>> 6; 
        
        while (v != 0) {   
            makeRoom (1);
            mBuffer [mPos++] = (byte) (v & 0xFF);
            v = v >>> 8;
        }
        
        mBuffer [pos] = (byte) (low6bits | ((mPos - addlPos) << 6));
    }

    /**
     *  Writes an unsigned long in the smallest possible number of bytes.
     */
    public void           writePackedUnsignedLong (long v) {
        if ((v & 0xE000000000000000L) != 0)
            throw new IllegalArgumentException ("High 3 bits must be 0; v=" + v);

        makeRoom (1);

        int         pos = mPos++;
        int         addlPos = mPos;
        int         low5bits = ((int) v) & 0x1F;
        
        v = v >>> 5; 
        
        while (v != 0) {   
            makeRoom (1);
            mBuffer [mPos++] = (byte) (v & 0xFF);
            v = v >>> 8;
        }
        
        mBuffer [pos] = (byte) (low5bits | ((mPos - addlPos) << 5));
    }

    public void           writeDouble (double v) {
        makeRoom (8);
        DataExchangeUtils.writeDouble (mBuffer, mPos, v);
        mPos += 8;
    }

    public void           writeDoubleInverted (double v) {
        makeRoom (8);
        DataExchangeUtils.writeDoubleInvertBytes (mBuffer, mPos, v);
        mPos += 8;
    }

    public void           writeFloat (float v) {
        makeRoom (4);
        DataExchangeUtils.writeFloat (mBuffer, mPos, v);
        mPos += 4;
    }

    public void           writeFloatInverted (float v) {
        makeRoom (4);
        DataExchangeUtils.writeFloatInvertBytes (mBuffer, mPos, v);
        mPos += 4;
    }

    public static final int         MAX_SCALE_EXP = 15;
    
    private static final long []    SCALES = new long [MAX_SCALE_EXP];
    private static final double []  DSCALES = new double [MAX_SCALE_EXP];
    
    static {
        long v = 1;
        
        for (int ii = 0; ii < MAX_SCALE_EXP; ii++) {
            SCALES [ii] = v;
            DSCALES [ii] = v;
            v *= 10;
        }
    }
    
    private boolean             writeSpecialScaledDouble (double v) {
        if (v == 0) {
            writeByte (0);
            return (true);
        }
        else if (Double.isNaN (v)) {
            writeByte (0x1F);
            return (true);
        }
        else if (v == Double.NEGATIVE_INFINITY) {
            writeByte (0x2F);
            return (true);
        }
        else if (v == Double.POSITIVE_INFINITY) {
            writeByte (0x3F);
            return (true);
        }
        else
            return (false);
    }
    
    public void           writeScaledDouble (double v, int precision) {
        if (writeSpecialScaledDouble (v))
            return;
        
        int                 signBit;
        
        if (v < 0) {
            signBit = 0x80;
            v = -v;
        }
        else
            signBit = 0;
            
        long                scale = SCALES [precision];
        long                lv = Math.round (v * scale);
        int                 exp = precision;
        
        while (exp > 1) {
            if ((lv % 10) != 0)
                break;
            
            lv = lv / 10;
            exp--;
        }

        // Prevent 8-byte significant from being written
        if ((lv & 0xFF00000000000000L) != 0) {
            writeByte (0x0F);
            writeDouble (v);
            return;
        }

        //  Discount leading zero bytes in x
        makeRoom (1);

        int                 headerPos = mPos++;
        int                 numBytes = writeLongBytes (lv);

        assert numBytes < 8;
        
        mBuffer [headerPos] = (byte) (exp | (numBytes << 4) | signBit);
    }

    public void           oldWriteScaledDouble (double v) {
        if (writeSpecialScaledDouble (v))
            return;

        int                 signBit;
        double              x;

        if (v < 0) {
            signBit = 0x80;
            x = -v;
        }
        else {
            signBit = 0;
            x = v;
        }

        int                 exp = 0;
        long                lv;

        for (;;) {
            double          scale = DSCALES [exp];

            lv = Math.round (x * scale);

            // Prevent 8-byte significant from being written
            if ((lv & 0xFF00000000000000L) != 0) {
                writeByte (0x0F);
                writeDouble (v);
                return;
            }

            if (x == lv / scale)
                break;

            exp++;

            if (exp == MAX_SCALE_EXP) {
                writeByte (0x0F);
                writeDouble (v);
                return;
            }
        }

        //  Discount leading zero bytes in x
        makeRoom (1);

        int                 headerPos = mPos++;
        int                 numBytes = writeLongBytes (lv);

        assert numBytes < 8;

        mBuffer [headerPos] = (byte) (exp | (numBytes << 4) | signBit);
    }

    public void           writeDecimal64 (double v) {
        if (Double.isNaN(v))
            writeLong(Decimal64Utils.NULL);
        else
            writeLong(Decimal64Utils.fromDouble(v));
    }

    public void           writeDecimal64 (Decimal64 v) {
        if (v == null)
            writeLong(Decimal64Utils.NULL);
        else
            writeLong(Decimal64.toUnderlying(v));
    }

    public void           writeDecimal64 (long v) {
        writeLong(v);
    }

    public void           writeScaledDouble (double v) {
        if (writeSpecialScaledDouble (v))
            return;

        int                 signBit;
        double              x;

        if (v < 0) {
            signBit = 0x80;
            x = -v;
        }
        else {
            signBit = 0;
            x = v;
        }

        int                 exp = 0;
        long                lv;

        for (;;) {
            double          scale = DSCALES [exp];

            lv = (long)(x * scale);

            // Prevent 8-byte significant from being written
            if ((lv & 0xFF00000000000000L) != 0) {
                writeByte (0x0F);
                writeDouble (v);
                return;
            }

            if (x == lv / scale || x == (lv + 1) / scale) {
                lv = Math.round (x * scale);
                if (x == lv / scale)
                    break;
            }

            exp++;

            if (exp == MAX_SCALE_EXP) {
                writeByte (0x0F);
                writeDouble (v);
                return;
            }
        }

        //  Discount leading zero bytes in x
        makeRoom (1);

        int                 headerPos = mPos++;
        int                 numBytes = writeLongBytes (lv);

        assert numBytes < 8;

        mBuffer [headerPos] = (byte) (exp | (numBytes << 4) | signBit);
    }

//    public long             round(double value, double scale) {
//        long l = (long) value;
//        double fraction = value - l;
//
//        long top = (long) (scale * 10 * fraction + 5);
//        long v = (long) (scale * fraction);
//
//        return (long)(value * scale) + (top - v * 10 > 10 ? 1 : 0);
//    }

    /**
     *  Writes out a long in LSBF (Little-Endian) order, and stops when all
     *  remaining bytes are 0.
     * 
     *  @param v The long to write.
     *  @return  The number of bytes written, between 0 .. 8 inclusively.
     */
    public int                      writeLongBytes (long v) {
        // Get number of non-zero bytes
        int numBytes = 8 - (Long.numberOfLeadingZeros(v) >>> 3);

        int pos = mPos;
        int newPos = pos + numBytes;
        // Expand buffer enough to cover the worst case
        ensureBufferSize(newPos);

        // Note: while generally this implementation can benefit from using VarHandle to copy 8 bytes at once as long,
        // in practice this method is called only with values that fit into 7 bytes.
        // So it's not useful to have a special case for 8 bytes.
        int i = 0;
        if (numBytes >= Integer.BYTES) {
            // Write first 4 bytes (lowest)
            LE_INT_HANDLE.set(mBuffer, pos, (int) v);
            i += Integer.BYTES;
        }
        for (; i < numBytes; i++) {
            mBuffer[pos + i] = (byte) (v >>> (i * Byte.SIZE));
        }

        mPos = newPos;
        mSize = Math.max(mSize, newPos);
        return numBytes;
    }
    
    public byte []        toByteArray () {
        byte []     ret = new byte [mPos];
        System.arraycopy (mBuffer, 0, ret, 0, mPos);
        return (ret);
    }
}