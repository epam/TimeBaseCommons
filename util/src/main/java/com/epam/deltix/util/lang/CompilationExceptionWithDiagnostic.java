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
