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

import java.lang.reflect.*;
import java.util.*;

/**
 *  Reflection-based HashMapSet, accessing a key field directly.
 */
public class FieldKeyedHashMapSet <K, V> extends HashMapSet <K, V> {
    private final Field                 field;
    
    public FieldKeyedHashMapSet (Field field, boolean mustSync, Collection <V> copy) {
        super (copy);
        this.field = field;
        field.setAccessible (true);
    }

    public FieldKeyedHashMapSet (Field field, boolean mustSync, int capacity) {
        super (capacity);
        this.field = field;
        field.setAccessible (true);
    }

    public FieldKeyedHashMapSet (Field field, boolean mustSync) {
        this.field = field;
        field.setAccessible (true);
    }
       
    @Override @SuppressWarnings ("unchecked")
    public K                            getKey (V value) {
        try {
            return ((K) field.get (value));
        } catch (IllegalAccessException ex) {
            throw new RuntimeException (ex);
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException (ex);
        }        
    }    
}