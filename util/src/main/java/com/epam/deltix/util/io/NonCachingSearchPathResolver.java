package com.epam.deltix.util.io;

import java.io.*;

/**
 *  Searches for files under a set of root directories. This class
 *	does not try to create and cache a map of all directories, so it
 *	will be inefficient in the rare instances when the number of 
 *	roots is really large.
 */
public class NonCachingSearchPathResolver implements FilenameResolver {
	private File []		mRoots;
	
	/**
	 *	@param roots	A set of directories that are sequentially
	 *					searched for the specified files.
	 */
	public NonCachingSearchPathResolver (File ... roots) {
		mRoots = roots;
	}
	
    public File         find (String relPath) throws IOException {
    	for (File root : mRoots) {
    		File			test = new File (root, relPath);
    		
    		if (test.exists ())
    			return (test);
    	}
    	
    	return (null);
    }
    
    public InputStream  open (String relPath) throws IOException {
    	File		f = find (relPath);    	
    	return (f == null ? null : new FileInputStream (f));
    }
}
