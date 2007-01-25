package deltix.qsrv.pub;

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
public interface AbstractCursor {
    /**
     *  Moves on to the next data element.
     *  
     *  @return     <code>false</code> if at the end of the cursor.
     */
    public boolean                  next ();
    
    /**
     *  Closes the cursor and releases any associated resources. This method is
     *  guaranteed not to throw exceptions; therefore, it is safe to use in a
     *  <tt>finally</tt> clause directly.
     */
    public void                     close ();
}
