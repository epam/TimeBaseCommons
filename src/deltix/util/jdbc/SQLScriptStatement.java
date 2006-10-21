package deltix.util.jdbc;

import deltix.util.lang.StringUtils;
import java.sql.SQLException;


class SQLScriptStatement implements ScriptStatement {
    private String  mSQL;

    public SQLScriptStatement (String sql) {
        mSQL = sql;
    }

    public void execute (ScriptExecutionEnvironment env) 
        throws SQLException, InterruptedException
    {
        if (Thread.interrupted ())
            throw new InterruptedException ();

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
        
        if (env.getLogger () != null)
            env.getLogger ().println (exeSQL);

        try {
            env.getStockStatement ().execute (exeSQL);
        }  catch (SQLException ex) {
            throw ex;
        }
    }
}
