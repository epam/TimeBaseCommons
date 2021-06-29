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

import com.epam.deltix.util.lang.InstanceOfFilter;
import java.util.Iterator;

/**
 *  Iterable that filters by type
 */
public final class TypeFilterIterable <Generic, Specific> 
    implements Iterable <Specific> 
{
    private Iterable <Generic>      delegate;
    private InstanceOfFilter        filter;

    public TypeFilterIterable (Iterable <Generic> delegate, Class <Specific> cls) {
        this.delegate = delegate;
        this.filter = new InstanceOfFilter (cls);
    }
        
    @Override
    public Iterator <Specific>      iterator () {  
        return (new TypeFilterIterator <Generic, Specific> (delegate.iterator (), filter));
    }        
}