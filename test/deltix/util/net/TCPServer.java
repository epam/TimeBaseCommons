package deltix.util.net;

import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Command line args: <bind port>");
            return;
        }

        int bindPort = Integer.parseInt(args[0]);

        int rateMessagesPerSec = (args.length > 1) ? Integer.parseInt(args[1]) : 100;


        ServerSocket ss = new ServerSocket(bindPort);
        System.out.println("TCP Server is listening on port " + ss.getLocalPort());

        while (true) {
            Socket socket = ss.accept();
            socket.setKeepAlive(true);
            socket.setTcpNoDelay(true);

            System.out.println("Accepted connection on " + bindPort);

            new TCPSender(rateMessagesPerSec, socket.getOutputStream()).start();

        }
    }

}
