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

import java.util.*;

/**
 *  Adapts an array of one type to the List interface of another type.
 */
public abstract class ListFromArrayEx <E, AE> extends AbstractList <E> {
    private final AE []         array;
    private final int           offset;
    private final int           length;

    public ListFromArrayEx (AE [] array, int offset, int length) {
        this.array = array;
        this.offset = offset;
        this.length = length;
    }
        
    public ListFromArrayEx (AE [] array) {
        this (array, 0, array.length);
    }

    protected abstract E    convert (AE value);
    
    @Override
    public E                get (int index) {
        return (convert (array [offset + index]));
    }

    @Override
    public int              size () {
        return (length);
    }        
}