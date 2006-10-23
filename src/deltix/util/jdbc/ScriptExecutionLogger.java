package deltix.util.jdbc;

/**
 *
 */
public interface ScriptExecutionLogger {
    public void         logCommand (String cmd);
    
    public void         logResults (String text);
    
    /**
     *  Width in characters
     */
    public int          getWidth ();
}
