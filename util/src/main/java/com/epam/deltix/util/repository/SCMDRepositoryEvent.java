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
package com.epam.deltix.util.repository;

public enum SCMDRepositoryEvent implements RepositoryEvent {    
    /**
     * An item exists in the repository by the moment of subscription.
     */
    SCANNED,
    /**
     * A new item is created.
     */
    CREATED,
    /**
     * An item is modified.
     */
    MODIFIED,
    /**
     * An item is deleted.
     */
    DELETED;        
    
    @Override
    public boolean isInto(RepositoryEvent... events) {
        if (events != null) {
            for (int i = 0; i < events.length; i++) {
                if (this == events[i]) {
                    return true;
                }
            }
        }
        return false;
    }
}