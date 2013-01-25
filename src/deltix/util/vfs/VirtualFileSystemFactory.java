package deltix.util.vfs;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

public class VirtualFileSystemFactory {
    
    private static final VirtualFileSystemFactory INSTANCE = new VirtualFileSystemFactory();

    public static VirtualFileSystemFactory getInstance() {
        return INSTANCE;
    }
   
    private VirtualFileSystemFactory() {        
    }
    
    public VirtualFileSystem mount(URL url) throws IOException {
        return mount(url, null);
    }

    public VirtualFileSystem mount(URL url, Properties props) throws IOException {
        final String protocol = url.getProtocol();
        final String path = url.getPath();
        switch (protocol) {
            case "file":
                if (path != null && path.toLowerCase().endsWith(".zip")) {
                    return new ZipFileSystem(new File(path));
                } else {
                    try {
                        return new LocalFileSystem(new File(url.toURI()));
                    } catch (URISyntaxException e) {
                        throw new IOException(e);
                    }
                }
            case "jar":
                return new ZipFileSystem(new File(path));
            case "http":
                return new HttpFileSystem(null);
            case "https":
                return new HttpsFileSystem();
            case "sftp":
                return new SftpFileSystem();
        }
        
        throw new IllegalArgumentException("Unsupported protocol: " + protocol);
    }
    
}
