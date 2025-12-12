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
package com.epam.deltix.util.oauth.authcode;

import com.epam.deltix.gflog.api.Log;
import com.epam.deltix.gflog.api.LogFactory;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

class HttpListener {
    private static final Log LOG = LogFactory.getLog(HttpListener.class);

    private HttpServer server;
    private int port;

    void startListener(int port, HttpHandler httpHandler) {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", httpHandler);
            this.port = server.getAddress().getPort();
            server.start();
            LOG.debug("Http listener started. Listening on port: " + port);
        } catch (Exception e) {
            LOG.error().append("Http listener (").append(port).append(".").append(e).commit();
            throw new RuntimeException(e.getMessage());
        }
    }

    void stopListener() {
        if (server != null) {
            server.stop(0);
            LOG.debug("Http listener stopped");
        }
    }

    int port() {
        return port;
    }
}
