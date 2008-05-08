package deltix.util.lang;

import java.io.*;

import deltix.util.io.IOUtil;
import java.net.URL;

/**
 *  UNTESTED
 */
public final class FileSystemClassLoader extends AbstractClassLoader {
    private final File mClassDir;
    
    public FileSystemClassLoader (File rootDir, boolean searchParentFirst) {
        super (FileSystemClassLoader.class.getClassLoader (), searchParentFirst);
        mClassDir = rootDir;
    }

    @Override
    public URL              findResource (String name) {
        return (IOUtil.createFileUrl (new File (mClassDir, name)));
    }
    
    @Override
    protected InputStream   findResourceAsStream (String name) throws IOException {
        return (new FileInputStream (new File (mClassDir, name)));
    }          
}
