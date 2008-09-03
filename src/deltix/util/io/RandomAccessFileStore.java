package deltix.util.io;

import java.io.*;

/**
 *
 */
public class RandomAccessFileStore implements AbstractDataStore {
    protected final File                file;
    protected RandomAccessFile          raf;
    protected boolean                   useLock = true;
    private boolean                     mIsReadOnly;
    private boolean                     mIsOpen;
    private long                        mMinSize = 0;
    private FileLockSynchronizer.Lock   lock = null;

    public RandomAccessFileStore (File f) {
        file = f;
    }
    
    protected void          force (boolean metaData)
        throws IOException, InterruptedException
    {
        IOUtil.force (raf.getChannel (), metaData);
    }
    
    private void            setMinimumSizeNOW () throws IOException {
        if (mMinSize > 0 && mMinSize > raf.length ())
            raf.setLength (mMinSize);
    }
    
    public void             setMinimumSize (long size) {
        mMinSize = size;
        
        if (isOpen ()) {
            try {
                setMinimumSizeNOW ();
            } catch (IOException iox) {
                throw new UncheckedIOException (iox);
            }            
        }
    }
    
    private void            openFile () {
        try {
            raf = new RandomAccessFile (file, mIsReadOnly ? "r" : "rw");
            
            if (useLock) 
                lock = FileLockSynchronizer.acquire (file, raf, mIsReadOnly);
            
            setMinimumSizeNOW ();
        } catch (IOException iox) {
            throw new UncheckedIOException (iox);
        }
    }
    
    public void             open (boolean readOnly) {
        mIsReadOnly = readOnly;        
        openFile ();
        mIsOpen = true;
    }

    public boolean          isReadOnly () {
        return (mIsReadOnly);
    }

    public boolean          checkFileOpen () {
        assert raf.getChannel ().isOpen () : 
            "The file under " + this + " is closed";
        
        return (true);
    }
    
    public void             reopen () {
        assert mIsOpen : this + " is closed";
        
        if (!raf.getChannel ().isOpen()) {
            releaseLock();
            openFile ();
        }
    }
    
    public boolean          isOpen () {
        return (mIsOpen);
    }

    public void             format () {
        mIsReadOnly = false;        
        openFile ();
        mIsOpen = true;  
    }

    public void             delete () {
        close ();
        
        if (!file.delete ())
            throw new UncheckedIOException ("Failed to delete " + file.getPath ());
    }

    public void             close () {
        mIsOpen = false;
        
        if (raf != null) {
            try {
                releaseLock();
                raf.close ();
            } catch (IOException iox) {
                throw new UncheckedIOException (iox);
            }
            
            raf = null;
        }
    }

    private void            releaseLock() {
        if (lock != null) {
            try {
                FileLockSynchronizer.release (lock);                
                lock = null;
            }
            catch (IOException iox) {
                throw new UncheckedIOException(iox);
            }
        }
    }
}
