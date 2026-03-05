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

import com.epam.deltix.util.collections.generated.ByteArrayList;
import com.epam.deltix.util.collections.SafeArrays;

/**
 *  Extension of java.io.ByteArrayInputStream
 */
public class ByteArrayInputStreamEx extends java.io.ByteArrayInputStream {
    private static final byte []    DUMMY = { };
    
    /**
     * Creates <code>ByteArrayInputStream</code>
     * that cannot read anything (will return EOFimmediately). It can be later reset.
     */
    public ByteArrayInputStreamEx () {
        super (DUMMY);
    }

    /**
     * Creates <code>ByteArrayInputStream</code>
     * that uses <code>buf</code> as its
     * buffer array. 
     * The buffer array is not copied. The buffer's mark is
     * set to 0.
     *
     * @param   buf      the input buffer.
     */
    public ByteArrayInputStreamEx (byte [] buf) {
        super (buf);
    }

    /**
     * Creates <code>ByteArrayInputStream</code>
     * that uses <code>buf</code> as its
     * buffer array. The initial value of <code>pos</code>
     * is <code>offset</code> and the initial value
     * of <code>count</code> is the minimum of <code>offset+length</code>
     * and <code>buf.length</code>.
     * The buffer array is not copied. The buffer's mark is
     * set to the specified offset.
     *
     * @param   buf      the input buffer.
     * @param   offset   the offset in the buffer of the first byte to read.
     * @param   length   the maximum number of bytes to read from the buffer.
     */
    public ByteArrayInputStreamEx (byte [] buf, int offset, int length) {  
        super (buf, offset, length);
    }
    
    /**
     * Creates <code>ByteArrayInputStream</code>
     * that reads the incoming {@link ByteArrayList}
     */
    public ByteArrayInputStreamEx (ByteArrayList blist) {
        super (blist.getInternalBuffer (), 0, blist.size ());
    }
    
    /**
     * Creates <code>ByteArrayInputStream</code>
     * that reads the incoming {@link ByteArrayOutputStreamEx}
     */
    public ByteArrayInputStreamEx (ByteArrayOutputStreamEx blist) {
        super (blist.getInternalBuffer (), 0, blist.size ());
    }

    /**
     *  Returns the current position.
     */
    public int          getPosition () {
        return (pos);
    }
    
    /**
     *  Sets the current position.
     */
    public void         setPosition (int p) {
        pos = p;
    }
    
    /**
     *  Returns the underlying buffer
     */
    public byte []      getBuffer () {
        return (buf);
    }
    
    public void         setBuffer (byte [] b) {
        buf = b;
    }
    
    /**
     *  Returns the read limit
     */
    public int          getLimit () {
        return (count);
    }
    
    /**
     *  Sets the read limit
     */
    public void         setLimit (int n) {
        count = n;
    }

    public byte []      toByteArray () {
    	byte []		ret = new byte [count - pos];
    	SafeArrays.safeArrayCopy (buf, pos, ret, 0, count - pos);
		return (ret);
    }

    public void         setBuffer (ByteArrayOutputStreamEx baos) {
        buf = baos.getInternalBuffer ();
        pos = 0;
        count = baos.size ();
    }

}