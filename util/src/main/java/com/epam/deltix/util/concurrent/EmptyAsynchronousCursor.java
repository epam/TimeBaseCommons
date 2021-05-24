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

import com.epam.deltix.util.memory.*;

/**
 *  An AsynchronousCursor that returns no frames.
 */
public class EmptyAsynchronousCursor 
    implements AsynchronousCursor
{
    private DisposableResourceTracker   mTracker;
    private boolean                     mNextWasCalled = false;
    
    public EmptyAsynchronousCursor () {
        mTracker = new DisposableResourceTracker (this);
    }
    
    public void close () {
        if (mTracker != null) {
            mTracker.close ();
            mTracker = null;
        }
    }
    
    public void             removeAvailabilityListener (Runnable listener) {
    }

    public void             addAvailabilityListener (Runnable listener) {
    }

    public boolean          isDataAvailable (long timeout) throws InterruptedException {
        return (true);
    }

    public boolean          isDataAvailable () {
        return (true);
    }
    
    public boolean          next () {
        if (isAtEnd ())
            throw new IllegalStateException ("Cursor is at end");
        
        mNextWasCalled = true;
        return (false);
    }
    
    public boolean          isAtBeginning () {
        return (!mNextWasCalled);
    }

    public boolean          isAtEnd () {
        return (mNextWasCalled);
    }

    
}