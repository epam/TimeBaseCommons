package deltix.util.vfs;

import deltix.util.lang.Util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFileSystem implements VirtualFileSystem {

    private final File zip;
    
    public ZipFileSystem(File file) throws IOException {
        if (!file.isAbsolute() &&
                !file.exists() &&
                !file.isFile()) {
            throw new IOException("Cannot find zip file: " + file);
        }
        
        zip = file;
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

    public Iterator<ReadeableZipEntry> openToRead() throws IOException {
        
        return new Iterator<ReadeableZipEntry>() {

            @Override
            public boolean hasNext() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public ReadeableZipEntry next() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }
    
    public void extractTo(File folder) throws IOException {
        if (!folder.exists()) {
            folder.mkdirs();
        } else if (!folder.isDirectory()) {
            throw new IOException(folder + " isn't a folder");
        }
        
        final ZipFile zipFile = new ZipFile(zip);
        final Enumeration<? extends ZipEntry> e = zipFile.entries();
        
        final byte[] buffer = new byte[4096];
        
        while (e.hasMoreElements()) {
            final ZipEntry entry = e.nextElement();
            File destinationFilePath = new File(folder, entry.getName());

            destinationFilePath.getParentFile().mkdirs();

            if (entry.isDirectory()) {
                continue;
            } else {
                
                InputStream bis = null;
                BufferedOutputStream bos = null;
                try {
                    bis = new BufferedInputStream(zipFile.getInputStream(entry));
                    bos = new BufferedOutputStream(new FileOutputStream(destinationFilePath), buffer.length);

                    int b;
                    while ((b = bis.read(buffer)) != -1) {
                        bos.write(buffer, 0, b);
                    }
                    bos.flush();

                } finally {
                    Util.close(bis);
                    Util.close(bos);
                }
            }
        }                        
    }
    
    @Override
    public void unmount() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public class ReadeableZipEntry implements Closeable {
        public final ZipEntry entry;
        public final InputStream content;

        private ReadeableZipEntry(ZipEntry entry, InputStream content) {
            this.entry = entry;
            this.content = content;
        }       
        
        @Override
        public void close() throws IOException {
            content.close();
        }
        
    }
}
