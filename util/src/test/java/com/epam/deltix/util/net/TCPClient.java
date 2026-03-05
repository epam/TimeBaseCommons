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

import java.net.Socket;

import static com.epam.deltix.util.net.TCPConfiguration.MESSAGE_RATE_PER_SEC;
import static com.epam.deltix.util.net.TCPConfiguration.configure;
import static com.epam.deltix.util.net.TCPConfiguration.print;

/**
 * Connects to server and bombards it with messages at specified rate
 */
public class TCPClient {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Command line args: <host> <port>");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        int rate = (args.length > 2) ? Integer.parseInt(args[2]) : MESSAGE_RATE_PER_SEC;

        Socket socket = new Socket(host, port);
        configure(socket);
        print("TCP Client connected", socket);

        new TCPSender(rate, socket.getOutputStream()).start();
    }

}