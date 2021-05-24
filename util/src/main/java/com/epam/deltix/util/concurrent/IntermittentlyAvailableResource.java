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
 * A resource that changes its state between available and unavailable.
 * Usage (assuming "resource" implements IntermittentlyAvailableResource):
 *<pre>
 *resource.setAvailabilityListener (
 *    new Runnable () {
 *        public void             run () {
 *            synchronized (myLock) {
 *                myLock.notify ();
 *            }
 *        }
 *    }
 *);
 *...
 *synchronized (myLock) {
 *    try {
 *        resource.criticalOperation ();
 *    } catch (UnavailableResourceException x) {
 *        // do something like myLock.wait ()...
 *    }
 *}
 *</pre>
 * 
 * Note that it is absolutely critical to synchronize the <tt>maybeAvailable</tt> runnable
 * callback on the same monitor as the critical operation call. This ensures
 * that, while UnavailableResourceException is being handled and the resource is
 * removed from the available pool, an opposite call to maybeAvailable cannot be
 * made.
 */
public interface IntermittentlyAvailableResource {    
    /**
     *  Installs the (only) availability listener.
     *
     *  @param maybeAvailable  The listener to be notified when the
     *              resource may have become available after the critical operation
     *              threw an UnavailableResourceException.
     */
    public void         setAvailabilityListener (Runnable maybeAvailable);
}