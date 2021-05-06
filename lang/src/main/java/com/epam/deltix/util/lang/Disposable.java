package com.epam.deltix.util.lang;

import java.io.Closeable;

/**
 *  A resource that can be closed without throwing a checked exception.
 *  Analogous to the dot Net IDisposable concept. This interface
 *  extends <tt>java.io.Closeable</tt> by overriding its <tt>close</tt> method without
 *  throwing <tt>java.io.IOException</tt>.
 */
public interface Disposable extends Closeable {
    /**
     *  Closes associated resources.
     */
    public void         close ();
}
