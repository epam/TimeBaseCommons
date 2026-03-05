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

import com.epam.deltix.util.lang.Filter;
import java.util.Iterator;

/**
 *  Filtering Iterable
 */
public final class FilterIterable <T> implements Iterable <T> {
    private Iterable <T>            delegate;
    private Filter <? super T>      filter;

    public FilterIterable (Iterable <T> delegate, Filter <? super T> filter) {
        this.delegate = delegate;
        this.filter = filter;
    }
        
    @Override
    public Iterator <T>             iterator () {  
        Iterator <T>        di = delegate.iterator ();
        
        return (filter == null ? di : new FilterIterator <T> (di, filter));
    }        
}