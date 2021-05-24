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
 *  Chains several iterators into one.
 */
public class ChainedIterator <T> implements Iterator <T> {
    private final Iterator <Iterator <? extends T>>     metaIterator;
    private boolean                                     firstTime = true;
    private Iterator <? extends T>                      nextIter = null;
    private Iterator <? extends T>                      lastIter = null;

    public ChainedIterator (Iterator <Iterator <? extends T>> metaIterator) {
        this.metaIterator = metaIterator;
    }

    @SafeVarargs
    public ChainedIterator (Iterator <? extends T> ... chain) {
        this (new ArrayIterator <> (chain));
    }
    
    @SafeVarargs @SuppressWarnings ("unchecked")
    public ChainedIterator (Iterable <? extends T> ... chain) {
        this (new IterableToIteratorTransformer (new ArrayIterator <> (chain)));
    }
    
    @Override
    public boolean                              hasNext () {
        if (firstTime) {
            firstTime = false;
            
            do {
                if (!metaIterator.hasNext ()) {
                    nextIter = null;
                    break;
                }
                                    
                nextIter = metaIterator.next ();                                
            } while (!nextIter.hasNext ());
        }
        
        return (nextIter != null);
    }

    @Override
    public T                                    next () {
        lastIter = nextIter;
        
        T                       ret = nextIter.next ();
        
        while (!nextIter.hasNext ()) {
            if (!metaIterator.hasNext ()) {
                nextIter = null;
                break;
            }
            
            nextIter = metaIterator.next ();            
        }
        
        return (ret);
    }

    @Override
    public void                                 remove () {
        if (lastIter == null)
            throw new IllegalStateException ("next () was not called");
        
        lastIter.remove ();
    }        
}