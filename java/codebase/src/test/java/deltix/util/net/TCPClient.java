package deltix.util.net;

import java.net.Socket;

public class TCPClient {

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

        new TCPSender(100, socket.getOutputStream()).start();

    }

}
