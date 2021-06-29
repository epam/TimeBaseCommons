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
package com.epam.deltix.util.parsers;

/**
 *
 */
public abstract class Element {
    public static final long    NO_LOCATION = 
        Location.combine (Location.NONE, Location.NONE);
    
    public final long           location;

    protected Element (long location) {
        this.location = location;
    }

    /**
     *  Convert the element to correct QQL. This method is used for
     *  serialization.
     */
    public void                 print (StringBuilder s) {
        throw new UnsupportedOperationException (getClass ().getName ());
    }

    public final int            getStartLine () {
        return (Location.getStartLine (location));
    }

    public final int            getEndLine () {
        return (Location.getEndLine (location));
    }

    public final int            getStartPosition () {
        return (Location.getStartPosition (location));
    }

    public final int            getEndPosition () {
        return (Location.getEndPosition (location));
    }

    /**
     *  Convert the element to correct QQL. This method is used for
     *  serialization. This method calls {@link #print(StringBuilder)}.
     */
    @Override
    public final String         toString () {
        StringBuilder               sb = new StringBuilder ();

        print (sb);

        return (sb.toString ());
    }
    
    public static void          printCommaSeparatedList (StringBuilder out, String ... strings) {
        if (strings == null || strings.length == 0)
            return;
        
        out.append (strings [0]);

        for (int ii = 1; ii < strings.length; ii++) {
            out.append (", ");
            out.append (strings [ii]);
        }        
    }
    
    @Override
    public boolean              equals (Object obj) {
        return this == obj || obj != null && getClass () == obj.getClass ();
    }

    @Override
    public int                  hashCode () {
        return getClass ().hashCode ();
    }
}