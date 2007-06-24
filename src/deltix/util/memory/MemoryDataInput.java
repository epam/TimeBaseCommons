package deltix.util.memory;

import deltix.util.collections.generated.ByteArrayList;

/**
 *  Equivalent of DataInputStream wrapped around
 *  ByteArrayInputStream optimized for extreme performance. This class uses
 *  no virtual method calls and presents a non-virtual public final API.
 */
public class MemoryDataInput {
    private byte []         mBuffer;
    private int             mPos;
    private int             mLimit;
    
    public final void       setBytes (byte [] buffer, int offset, int length) {
        mBuffer = buffer;
        mLimit = offset + length;
        mPos = offset;
    }
    
    public final void       setBytes (ByteArrayList buffer) {
        mBuffer = buffer.getInternalBuffer ();
        mLimit = buffer.size ();
        mPos = 0;
    }
    
    public final void       setBytes (MemoryDataOutput out) {
        mBuffer = out.getBuffer ();
        mLimit = out.getSize ();
        mPos = 0;
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

    private final long      readLongByte () {
        return (((long) mBuffer [mPos++]) & 0xFFL);
    }
    
    public final long       readPackedUnsignedLong () {        
        int     head = mBuffer [mPos++];
        long    ret = head & 0x1F;
        int     numAddlBytes = (head >>> 5) & 0x7;
        
        switch (numAddlBytes) {
            case 7:
                ret |= readLongByte () << 5;
                ret |= readLongByte () << 13;
                ret |= readLongByte () << 21;
                ret |= readLongByte () << 29;
                ret |= readLongByte () << 37;
                ret |= readLongByte () << 45;
                ret |= readLongByte () << 53;
                break;
                
            case 6:
                ret |= readLongByte () << 5;
                ret |= readLongByte () << 13;
                ret |= readLongByte () << 21;
                ret |= readLongByte () << 29;
                ret |= readLongByte () << 37;
                ret |= readLongByte () << 45;
                break;
                
            case 5:
                ret |= readLongByte () << 5;
                ret |= readLongByte () << 13;
                ret |= readLongByte () << 21;
                ret |= readLongByte () << 29;
                ret |= readLongByte () << 37;
                break;
                
            case 4:
                ret |= readLongByte () << 5;
                ret |= readLongByte () << 13;
                ret |= readLongByte () << 21;
                ret |= readLongByte () << 29;
                break;
                
            case 3:
                ret |= readLongByte () << 5;
                ret |= readLongByte () << 13;
                ret |= readLongByte () << 21;
                break;
                
            case 2:
                ret |= readLongByte () << 5;
                ret |= readLongByte () << 13;
                break;
                
            case 1:
                ret |= readLongByte () << 5;
                break;
                
            case 0:
                break;               
        }
        
        return (ret);
    }

    public final short      readShort () {
        short    ret = DataExchangeUtils.readShort (mBuffer, mPos);
        mPos += 2;
        return (ret);
    }

    public final String     readString () {
        throw new RuntimeException ();
    }
    
    public final String     readUTF () {
        throw new RuntimeException ();
    }
}
