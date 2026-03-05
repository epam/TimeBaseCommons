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

import java.io.*;
import java.util.*;
import java.util.jar.*;

/**
 *  Caches the contents of a jar file (selectively) mainly for the purpose
 *  of loading classes.
 */
public class JarClassLoader extends AbstractClassLoader {
    private final JarFile               mJarFile;
    
    public JarClassLoader (JarFile f, ClassLoader parent, boolean searchParentFirst) {
        super (parent, searchParentFirst);
        mJarFile = f;
    }
        
    @Override
    protected InputStream       findResourceAsStream (String name) throws IOException {
        JarEntry    e = mJarFile.getJarEntry (name);
        
        return (e == null ? null : mJarFile.getInputStream (e));
    }
    
    public void                 loadAllClasses (Collection <Class <?>> classes)
        throws ClassNotFoundException
    {
        for (
            Enumeration <JarEntry> entries = mJarFile.entries ();
            entries.hasMoreElements ();
        )
        {
            JarEntry    e = entries.nextElement ();
            String      name = e.getName ();
            
            if (name.endsWith (".class")) 
                classes.add (loadClass (resourcePathToClassName (name)));            
        }
    }
    
    public void                 loadAllClassesSkipErrors (
        Collection <Class <?>>      classes
    )
    {
        for (
            Enumeration <JarEntry> entries = mJarFile.entries ();
            entries.hasMoreElements ();
        )
        {
            JarEntry    e = entries.nextElement ();
            String      name = e.getName ();
            
            if (name.endsWith (".class")) {
                try {
                    classes.add (loadClass (resourcePathToClassName (name)));
                } catch (NoClassDefFoundError x) {
                    // Skip
                    x.printStackTrace();
                } catch (ClassNotFoundException x) {
                    // Skip
                }
            }
        }
    }
    /*
    public static void          main (String [] args) throws Exception {
        JarClassLoader               jcl = 
            new JarClassLoader (            
                new JarFile ("D:/dev/build/dxstrats.jar"),
                JarClassLoader.class.getClassLoader (),
                false
            );
        
        Collection <Class <?>>  classes = new ArrayList <Class <?>> ();
        
        jcl.loadAllClasses (classes);
        
        for (Class <?> c : classes) {
            System.out.println (c + " from " + c.getClassLoader ());
        }
    }  
     */  
}