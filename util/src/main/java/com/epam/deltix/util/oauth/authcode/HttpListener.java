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
