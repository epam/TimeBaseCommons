package deltix.util.jdbc;

import java.sql.*;

class SQLScriptStatement implements ScriptStatement {
    private String  mSQL;

    public SQLScriptStatement (String sql) {
        mSQL = sql;
    }

    public void execute (ScriptExecutionEnvironment env) 
        throws SQLException, InterruptedException
    {
        String      exeSQL;
        String []   paramValues = env.getParameterValues ();
        
        if (paramValues != null) {
            StringBuffer    sb = new StringBuffer ();
            int             pos = 0;
            int             len = mSQL.length ();
            
            for (;;) {
                int         idx = mSQL.indexOf ("&", pos);
                
                if (idx == -1)
                    break;
                
                int         idx1 = idx + 1;
                
                if (idx1 == len)
                    break;
                
                char        ch = mSQL.charAt (idx1);
                int         pidx = ch - '1';
                
                if (pidx > 0 && pidx < paramValues.length) {
                    sb.append (mSQL, pos, idx);
                    sb.append (paramValues [pidx]);
                    pos = idx + 2;
                }
            }
            
            sb.append (mSQL, pos, len);
            exeSQL = sb.toString ();
        }
        else
            exeSQL = mSQL;
        
        ScriptExecutionLogger   logger = env.getLogger ();
        
        if (logger != null)
            logger.logCommand (exeSQL);

        if (env.getConnection () != null) {
            PreparedStatement   stmt = 
                env.getConnection ().prepareStatement (exeSQL);

            try {
                if (logger != null && exeSQL.toLowerCase ().trim ().startsWith ("select")) {
                    ResultSet       rs = stmt.executeQuery ();
                    StringBuffer    sb = new StringBuffer ();
                    
                    JDBCUtils.formatResultSet (
                        sb,
                        rs,
                        logger.getWidth (),
                        true,
                        "| ",
                        " | ",
                        " |",
                        '-',
                        "...",
                        null
                    );      
                    
                    logger.logResults (sb.toString ());
                }
                else
                    stmt.execute ();
                
                stmt.close ();
            } finally {
                JDBCUtils.close (stmt);
            }
        }
    }
}
