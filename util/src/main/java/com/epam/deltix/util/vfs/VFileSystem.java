package com.epam.deltix.util.vfs;

import java.io.Closeable;
import java.io.IOException;

public interface VFileSystem<T extends VFile> extends Closeable {
        
    T getRoot() throws IOException;

}
