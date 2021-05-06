package com.epam.deltix.util.io;

import com.epam.deltix.util.lang.Disposable;

/**
 *  A persistent object used to store structured data of some sort.
 */
public interface AbstractDataStore extends Disposable {
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
     *  Determines whether the store is open as read-only.
     */
    public boolean      isReadOnly ();
    
    /**
     *  Open the data store.
     */
    public void         open (boolean readOnly);
    
    /**
     *  Returns a meaningful identification of this object,
     *  such as a file path or url.
     */
    public String       getId ();
}
