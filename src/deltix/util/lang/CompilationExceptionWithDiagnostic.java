package deltix.util.lang;

import java.util.*;

import javax.tools.*;

public class CompilationExceptionWithDiagnostic extends RuntimeException {

    private final List<Diagnostic<? extends JavaFileObject>> diagnostics;

    public CompilationExceptionWithDiagnostic (final String message,
                                 final List<Diagnostic<? extends JavaFileObject>> diagnostics) {
        super (message);
        this.diagnostics = diagnostics;
    }

    public List<Diagnostic<? extends JavaFileObject>> getDiagnostics () {
        return diagnostics;
    }

}
