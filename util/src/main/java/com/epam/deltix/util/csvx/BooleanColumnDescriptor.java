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
package com.epam.deltix.util.csvx;

import java.util.regex.*;

/**
 *
 */
public class BooleanColumnDescriptor extends ColumnDescriptor {
    static final Pattern        DEFAULT_TRUE_PATTERN = 
        Pattern.compile ("y|Y|true|TRUE");
    
    private Pattern             mCompiledTruePattern = DEFAULT_TRUE_PATTERN;
    
    public BooleanColumnDescriptor () { 
    }
    
    public BooleanColumnDescriptor (String header) {
        super (header);
    }    
    
    public void                 setTruePattern (String pattern) {
        mCompiledTruePattern = Pattern.compile (pattern);
    }
    
    public boolean              getBoolean () {
        return (mCompiledTruePattern.matcher (getCharSequence ()).matches ());
    }
}