package deltix.util.jdbc;

import java.sql.*;
import java.io.*;

/**
 *  Some action on an open connection.
 */
public interface DatabaseOperation {
    /**
     *  Performs the action. Must not close Connection.
     *
     *  @param env     The execution environment.
     */
    public void     run (ScriptExecutionEnvironment env) 
        throws SQLException, InterruptedException, IOException;
}
