package deltix.qsrv.hf.framework;

import net.jcip.annotations.GuardedBy;

import java.util.List;
import java.util.ArrayList;

/**
 *  Helps implement the {@link Disconnectable} interface. 
 */
public class DisconnectableImpl extends DisconnectableEventHandler {
    private volatile boolean isConnected = false;

    public boolean isConnected() {
        return isConnected;
    }

    public void onReconnected() {
        if (!isConnected) {
            isConnected = true;
            super.onReconnected();
        }
    }

    public void onDisconnected() {
        if (isConnected) {
            isConnected = false;
            super.onDisconnected();
        }
    }
}
