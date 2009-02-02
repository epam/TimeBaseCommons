package deltix.util.memory;

import deltix.util.collections.generated.ByteArrayList;
import deltix.util.io.ByteArrayOutputStreamEx;
import deltix.util.io.UncheckedIOException;
import deltix.qsrv.hf.pub.md.IntegerDataType;

/**
 *  Equivalent of DataInputStream wrapped around
 *  ByteArrayInputStream optimized for extreme performance. This class uses
 *  no virtual method calls and presents a non-virtual public final API.
 */
public class MemoryDataInput {
    private byte []         mBuffer;
    private int             mStart;
    private int             mPos;
    private int             mLimit;
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
    
    public MemoryDataInput (ByteArrayList list) {
        setBytes (list);
    }
    
    public MemoryDataInput (ByteArrayOutputStreamEx buffer) {
        setBytes (buffer);
    }
    
    public MemoryDataInput (MemoryDataOutput mout) {
        setBytes (mout);
    }
    
    public final void       setBytes (byte [] buffer, int offset, int length) {
        mBuffer = buffer;
        mLimit = offset + length;
        mStart = mPos = offset;
    }
    
    public final void       setBytes (ByteArrayList buffer) {
        mBuffer = buffer.getInternalBuffer ();
        mLimit = buffer.size ();
        mPos = 0;
    }
    
    public final void       setBytes (ByteArrayOutputStreamEx buffer) {
        mBuffer = buffer.getInternalBuffer ();
        mLimit = buffer.size ();
        mPos = 0;
    }
    
    public final void       setBytes (byte [] buffer) {
        mBuffer = buffer;
        mLimit = buffer.length;
        mPos = 0;
    }
    
    public final void       setBytes (MemoryDataOutput out) {
        mBuffer = out.getBuffer ();
        mLimit = out.getSize ();
        mPos = 0;
    }
    
    public final void       reset (int newSize) {
        mLimit = newSize;
        mPos = 0;
    }
    
    public final byte []    getBytes () {
        return (mBuffer);
    }
        
    /**
     *  Returns the current position relative to start.
     */
    public final int        getPosition () {
        return (mPos - mStart);
    }
    
    public final int        getLength () {
        return (mLimit - mStart);
    }
    
    public final void       readFully (byte[] b, int off, int len) {
        System.arraycopy (mBuffer, mPos, b, off, len);
        mPos += len;
    }

    public final void       readFully (byte[] b) {
        readFully (b, 0, b.length);
    }

    public final void       skipBytes (int n) {
        mPos += n;
    }

    public final void       seek (int n) {
        mPos = mStart + n;
    }

    public final int        readUnsignedShort () {
        int     ret = DataExchangeUtils.readUnsignedShort (mBuffer, mPos);
        mPos += 2;
        return (ret);
    }

    public final long       readUnsignedInt () {
        long    ret = DataExchangeUtils.readUnsignedInt (mBuffer, mPos);
        mPos += 4;
        return (ret);
    }

    public final int        readUnsignedByte () {
        return (mBuffer [mPos++] & 0xFF);
    }

    public final boolean    readBoolean () {
        return (mBuffer [mPos++] != 0);
    }

    public final byte       readByte () {
        return (mBuffer [mPos++]);
    }

    public final char       readChar () {
        char    ret = DataExchangeUtils.readChar (mBuffer, mPos);
        mPos += 2;
        return (ret);
    }

    public final double     readDouble () {
        double    ret = DataExchangeUtils.readDouble (mBuffer, mPos);
        mPos += 8;
        return (ret);
    }

    public final float      readFloat () {
        float    ret = DataExchangeUtils.readFloat (mBuffer, mPos);
        mPos += 4;
        return (ret);
    }

    public final int        readInt () {
        int    ret = DataExchangeUtils.readInt (mBuffer, mPos);
        mPos += 4;
        return (ret);
    }

    public final long       readLong () {
        long    ret = DataExchangeUtils.readLong (mBuffer, mPos);
        mPos += 8;
        return (ret);
    }

    public final long       readLong48 () {
        long    ret = DataExchangeUtils.readLong48 (mBuffer, mPos);
        mPos += 6;
        return (ret);
    }

    public final long       readLongUnsignedByte () {
        return (((long) mBuffer [mPos++]) & 0xFFL);
    }
    
    public final long       readPackedUnsignedLong () {        
        int     head = mBuffer [mPos++];
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

        // make back shift
        ret = (ret == 0) ? IntegerDataType.PUINT61_NULL : ret - 1;

        return (ret);
    }

    public final int       readPackedUnsignedInt () {        
        int     head = mBuffer [mPos++];
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
        
        // make back shift
        ret = (ret == 0) ? IntegerDataType.PUINT30_NULL : ret - 1;

        return (ret);
    }

    public final short      readShort () {
        short    ret = DataExchangeUtils.readShort (mBuffer, mPos);
        mPos += 2;
        return (ret);
    }

    public final String         readString () {
        CharSequence   sb = readCharSequence ();
        
        return (sb == null ? null : sb.toString ());
    }
    
    /**
     *  Uses an internal buffer. The returned value is valid until the next call to
     *  this method. Returns null if the string value is null. 
     */
    public final CharSequence   readCharSequence () {
        if (mStringBuilder == null)
            mStringBuilder = new StringBuilder ();
        
        return (readStringBuilder (mStringBuilder));
    }
    
        
    /**
     *  Returns null if the string value is null. 
     */
    public final StringBuilder  readStringBuilder (StringBuilder sb) {
        sb.setLength (0);
        return (appendToStringBuilder (sb));
    }
    
    /**
     *  Returns false if the string value is null. 
     */
    public final StringBuilder    appendToStringBuilder (StringBuilder sb) {
        int         utflen = readUnsignedShort ();
        
        if (utflen == 0xFFFF)
            return (null);
        
        if (utflen == 0)
            return (sb);
        
        int c = -2;
        int char2, char3;
        int count = 0;        
        
        for (;;) {
            c = readByte ();    
            if (c > 127) 
                break;
            
            count++;
            sb.append ((char) c);
            
            if (count >= utflen)
                return (sb);
        }
        //  If we are here, we have broken out of the previous loop and there is an
        //  unhandled escape character in variable c.        
        for (;;) {
            switch (c >> 4) {
                case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
                    /* 0xxxxxxx*/
                    count++;
                    sb.append ((char)c);
                    break;
                    
                case 12: case 13:
                    /* 110x xxxx   10xx xxxx*/
                    count += 2;
                    
                    if (count > utflen)
                        throw new UncheckedIOException (
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
                        throw new UncheckedIOException (
                            "malformed input: partial character at end"
                        );
                    char2 = readByte ();
                    char3 = readByte ();
                    if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
                        throw new UncheckedIOException(
                            "malformed input around byte " + (count-1));
                    sb.append ((char)(((c & 0x0F) << 12) |
                                                    ((char2 & 0x3F) << 6)  |
                                                    ((char3 & 0x3F) << 0)));
                    break;
                    
                default:
                    /* 10xx xxxx,  1111 xxxx */
                    throw new UncheckedIOException (
                        "malformed input around byte " + count
                    );
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
