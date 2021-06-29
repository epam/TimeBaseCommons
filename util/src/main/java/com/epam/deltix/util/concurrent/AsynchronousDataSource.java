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

/**
 *  An abstract source of data, allowing a single consumer to
 *  perform work while the data is not available, or to multiplex
 *  multiple data sources.
 */
public interface AsynchronousDataSource {
    public void         addAvailabilityListener (Runnable listener);
    
    public void         removeAvailabilityListener (Runnable listener);
    
    /**
     *  Performs a non-blocking check for data availability.
     *
     *  @param timeout      Maximum time to wait until data is available, 
     *                      in milliseconds. The value of 0 causes a non-blocking
     *                      check to be performed.
     *  @return             True if data is available, false if timeout has expired.
     */
    public boolean      isDataAvailable (long timeout)
        throws InterruptedException;
    
    /**
     *  Performs a non-blocking check for data availability.
     *
     *  @return             If data is available.
     */
    public boolean      isDataAvailable ();    
}