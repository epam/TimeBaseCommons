package deltix.qsrv.hf.framework;


/** 
 * One of use cases: FeedProvider may optionally implement this interface to notify listener about connection problems 
 * 
 * @see DisconnectEventListener
 */  
public interface Disconnectable {

    void setDisconnectEventListener(DisconnectEventListener listener);
}
