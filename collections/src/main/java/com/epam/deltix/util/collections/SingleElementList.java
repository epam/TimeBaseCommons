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
package com.epam.deltix.util.collections;

import com.epam.deltix.util.lang.Util;
import java.util.*;

/**
 *
 */
public class SingleElementList <T>
    extends AbstractList <T>
{
    private T           mObject;
    
    public SingleElementList (T object) {
        mObject = object;
    }
    
    public int      lastIndexOf (Object o) {
        return (indexOf (o));             
    }

    public int      indexOf (Object o) {
        return (Util.xequals (mObject, o) ? 0 : -1);  
    }

    public T        get (int index) {
        if (index != 0)
            throw new ArrayIndexOutOfBoundsException (index);
        
        return (mObject);
    }

    public int      size () {
        return (1);
    }    
}