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
package com.epam.deltix.util.io;

import com.epam.deltix.util.lang.Disposable;

/**
 *  A persistent object used to store structured data of some sort.
 */
public interface AbstractDataStore extends Disposable {
    /**
     *  Create a new object on disk and format internally. The data store is
     *  left open for read-write at the end of this method.
     */
    public void         format ();
    
    /**
     *  Close the store and delete all underlying files from disk.
     */
    public void         delete ();
    
    /**
     *  Determines whether the store is open.
     */
    public boolean      isOpen ();
    
    /**
     *  Determines whether the store is open as read-only.
     */
    public boolean      isReadOnly ();
    
    /**
     *  Open the data store.
     */
    public void         open (boolean readOnly);
    
    /**
     *  Returns a meaningful identification of this object,
     *  such as a file path or url.
     */
    public String       getId ();
}