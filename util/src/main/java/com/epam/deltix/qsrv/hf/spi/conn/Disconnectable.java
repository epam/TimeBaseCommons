package com.epam.deltix.qsrv.hf.spi.conn;


/** 
 * One of use cases: FeedProvider may optionally implement this interface to notify listener about connection problems 
 * 
 * @see DisconnectEventListener
 */  
public interface Disconnectable {

    void addDisconnectEventListener(DisconnectEventListener listener);

    void removeDisconnectEventListener(DisconnectEventListener listener);

    boolean isConnected();
}
