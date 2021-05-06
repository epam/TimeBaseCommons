package com.epam.deltix.util.os;

import com.epam.deltix.util.lang.Disposable;
import com.epam.deltix.util.lang.Util;

import java.io.*;

/**
 *
 */
public class NamedPipe implements Closeable {
    final private RandomAccessFile pipe;
    private byte[] readBuf = new byte[2048];

    public NamedPipe(String name, String mode) throws FileNotFoundException {
        pipe = new RandomAccessFile(name, mode);
    }

    public synchronized void             close() throws IOException {
        pipe.close();
    }

    public synchronized RandomAccessFile getRAF() {
        return pipe;
    }

    public synchronized void             writeString(String msg) throws IOException {
        pipe.write(msg.getBytes());
    }

    public synchronized String           readString() throws IOException {
        int count = pipe.read(readBuf);
        if (count > 0)
            return new String(readBuf, 0, count, "UTF-8");
        else
            return "";
    }

}

