package deltix.util.net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class TCPClient {
    static class Sender extends Thread {
        private final long intervalBetweenTransmissions;
        private final int blockSize;
        private final OutputStream os;

        public Sender(long intervalBetweenTransmissions, int blockSize, OutputStream os) throws IOException {
            this.intervalBetweenTransmissions = intervalBetweenTransmissions;
            this.blockSize = blockSize;
            this.os = os;
        }

        public void run() {
            final byte[] buf = new byte[blockSize];
            for (int i = 0; i < blockSize; i++) {
                buf [i] = (byte) i;
            }

            try {
                while (true) {
                    os.write(buf, 0, blockSize);
                    Thread.sleep(intervalBetweenTransmissions);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Command line args: <host> <port>");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        Socket socket = new Socket(host, port);
        socket.setKeepAlive(true);
        socket.setTcpNoDelay(true);

        System.out.println("Emitter is connecting to " + host + ":" + port);


        while (true) {
            new Sender(1000, 256, socket.getOutputStream()).start();
        }
    }

}
