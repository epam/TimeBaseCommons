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

import com.epam.deltix.util.io.IOUtil;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *  
 */
public final class MemoryZipClassLoader extends AbstractClassLoader {
    private final Map <String, byte []>     mResources =
        new HashMap <String, byte []> ();
        
    private final Map <String, URL>     mResourceURLs =
        new HashMap <String, URL> ();
    
    public MemoryZipClassLoader (InputStream is, boolean searchParentFirst) 
        throws IOException, InterruptedException 
    {
        this (is, MemoryZipClassLoader.class.getClassLoader (), searchParentFirst);
    }
    
    public MemoryZipClassLoader (InputStream is, ClassLoader parent, boolean searchParentFirst) 
        throws IOException, InterruptedException 
    {
        super (parent, searchParentFirst);
        
        ZipInputStream      zis = new ZipInputStream (is);
        
        for (;;) {
            ZipEntry        zentry = zis.getNextEntry ();
            
            if (zentry == null)
                break;
            
            String          name = zentry.getName ();
            
            if (name.endsWith ("/"))
                continue;
            
            final byte[] bytes = IOUtil.readBytes (zis);
            mResources.put (name, bytes);
            mResourceURLs.put(name, new URL("memoryzip", getClass().getName(), 0, "/internal", new ByteArrayURLStreamHandler(bytes)));
        }
    }

    @Override
    protected byte []           findResourceAsByteArray (String name) {
        return (mResources.get (name));
    }

    @Override
    protected InputStream       findResourceAsStream (String name) 
        throws IOException 
    {
        byte []     bytes = mResources.get (name);
        
        return (bytes == null ? null : new ByteArrayInputStream (bytes));
    }
    
    @Override
    protected URL findResource(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }
        URL url = mResourceURLs.get(name);
        if (url == null) {
            url = getParent().getResource(name);
        }
        return url;
    }
    
    @Override
    //TODO: maybe use the parent one and remove this one? And make the parent method final
    public Enumeration<URL> getResources(final String name) throws IOException {
        if (mResourceURLs.containsKey(name)) {
            return new Enumeration<URL>() {
                private URL url = findResource(name);

                public boolean hasMoreElements() {
                    return url != null;
                }

                public URL nextElement() {
                    URL result = url;
                    url = null;
                    return result;
                }
            };
        } else {
            return getParent().getResources(name);
        }
    }
    
    private static class ByteArrayURLStreamHandler extends URLStreamHandler {
        private final byte[] bytes; 
        
        ByteArrayURLStreamHandler(byte[] bytes) {
            this.bytes = bytes;
        }

        protected URLConnection openConnection(URL url) throws IOException {
            return new ByteArrayURLConnection(url, bytes);
        }
    }
    
    private static class ByteArrayURLConnection extends URLConnection {
        private final byte[] bytes;

        protected ByteArrayURLConnection(URL url, byte[] bytes) {
            super(url);
            this.bytes = bytes;
        }

        public void connect() throws IOException {
            // empty
        }
        
        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(bytes);
        }
    }
}