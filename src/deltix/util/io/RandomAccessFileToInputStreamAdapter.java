package deltix.util.io;

import java.io.*;

/**
 *  Adapts java.io.RandomAccessFile to java.io.InputStream interface. 
 *  Useful for buffering data being read from RandomAccessFiles.
 *  Override close () if necessary, default implementation does nothing!
 */
public class RandomAccessFileToInputStreamAdapter extends InputStream {
    private final RandomAccessFile      mIn;
    private long                        mMark = -1;
    
    public RandomAccessFileToInputStreamAdapter (RandomAccessFile in) {
        mIn = in;
    }
    
    protected RandomAccessFile  randomAccessFile () {
        return (mIn);
    }

    public int                  read (byte [] b, int off, int len) 
        throws IOException 
    {
        return (mIn.read (b, off, len));
    }

    public int                  read (byte [] b) 
        throws IOException 
    {
        return (mIn.read (b));
    }

    public int                  read () throws IOException {
        return (mIn.read ());
    }

    public void mark (int readlimit) {
        super.mark(readlimit);
    }

    public int                  available () throws IOException {
        long        n = mIn.length () - mIn.getFilePointer ();
        
        if (n > Integer.MAX_VALUE)
            return (Integer.MAX_VALUE);
        
        return ((int) n);
    }

    public void                 reset () throws IOException {
        if (mMark < 0)
            throw new IOException ("mark () has not been called");
        
        mIn.seek (mMark);
    }

    public boolean              markSupported () {
        return (true);
    }

    public long                 skip (long n) throws IOException {
        if (n <= 0)
            return (0);
        
        long            curPos = mIn.getFilePointer ();
        long            limit = mIn.length ();
        long            newPos = curPos + n;
        
        if (newPos > limit) {
            n = limit - curPos;
            newPos = limit;
        }
        
        mIn.seek (newPos);
        return (n);
    }
}
