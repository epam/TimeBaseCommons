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

/**
 *  Provides implementation-independent equals/hashCode implementation
 *  for comparing instances of CharSequence interface purely based on their
 *  character content. Therefore, equal CharSequences are guaranteed to be equal.
 *  If an instance of CharSequenceKey is added to a Map, the caller is responsible
 *  for keeping the charSequence object immutable.
 */
public class CharSequenceKey 
    implements CharSequence, Comparable <CharSequence> 
{
    public CharSequence             charSequence;
    
    public CharSequenceKey (CharSequence cs) {
        charSequence = cs;
    }
    
    public CharSequenceKey () {
        charSequence = null;
    }
    
    /**
     *  Converts the nested charSequence to String. This action clones the string content
     *  of a string buffer, preventing it from changing.
     */
    public void                     fix () {        
        charSequence = charSequence.toString ();
    }
    
    public boolean                  equals (Object other) {
        return (
            this == other ||
            other instanceof CharSequenceKey &&
                Util.equals (charSequence, ((CharSequenceKey) other).charSequence) ||                                
            other instanceof CharSequence &&
                Util.equals (charSequence, (CharSequence) other)
        );
    }
    
    public int                      hashCode () {
        return (Util.hashCode (charSequence));
    }
    
    public String                   toString () {
        return (charSequence.toString ());
    }

    public int                      compareTo (CharSequence o) {
        return (Util.compare (charSequence, o, false));
    }

    public char                     charAt (int index) {
        return (charSequence.charAt (index));
    }

    public CharSequence             subSequence (int start, int end) {
        return (charSequence.subSequence (start, end));
    }

    public int                      length () {
        return (charSequence.length ());
    }
}