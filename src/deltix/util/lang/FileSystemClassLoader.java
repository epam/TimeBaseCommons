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
        this (rootDir, FileSystemClassLoader.class.getClassLoader (), searchParentFirst);
    }

    public FileSystemClassLoader (File rootDir, ClassLoader parent, boolean searchParentFirst) {
        super (parent, searchParentFirst);
        mClassDir = rootDir;        
        map (mClassDir, null);
    }

    private void            map (File dir, String packPath) {
        File []     files = dir.listFiles ();
        
        if (files == null)
            return;
        
        for (File f : files) {
            String          name = f.getName ();
            
            if (f.isDirectory ())
                map (f, packPath == null ? name : packPath + '.' + name);
            else if (name.equals ("package-info.class"))
                definePackage (packPath, null, null, null, null, null, null, null);
        }
    }
    
    @Override
    public URL              findResource (String name) {
        File        f = new File (mClassDir, name);
        return (f.exists () ? IOUtil.createFileUrl (f) : null);
    }
    
    @Override
    protected InputStream   findResourceAsStream (String name) {
        try {
            return (new FileInputStream (new File (mClassDir, name)));
        } catch (FileNotFoundException x) {
            return (null);
        }
    }          
}
