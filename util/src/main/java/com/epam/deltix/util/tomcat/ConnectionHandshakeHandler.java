package com.epam.deltix.util.tomcat;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Inner implementation of Tomcat connection handler (TimeBase, SNMP, FIX)
 */
public interface ConnectionHandshakeHandler {
    /**
     *  Handle the initial transport-level handshake with a client.
     *
     *  @param socket   Socket to perform the handshake with.
     *  @return     true if the connection is accepted and socket added to the
     *              set of transport channels. false if socket should be closed.
     *
     *  @throws IOException
     */
    public boolean      handleHandshake(
            Socket socket,
            BufferedInputStream bis,
            OutputStream os
    ) throws IOException;
}
