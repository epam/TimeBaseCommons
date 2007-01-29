package deltix.util.concurrent;

import deltix.util.Disposable;

/**
 *  An abstract cursor for iterating over arbitrary sequences of data.
 *  Usage:
 *<pre>try {
 *     while (cursor.next ()) {        
 *         ... cursor.get??? () ...
 *    }
 *} finally {
 *    cursor.close ();
 *}
 * </pre>
 *  All implementations of this interface are designed to be used from a single
 *  thread and must be externally protected against concurrent calls.
 */
public interface AbstractCursor extends Disposable {
    /**
     *  Moves on to the next data element. This method blocks until 
     *  the next element becomes available, or until the cursor is
     *  determined to be at the end of the sequence.
     *  
     *  @return     <code>false</code> if at the end of the cursor.
     */
    public boolean                  next ()
        throws InterruptedException;
}
