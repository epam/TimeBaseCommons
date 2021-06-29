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
package com.epam.deltix.util.lang;

import java.io.File;
import java.io.IOException;

import com.epam.deltix.util.io.IOUtil;

/**
 *  Loads a single specified class from the supplied byte array.
 */
public class SingleByteArrayClassLoader extends AbstractClassLoader {
    private String          mResourceName;
    private byte []         mBytes;
    
    public SingleByteArrayClassLoader (String className, byte [] bytes) {
        super (SingleByteArrayClassLoader.class.getClassLoader (), false);
        mResourceName = classNameToResourcePath(className);
        mBytes = bytes;
    }
    
    @Override
    protected byte[] findResourceAsByteArray(String resourceName) {
        if (!resourceName.equals (mResourceName))
            return null;
        
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
        	try {
        	    classFile.delete();
        	} catch (Exception e) {
        	    e.printStackTrace();
        	}
        }
	}
}