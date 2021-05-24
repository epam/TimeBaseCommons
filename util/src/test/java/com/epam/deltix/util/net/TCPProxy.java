/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.epam.deltix.util.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static com.epam.deltix.util.net.TCPConfiguration.MESSAGE_RECEIVE_BUFFER_SIZE;
import static com.epam.deltix.util.net.TCPConfiguration.MESSAGE_SIZE;
import static com.epam.deltix.util.net.TCPConfiguration.PASS_EACH_N;
import static com.epam.deltix.util.net.TCPConfiguration.configure;
import static com.epam.deltix.util.net.TCPConfiguration.print;

/**
 * Retransmits inbound messages. Can work in 3 modes:
 * <p>
 * MODE 1: Normal proxy
 * =====================
 * Binds on given port. When client connects we establish connection to destination host/port and start forwarding everything we receive from client to destination.
 * <p>
 * MODE 2: Reverse proxy
 * =====================
 * Binds on given port. When client connects we establish connection to source host/port and start forwarding everything we receive from source to the client.
 * <p>
 * MODE 3: Client
 * =====================
 * Establishes connection to source and destination servers. Forwards messages from source to destination.
 */
public class TCPProxy {

    private static class Proxy extends Thread {

        private final InputStream in;
        private final OutputStream out;
        private final int passEachN;

        private int messages;

        public Proxy(InputStream is, OutputStream os) throws IOException {
            this.in = is;
            this.out = os;
            this.passEachN = PASS_EACH_N;
        }

        public void run() {
            byte[] buffer = new byte[MESSAGE_RECEIVE_BUFFER_SIZE];

            try {
                for (int offset = 0; ; ) {
                    int bytesRead = in.read(buffer, offset, buffer.length - offset);
                    if (bytesRead < 0) {
                        break;
                    }

                    int remaining = offset + bytesRead;
                    offset = 0;

                    while (remaining >= MESSAGE_SIZE) {
                        onMessage(buffer, offset, MESSAGE_SIZE);
                        offset += MESSAGE_SIZE;
                        remaining -= MESSAGE_SIZE;
                    }

                    if (remaining > 0) {
                        System.arraycopy(buffer, offset, buffer, 0, remaining);
                    }

                    offset = remaining;
                }
            } catch (IOException iox) {
                iox.printStackTrace();
            }
        }

        private void onMessage(byte[] buffer, int offset, int length) throws IOException {
            if (++messages == passEachN) {
                forwardMessage(buffer, offset, length);
                messages = 0;
            }
        }

        private void forwardMessage(byte[] buffer, int offset, int length) throws IOException {
            out.write(buffer, offset, length);
        }

    }

    public static void main(String[] args) throws Exception {
        if (args.length != 3 && args.length != 4) {
            System.out.println("Command line args:\n\t<bind port> <proxy host> <proxy port>\nOR\n\t<src host> <src port> <proxy host> <proxy port>");
            return;
        }

        if (args.length == 3) {
            int bindPort = Integer.parseInt(args[0]);
            String proxyHost = args[1];
            int proxyPort = Integer.parseInt(args[2]);


            if (Boolean.getBoolean("reverse"))
                runServerReverse(bindPort, proxyHost, proxyPort);
            else
                runServer(bindPort, proxyHost, proxyPort);

        } else {
            String srcHost = args[0];
            int srcPort = Integer.parseInt(args[1]);
            String proxyHost = args[2];
            int proxyPort = Integer.parseInt(args[3]);
            runClient(srcHost, srcPort, proxyHost, proxyPort);
        }
    }

    /**
     * Connects to source host:port and re-transmits received data to proxyHost:proxyPort
     */
    private static void runClient(String srcHost, int srcPort, String proxyHost, int proxyPort) throws IOException {
        Socket input = new Socket(srcHost, srcPort);
        configure(input);
        print("TCP Proxy input", input);

        Socket output = new Socket(proxyHost, proxyPort);
        configure(output);
        print("TCP Proxy output", output);

        new Proxy(input.getInputStream(), output.getOutputStream()).start();
    }

    /**
     * Launches server on bindPort and re-transmits received data to proxyHost:proxyPort
     */
    private static void runServer(int bindPort, String proxyHost, int proxyPort) throws IOException {
        ServerSocket ss = new ServerSocket(bindPort);
        System.out.println("Proxy is listening on port " + ss.getLocalPort());

        Socket input = ss.accept();
        configure(input);
        print("TCP Proxy input", input);

        Socket output = new Socket(proxyHost, proxyPort);
        configure(output);
        print("TCP Proxy output", output);

        new Proxy(input.getInputStream(), output.getOutputStream()).start();
    }

    /**
     * Connects to proxyHost:proxyPort and re-transmit received data to clients who connect on bindPort
     */
    private static void runServerReverse(int bindPort, String proxyHost, int proxyPort) throws IOException {
        ServerSocket ss = new ServerSocket(bindPort);
        System.out.println("ReverseProxy is listening on port " + ss.getLocalPort());

        Socket output = ss.accept();
        configure(output);
        print("TCP Proxy output", output);

        Socket input = new Socket(proxyHost, proxyPort);
        configure(input);
        print("TCP Proxy input", input);

        new Proxy(input.getInputStream(), output.getOutputStream()).start();
    }

}