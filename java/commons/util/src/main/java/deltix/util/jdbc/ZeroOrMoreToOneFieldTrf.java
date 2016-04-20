package deltix.util.jdbc;

import java.sql.*;

/**
 *
 */
public abstract class ZeroOrMoreToOneFieldTrf extends FieldTrf {
    private final String        mOutName;
    
    public ZeroOrMoreToOneFieldTrf (String outName) {
        mOutName = outName;
    }
    
    public int                  getNumOutColumns () {
        return (1);
    }

    public String               getOutColumnName (int idx) {
        return (mOutName);
    }
    
    protected abstract Object   transform (ResultSet in)
        throws SQLException;
    
    public final void           transform (
        ResultSet                   in, 
        Object []                   out,
        int                         offset
    ) 
        throws SQLException 
    {
        out [offset] = transform (in);
    }
}
