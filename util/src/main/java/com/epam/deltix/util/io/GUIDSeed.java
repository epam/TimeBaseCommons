package com.epam.deltix.util.io;

import com.epam.deltix.util.concurrent.UncheckedInterruptedException;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Externalized GUID seed generation from {@link GUID}
 */
public class GUIDSeed {
    final long           time;
    final int            port;

    public GUIDSeed () {
        ServerSocket socket = null;

        try {
            socket = new ServerSocket ();

            socket.bind (null);

            //
            //  Sleep for 2 ticks to prevent the (extremely unlikely) situation
            //  where somebody else owned the port for a fraction of the previous tick.
            //
            Thread.sleep (2);

            //  Get a time at which we definitely owned the socket ...
            time = System.currentTimeMillis () - 1;
            port = socket.getLocalPort ();
        } catch (InterruptedException x) {
            throw new UncheckedInterruptedException(x);
        } catch (IOException x) {
            throw new com.epam.deltix.util.io.UncheckedIOException(x);
        } finally {
            IOUtil.close (socket);
        }
    }

}
