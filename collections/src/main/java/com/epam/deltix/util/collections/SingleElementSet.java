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
 *  A read-only set consisting of a single fixed element.
 */
public class SingleElementSet <T>
    extends AbstractSet <T> 
    implements Set <T> 
{
    private T           mElement;
    
    public SingleElementSet (T elem) {
        mElement = elem;
    }
    
    public boolean          contains (Object o) {
        return (mElement.equals (o));
    }

    public int              size () {
        return (1);
    }
    
    public boolean          isEmpty () {
        return (false);
    }
    
    @SuppressWarnings ("unchecked")
    public Iterator <T>     iterator () {
        return (new ArrayIterator <T> (mElement));
    }
    
    public boolean          remove (Object o) {
        throw new UnsupportedOperationException ();
    }

    public boolean          addAll (Collection<? extends T> c) {
        throw new UnsupportedOperationException ();
    }

    public boolean          removeAll (Collection<?> c) {
        throw new UnsupportedOperationException ();
    }

    public boolean          retainAll (Collection<?> c) {
        throw new UnsupportedOperationException ();
    }

    public boolean          add (T o) {
        throw new UnsupportedOperationException ();
    }

    public void             clear () {
        throw new UnsupportedOperationException ();
    }
}