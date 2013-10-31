package deltix.util.net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class TCPClient {

    public static final String MSG = "8=FIX.4.4\u00019=189\u000135=D\u000134=25\u000149=DEMO2Kweoj_DEMOFIX\u000152=20130605-15:41:28.638\u000156=DUKASCOPYFIX\u000111=5080\u000115=EUR\u000121=1\u000138=10000\u000140=Q\u000144=1.209\u000154=1\u000155=EUR/USD\u000159=6\u000160=20130605-15:41:28.638\u0001126=20130605-15:46:23.000\u000110=246\u0001";

    static class Sender extends Thread {
        private final long intervalBetweenTransmissions;
        private final int blockSize;
        private final byte[] buf;
        private final OutputStream os;

        public Sender(long intervalBetweenTransmissions, int blockSize, OutputStream os) throws IOException {
            this.intervalBetweenTransmissions = intervalBetweenTransmissions;
            this.blockSize = blockSize;
            buf = new byte[blockSize];
            for (int i = 0; i < blockSize; i++) {
                buf [i] = (byte) i;
            }
            this.os = os;
        }

        public Sender(long intervalBetweenTransmissions, String message, OutputStream os) throws IOException {
            this.intervalBetweenTransmissions = intervalBetweenTransmissions;
            buf = message.getBytes();
            blockSize = buf.length;
            this.os = os;
        }

        public void run() {

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



        new Sender(500, MSG, socket.getOutputStream()).start();

    }

}
