package com.epam.deltix.util.concurrent;

/**
 *  Thrown from {@link AbstractCursor#next} if the cursor has been
 *  previously or asynchronously closed.
 */
public class CursorIsClosedException extends ConsumerAbortedException {
    public CursorIsClosedException() {
        super ("Cursor is closed");
    }

    public CursorIsClosedException(Throwable cause) {
        super("Cursor is closed", cause);
    }
}
