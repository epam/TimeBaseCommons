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
public class CompilationException extends RuntimeException {
    private static final boolean        FILL_STACK_TRACE = 
        Boolean.getBoolean ("deltix.qql.trace");
    
    public final long                   location;
    public final String                 diag;
        
    public CompilationException (String msg, long location) {
        super (Location.toString (location) + msg);
        
        this.location = location;
        this.diag = msg;
    }

    public CompilationException (String msg, Element [] elems) {
        this (msg, Location.fromTo (elems [0].location, elems [elems.length - 1].location));
    }

    public CompilationException (String msg, Element elem) {
        this (msg, elem.location);
    }

    @Override
    public Throwable            fillInStackTrace () {
        return (FILL_STACK_TRACE ? super.fillInStackTrace () : null);
    }
    
    /**
     *  Returns the 0-based start line of the problem area.
     */
    public final int            getStartLine () {
        return (Location.getStartLine (location));
    }

    /**
     *  Returns the 0-based end line of the problem area.
     */
    public final int            getEndLine () {
        return (Location.getEndLine (location));
    }

    /**
     *  Returns the 0-based start position of the problem area.
     */
    public final int            getStartPosition () {
        return (Location.getStartPosition (location));
    }

    /**
     *  Returns the 0-based end position of the problem area.
     */
    public final int            getEndPosition () {
        return (Location.getEndPosition (location));
    }
}