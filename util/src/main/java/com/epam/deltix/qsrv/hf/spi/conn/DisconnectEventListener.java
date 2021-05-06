package com.epam.deltix.qsrv.hf.spi.conn;

public interface DisconnectEventListener {

    /** Feed is connected after UHFS restart or reconnected after disconnection */
    void onDisconnected();
    
    void onReconnected();
    
}
