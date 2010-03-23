package deltix.util.concurrent;

/**
 *  Thrown from {@link AbstractCursor#next} if the cursor has been
 *  previously or asynchronously closed.
 */
public class CursorIsClosedException extends RuntimeException {
    public CursorIsClosedException() {
    }

    public CursorIsClosedException(Throwable cause) {
        super(cause);
    }
}
