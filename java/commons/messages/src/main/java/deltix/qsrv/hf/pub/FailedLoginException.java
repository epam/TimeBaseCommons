package deltix.qsrv.hf.pub;

import java.io.IOException;

/**
 * Thrown when authentication failed.
 * For example, in case of incorrect username or/and password.
 */
public class FailedLoginException extends IOException {
    public FailedLoginException(String message) {
        super(message);
    }
}
