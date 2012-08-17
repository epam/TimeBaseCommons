package deltix.util.jdbc;

import java.sql.SQLException;

class CommitStatement implements ScriptStatement {
    public CommitStatement () {
    }

    @Override
    public void execute (ScriptExecutionEnvironment env)
        throws SQLException, InterruptedException
    {
        ScriptExecutionLogger   logger = env.getLogger ();
        
        if (logger != null)
            logger.logCommand ("COMMIT");
        
        if (env.getConnection () != null)
            env.getConnection ().commit ();
    }
}
