package deltix.util.lang;

import deltix.util.io.IOUtil;
import java.io.*;

/**
 *  UNTESTED
 */
public class FileSystemClassLoader extends AbstractClassLoader {
    private File            mClassDir;
    
    public FileSystemClassLoader (File rootDir) {
        mClassDir = rootDir;
    }

    protected byte []       loadClassBytes (String name)
        throws ClassNotFoundException 
    {
        File        classFile = new File (mClassDir, name.replace (".", "/") + ".class");
        
        if (!classFile.exists ())
            throw new ClassNotFoundException ("File for class " + name + " not found in " + mClassDir);
        
        try {
            return (IOUtil.readBytes (classFile));
        } catch (IOException iox) {
            throw new ClassNotFoundException (
                "Failed to read file: " + classFile + " due to: " + iox, 
                iox
            );
        }
    }        
}
