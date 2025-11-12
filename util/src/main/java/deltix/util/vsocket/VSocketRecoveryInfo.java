package deltix.util.vsocket;

import deltix.util.annotations.TimestampMs;
import net.jcip.annotations.GuardedBy;

/**
 * @author Alexei Osipov
 */
class VSocketRecoveryInfo {
    private final VSocket socket;

    private int reconnectAttempts = 0;
    private long lastReconnectAttemptTs = Long.MIN_VALUE;
    @TimestampMs
    private final long recoveryDeadlineTs;

    private boolean recoveryFailed;
    private boolean recoverySucceeded;

    // If true, it means that currently a thread actively attempts to recover corresponding channel
    private boolean recoveryAttemptInProgress;

    VSocketRecoveryInfo(VSocket socket, long recoveryDeadlineTs) {
        this.socket = socket;
        this.recoveryDeadlineTs = recoveryDeadlineTs;
    }

    int addReconnectAttempt(long reconnectAttemptTimestamp) {
        reconnectAttempts++;
        lastReconnectAttemptTs = reconnectAttemptTimestamp;
        return reconnectAttempts;
    }

    int getReconnectAttempts() {
        return reconnectAttempts;
    }

    long getLastReconnectAttemptTs() {
        return lastReconnectAttemptTs;
    }

    long getRecoveryDeadlineTs() {
        return recoveryDeadlineTs;
    }

    VSocket getSocket() {
        return socket;
    }

    void markRecoveryFailed() {
        recoveryFailed = true;
    }

    @GuardedBy("this")
    boolean tryMarkRecoverySucceeded() {
        if (!isRecoveryEnded()) {
            recoverySucceeded = true;
            return true;
        } else {
            return false;
        }
    }

    boolean isRecoveryFailed() {
        return recoveryFailed;
    }

    boolean isRecoverySucceeded() {
        return recoverySucceeded;
    }

    boolean isRecoveryEnded() {
        return recoverySucceeded || recoveryFailed;
    }

    boolean isWaitingForRecovery() {
        return recoveryAttemptInProgress || !isRecoveryEnded();
    }

    boolean startRecoveryAttempt() {
        if (recoveryAttemptInProgress || isRecoveryEnded()) {
            // Only one attempt at a time
            return false;
        } else {
            recoveryAttemptInProgress = true;
            return true;
        }
    }

    void stopRecoveryAttempt() {
        if (recoveryAttemptInProgress) {
            // Only one attempt at a time
            recoveryAttemptInProgress = false;
        } else {
            throw new IllegalStateException("Attempt to stop recovery multiple times");
        }
    }

    public boolean isRecoveryAttemptInProgress() {
        return recoveryAttemptInProgress;
    }
}
