package deltix.util.jdbc;

import java.sql.SQLException;

/**
 *  Unchecked exception, used to wrap the checked java.sql.SQLException 
 *  occurring because of system problems. 
 */
public class SystemSQLException extends RuntimeException {
    public SystemSQLException (String msg, SQLException sx) {
        super (msg, sx);
    }
    
    public SystemSQLException (SQLException sx) {
        super ("System IO error: " + sx, sx);
    }
    
    public SystemSQLException (String msg) {
        super (msg);
    }
}
