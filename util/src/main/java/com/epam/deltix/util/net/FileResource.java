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
package com.epam.deltix.util.net;

import java.io.*;

/**
 *
 */
public class FileResource extends Resource {
    private final File          file;

    public FileResource (File file) {
        this.file = file;
    }

    @Override
    public long             getSize () throws IOException {
        return (file.length ());
    }

    @Override
    public long             getLastModified() throws IOException {
        return (file.lastModified());
    }

    @Override
    public InputStream      openStream () throws IOException {
        return (new FileInputStream (file));
    }

    @Override
    public InputStream      openStream(int timeout) throws IOException {
        return openStream();
    }

    @Override
    public String           toString () {
        return (file.getPath ());
    }
}