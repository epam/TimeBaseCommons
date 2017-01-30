package deltix.util.jdbc;

import java.sql.*;

/**
 *
 */
public abstract class FieldTrf {
    public abstract int         getNumOutColumns ();
    
    public abstract String      getOutColumnName (int idx);
    
    public String               getOutExpression (int idx) {
        return ("?");
    }
    
    public abstract void        init (
        ColumnMap                   inMap
    );
    
    public abstract void        transform (
        ResultSet                   in, 
        Object []                   out,
        int                         offset
    )
        throws SQLException;
}
