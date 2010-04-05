package deltix.qsrv.hf.spi.conn;

import deltix.qsrv.hf.spi.conn.DisconnectableEventHandler;

/**
 *  Helps implement the {@link deltix.qsrv.hf.spi.conn.Disconnectable} interface.
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
