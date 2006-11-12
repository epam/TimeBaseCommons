package deltix.util.io;

import java.io.OutputStream;

/**
 * Null output.
 *
 * Example of use: code that uses Java serialization only to traverse object graph.
 */
public class NullOutputStream extends OutputStream {

    public void write(int b) {}

    public void write(byte b[]) {}

    public void write(byte b[], int off, int len) {}

    public void flush() {}

    public void close()  {}
}
