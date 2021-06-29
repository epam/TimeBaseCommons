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
package com.epam.deltix.util.vfs;

import com.epam.deltix.util.lang.Util;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpFileSystem implements VFileSystem<HttpFileSystem.HttpFile> {

    private final URL root;

    HttpFileSystem(URL root) {
        this.root = root;
    }        

    @Override
    public HttpFile getRoot() throws IOException {                        
        return new HttpFile(root);
    }

    @Override
    public void close() throws IOException {        
    }
        
    public class HttpFile implements VFile, VFileAttributes {

        private final URL       url;
        private final String    absolutePath;
        private final String    name;
        
        private HttpFile(URL url) {
            this.url = url;
            this.absolutePath = url.getPath();            
            this.name = VFiles.getName(url);
        }
        
        @Override
        public boolean exists() throws IOException {
            final HttpURLConnection connection = openHeaderConnection();

            try {
                return (connection.getResponseCode() == HttpURLConnection.HTTP_OK);
            } finally {
                connection.disconnect();
            }
        }

        @Override
        public VFileAttributes getAttributes() {
            return this;
        }
                
        @Override
        public void delete() throws IOException {
            throw new UnsupportedOperationException("Not supported.");
        }
        
        @Override
        public OutputStream openToWrite() throws IOException {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public InputStream openToRead() throws IOException {
            return new HttpInputStream(openGetConnection());
        }

        @Override
        public long creationTime() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public boolean isDirectory() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isFile() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public long lastModifiedTime() {
            final HttpURLConnection connection = openHeaderConnection();

            try {
                return (connection.getLastModified());
            } finally {
                connection.disconnect();
            }
        }

        @Override
        public long size() {
            final HttpURLConnection connection = openHeaderConnection();

            try {
                return (connection.getContentLength());
            } finally {
                connection.disconnect();
            }
        }
        
        @Override
        public String absolutePath() {
            return absolutePath;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public VFile get(String path) throws IOException {
            return new HttpFile(VFiles.appendRelativePath(url, path));
        }

        @Override
        public void walkTree(VFileVisitor<VFile> visitor) throws IOException, InterruptedException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void mkdirs() throws IOException {
            throw new UnsupportedOperationException("Not supported.");
        }                       
        
        private HttpURLConnection openHeaderConnection() {
            try {
                final HttpURLConnection result = (HttpURLConnection) url.openConnection();
                result.setUseCaches(false);
                result.setRequestMethod("HEAD");
                result.setDoInput(true);
                result.setDoOutput(false);

                result.connect();

                return result;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        private HttpURLConnection openGetConnection() {
            try {
                final HttpURLConnection result = (HttpURLConnection) url.openConnection();
                result.setUseCaches(false);
                
                result.connect();
                
                return result;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }        
        
        private class HttpInputStream extends InputStream {

            private final HttpURLConnection connection;
            private final InputStream delegate;

            public HttpInputStream(HttpURLConnection connection) throws IOException {
                this.connection = connection;
                delegate = connection.getInputStream();
            }
                                    
            @Override
            public int read() throws IOException {
                return delegate.read();
            }

            @Override
            public void close() throws IOException {
                Util.close(delegate);
                connection.disconnect();
            }                                    
        } 
    }
}