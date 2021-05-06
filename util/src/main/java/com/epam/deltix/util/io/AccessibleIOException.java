package com.epam.deltix.util.io;

import java.io.*;

public class AccessibleIOException extends IOException {

    private final File _file;

    public AccessibleIOException (Throwable cause,
                                  File file) {
        super (cause);
        _file = file;
    }

    public File getFile () {
        return _file;
    }

}
