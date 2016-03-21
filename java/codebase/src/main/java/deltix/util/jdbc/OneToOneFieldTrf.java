package deltix.util.jdbc;

import java.sql.*;

/**
 *
 */
public abstract class OneToOneFieldTrf extends ZeroOrMoreToOneFieldTrf {
    private final String        mInName;
    private int                 mInIndex = -1;
    
    public OneToOneFieldTrf (String name) {
        super (name);
        mInName = name;        
    }
    
    public OneToOneFieldTrf (String inName, String outName) {
        super (outName);
        mInName = inName;        
    }
    
    public void                 init (
        ColumnMap                   inMap
    )
    {
        mInIndex = inMap.getIdx (mInName);
    }
    
    public String               getInColumnName () {
        return (mInName);
    }
    
    protected Object            getInObject (ResultSet in) 
        throws SQLException 
    {
        return (in.getObject (mInIndex));        
    }
    
    protected abstract Object   transform (Object in);
    
    public final Object         transform (
        ResultSet                   in
    ) 
        throws SQLException 
    {
        return (transform (getInObject (in)));
    }
}
