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
package com.epam.deltix.util.io;

import java.io.*;
import java.util.regex.*;

/**
 *
 */
public class RegexFilenameFilter implements FilenameFilter {
    private Pattern     mNamePattern;
    private Pattern     mParentPattern;
    
    public RegexFilenameFilter (String namePattern) {
        this (null, namePattern);   
    }
    
    public RegexFilenameFilter (String parentDirPattern, String namePattern) {
        if (parentDirPattern != null)
            mParentPattern = Pattern.compile (parentDirPattern);
        
        if (namePattern != null)
            mNamePattern = Pattern.compile (namePattern);                
    }
    
    public boolean      accept (File dir, String name) {
        return (
            (mNamePattern == null || mNamePattern.matcher (name).matches ()) &&
            (mParentPattern == null || mParentPattern.matcher (dir.getAbsolutePath ()).matches ())
        );
    }
}