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

import java.io.IOException;
import java.io.OutputStream;

public class ByteQueueOutputStream extends OutputStream {
    private final ByteQueueInputStream in;
    private boolean closed = false;

    public ByteQueueOutputStream(ByteQueueInputStream in) {
        this.in = in;
    }

    @Override
    public void write(int b) throws IOException {
        assertOpen();
        in.putData((byte)b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        assertOpen();
        in.putData(b, off, len);
    }

    @Override
    public void write(byte[] b) throws IOException {
        assertOpen();
        write(b, 0, b.length);
    }

    private void assertOpen() throws IOException {
        if (closed)
            throw new EOQException("Closed");
    }

    @Override
    public void flush() throws IOException {
        super.flush();
    }

    @Override
    public void        close () {
        closed = true;
    }
}