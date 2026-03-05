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
package com.epam.deltix.util.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *  A synchronized counter, which notifies subclasses when the value changes.
 */
public abstract class NotifyingAtomicInteger {
    private int             mValue;
    
    public NotifyingAtomicInteger (int initialValue) {
        mValue = initialValue;
    }
    
    /**
     *  Start as 0
     */
    public NotifyingAtomicInteger () {
        this (0);
    }
    
    public final int                    addAndGet (int n) {
        int             oldValue, newValue;
    
        synchronized (this) {
            oldValue = mValue;
            newValue = mValue = (mValue + n);
            syncValueChanged (oldValue, newValue);
        }
        
        valueChanged (oldValue, newValue);
        
        return (newValue);
    }
    
    public final synchronized int       getAndAdd (int n) {
        int             oldValue, newValue;
    
        synchronized (this) {
            oldValue = mValue;
            newValue = mValue = (mValue + n);
            syncValueChanged (oldValue, newValue);
        }
        
        valueChanged (oldValue, newValue);
        
        return (oldValue);
    }
    
    /**
     *  Same as addAndGet (1)
     */
    public final int                    incrementAndGet () {
        return (addAndGet (1));
    }
    
    /**
     *  Same as addAndGet (-1)
     */
    public final int                    decrementAndGet () {
        return (addAndGet (-1));
    }
    
    /**
     *  Same as getAndAdd (1)
     */
    public final int                    getAndIncrement () {
        return (getAndAdd (1));
    }
    
    /**
     *  Same as getAndAdd (-1)
     */
    public final int                    getAndDecrement () {
        return (getAndAdd (-1));
    }
    
    public final synchronized int       get () {
        return (mValue);
    }
    
    public void                         valueChanged (int oldValue, int newValue) {        
    }
    
    public void                         syncValueChanged (int oldValue, int newValue) {        
    }
}