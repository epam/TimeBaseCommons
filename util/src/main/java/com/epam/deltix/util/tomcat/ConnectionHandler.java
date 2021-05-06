package com.epam.deltix.util.tomcat;

import com.epam.deltix.util.lang.Disposable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 */
public interface ConnectionHandler extends Disposable {
    public int          getMarkLimit();

    public boolean      handleConnection(
            Socket socket,
            BufferedInputStream bis,
            OutputStream os
    ) throws IOException;

    @Override
    void close();
}
