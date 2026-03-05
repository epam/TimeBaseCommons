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

import com.epam.deltix.util.lang.Disposable;

/**
 *  An abstract cursor for iterating over arbitrary sequences of data.
 *  Usage:
 *<pre>try {
 *     while (cursor.next ()) {        
 *         ... cursor.get??? () ...
 *    }
 *} finally {
 *    cursor.close ();
 *}
 * </pre>
 *  All implementations of this interface are designed to be used from a single
 *  thread and must be externally protected against concurrent calls.
 */
public interface AbstractCursor extends Disposable {
    /**
     *  Moves on to the next data element. This method blocks until 
     *  the next element becomes available, or until the cursor is
     *  determined to be at the end of the sequence. this method is illegal to
     *  call if <code>isAtEnd ()</code> returns <code>true</code>.
     *  
     *  @return     <code>false</code> if at the end of the cursor.
     */
    public boolean                  next ();
    
    /**
     *  Returns <code>true</code> if the last call to <code>next ()</code> returned <code>false</code>.
     *  Returns <code>false</code> if <code>next ()</code> has not been called yet.
     *  This method is legal to call any number of times at any
     *  point in the cursor's lifecycle.
     */
    public boolean                  isAtEnd ();
}