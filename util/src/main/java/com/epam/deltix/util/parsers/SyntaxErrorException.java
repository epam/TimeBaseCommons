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
public class SyntaxErrorException extends CompilationException {
    public SyntaxErrorException (long location) {
        this ("Syntax error", location);
    }

    public SyntaxErrorException(String msg) {
        this(msg, Element.NO_LOCATION);
    }

    public SyntaxErrorException (String msg, long location) {
        super (msg, location);
    }
}