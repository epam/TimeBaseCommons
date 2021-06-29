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
 *  Filtering iterator. Do not call remove() after hasNext().
 */
public final class TypeFilterIterator <Generic, Specific> 
    implements Iterator <Specific> 
{    
    private final Iterator <Generic>    delegate;
    
    public TypeFilterIterator (Iterator <Generic> delegate, Class <Specific> cls) {
        this.delegate = new FilterIterator <Generic> (delegate, new InstanceOfFilter (cls));
    }

    public TypeFilterIterator (Iterator <Generic> delegate, InstanceOfFilter filter) {
        this.delegate = new FilterIterator <Generic> (delegate, filter);
    }

    @Override
    public boolean                      hasNext () {
        return (delegate.hasNext ());                        
    }

    @Override @SuppressWarnings ("unchecked")
    public Specific                     next () {       
        return ((Specific) delegate.next ());        
    }

    @Override
    public void                         remove () {
        delegate.remove ();
    }        
}