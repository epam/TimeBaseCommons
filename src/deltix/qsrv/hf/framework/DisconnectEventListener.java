package deltix.qsrv.hf.framework;

public interface DisconnectEventListener {

    /** Feed is connected after UHFS restart or reconnected after disconnection */
    void onDisconnected();
    
    void onReconnected();
    
}
