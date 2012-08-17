package deltix.util.jdbc;

import java.sql.*;

class SQLScriptStatement implements ScriptStatement {
    private String  mSQL;

    public SQLScriptStatement (String sql) {
        mSQL = sql;
    }

    @Override
    public void execute (ScriptExecutionEnvironment env) 
        throws SQLException, InterruptedException
    {
        String                  exeSQL = env.substituteParameters (mSQL);
        ScriptExecutionLogger   logger = env.getLogger ();
        
        if (logger != null)
            logger.logCommand (exeSQL);

        if (env.getConnection () != null) {
            Statement   stmt = env.getConnection ().createStatement ();

            try {
                if (logger != null && exeSQL.toLowerCase ().trim ().startsWith ("select")) {
                    ResultSet       rs = stmt.executeQuery (exeSQL);
                    StringBuffer    sb = new StringBuffer ();
                    
                    JDBCUtils.formatResultSet (
                        sb,
                        rs,
                        logger.getWidth (),
                        true,
                        "",
                        " | ",
                        "",
                        '-',
                        "...",
                        null
                    );      
                    
                    logger.logResults (sb.toString ());
                }
                else {
                    int     nmod = stmt.executeUpdate (exeSQL);
                    
                    if (logger != null)
                        logger.logResults (nmod + " rows changed.");
                }
                
                stmt.close ();
            } finally {
                JDBCUtils.close (stmt);
            }
        }
    }
}
