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
package com.epam.deltix.util.collections;

import com.epam.deltix.util.lang.Util;

/**
 *
 */
public class ByteArrayToCharSequence implements CharSequence {
    public byte []         bytes;
    public int             start;
    public int             end;

    public ByteArrayToCharSequence () {
        bytes = null;
        start = -1;
        end = -1;
    }
    
    public ByteArrayToCharSequence (byte [] bytes) {
        set (bytes, bytes.length);        
    }

    public ByteArrayToCharSequence (byte [] bytes, int start, int end) {
        set (bytes, start, end);
    }

    public void                 set (byte [] bytes, int length) {
        this.bytes = bytes;
        this.start = 0;
        this.end = length;
    }

    public void                 set (byte [] bytes, int start, int end) {
        this.bytes = bytes;
        this.start = start;
        this.end = end;
    }

    public ByteArrayToCharSequence setContent (byte [] bytes, int length) {
        if (this.bytes == null || this.bytes.length < length) {
            this.bytes = new byte[length];
        }
        System.arraycopy(bytes, 0, this.bytes, 0, length);
        this.start = 0;
        this.end = length;

        return this;
    }

    public ByteArrayToCharSequence setContent (byte [] bytes, int srcPos, int length) {
        if (this.bytes == null || this.bytes.length < length) {
            this.bytes = new byte[length];
        }
        System.arraycopy(bytes, srcPos, this.bytes, 0, length);
        this.start = 0;
        this.end = length;

        return this;
    }

    public final char             charAt (int index) {
        return ((char) bytes [start + index]);
    }

    public final CharSequence     subSequence (int inStart, int inEnd) {
        return (new ByteArrayToCharSequence (bytes, start + inStart, start + inEnd));
    }

    public final int              length () {
        return (end - start);
    }

    @Override
    public final String           toString () {
        return (new String (bytes, start, end - start));
    }

    public final CharSequence     trimWhitespace () {
        while (start < end && Character.isWhitespace (bytes [start]))
            start++;

        while (start < end) {
            final int       prev = end - 1;

            if (!Character.isWhitespace (bytes [prev]))
                break;

            end = prev;
        }
        
        return this;
    }

    public final CharSequence     trimWhitespaceC () {
        while (start < end && (Character.isWhitespace (bytes [start]) || bytes [start] == 0))
            start++;

        while (start < end) {
            final int       prev = end - 1;

            if (!(Character.isWhitespace (bytes [prev]) || bytes [prev] == 0))
                break;

            end = prev;
        }
        
        return this;
    }
    
    @Override
    public boolean                  equals (Object other) {
        return (
            this == other ||
            other instanceof CharSequence &&
                Util.equals (this, (CharSequence) other)
        );
    }

    @Override
    public int                      hashCode () {
        return (Util.hashCode (this));
    }
}