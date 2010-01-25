package deltix.qsrv.hf.tickdb.lang.errors;

/**
 *
 */
public class SyntaxErrorException extends CompilationException {
    public SyntaxErrorException (long location) {
        super ("Syntax error", location);
    }
}
