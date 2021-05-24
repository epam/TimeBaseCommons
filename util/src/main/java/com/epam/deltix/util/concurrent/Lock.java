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

import java.util.*;

import com.epam.deltix.util.lang.Disposable;

/**
 *
 */
public final class Lock extends Throwable implements Disposable {
    public interface Manager {
        public Object           getSyncObject ();
        
        public void             release (Lock lock);
    }
    
    private Manager         mParent;
    private Thread              mThread = Thread.currentThread ();        
    private long                mCreationTime = System.currentTimeMillis ();
    private boolean             mIsShared;
    private boolean             mIsClosed = false;

    public Lock (Manager parent, boolean isShared) {
        mParent = parent;
        mIsShared = isShared;
    }

    public void                 close () {
        synchronized (mParent.getSyncObject ()) {
            if (mIsClosed) 
                throw new IllegalStateException (this + " is already closed");

            mParent.release (this);
            mIsClosed = true;
        }
    }        

    public boolean              isShared () {
        return (mIsShared);
    }

    public Thread               getCreatorThread () {
        return (mThread);
    }

    public long                 getCreationTime () {
        return (mCreationTime);
    }

    public Thread               getCreator () {
        return (mThread);
    }

    @Override
    public String               toString () {
        return (
            (mIsShared ? "Shared" : "Exclusive") +
            " lock obtained by " + mThread.getName () + " at " + 
            new Date (mCreationTime)
        );
    }
}