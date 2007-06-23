package deltix.util.memory;

/**
 *  Equivalent of DataOutputStream wrapped around
 *  ByteArrayOutputStream optimized for extreme performance. This class uses
 *  no virtual method calls and presents a non-virtual public final API.
 */
public class MemoryDataOutput {
    private byte []             mBuffer;
    private int                 mPos = 0;
    
    public MemoryDataOutput () {
        this (4096);
    }
    
    public MemoryDataOutput (int capacity) {
        mBuffer = new byte [capacity];
    }
    
    private final void          makeRoom (int space) {
        int         requiredSize = mPos + space;
        int         currentSize = mBuffer.length;
        
        if (currentSize < requiredSize) {
            do { 
                currentSize = currentSize << 1; 
            } while (currentSize < requiredSize);
            
            byte [] newBuffer = new byte [currentSize];
            System.arraycopy (mBuffer, 0, newBuffer, 0, mPos);
            mBuffer = newBuffer;
        }
    }
    
    public final void           reset () {
        mPos = 0;
    }
    
    public final byte []        getBuffer () {
        return (mBuffer);
    }
    
    public final int            getSize () {
        return (mPos);
    }
    
    public final void           writeString (String str) {
        boolean         isNotNull = str != null;
        writeBoolean (isNotNull);
        if (isNotNull)
            writeUTF (str);
    }

    public final void           writeUTF (String str) {
        throw new RuntimeException ();
    }

    public final void           write (byte[] b, int off, int len) {
        makeRoom (len);
        System.arraycopy (b, off, mBuffer, mPos, len);
        mPos += len;
    }

    public final void           write (byte[] b) {
        write (b, 0, b.length);
    }

    public final void           writeByte (byte v) {
        makeRoom (1);
        mBuffer [mPos] = v;
        mPos++;
    }

    public final void           writeChar (char v) {
        makeRoom (2);
        DataExchangeUtils.writeChar (mBuffer, mPos, v);
        mPos += 2;
    }

    public final void           writeInt (int v) {
        makeRoom (4);
        DataExchangeUtils.writeInt (mBuffer, mPos, v);
        mPos += 4;
    }

    public final void           writeShort (short v) {
        makeRoom (2);
        DataExchangeUtils.writeShort (mBuffer, mPos, v);
        mPos += 2;
    }

    public final void           writeUnsignedShort (int v) {
        makeRoom (2);
        DataExchangeUtils.writeUnsignedShort (mBuffer, mPos, v);
        mPos += 2;
    }

    public final void           writeBoolean (boolean v) {
        makeRoom (1);
        mBuffer [mPos] = v ? (byte) 1 : 0;
        mPos += 1;
    }

    public final void           writeLong (long v) {
        makeRoom (8);
        DataExchangeUtils.writeLong (mBuffer, mPos, v);
        mPos += 8;
    }

    public final void           writePackedLong (long v) {
        makeRoom (8);
        throw new RuntimeException ();
    }

    public final void           writeDouble (double v) {
        makeRoom (8);
        DataExchangeUtils.writeDouble (mBuffer, mPos, v);
        mPos += 8;
    }
    
    public final void           writeFloat (float v) {
        makeRoom (4);
        DataExchangeUtils.writeFloat (mBuffer, mPos, v);
        mPos += 4;
    }
}
