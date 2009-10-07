package deltix.qsrv.hf.framework;

import net.jcip.annotations.GuardedBy;

import java.util.List;
import java.util.ArrayList;

/**
 * Helps implement the {@link Disconnectable} interface.
 * <p> Doesn't maintain a connection status, so <code>isConnected</code> must be implemented by a client.
 * </p> 
 */
public class DisconnectableEventHandler implements Disconnectable {
    @GuardedBy("this")
    private final List<DisconnectEventListener> listeners = new ArrayList<DisconnectEventListener>();

    public synchronized void addDisconnectEventListener(
        DisconnectEventListener listener
    ) {
        listeners.add(listener);
    }

    public synchronized void removeDisconnectEventListener(
        DisconnectEventListener listener
    ) {
        listeners.remove(listener);
    }

    public boolean isConnected() {
        throw new UnsupportedOperationException();
    }

    public synchronized void onReconnected() {
        if (!listeners.isEmpty())
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onReconnected();
            }
    }

    public synchronized void onDisconnected() {
        if (!listeners.isEmpty())
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onDisconnected();
            }
    }
}