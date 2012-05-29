package deltix.qsrv.hf.framework.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import deltix.qsrv.hf.spi.conn.DisconnectEventListener;
import deltix.qsrv.hf.spi.conn.Disconnectable;

public final class DisconnectableUtil {

    public static final Disconnectable CONNECTED_DISCONNECTABLE = new Disconnectable() {
        @Override
        public void addDisconnectEventListener(DisconnectEventListener listener) {
        }

        @Override
        public void removeDisconnectEventListener(DisconnectEventListener listener) {
        }

        @Override
        public boolean isConnected() {
            return true;
        }
    };

    public static boolean waitConnected(Disconnectable connection, long timeout) throws InterruptedException {
        if (connection.isConnected())
            return true;

        final CountDownLatch latch = new CountDownLatch(1);
        // create listener
        DisconnectEventListener listener = new DisconnectEventListener() {
            @Override
            public void onDisconnected() {
            }

            @Override
            public void onReconnected() {
                latch.countDown();
            }
        };

        try {
            // subscribe
            connection.addDisconnectEventListener(listener);
            // wait if needed
            boolean result = true;
            if (!connection.isConnected()) {
                if (timeout > 0) {
                    result = latch.await(timeout, TimeUnit.MILLISECONDS);
                } else {
                    latch.await();
                }
            }
            // return result
            return result;
        } finally {
            // unsubscribe
            connection.removeDisconnectEventListener(listener);
        }
    }
}
