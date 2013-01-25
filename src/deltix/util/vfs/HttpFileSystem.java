package deltix.util.vfs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class HttpFileSystem implements VirtualFileSystem {

    private final URL root;

    HttpFileSystem(URL root) {
        this.root = root;
    }        
    
    @Override
    public boolean exists(String path) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDirectory(String path) throws PathNotFoundException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isFile(String path) throws PathNotFoundException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String[] listFiles(String path) throws PathNotFoundException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String[] listDirectories(String path) throws PathNotFoundException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mkdirs(String path) throws PathNotFoundException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(String path) throws PathNotFoundException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OutputStream openToWrite(String path) throws PathNotFoundException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public InputStream openToRead(String path) throws PathNotFoundException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void unmount() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
