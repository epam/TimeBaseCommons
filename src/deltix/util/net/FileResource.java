package deltix.util.net;

import java.io.*;

/**
 *
 */
public class FileResource extends Resource {
    private final File          file;

    public FileResource (File file) {
        this.file = file;
    }

    @Override
    public long             getSize () throws IOException {
        return (file.length ());
    }

    @Override
    public InputStream      openStream () throws IOException {
        return (new FileInputStream (file));
    }

    @Override
    public String           toString () {
        return (file.getPath ());
    }
}
