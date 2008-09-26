package deltix.util.lang;

import java.io.Closeable;

/**
 *  Something that can be closed. Analogous to the dot Net IDisposable concept.
 */
public interface Disposable extends Closeable {
    /**
     *  Closes associated resources.
     */
    public void         close ();
}
