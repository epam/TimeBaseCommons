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

import java.io.InputStream;

/**
 *	Caps an InputStream. This kind of Thread reads the
 *	specified InputStream until it hits the EOF, completely
 *	discarding the contents. Useful to attach this to the
 *	InputStreams of a Process, so the process does not block
 *	forever when it fills the buffers.
 */
public class InputStreamSink extends Thread {
    private InputStream         mInputStream;
    private boolean				mClose;
    
    /**
     *	Constructs an InputStreamSink.
     *
     *	@param is		The input stream to empty.
     *	@param close	Whether to close the stream when it ends.
     */
    public InputStreamSink (InputStream is, boolean close) {
        mInputStream = is;
        mClose = close;
    }

    /**
     *	Constructs an InputStreamSink which will close the stream at EOF.
     *
     *	@param is		The input stream to empty.
     */
    public InputStreamSink (InputStream is) {
        this (is, true);
    }

    public void     run () {
        try {
            byte []     buf = new byte [512];
            
            while (mInputStream.read (buf) >= 0);
            
            if (mClose)
            	mInputStream.close ();
        } catch (Exception x) {
        	x.printStackTrace ();
        }
    }
}