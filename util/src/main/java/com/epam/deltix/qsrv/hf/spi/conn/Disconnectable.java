package com.epam.deltix.qsrv.hf.spi.conn;

import com.epam.deltix.qsrv.hf.spi.conn.DisconnectEventListener;


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
