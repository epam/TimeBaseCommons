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

import com.epam.deltix.dfp.Decimal64Utils;
import com.epam.deltix.util.io.UncheckedIOException;

/**
 *  Equivalent of DataInputStream wrapped around
 *  ByteArrayInputStream optimized for extreme performance. This class uses
 *  no virtual method calls and presents a non-virtual public API.
 */
public final class MemoryDataInput {
    // Contract: 0 <= mStart <= mPos <= mLimit <= mBuffer.length
    private byte []         mBuffer;
    private int             mStart; // Low boundary (inclusive). First index of mBuffer that is accessible by this instance of MemoryDataInput. Similar to ByteBuffer.offset field.
    private int             mPos; // Current position (includes mStart offset).
    private int             mLimit; // Hi boundary (exclusive, includes mStart offset).

    private StringBuilder   mStringBuilder;
        
    public MemoryDataInput (int capacity) {
        mBuffer = new byte [capacity];
        mLimit = 0;
        mPos = 0;
    }
    
    public MemoryDataInput () {
        mBuffer = null;
        mLimit = 0;
        mPos = 0;
    }
    
    public MemoryDataInput (byte [] buffer, int offset, int length) {
        setBytes (buffer, offset, length);
    }
    
    public MemoryDataInput (byte [] buffer) {
        setBytes (buffer);
    }
    
    public MemoryDataInput (MemoryDataOutput mout) {
        setBytes (mout);
    }
    
    public void       setBytes (byte [] buffer, int offset, int length) {
        assert (buffer == null ? length == 0 : offset + length <= buffer.length) : "Insufficient buffer length offset: " + offset + "; length: " + length;
        
        mBuffer = buffer;
        mLimit = offset + length;
        mStart = mPos = offset;
    }
    

    public void       setBytes (byte [] buffer) {
        mBuffer = buffer;
        mLimit = buffer.length;
        mStart = mPos = 0;
    }
    
    public void       setBytes (MemoryDataOutput out) {
        setBytes (out.getBuffer (), 0, out.getSize ());
    }
    
    public void       reset (int newSize) {
        assert newSize <= mBuffer.length :
            "Insufficient buffer length " + mBuffer.length +
            "; newSize: " + newSize;

        mLimit = newSize;
        mStart = mPos = 0;
    }
    
    public void       setLimit (int newSize) {
        assert newSize <= mBuffer.length :
            "Insufficient buffer length " + mBuffer.length +
            "; newSize: " + newSize;

        mLimit = newSize;
    }
    
    public byte []    getBytes () {
        return (mBuffer);
    }
        
    /**
     *  Returns the current position relative to start.
     */
    public int        getPosition () {
        return (mPos - mStart);
    }
    
    public int        getStart () {
        return (mStart);
    }

    public int        getLength () {
        return (mLimit - mStart);
    }

    /**
     *  Distance from current position until the end
     */
    public int        getAvail () {
        return (mLimit - mPos);
    }

    /**
     *  Do we have any available bytes?
     */
    public boolean    hasAvail () {
        return (mPos < mLimit);
    }

    /**
     *  Returns the current position relative to the byte buffer.
     */
    public int        getCurrentOffset () {
        return (mPos);
    }

    public boolean    checkAvailable (int n) {
        if (getAvail () < n)
            throw new AssertionError ("Cannot read " + n + " bytes; available: " + getAvail ());

        return (true);
    }

    public void       readFully (byte[] b, int off, int len) {
        assert checkAvailable (len);
        
        System.arraycopy (mBuffer, mPos, b, off, len);
        mPos += len;
    }

    public void       readFully (byte[] b) {
        readFully (b, 0, b.length);
    }

    public void       skipBytes (int n) {
        assert checkAvailable (n);
        mPos += n;
    }

    public void       skipBytesUpTo (int n) {
        mPos += n;
        
        if (mPos > mLimit)
            mPos = mLimit;
    }

    public void       seekOffset (int n) {
        assert n <= mLimit :
            "Cannot seek to " + n + " bytes; limit: " + mLimit;

        assert n >= mStart : 
            "Cannot set position to " + n + " before start=" + mStart;
       
        mPos = n;
    }

    public void       seek (int n) {
        assert n <= getLength () :
            "Cannot seek to " + n + " bytes; length: " + getLength ();

        mPos = mStart + n;
    }

    public int        readUnsignedShort () {
        assert checkAvailable (2);

        int     ret = DataExchangeUtils.readUnsignedShort (mBuffer, mPos);
        mPos += 2;
        return (ret);
    }

    public long       readUnsignedInt () {
        assert checkAvailable (4);

        long    ret = DataExchangeUtils.readUnsignedInt (mBuffer, mPos);
        mPos += 4;
        return (ret);
    }

    public int        readUnsignedByte () {
        assert checkAvailable (1);

        return (mBuffer [mPos++] & 0xFF);
    }

    public boolean    readBoolean () {
        assert checkAvailable (1);

        return (mBuffer [mPos++] == 1);
    }

    public byte       readByte () {
        assert checkAvailable (1);

        return (mBuffer [mPos++]);
    }

    public char       readChar () {
        assert checkAvailable (2);

        char    ret = DataExchangeUtils.readChar (mBuffer, mPos);
        mPos += 2;
        return (ret);
    }

    public double     readDouble () {
        assert checkAvailable (8);

        double    ret = DataExchangeUtils.readDouble (mBuffer, mPos);
        mPos += 8;
        return (ret);
    }

    public float      readFloat () {
        assert checkAvailable (4);

        float    ret = DataExchangeUtils.readFloat (mBuffer, mPos);
        mPos += 4;
        return (ret);
    }

    public int        readInt () {
        assert checkAvailable (4);

        int    ret = DataExchangeUtils.readInt (mBuffer, mPos);
        mPos += 4;
        return (ret);
    }

    public long       readLong () {
        assert checkAvailable (8);

        long    ret = DataExchangeUtils.readLong (mBuffer, mPos);
        mPos += 8;
        return (ret);
    }

    public long       readLong48 () {
        assert checkAvailable (6);

        long    ret = DataExchangeUtils.readLong48 (mBuffer, mPos);
        mPos += 6;
        return (ret);
    }

    public long       readLongUnsignedByte () {
        assert checkAvailable (1);

        return (((long) mBuffer [mPos++]) & 0xFFL);
    }
    
    public long       readPackedUnsignedLong () {        
        int     head = readByte ();
        long    ret = head & 0x1F;
        int     numAddlBytes = (head >>> 5) & 0x7;
        
        switch (numAddlBytes) {
            case 7:
                ret |= readLongUnsignedByte () << 5;
                ret |= readLongUnsignedByte () << 13;
                ret |= readLongUnsignedByte () << 21;
                ret |= readLongUnsignedByte () << 29;
                ret |= readLongUnsignedByte () << 37;
                ret |= readLongUnsignedByte () << 45;
                ret |= readLongUnsignedByte () << 53;
                break;
                
            case 6:
                ret |= readLongUnsignedByte () << 5;
                ret |= readLongUnsignedByte () << 13;
                ret |= readLongUnsignedByte () << 21;
                ret |= readLongUnsignedByte () << 29;
                ret |= readLongUnsignedByte () << 37;
                ret |= readLongUnsignedByte () << 45;
                break;
                
            case 5:
                ret |= readLongUnsignedByte () << 5;
                ret |= readLongUnsignedByte () << 13;
                ret |= readLongUnsignedByte () << 21;
                ret |= readLongUnsignedByte () << 29;
                ret |= readLongUnsignedByte () << 37;
                break;
                
            case 4:
                ret |= readLongUnsignedByte () << 5;
                ret |= readLongUnsignedByte () << 13;
                ret |= readLongUnsignedByte () << 21;
                ret |= readLongUnsignedByte () << 29;
                break;
                
            case 3:
                ret |= readLongUnsignedByte () << 5;
                ret |= readLongUnsignedByte () << 13;
                ret |= readLongUnsignedByte () << 21;
                break;
                
            case 2:
                ret |= readLongUnsignedByte () << 5;
                ret |= readLongUnsignedByte () << 13;
                break;
                
            case 1:
                ret |= readLongUnsignedByte () << 5;
                break;
                
            case 0:
                break;               
        }

        return (ret);
    }

    public int       readPackedUnsignedInt () {        
        int     head = readByte ();
        int     ret = head & 0x3F;
        int     numAddlBytes = (head >>> 6) & 0x3;
        
        switch (numAddlBytes) {
            case 3:
                ret |= readUnsignedByte () << 6;
                ret |= readUnsignedByte () << 14;
                ret |= readUnsignedByte () << 22;
                break;
                
            case 2:
                ret |= readUnsignedByte () << 6;
                ret |= readUnsignedByte () << 14;
                break;
                
            case 1:
                ret |= readUnsignedByte () << 6;
                break;
                
            case 0:
                break;               
        }
        
        return (ret);
    }

    public short      readShort () {
        assert checkAvailable (2);

        short    ret = DataExchangeUtils.readShort (mBuffer, mPos);
        mPos += 2;
        return (ret);
    }

    public String         readString () {
        CharSequence   sb = readCharSequence ();
        
        return (sb == null ? null : sb.toString ());
    }
    
    /**
     *  Uses an internal buffer. The returned value is valid until the next call to
     *  this method. Returns null if the string value is null. 
     */
    public CharSequence   readCharSequence () {
        if (mStringBuilder == null)
            mStringBuilder = new StringBuilder ();
        
        return (readStringBuilder (mStringBuilder));
    }
    
        
    /**
     *  Returns null if the string value is null. 
     */
    public StringBuilder  readStringBuilder (StringBuilder sb) {
        sb.setLength (0);
        return (appendToStringBuilder (sb));
    }

    public void           skipCharSequence () {
        int         utflen = readUnsignedShort ();
        
        if (utflen != 0xFFFF)
            skipBytes (utflen);
    }
    
    /**
     *  Appends to the StringBuilder
     */
    public StringBuilder    appendToStringBuilder (StringBuilder sb) {
        int         utflen = readUnsignedShort ();
        
        if (utflen == 0xFFFF)
            return (null);
        
        if (utflen == 0)
            return (sb);
        
        int c;
        int char2, char3, char4;
        int count = 0;        
        
        for (;;) {
            c = (int) readByte () & 0xff; // convert to unsigned
            if (c > 127) break; // we have character takes more than 1 byte

            count++;
            sb.append ((char) c);
            
            if (count >= utflen)
                return (sb);
        }
        //  If we are here, we have broken out of the previous loop and there is an
        //  unhandled escape character in variable c.        
        for (;;) {
            switch ((0xff & c) >> 4) {
                case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
                    /* 0xxxxxxx*/
                    count++;
                    sb.append ((char)c);
                    break;
                    
                case 12: case 13:
                    /* 110x xxxx   10xx xxxx*/
                    count += 2;
                    
                    if (count > utflen)
                        throw new UncheckedIOException(
                            "malformed input: partial character at end"
                        );
                    
                    char2 = readByte ();
                    
                    if ((char2 & 0xC0) != 0x80)
                        throw new UncheckedIOException (
                            "malformed input around byte " + count
                        ); 
                    
                    sb.append ((char)(((c & 0x1F) << 6) | (char2 & 0x3F)));  
                    break;
                    
                case 14:
                    /* 1110 xxxx  10xx xxxx  10xx xxxx */
                    count += 3;
                    if (count > utflen)
                        throw new UncheckedIOException(
                            "malformed input: partial character at end"
                        );
                    char2 = readByte ();
                    char3 = readByte ();
                    if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
                        throw new UncheckedIOException(
                            "malformed input around byte " + (count-1));
                    sb.append ((char)(((c & 0x0F) << 12) |
                                                    ((char2 & 0x3F) << 6)  |
                                                     (char3 & 0x3F)));
                    break;

                default: // support for .NET
                    /* 10xx xxxx,  1111 xxxx */
                    count += 4;
                    char2 = readByte ();
                    char3 = readByte ();
                    char4 = readByte ();
                    if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80) || ((char4 & 0xC0) != 0x80))
                        throw new UncheckedIOException(
                                "malformed input around byte " + (count-1));

                    sb.append ((char) (((c & 0xF7) << 18) | ((char2 & 0x3F) << 12)  | ((char3 & 0x3F) << 6) | (char4 & 0x3F)));
                    break;
            }
                        
            if (count >= utflen)
                break;
            
            c = readByte ();
        }        
        
        return (sb);
    }

    private static final double []    SCALES = new double [MemoryDataOutput.MAX_SCALE_EXP];
    
    static {
        long v = 1;
        
        for (int ii = 0; ii < MemoryDataOutput.MAX_SCALE_EXP; ii++) {
            SCALES [ii] = v;
            v *= 10;
        }
    }

    public double       readDecimal64 () {
        long v = readLong();
        return v == Decimal64Utils.NULL ? Double.NaN : Decimal64Utils.toDouble(v);
    }
    
    public double       readScaledDouble () {
        int                 header = readByte ();
        
        switch (header) {
            case 0x00:      return (0);
            case 0x1F:      return (Double.NaN);
            case 0x2F:      return (Double.NEGATIVE_INFINITY);
            case 0x3F:      return (Double.POSITIVE_INFINITY);
            case 0x0F:      return (readDouble ());
        }
        
        int                 numBytes = (header >> 4) & 0x07;
        long                lv = 0;
        int                 shift = 0;
        
        while (numBytes > 0) {
            lv |= (readLongUnsignedByte () << shift);
            numBytes--;
            shift += 8;
        }
        
        double              scale = SCALES [header & 0x0F];
        
        if ((header & 0x80) != 0)
            scale = -scale;
        
        return (lv / scale);
    }
}