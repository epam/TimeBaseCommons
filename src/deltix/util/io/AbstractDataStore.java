package deltix.util.io;

import java.io.*;

/**
 *  A persistent object used to store structured data of some sort.
 */
public interface AbstractDataStore {
    /**
     *  Create a new object on disk and format internally. The data store is
     *  left open for read-write at the end of this method.
     */
    public void         format ();
    
    /**
     *  Close the store and delete all underlying files from disk.
     */
    public void         delete ();
    
    /**
     *  Determines whether the store is open.
     */
    public boolean      isOpen ();
    
    /**
     *  Open the data store.
     */
    public void         open (boolean readOnly);
    
    /**
     *  Close the data store and release all resources, such as caches and
     *  file descriptors.
     */
    public void         close ();
}
