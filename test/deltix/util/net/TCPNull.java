package deltix.util.net;

import deltix.util.time.GlobalTimer;
import deltix.util.time.TimerRunner;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPNull {

    private static final int intervalInMillis = 60000;
    private final ServerSocket ss;
    private volatile int messageCount;

    private TCPNull (int bindPort) throws IOException {
        ss = new ServerSocket(bindPort);
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
        if (args.length != 1) {
            System.out.println("Command line args: <bind port>");
            return;
        }

        int bindPort = Integer.parseInt(args[0]);
        TCPNull nul = new TCPNull(bindPort);
        nul.setupStatsTimer();
        nul.run();


    }
}
