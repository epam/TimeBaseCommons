package deltix.util.jdbc;

import java.sql.SQLException;

class CommitStatement implements ScriptStatement {
    public CommitStatement () {
    }

    public void execute (ScriptExecutionEnvironment env)
        throws SQLException, InterruptedException
    {
        env.getLogger ().println ("COMMIT");
        env.getConnection ().commit ();
    }
}
