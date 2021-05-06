package com.epam.deltix.util.net;

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
    public long             getLastModified() throws IOException {
        return (file.lastModified());
    }

    @Override
    public InputStream      openStream () throws IOException {
        return (new FileInputStream (file));
    }

    @Override
    public InputStream      openStream(int timeout) throws IOException {
        return openStream();
    }

    @Override
    public String           toString () {
        return (file.getPath ());
    }
}
