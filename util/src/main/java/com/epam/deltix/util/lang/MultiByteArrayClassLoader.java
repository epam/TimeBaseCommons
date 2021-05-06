package com.epam.deltix.util.lang;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;

import com.epam.deltix.util.io.IOUtil;

/**  
 * Variation of SingleByteArrayClassLoader that supports nested classes.
 * 
 * It is used by JavaCompilationWorkspace to load auto-generated cursor classes.
 * 
 * NOTE: Only classes that belong to default package are supported ! 
 */
public class MultiByteArrayClassLoader extends ClassLoader {
	
	private final String [] classNames;
	private final byte [] [] classBytes;
	
	public MultiByteArrayClassLoader (File rootDir, final String className) 
		throws ClassNotFoundException 
	{
		final File[] classFiles = rootDir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (name.startsWith(className) && name.endsWith(".class")) {
					char ch = name.charAt(className.length()); 
					return ch == '.' || ch == '$';
				}
				return false;
			}
		});
		
		final int numClasses = classFiles.length;
		classNames = new String [numClasses];
		classBytes = new byte [numClasses][];
		for (int i=0; i < numClasses; i++) {
			File classFile = classFiles [i];
			String fileName = classFile.getName();
			classNames [i] = fileName.substring(0, fileName.length() - 6); // cut ".class"
			classBytes [i] = loadClassBytes (classFile);
		}
		
		for (int i=0; i < numClasses; i++) 
			classFiles [i].delete();
	}

	@Override
    public InputStream      getResourceAsStream (String name) {
		final int numClasses = classNames.length;
		for (int i=0; i < numClasses; i++) 
			if (classNames[i].equals(name))
				return new ByteArrayInputStream (classBytes[i]);
		
		return null;
    }

	@Override
    protected Class <?>             findClass (String name)
	    throws ClassNotFoundException
	{
		final int numClasses = classNames.length;
		for (int i=0; i < numClasses; i++) { 
			if (classNames[i].equals(name)) {
				byte [] b = classBytes[i];
				return defineClass (name, b, 0, b.length);
			}
		}
		throw new ClassNotFoundException (name);
	}
	
    private static byte [] loadClassBytes (File classFile)
	    throws ClassNotFoundException 
	{
	    try {
	        return (IOUtil.readBytes (classFile));
	    } catch (IOException iox) {
	        throw new ClassNotFoundException (
	            "Failed to read file: " + classFile + " due to: " + iox, 
	            iox
	        );
	    }
	}
	
	public static Class loadClass(File workingDir, String className) 
		throws ClassNotFoundException
	{
		return new MultiByteArrayClassLoader (workingDir, className).loadClass (className);
	}
    
}
