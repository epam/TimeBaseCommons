package deltix.util.vsocket;

import deltix.util.concurrent.QuickExecutor;

import java.io.IOException;

/**
 *
 */
public interface VSConnectionListener {
    public void         connectionAccepted (
        QuickExecutor       executor,
        VSChannel           serverChannel
    ) throws IOException;
}
