package deltix.util.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class BufferedInputStreamEx extends BufferedInputStream {
    public BufferedInputStreamEx(InputStream in, byte[] buf, int size) {
        super(in);

        //copy buf to this.buf
        for (int i = 0; i < size && i < this.buf.length; ++i)
            this.buf[i] = buf[i];

        this.count = size;
    }

}
