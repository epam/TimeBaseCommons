package deltix.util.jdbc;

import java.io.IOException;
import java.sql.SQLException;

import deltix.util.Util;

class JavaCallStatement implements ScriptStatement {
    private String      mClassName;

    public JavaCallStatement (String className) throws IOException
    {
        mClassName = className;
    }

    public void execute (ScriptExecutionEnvironment env)
        throws SQLException, IOException, InterruptedException
    {
        env.getLogger ().logCommand ("CALL new " + mClassName + " ().run (env)");
        
        DatabaseOperation   op;
        
        try {
            op = (DatabaseOperation) Util.newInstance (mClassName);           
        } catch (ClassCastException x) {
            throw new SQLException ("Class '" + mClassName + " is not an instance of DatabaseOperation");
        } catch (Exception x) {
            throw new SQLException ("Cannot instantiate class '" + mClassName + "': " + x);
        }
        
        op.run (env);
    }
}
