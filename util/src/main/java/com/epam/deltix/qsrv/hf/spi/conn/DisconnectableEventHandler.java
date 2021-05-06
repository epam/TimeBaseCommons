package com.epam.deltix.qsrv.hf.spi.conn;

import com.epam.deltix.qsrv.hf.spi.conn.Disconnectable;
import com.epam.deltix.qsrv.hf.spi.conn.DisconnectEventListener;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Helps implement the {@link deltix.qsrv.hf.spi.conn.Disconnectable} interface.
 * <p> Doesn't maintain a connection status, so <code>isConnected</code> must be implemented by a client.</p>
 */
public class DisconnectableEventHandler implements Disconnectable {
    private final CopyOnWriteArrayList<DisconnectEventListener> listeners =
        new CopyOnWriteArrayList<DisconnectEventListener>();

    public void addDisconnectEventListener(DisconnectEventListener listener) {
        listeners.addIfAbsent(listener);
    }

    public void removeDisconnectEventListener(DisconnectEventListener listener) {
        listeners.remove(listener);
    }

    public boolean isConnected() {
        throw new UnsupportedOperationException();
    }

    public void onReconnected() {
        for (DisconnectEventListener listener : listeners) {
            listener.onReconnected();
        }
    }

    public void onDisconnected() {
        for (DisconnectEventListener listener : listeners) {
            listener.onDisconnected();
        }
    }
}