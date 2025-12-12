package com.epam.deltix.util.vfs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface VFile {    

    VFileAttributes getAttributes();    
    
    VFile get(String path) throws IOException;
    
    boolean exists() throws IOException;
    
    void walkTree(VFileVisitor<VFile> visitor) throws IOException, InterruptedException;
    
    void mkdirs() throws IOException;
                
    void delete() throws IOException;
    
    OutputStream openToWrite() throws IOException;

    InputStream openToRead() throws IOException;                 
        
}
