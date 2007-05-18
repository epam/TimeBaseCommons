package deltix.util.io;

import java.io.*;

/**
 *  Allows access to the internal buffer.
 */
public class ByteArrayOutputStreamEx extends ByteArrayOutputStream {
    /**
     * Creates a new byte array output stream. The buffer capacity is 
     * initially 32 bytes, though its size increases if necessary. 
     */
    public ByteArrayOutputStreamEx () {
    }

    /**
     * Creates a new byte array output stream, with a buffer capacity of 
     * the specified size, in bytes. 
     *
     * @param   size   the initial size.
     * @exception  IllegalArgumentException if size is negative.
     */
    public ByteArrayOutputStreamEx (int size) {    
        super (size);
    }
    
    public byte []          getInternalBuffer () {
        return (buf);
    }
    
    /**
     *  Resets the current position. Seeking forward will lead to arbitrary
     *  data being placed in the buffer.
     */
    public void             reset (int position) {    
        if (position > buf.length) {
            byte newbuf [] = new byte [Math.max (buf.length << 1, position)];
            System.arraycopy (buf, 0, newbuf, 0, count);
            buf = newbuf;
        }
        
        count = position;
    }
    
    public InputStream      openInput () {
        return (new ByteArrayInputStreamEx (buf, 0, count));
    }
}
