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
    
    public final void           writeString (CharSequence str) {
        if (str == null) {
            writeUnsignedShort (0xFFFF);        
            return;
        }
        
        int     strlen = str.length();
        int     utflen = 0;
        int     c, count = 0;

        /* use charAt instead of copying String to char array */
        for (int i = 0; i < strlen; i++) {
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
        
        for (i=0; i<strlen; i++) {
           c = str.charAt (i);
           
           if (!((c >= 0x0001) && (c <= 0x007F))) 
               break;
           
           mBuffer [mPos++] = (byte) c;
        }
	
        for (; i < strlen; i++) {
            c = str.charAt(i);
            
            if ((c >= 0x0001) && (c <= 0x007F)) 
                mBuffer [mPos++] = (byte) c;
            else if (c > 0x07FF) {
                mBuffer [mPos++] = (byte) (0xE0 | ((c >> 12) & 0x0F));
                mBuffer [mPos++] = (byte) (0x80 | ((c >>  6) & 0x3F));
                mBuffer [mPos++] = (byte) (0x80 | ((c >>  0) & 0x3F));
            }
            else {
                mBuffer [mPos++] = (byte) (0xC0 | ((c >>  6) & 0x1F));
                mBuffer [mPos++] = (byte) (0x80 | ((c >>  0) & 0x3F));
            }
        }
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

    public final void           writeUnsignedByte (int v) {
        makeRoom (1);
        mBuffer [mPos] = (byte) v;
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

    public final void           writePackedUnsignedLong (long v) {
        if ((v & 0xE000000000000000L) != 0)
            throw new IllegalArgumentException ("High 3 bits must be 0; v=" + v);
        
        makeRoom (8);   
        
        int         pos = mPos++;
        int         addlPos = mPos;
        int         low5bits = ((int) v) & 0x1F;
        
        v = v >>> 5; 
        
        while (v != 0) {   
            mBuffer [mPos++] = (byte) (v & 0xFF);
            v = v >>> 8;
        }
        
        mBuffer [pos] = (byte) (low5bits | ((mPos - addlPos) << 5));
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
