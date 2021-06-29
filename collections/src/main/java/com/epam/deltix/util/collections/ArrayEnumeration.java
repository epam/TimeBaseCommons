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

import java.util.Enumeration;
import java.util.NoSuchElementException;

public class ArrayEnumeration <T> implements Enumeration <T> {
    private Object[] mArray;
    private int mIdx;
    private int mEnd;
    private boolean mHasNext;

    public ArrayEnumeration (T [] array, int offset, int length) {
        mArray = array;
        mIdx = offset;
        mEnd = offset + length;
        mHasNext = mIdx < mEnd;
    }

    @SafeVarargs
    public ArrayEnumeration (T ... array) {
        this (array, 0, array == null ? 0 : array.length);
    }

    public boolean hasMoreElements() {
        return mHasNext;
    }

    @SuppressWarnings ("unchecked")
    public T nextElement() {
        if (mHasNext) {
            Object o = mArray[mIdx];
            mIdx++;
            mHasNext = (mIdx < mEnd);
            return (T) o;
        } 
        else
            throw new NoSuchElementException("No Elements left in Enumeration");
    }
}