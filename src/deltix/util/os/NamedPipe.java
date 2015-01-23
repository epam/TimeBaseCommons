package deltix.util.os;

import deltix.util.lang.Disposable;
import deltix.util.lang.Util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 */
public class NamedPipe implements Disposable {
    RandomAccessFile pipe = null;
    byte[] readBuf = new byte[2048];

    public                  NamedPipe() {
    }

    public void             open(String name, String mode) throws IllegalArgumentException, FileNotFoundException {
        if (!Util.IS_WINDOWS_OS && mode == "rw")
            throw new IllegalArgumentException("Bidirection mode available only for Windows.");

        pipe = new RandomAccessFile(name, mode);
    }

    public void             close() {
        try {
            pipe.close();
        } catch (Exception e) {
            Util.LOGGER.warning(e.getMessage());
        }
        pipe = null;
    }

    public void             writeString(String msg) throws IOException {
        pipe.write(msg.getBytes());
    }

    public String           readString() throws IOException {
        int count = pipe.read(readBuf);
        if (count > 0)
            return new String(readBuf, 0, count, "UTF-8");
        else
            return "";
    }

}

