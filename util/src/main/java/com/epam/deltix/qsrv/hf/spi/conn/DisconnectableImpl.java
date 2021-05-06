package com.epam.deltix.qsrv.hf.spi.conn;

/**
 *  Helps implement the {@link deltix.qsrv.hf.spi.conn.Disconnectable} interface.
 */
public class DisconnectableImpl extends DisconnectableEventHandler {
    private volatile boolean isConnected = false;

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void onReconnected() {
        if (!isConnected) {
            isConnected = true;
            super.onReconnected();
        }
    }

    @Override
    public void onDisconnected() {
        if (isConnected) {
            isConnected = false;
            super.onDisconnected();
        }
    }
}
