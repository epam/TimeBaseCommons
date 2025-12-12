package com.epam.deltix.util.io;

import java.io.*;

/**
 *  Adapts java.lang.ClassLoader to the FilenameResolver interface.
 */
public class ClassLoaderFilenameResolver implements FilenameResolver {
    public static final FilenameResolver    STD_CLASSPATH_RESOLVER =
        new ClassLoaderFilenameResolver (
            ClassLoaderFilenameResolver.class.getClassLoader ()
        );
    
    private ClassLoader         mDelegate;
    private String []           mRoots;
    
    /**
     *	Creates an instance of FilenameResolver, which delegates all requests
     *	to <code>delegate</code>.
     *
     *  @param delegate A ClassLoader on which getResourceAsStream () is called.
     *                  Supply null to use the SystemClassLoader.
	 *	@param roots	A set of paths that are sequentially
	 *					searched for the specified relative paths. Not supplying any arguments
     *                  is equivalent to supplying a single empty string, which causes
     *                  the search to start at the top.
	 */
    public ClassLoaderFilenameResolver (ClassLoader delegate, String ... roots) {
    	mDelegate = delegate;
        mRoots = roots.length == 0 ? new String [] { "" } : roots;
    }
    
    /**
     *	Creates an instance of FilenameResolver, which delegates all requests
     *	to the system ClassLoader.
     *
	 *	@param roots	A set of paths that are sequentially
	 *					searched for the specified relative paths. Not supplying any arguments
     *                  is equivalent to supplying a single empty string, which causes
     *                  the search to start at the top.
     */
    public ClassLoaderFilenameResolver (String ... roots) {
    	this (null, roots);
    }
    
    /**
     *  Throws an exception.
     */
    public File         find (String relPath) {
    	throw new UnsupportedOperationException (
    		"Impossible to get a File out of a ClassLoader"
    	);
    }
    
    /**
     *  Opens a resource using the underlying class loader.
     */
    public InputStream  open (String relPath) throws IOException {
        StringBuffer        sb = new StringBuffer ();
        
        for (String root : mRoots) {
            if (root.length () > 0) {
                sb.append (root);

                if (!root.endsWith ("/"))
                    sb.append ("/");
            }
            
            sb.append (relPath);
            
            String          resultingPath = sb.toString ();
            
            InputStream     is =
                mDelegate == null ?
                    ClassLoader.getSystemResourceAsStream (resultingPath) :
                    mDelegate.getResourceAsStream (resultingPath);
            
            if (is != null)
                return (is);
            
            sb.setLength (0);
        }
        
        return (null);
    }
}
