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

import com.epam.deltix.util.vfs.VFileVisitor.VFileVisitResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LocalFileSystem implements VFileSystem<LocalFileSystem.LocalFile> {

    private final File root;

    LocalFileSystem(File root) throws IOException {
        if (!root.isAbsolute() ||
                !root.exists() ||
                !root.isDirectory()) {
            throw new IOException(root + " isn't a folder.");
        }
        this.root = root;        
    }        

    @Override
    public LocalFile getRoot() throws IOException {
        return new LocalFile(root);
    }

    @Override
    public void close() throws IOException {        
    }        
    
    public class LocalFile implements VFile, VFileAttributes {

        private final File file;

        public LocalFile(File file) {
            this.file = file;
        }                
        
        @Override
        public boolean exists() throws IOException {
            return file.exists();
        }
        
        @Override
        public VFileAttributes getAttributes() {
            return this;
        }        
        
        @Override
        public VFile get(String path) throws IOException {
            return new LocalFile(new File(file, path));
        }

        @Override
        public void walkTree(VFileVisitor<VFile> visitor) throws IOException, InterruptedException {
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
                for (File subFile : file.listFiles()) {
                    if (new LocalFile(subFile).doWalkTree(visitor) == VFileVisitResult.TERMINATE) {
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
            file.mkdirs();
        }

        @Override
        public void delete() throws IOException {
            file.delete();
        }

        @Override
        public OutputStream openToWrite() throws IOException {
            return new FileOutputStream(file);
        }

        @Override
        public InputStream openToRead() throws IOException {
            return new FileInputStream(file);
        }

        @Override
        public String absolutePath() {
            return file.getAbsolutePath();
        }

        @Override
        public String name() {
            return file.getName();
        }

        @Override
        public long creationTime() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isDirectory() {
            return file.isDirectory();
        }

        @Override
        public boolean isFile() {
            return file.isFile();
        }

        @Override
        public long lastModifiedTime() {
            return file.lastModified();
        }

        @Override
        public long size() {
            return file.length();
        }
        
    }
}