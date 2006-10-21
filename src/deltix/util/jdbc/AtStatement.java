package deltix.util.jdbc;

import java.io.IOException;
import java.sql.SQLException;

import deltix.util.io.FilenameResolver;

class AtStatement implements ScriptStatement {
    private Script mScript;

    public AtStatement (String relPath, FilenameResolver scriptFinder) throws IOException {
        mScript = new Script ();
        mScript.setScriptFinder (scriptFinder);
        mScript.read (relPath);
    }

    public void execute (ScriptExecutionEnvironment env)
        throws SQLException, InterruptedException, IOException
    {
        mScript.setConnection (env.getConnection ());
        mScript.setLogger (env.getLogger ());
        mScript.setParameterValues (env.getParameterValues ());
        mScript.execute ();
    }
}