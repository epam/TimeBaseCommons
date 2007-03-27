package deltix.util.jdbc;

import java.sql.*;
import java.util.*;

/**
 *
 */
public class ColumnMap {
    private Map <String, Integer>           mMap = new HashMap <String, Integer> ();
    
    public ColumnMap () {        
    }
    
    public void             put (String columnName, int idx) {
        mMap.put (columnName, idx);        
    }
    
    public void             init (ResultSetMetaData md) throws SQLException {
        for (int col = 1; col <= md.getColumnCount (); col++) 
            put (md.getColumnName (col), col);
    }

    public int              getIdx (String columnName) {
        Integer     ret = mMap.get (columnName);
        
        if (ret == null)
            throw new RuntimeException ("Column " + columnName + " was not found.");
        
        return (ret);
    }  
}
