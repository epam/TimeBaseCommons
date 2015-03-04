package deltix.util.net;

import deltix.util.time.GlobalTimer;
import deltix.util.time.TimerRunner;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server socket that discards incoming messages and collects avg msg/sec statistic. Used in latency experiments.
 * Usage: java deltix.util.net.TCPNull port
 *
 *
 */
public class TCPNull {

    private static final int intervalInMillis = 60000;
    private final ServerSocket ss;
    private volatile int messageCount;

    private TCPNull (int bindPort, String iface) throws IOException {

        InetAddress bindAddr = (iface != null) ? InetAddress.getByName(iface) : null;
        ss = new ServerSocket(bindPort, 50, bindAddr);
        System.out.println("TCP Null is listening on port " + ss.getLocalPort());
    }

    private void setupStatsTimer() {
        TimerRunner meter = new TimerRunner() {
            private long lastMessageCount;
            private final int intevalInSeconds = intervalInMillis / 1000;
            protected void runInternal() throws Exception {
                final long currentMessageCount = messageCount; // volatile
                System.out.println("Average messages per second: " + (currentMessageCount- lastMessageCount) / intevalInSeconds);
                lastMessageCount = currentMessageCount;
            }
        };
        GlobalTimer.INSTANCE.scheduleAtFixedRate(meter, intervalInMillis, intervalInMillis);
    }

    private void run () throws IOException {
        while (true) {
            Socket s = ss.accept();
            final InputStream is = s.getInputStream();
            new Thread() {
                @Override
                public void run() {
                    System.out.println("Accepted new client");
                    byte[] buf = new byte[8192];
                    try {
                        while (true) {
                            int numRead = is.read(buf);
                            if (numRead < 0)
                                break;

                            messageCount++;

                        }
                    } catch (IOException iox) {
                        iox.printStackTrace();
                    }
                }
            }.start();
        }    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Command line args: <bind-port> <optional-bind-interface>");
            return;
        }

        int bindPort = Integer.parseInt(args[0]);
        String iface = (args.length > 1) ? args[1] : null;
        TCPNull nul = new TCPNull(bindPort, iface);
        nul.setupStatsTimer();
        nul.run();


    }
}
