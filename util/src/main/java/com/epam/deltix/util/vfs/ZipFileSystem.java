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

import com.epam.deltix.util.io.ByteCountingInputStream;
import com.epam.deltix.util.lang.Util;
import com.epam.deltix.util.progress.ProgressIndicator;
import com.epam.deltix.util.vfs.VFileVisitor.VFileVisitResult;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFileSystem implements VFileSystem<ZipFileSystem.ZipFile> {
    public static final String          ZFS_WRITE_MODE = "ZFS_WRITE_MODE";
    
    private boolean                     writeMode;
    private final ZipInputStream        zin;
    private final ZipOutputStream       zout;
    
    public static void unzip(VFile vin, final File to) throws IOException, InterruptedException {
        unzip(vin, to, null);
    }
    
    public static void unzip(VFile vin, final File to, ProgressIndicator progress) throws IOException, InterruptedException {
        InputStream in = null;
        try {
            in = new BufferedInputStream(vin.openToRead(), 8192);            
            unzip(in, to);            
        } finally {
            Util.close(in);
        }        
    }

    public static void unzip(InputStream in, final File to) throws IOException, InterruptedException {
        unzip(in, to, null);
    }
    
    public static void unzip(InputStream in, final File to, final ProgressIndicator progress) throws IOException, InterruptedException {
        
        if (!to.exists()) {
            to.mkdirs();
        } else if (!to.isDirectory()) {
            throw new IOException(to + " isn't a folder.");
        }
        
        final ZipFileSystem fs = new ZipFileSystem(in);
        try {
            final byte[] buff = new byte[8192];
            fs.getRoot().doWalkTree(new SimpleVFileVisitor<VFile>() {
                @Override
                public VFileVisitResult visitFile(VFile file) throws IOException, InterruptedException {                                        
                    
                    checkNotInterrupted();
                    
                    final File localFile = new File(to, file.getAttributes().absolutePath());
                    localFile.getParentFile().mkdirs();
                    
                    InputStream in = null;
                    OutputStream out = null;
                    int l;
                    try {
                        in = file.openToRead();
                        
                        if (progress != null) {
                            in = new ByteCountingInputStream(in) {
                                @Override
                                public void numBytesChanged() {
                                    progress.setWorkDone(getNumBytesRead());
                                }
                            };
                        }
                        
                        out = new BufferedOutputStream(new FileOutputStream(localFile), buff.length);                        
                                                
                        while ((l = in.read(buff)) > -1) {
                            checkNotInterrupted();
                            out.write(buff, 0, l);
                        }                       
                    } finally {
                        Util.close(in);
                        Util.close(out);
                    }
                    
                    return VFileVisitResult.CONTINUE;                    
                }
            });
            
        } finally {
            Util.close(fs);
        }
    }
    
    public ZipFileSystem(InputStream in) throws IOException {        
        writeMode = false;
        zin = new ZipInputStream(in);
        zout = null;
    }
    
    public ZipFileSystem(OutputStream out) throws IOException {        
        writeMode = true;
        zin = null;
        zout = new ZipOutputStream(out);
    }

    public ZipFileSystem(File file) throws IOException {
        this(file, null);
    }
    
    public ZipFileSystem(File file, Properties properties) throws IOException {
        
        writeMode = properties != null
                && properties.getProperty(ZFS_WRITE_MODE, "false").
                toLowerCase().equals("true");

        if (writeMode) {
            zin = null;
            zout = new ZipOutputStream(new FileOutputStream(file));
        } else {
            zin = new ZipInputStream(new FileInputStream(file));
            zout = null;
        }
    }                

    @Override
    public ZipFile getRoot() throws IOException {
        return new ZipFile(null);
    }

    @Override
    public void close() throws IOException {
        if (writeMode) {
            Util.close(zout);
        } else {
            Util.close(zin);
        }
    }    
    
    public class ZipFile implements VFile, VFileAttributes {
        
        private final String absolutePath;
        private final String name;
        private final ZipEntry entry;
        
        protected ZipFile(ZipEntry entry) {            
            this.entry = entry;
            this.absolutePath = entry != null ? '/' + entry.getName() : "/";
            this.name = VFiles.getName(absolutePath);
        }
        
        @Override
        public boolean exists() throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
        @Override
        public VFileAttributes getAttributes() {
            return this;
        }

        @Override
        public VFile get(String path) throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void walkTree(VFileVisitor<VFile> visitor) throws IOException, InterruptedException {
            if (writeMode) {
                throw new IOException("Cannot walk in write mode.");
            }
            doWalkTree(visitor);
        }

        private VFileVisitResult doWalkTree(VFileVisitor<VFile> visitor) throws IOException, InterruptedException {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }

            if (isDirectory()) {
                if (visitor.preVisitDirectory(this) == VFileVisitResult.TERMINATE) {
                    return VFileVisitResult.TERMINATE;
                }
                ZipEntry nextEntry;
                while ((nextEntry = zin.getNextEntry()) != null) {
                    if (new ZipFile(nextEntry).doWalkTree(visitor) == VFileVisitResult.TERMINATE) {
                        return VFileVisitResult.TERMINATE;
                    }
                }
                if (visitor.postVisitDirectory(this) == VFileVisitResult.TERMINATE) {
                    return VFileVisitResult.TERMINATE;
                }
            } else {
                if (visitor.visitFile(this) == VFileVisitResult.TERMINATE) {
                    return VFileVisitResult.TERMINATE;
                }
            }
            
            return VFileVisitResult.CONTINUE;
        }
        
        @Override
        public void mkdirs() throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void delete() throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public OutputStream openToWrite() throws IOException {
            if (!writeMode) {
                throw new IOException("Cannot write in read mode.");
            }
            if (entry == null) {
                throw new IOException("Cannot write to root.");
            }
            
            return new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    zout.write(b);
                }

                @Override
                public void flush() throws IOException {
                    zout.flush();
                }
                
                @Override
                public void close() throws IOException {
                    zout.closeEntry();
                }                                
            };
        }

        @Override
        public InputStream openToRead() throws IOException {
            if (writeMode) {
                throw new IOException("Cannot read in write mode.");
            }
            if (entry == null) {
                throw new IOException("Cannot read from root.");
            }
            
            return new InputStream() {
                @Override
                public int read() throws IOException {
                    return zin.read();
                }
                
                @Override
                public void close() throws IOException {
                    zin.closeEntry();
                }                                
            };
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
        public long creationTime() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public boolean isDirectory() {
            return entry == null || entry.isDirectory();
        }

        @Override
        public boolean isFile() {
            return !isDirectory();
        }

        @Override
        public long lastModifiedTime() {
            return entry == null ? 0 : entry.getTime();
        }

        @Override
        public long size() {
            return isDirectory() ? 0 : entry.getSize();
        }               
    }        
}