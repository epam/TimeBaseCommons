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
package com.epam.deltix.util.os;

import com.epam.deltix.util.lang.Disposable;
import com.epam.deltix.util.lang.Util;

import java.io.*;

/**
 *
 */
public class NamedPipe implements Closeable {
    final private RandomAccessFile pipe;
    private byte[] readBuf = new byte[2048];

    public NamedPipe(String name, String mode) throws FileNotFoundException {
        pipe = new RandomAccessFile(name, mode);
    }

    public synchronized void             close() throws IOException {
        pipe.close();
    }

    public synchronized RandomAccessFile getRAF() {
        return pipe;
    }

    public synchronized void             writeString(String msg) throws IOException {
        pipe.write(msg.getBytes());
    }

    public synchronized String           readString() throws IOException {
        int count = pipe.read(readBuf);
        if (count > 0)
            return new String(readBuf, 0, count, "UTF-8");
        else
            return "";
    }

}