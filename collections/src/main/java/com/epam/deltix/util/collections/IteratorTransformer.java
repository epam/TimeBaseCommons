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

import java.util.Iterator;

/**
 *  Adapts an iterator over one data type to an iterator over another data type,
 *  by applying user-defined transformation.
 */
public abstract class IteratorTransformer <X, Y> implements Iterator <Y> {
    private final Iterator <X>          delegate;

    public IteratorTransformer (Iterator <X> delegate) {
        this.delegate = delegate;
    }

    protected abstract Y            transform (X arg);
    
    @Override
    public final boolean            hasNext () {
        return (delegate.hasNext ());
    }

    @Override
    public final Y                  next () {
        return (transform (delegate.next ()));
    }

    @Override
    public final void               remove () {
        delegate.remove ();
    }        
}