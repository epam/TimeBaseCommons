package deltix.util.lang;

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
        s.println (code);
    }

    @Override
    public void                     printStackTrace (PrintWriter s) {
        super.printStackTrace (s);
        s.println ();
        s.println (code);
    }

    
    public List <Diagnostic <? extends JavaFileObject>>     getDiagnostics () {
        return diagnostics;
    }
}
