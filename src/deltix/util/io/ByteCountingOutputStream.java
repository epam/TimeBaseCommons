package deltix.util.io;

import java.io.*;

/**
 *  Counts bytes that pass through.
 */
public class ByteCountingOutputStream extends FilterOutputStream {
    private long                    mNumBytesWritten = 0;
    
    public ByteCountingOutputStream (OutputStream os) {
        super (os);
    }
    
    public void             reset () {
        mNumBytesWritten = 0;
    }
    
    public void             setNumBytesWritten (long n) {
        mNumBytesWritten = n;
    }
    
    public long             getNumBytesWritten () {
        return (mNumBytesWritten);
    }
    
    public void             write (byte [] b) throws IOException {
        out.write (b);
        mNumBytesWritten += b.length;
    } 
    
    public void             write (byte [] b, int off, int len) throws IOException {
        out.write (b, off, len);
        mNumBytesWritten += len;
    } 
    
    public void             write (int b) throws IOException {
        mNumBytesWritten++;
        out.write (b);
    } 
}
