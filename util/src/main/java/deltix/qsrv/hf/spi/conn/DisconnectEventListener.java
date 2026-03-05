package deltix.qsrv.hf.spi.conn;

import org.jetbrains.annotations.ApiStatus;

/**
 * In general case, callees of this interface do not provide strong guarantee about call ordering.
 * <p>
 * So implementations of this interface should be ready to handle out-of-order calls of {@link #onDisconnected()} and {@link #onReconnected()} methods.
 * Ideally, this interface should not be used at all, unless specific API guarantees are provided by callee implementations.
 */
@ApiStatus.Obsolete
public interface DisconnectEventListener {

    /**
     * This method is called at some point of time after connection loss is detected.
     * <p>
     * In general, it is NOT guaranteed that this method will be called immediately after disconnection,
     * and it's not guaranteed that it case of disconnect and reconnect this method will be called before {@link #onReconnected()}
     * (no order guarantees between these two methods).
     * <p>
     * It's not guaranteed that immediately after this method call the connection is in "disconnected" state.
     * <p>
     * Some callee implementations may provide different guarantees.
     */
    void onDisconnected();

    /**
     * This method is called at some point of time after connection is re-established.
     * <p>
     * In general, it is NOT guaranteed that this method will be called immediately after reconnection,
     * and it's not guaranteed that it case of disconnect and reconnect this method will be called after {@link #onDisconnected()}
     * (no order guarantees between these two methods).
     * <p>
     * It's not guaranteed that immediately after this method call the connection is in "connected" state.
     * <p>
     * Some callee implementations may provide different guarantees.
     */
    void onReconnected();
    
}
