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
package com.epam.deltix.util.collections.hash;

public class BXHashCodeComputer extends HashCodeComputer {
    public static final HashCodeComputer     INSTANCE = new BXHashCodeComputer ();

    private static final long serialVersionUID = 1L;

    protected BXHashCodeComputer () { }
    
    @Override
    public int              modHashCode (int key, int mod) {
        if ((mod >>> 16) == 0) {
            key = key ^ (key >> 16);
        
            if ((mod >>> 24) == 0)
                key = key ^ (key >> 8);
        }
        
        return (super.modHashCode (key, mod));        
    }

    private Object readResolve() {
        return INSTANCE;
    }
}