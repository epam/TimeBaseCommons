package deltix.util.lang;

import java.io.*;

import deltix.util.io.IOUtil;

/**
 *  UNTESTED
 */
public class FileSystemClassLoader extends AbstractClassLoader {
    private File            mClassDir;
    
    public FileSystemClassLoader (File rootDir) {
        mClassDir = rootDir;
    }

    public InputStream      getResourceAsStream (String name) {
        try {
            return (new FileInputStream (new File (mClassDir, name)));
        } catch (IOException iox) {
            return (null);
        }
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
    
    @Override
    public synchronized Class<?> loadClass(String name, boolean resolve) 
    	throws ClassNotFoundException
    {
    	return super.loadClass(name, resolve);
    }
}
