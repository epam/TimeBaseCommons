package deltix.util.jdbc;

import java.io.IOException;
import java.sql.SQLException;


interface ScriptStatement {
    public void execute (ScriptExecutionEnvironment env) 
        throws SQLException, InterruptedException;
}