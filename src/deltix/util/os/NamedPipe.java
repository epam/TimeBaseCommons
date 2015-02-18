package deltix.util.os;

import deltix.util.lang.Disposable;
import deltix.util.lang.Util;

import java.io.*;

/**
 *
 */
public class NamedPipe implements Disposable {
    final private RandomAccessFile pipe;
    private byte[] readBuf = new byte[2048];

    public NamedPipe(String name, String mode) {
        if (!Util.IS_WINDOWS_OS && mode == "rw")
            throw new IllegalArgumentException("Bidirection mode available only for Windows.");

        try {
            pipe = new RandomAccessFile(name, mode);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public synchronized void             close() {
        try {
            pipe.close();
        } catch (Exception e) {
            Util.LOGGER.warning(e.getMessage());
        }
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

