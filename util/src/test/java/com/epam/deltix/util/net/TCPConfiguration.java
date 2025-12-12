package com.epam.deltix.util.net;

import java.io.IOException;
import java.net.Socket;


class TCPConfiguration {

    public static final String MESSAGE = "8=FIX.4.4|9=224|35=D|34=00000000000000000000|49=DEMO2Kweoj_DEMOFIX|52=20130605-15:41:28.638|56=DUKASCOPYFIX|11=5080|15=EUR|21=1|38=10000|40=Q|44=1.209|54=1|55=EUR/USD|59=6|60=20130605-15:41:28.638|126=20130605-15:46:23.000|10=246|".replace("|", "\u0001");
    public static final int MESSAGE_SIZE = MESSAGE.length();

    public static final int MESSAGE_RATE_PER_SEC = 1000000;
    public static final int MESSAGE_RECEIVE_BUFFER_SIZE = 1 << 16;
    public static final int MESSAGE_SEND_BUFFER_SIZE = 1 << 16;

    public static final int PASS_EACH_N = Integer.getInteger("pass.each.n", 1);
    public static final int STATISTICS_INTERVAL_S = Integer.getInteger("stat.interval", 10);

    public static final int SOCKET_RECEIVE_BUFFER_SIZE = Integer.getInteger("socket.receive.buffer.size", 1 << 16);
    public static final int SOCKET_SEND_BUFFER_SIZE = Integer.getInteger("socket.send.buffer.size", 1 << 16);
    public static final boolean SOCKET_KEEP_ALIVE = false;
    public static final boolean SOCKET_TCP_NO_DELAY = !Boolean.getBoolean("socket.tcp.with.delay");


    public static void configure(Socket socket) throws IOException {
        socket.setKeepAlive(SOCKET_KEEP_ALIVE);
        socket.setTcpNoDelay(SOCKET_TCP_NO_DELAY);
        socket.setReceiveBufferSize(SOCKET_RECEIVE_BUFFER_SIZE);
        socket.setSendBufferSize(SOCKET_SEND_BUFFER_SIZE);
    }

    public static void print(String prefix, Socket socket) throws IOException {
        System.out.printf("%s %s: receive buffer - %s, send buffer - %s, tcp no delay (Naggle is off) - %s\n",
                prefix, socket, socket.getReceiveBufferSize(), socket.getSendBufferSize(), socket.getTcpNoDelay());
    }

}
