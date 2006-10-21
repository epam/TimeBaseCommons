package deltix.util.io;

import java.io.*;

/**
 *  Adapts java.lang.ClassLoader to the FilenameResolver interface.
 */
public class ClassLoaderFilenameResolver implements FilenameResolver {
    private ClassLoader         mDelegate;
    
    /**
     *	Creates an instance of FilenameResolver, which delegates all requests
     *	to <code>delegate</code>.
     */
    public ClassLoaderFilenameResolver (ClassLoader delegate) {
    	mDelegate = delegate;
    }
    
    /**
     *	Creates an instance of FilenameResolver, which delegates all requests
     *	to the system ClassLoader.
     */
    public ClassLoaderFilenameResolver () {
    	this (null);
    }
    
    /**
     *  Throws an exception.
     */
    public File         find (String relPath) {
    	throw new RuntimeException (
    		"Impossible to get a File out of a ClassLoader"
    	);
    }
    
    /**
     *  Opens a resource using the underlying class loader.
     */
    public InputStream  open (String relPath) throws IOException {
    	return (
    		mDelegate == null ?
    			ClassLoader.getSystemResourceAsStream (relPath) :
    			mDelegate.getResourceAsStream (relPath)
    	);
    }
}
