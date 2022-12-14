package deltix.util.io.waitstrat;

import deltix.util.lang.Changeable;
import deltix.util.lang.Disposable;

/**
 *
 */
public interface WaitForChangeStrategy extends Disposable {
    public void                 waitFor(Changeable value) throws InterruptedException;
}
