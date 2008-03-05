package deltix.util.lang;

import java.io.File;
import java.io.IOException;

import deltix.util.io.IOUtil;

/**
 *  Loads a single specified class from the supplied byte array.
 */
public class SingleByteArrayClassLoader extends AbstractClassLoader {
    private String          mClassName;
    private byte []         mBytes;
    
    public SingleByteArrayClassLoader (String className, byte [] bytes) {
        mClassName = className;
        mBytes = bytes;
    }
    
    protected byte [] loadClassBytes (String name) throws ClassNotFoundException {
        if (!name.equals (mClassName))
            throw new ClassNotFoundException (name);
        
        return (mBytes);
    }

	public static Class loadClass(File workingDir, String className) 
		throws ClassNotFoundException
	{
		File classFile = new File (workingDir, className.replace ('.', File.separatorChar) + ".class");
		if ( ! classFile.exists())
			throw new ClassNotFoundException (className);
		
        try {
            return new SingleByteArrayClassLoader (className, IOUtil.readBytes (classFile)).loadClass (className);
        } catch (IOException x) {
            throw new ClassNotFoundException ("Cannot load class bytes: " + x.getMessage(), x);
        } finally {
        	classFile.delete();
        }
	}
}
