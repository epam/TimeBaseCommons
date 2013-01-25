package deltix.util.vfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LocalFileSystem implements VirtualFileSystem {

    private final File root;

    LocalFileSystem(File root) throws IOException {
        if (!root.isAbsolute() ||
                !root.exists() ||
                !root.isDirectory()) {
            throw new IOException(root + " isn't a folder.");
        }
        this.root = root;        
    }        
    
    @Override
    public boolean exists(String path) throws IOException {
        return new File(root, path).exists();
    }

    @Override
    public boolean isDirectory(String path) throws PathNotFoundException, IOException {        
        return checkAndGetFile(path).isDirectory();
    }

    @Override
    public boolean isFile(String path) throws PathNotFoundException, IOException {
        return checkAndGetFile(path).isFile();
    }

    @Override
    public String[] listFiles(String path) throws PathNotFoundException, IOException {
        return checkAndGetFile(path).list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {                
                return new File(dir, name).isFile();
            }
        });
    }

    @Override
    public String[] listDirectories(String path) throws PathNotFoundException, IOException {
        return checkAndGetFile(path).list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {                
                return new File(dir, name).isDirectory();
            }
        });
    }

    @Override
    public void mkdirs(String path) throws PathNotFoundException, IOException {
        checkAndGetFile(path).mkdirs();
    }

    @Override
    public void delete(String path) throws PathNotFoundException, IOException {
        checkAndGetFile(path).delete();
    }

    @Override
    public OutputStream openToWrite(String path) throws PathNotFoundException, IOException {
        return new FileOutputStream(checkAndGetFile(path));
    }

    @Override
    public InputStream openToRead(String path) throws PathNotFoundException, IOException {
        return new FileInputStream(checkAndGetFile(path));
    }

    @Override
    public void unmount() throws IOException {        
    }
    
    private File checkAndGetFile(String path) throws PathNotFoundException {
        final File file = new File(root, path);
        if (!file.exists()) {
            throw new PathNotFoundException(path + " not found for " + file.getAbsolutePath());
        }     
        return file;
    }
}
