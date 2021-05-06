package com.epam.deltix.util.io;

import java.io.*;

/**
 *  Abstracts the notion of searching for files by relative path.
 */
public interface FilenameResolver {
    /**
     *  Searches for the specified file or directory by relative path
     *  in a way that is implementation-dependent.
     */
    public File         find (String relPath) throws IOException;
    
    /**
     *  Opens the specified file or directory by relative path
     *  in a way that is implementation-dependent.
     */
    public InputStream  open (String relPath) throws IOException;
}
