package com.epam.deltix.util.vfs;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class VFileSystemFactory {
    
    private static final VFileSystemFactory INSTANCE = new VFileSystemFactory();

    public static VFileSystemFactory getInstance() {
        return INSTANCE;
    }
   
    private VFileSystemFactory() {        
    }
    
    public VFileSystem mount(URL url) throws IOException {
        return mount(url, null);
    }

    public VFileSystem mount(URL url, Properties props) throws IOException {
        final String protocol = url.getProtocol();
        final String path = url.getPath();
        switch (protocol) {
            case "file":
                if (path != null && path.toLowerCase().endsWith(".zip")) {
                    return new ZipFileSystem(new File(path), props);
                } else {
                    try {
                        return new LocalFileSystem(new File(url.toURI()));
                    } catch (URISyntaxException e) {
                        throw new IOException(e);
                    }
                }
            case "jar":
                return new ZipFileSystem(new File(path), props);
            case "http":
            case "https":
                return new HttpFileSystem(url);
        }
        
        throw new IllegalArgumentException("Unsupported protocol: " + protocol);
    }    
    
}
