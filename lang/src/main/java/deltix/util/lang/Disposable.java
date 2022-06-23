package deltix.util.lang;

import java.io.Closeable;

/**
 *  A resource that can be closed without throwing a checked exception.
 *  Analogous to the dot Net IDisposable concept. This interface
 *  extends <code>java.io.Closeable</code> by overriding its <code>close</code> method without
 *  throwing <code>java.io.IOException</code>.
 */
public interface Disposable extends Closeable {
    /**
     *  Closes associated resources.
     */
    public void         close ();
}
