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
    
    public final void       setBytes (ByteArrayList buffer) {
        mBuffer = buffer.getInternalBuffer ();
        mLimit = buffer.size ();
        mPos = 0;
    }
    
    public final void       setBytes (byte [] buffer, int offset, int length) {
        mBuffer = buffer;
        mLimit = offset + length;
        mPos = offset;
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
        throw new RuntimeException ();
    }

    public final boolean    readBoolean () {
        return (readByte () != 0);
    }

    public final byte       readByte () {
        throw new RuntimeException ();
    }

    public final char       readChar () {
        throw new RuntimeException ();
    }

    public final double     readDouble () {
        throw new RuntimeException ();
    }

    public final float      readFloat () {
        throw new RuntimeException ();
    }

    public final int        readInt () {
        throw new RuntimeException ();
    }

    public final long       readLong () {
        throw new RuntimeException ();
    }

    public final long       readPackedLong () {
        throw new RuntimeException ();
    }

    public final short      readShort () {
        throw new RuntimeException ();
    }

    public final String     readString () {
        throw new RuntimeException ();
    }
    
    public final String     readUTF () {
        throw new RuntimeException ();
    }
}
