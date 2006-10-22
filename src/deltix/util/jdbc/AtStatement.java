package deltix.util.jdbc;

import java.io.IOException;
import java.sql.SQLException;

import deltix.util.io.FilenameResolver;

/**
 *  Invoke another script
 */
class AtStatement implements ScriptStatement {
    private Script mScript;

    public AtStatement (Script script) throws IOException {
        mScript = script;
    }

    public void execute (ScriptExecutionEnvironment env)
        throws SQLException, InterruptedException, IOException
    {
        mScript.execute ();
    }
}
