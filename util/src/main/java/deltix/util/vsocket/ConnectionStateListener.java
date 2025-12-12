package deltix.util.vsocket;

abstract class ConnectionStateListener {
    /**
     * Triggered when connection loss causes dispatcher to give up on recovery.
     * <p>
     * Triggered only once per dispatcher lifecycle.
     * <p>
     * Not triggered if dispatcher is stopped normally with {@link VSDispatcher#close()}.
     */
    abstract void onDisconnected();

    /**
     * Triggered when the first connection is established.
     */
    abstract void onConnected();

    /**
     * Triggered when transport is stopped (e.g. connection lost) but may be recoverable.
     *
     * @return true if transport is already known to be unrecoverable (and recovery should be stopped right away)
     */
    abstract boolean onTransportRecoveryStart(VSocketRecoveryInfo recoveryInfo);

    /**
     * Triggered when transport recovery have to stop (because of timeout or dispatcher shutdown).
     *
     * @return true if transport was permanently lost (can't be recovered anymore)
     */
    abstract boolean onTransportRecoveryStop(VSocketRecoveryInfo recoveryInfo);
}
