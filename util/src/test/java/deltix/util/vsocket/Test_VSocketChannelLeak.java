package deltix.util.vsocket;

import deltix.util.concurrent.QuickExecutor;
import deltix.util.lang.Disposable;
import deltix.util.lang.DisposableListener;
import org.junit.Test;

import java.io.IOException;

/**
 * Tests if there is a memory leak in VSChannel when channel gets closed on the client side.
 */
public class Test_VSocketChannelLeak {
    public static void main (String [] args) throws Exception {
        testImpl();
        //Thread.sleep(Long.MAX_VALUE);
    }

    @Test
    public void test() throws IOException, InterruptedException {
        testImpl();
    }

    @SuppressWarnings("Convert2Lambda")
    private static void testImpl() throws IOException, InterruptedException {
        VSServer server = new VSServer(0);
        server.setConnectionListener(new VSConnectionListener() {
            @Override
            public void connectionAccepted(QuickExecutor executor, VSChannel serverChannel) {
                // This payload will be kept in memory until the channel is closed
                byte[] payload = new byte[1_000_000];
                payload[0] = 1;

                serverChannel.addDisposableListener(new DisposableListener() {
                    @Override
                    public void disposed(Disposable resource) {
                        byte val = payload[11];
                        if (val != 0) {
                            System.out.println("Should never happen");
                        }
                        // Intentionally do not remove the listener from the channel to release the memory only if channel is released
                    }
                });
            }
        });
        server.setDaemon(true);
        server.start();
        System.out.println("Server started on " + server.getLocalPort());

        createConnections("localhost", server.getLocalPort());
    }

    public static void createConnections(String host, int port)
            throws IOException, InterruptedException {

        VSClient c = new VSClient(host, port);
        c.connect();

        // This loop fails with OutOfMemoryError
        for (int i = 0; i < 10000; i++) {
            VSChannel s = c.openChannel();
            s.close(false);
        }
        long usedMemory1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        System.gc();
        Thread.sleep(1000);

        long usedMemory2 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Used memory before gc: " + usedMemory1);
        System.out.println("Used memory after gc: " + usedMemory2);
    }
}
