package com.epam.deltix.util.io;

import java.io.IOException;
import java.io.OutputStream;

public class ByteQueueOutputStream extends OutputStream {
    private final ByteQueueInputStream in;
    private boolean closed = false;

    public ByteQueueOutputStream(ByteQueueInputStream in) {
        this.in = in;
    }

    @Override
    public void write(int b) throws IOException {
        assertOpen();
        in.putData((byte)b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        assertOpen();
        in.putData(b, off, len);
    }

    @Override
    public void write(byte[] b) throws IOException {
        assertOpen();
        write(b, 0, b.length);
    }

    private void assertOpen() throws IOException {
        if (closed)
            throw new EOQException("Closed");
    }

    @Override
    public void flush() throws IOException {
        super.flush();
    }

    @Override
    public void        close () {
        closed = true;
    }
}
