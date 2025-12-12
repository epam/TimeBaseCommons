package com.epam.deltix.qsrv.hf.spi.conn;

import com.epam.deltix.util.io.IOUtil;
import com.epam.deltix.util.lang.Util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;

/**
 * Based on deltix.temp.utiltests.DisconnectableTest in QS repo.
 *
 * <p>This is not really a test, but more an usage example of ReconnectableImpl.</p>
 *
 * <p>Run, disconnect from the network, reconnect, and see the output.</p>
 */
public class ReconnectableImplTest {

    public static void main(String[] args) throws InterruptedException {
        MyReconnector reconnector = new MyReconnector();
        ReconnectableImpl mgr = new ReconnectableImpl();
        mgr.setLogLevel(Level.INFO);
        mgr.setReconnector(reconnector);
        mgr.connected();

        while (true) {
            reconnector.checkedPing(mgr);
            Thread.sleep(3000);
        }
    }

    private static class MyReconnector implements ReconnectableImpl.Reconnector {
        @Override
        public boolean tryReconnect(int numAttempts, long timeSinceDisconnected, ReconnectableImpl helper) throws Exception {
            ping();

            System.out.println ("    Success");
            helper.connected();

            return (true);
        }

        private void ping() throws IOException {
            URL url = new URL("http://www.deltixlab.com");

            InputStream is = url.openStream();

            try {
                IOUtil.readBytes(is);
            } catch (InterruptedException x) {
                throw new RuntimeException("unexpected", x);
            } finally {
                Util.close(is);
            }
        }

        private void checkedPing(ReconnectableImpl mgr) {
            System.out.println ("Checked ping...");

            if (!mgr.isConnected()) {
                System.out.println ("    not connected.");
                return;
            }

            try {
                ping();
                System.out.println ("    All is well.");
            } catch (IOException iox) {
                System.out.println ("    Lost it!");
                mgr.disconnected();
                mgr.scheduleReconnect();
            }
        }
    }
}
