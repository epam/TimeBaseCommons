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
package com.epam.deltix.util.lang;

import com.epam.deltix.util.io.IOUtil;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;

import javax.tools.*;

public class CompilationExceptionWithDiagnostic extends RuntimeException {
    public final List <Diagnostic <? extends JavaFileObject>>   diagnostics;
    public final String                                         code;
    
    public CompilationExceptionWithDiagnostic (
        final String                                        message,
        final String                                        code,
        final List <Diagnostic <? extends JavaFileObject>>  diagnostics
    )
    {
        super (message);
        this.code = code;
        this.diagnostics = diagnostics;
    }

    @Override
    public void                     printStackTrace (PrintStream s) {
        super.printStackTrace (s);
        s.println ();
        IOUtil.dumpWithLineNumbers (code, s);
    }

    @Override
    public void                     printStackTrace (PrintWriter s) {
        super.printStackTrace (s);
        s.println ();
        IOUtil.dumpWithLineNumbers (code, s);
    }

    
    public List <Diagnostic <? extends JavaFileObject>>     getDiagnostics () {
        return diagnostics;
    }
}