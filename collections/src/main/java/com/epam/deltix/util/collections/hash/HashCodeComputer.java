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

import java.io.Serializable;
import java.util.*;

public abstract class HashCodeComputer implements Serializable {
    protected HashCodeComputer () { }
    
    public int              modHashCode (Object key, int mod) {
        return (modHashCode (Objects.hashCode (key), mod));
    }

    public int              modHashCode (float key, int mod) {
        return (modHashCode (Float.floatToIntBits (key), mod));
    }

    public int              modHashCode (double key, int mod) {
        return (modHashCode (Double.doubleToLongBits (key), mod));
    }

    public int              modHashCode (long key, int mod) {
        return (modHashCode ((int) (key ^ (key >>> 32)), mod));
    }

    public int              modHashCode (int key, int mod) {
        return (computeModHashCode (key, mod));
    }
    
    public static int       computeModHashCode (int key, int mod) {
        if (key == Integer.MIN_VALUE)
            return (1);
        
        if (key < 0)
            key = -key;
        
        return (key % mod);        
    }
}