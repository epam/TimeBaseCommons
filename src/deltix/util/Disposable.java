package deltix.util;

/**
 *  Something that can be closed. Analogous to the dot Net IDisposable concept.
 */
public interface Disposable {
    /**
     *  Closes associated resources.
     */
    public void         close ();
}
