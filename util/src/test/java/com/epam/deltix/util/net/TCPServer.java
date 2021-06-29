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

import java.net.ServerSocket;
import java.net.Socket;

import static com.epam.deltix.util.net.TCPConfiguration.configure;

/** Binds on given port, when client connects this server bombards it with messages at specified rate */
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
            configure(socket);

            System.out.println("Accepted connection on " + bindPort);

            new TCPSender(rateMessagesPerSec, socket.getOutputStream()).start();

        }
    }

}