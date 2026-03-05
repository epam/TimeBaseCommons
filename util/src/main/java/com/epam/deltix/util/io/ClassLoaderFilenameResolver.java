/*
 * Copyright 2021 EPAM Systems, Inc
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
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