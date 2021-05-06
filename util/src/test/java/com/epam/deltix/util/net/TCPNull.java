package com.epam.deltix.util.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import static com.epam.deltix.util.net.TCPConfiguration.MESSAGE_RECEIVE_BUFFER_SIZE;
import static com.epam.deltix.util.net.TCPConfiguration.MESSAGE_SIZE;
import static com.epam.deltix.util.net.TCPConfiguration.STATISTICS_INTERVAL_S;
import static com.epam.deltix.util.net.TCPConfiguration.configure;
import static com.epam.deltix.util.net.TCPConfiguration.print;

import com.epam.deltix.util.time.GlobalTimer;
import com.epam.deltix.util.time.TimerRunner;

/**
 * Server socket that discards incoming messages and collects avg msg/sec statistic. Used in latency experiments.
 * Usage: java deltix.util.net.TCPNull port
 */
public class TCPNull {

    private final ServerSocket serverSocket;
    private volatile long reads;
    private volatile long bytes;

    TCPNull(String host, int port) throws IOException {
        InetAddress address = (host != null) ? InetAddress.getByName(host) : null;
        serverSocket = new ServerSocket(port, 50, address);

        System.out.println("TCP Null is listening on port " + serverSocket.getLocalPort());
        System.out.println("Assuming each message is " + MESSAGE_SIZE + " bytes");
    }

    private void setupStatsTimer() {
        TimerRunner meter = new TimerRunner() {

            private long lastReads;
            private long lastBytes;

            protected void runInternal() throws Exception {
                long reads = TCPNull.this.reads; // volatile
                long bytes = TCPNull.this.bytes; // freeze
                long messages = (bytes - lastBytes) / MESSAGE_SIZE;

                System.out.println("Reads: " + (reads - lastReads) / STATISTICS_INTERVAL_S + "/sec; Messages: " + messages / STATISTICS_INTERVAL_S + "/sec");

                lastReads = reads;
                lastBytes = bytes;
            }

        };

        long statisticsIntervalMs = TimeUnit.SECONDS.toMillis(STATISTICS_INTERVAL_S);
        GlobalTimer.INSTANCE.scheduleAtFixedRate(meter, statisticsIntervalMs, statisticsIntervalMs);
    }

    void run() throws IOException {
        if (STATISTICS_INTERVAL_S > 0)
            setupStatsTimer();

        Socket socket = serverSocket.accept();
        configure(socket);
        print("Accepted new client", socket);
        final InputStream stream = socket.getInputStream();

        new Thread() {
            @Override
            public void run() {
                byte[] buffer = new byte[MESSAGE_RECEIVE_BUFFER_SIZE];

                try {
                    while (true) {
                        int bytesRead = stream.read(buffer);
                        if (bytesRead < 0)
                            break;

                        reads++;
                        bytes += bytesRead;
                    }
                } catch (IOException iox) {
                    System.err.println("Error in receiver thread");
                    iox.printStackTrace();
                }
            }
        }.start();
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Command line args: <bind-port> <optional-bind-interface>");
            return;
        }

        int port = Integer.parseInt(args[0]);
        String host = (args.length > 1) ? args[1] : null;
        TCPNull nul = new TCPNull(host, port);
        nul.run();
    }

}
