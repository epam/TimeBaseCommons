package deltix.util.io;

import java.io.*;

/**
 *
 */
public class RandomAccessFileStore implements AbstractDataStore {
    protected final File            file;
    protected RandomAccessFile      raf;
    private boolean                 mIsReadOnly;
    
    public RandomAccessFileStore (File f) {
        file = f;
    }
    
    protected void          force (boolean metaData) throws IOException {
        raf.getChannel ().force (metaData);
    }
    
    public void             open (boolean readOnly) {
        mIsReadOnly = readOnly;
        
        try {
            raf = new RandomAccessFile (file, readOnly ? "r" : "rw");
        } catch (IOException iox) {
            throw new UncheckedIOException (iox);
        }
    }

    public boolean          isReadOnly () {
        return (mIsReadOnly);
    }

    public boolean          isOpen () {
        return (raf != null);
    }

    public void             format () {
        mIsReadOnly = false;
        
        try {
            raf = new RandomAccessFile (file, "rw");
        } catch (IOException iox) {
            throw new UncheckedIOException (iox);
        }
    }

    public void             delete () {
        close ();
        
        if (!file.delete ())
            throw new UncheckedIOException ("Failed to delete " + file.getPath ());
    }

    public void             close () {
        if (raf != null) {
            try {
                raf.close ();
            } catch (IOException iox) {
                throw new UncheckedIOException (iox);
            }
            
            raf = null;
        }
    }
    
    
}
