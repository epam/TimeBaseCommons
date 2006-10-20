package deltix.util.jdbc;

import deltix.custom.statestreet.fxa.utils.*;
import java.sql.*;
import java.util.logging.*;

/**
 *
 */
public class JDBCUtils {
    /**
     *  Converts NaN to NULL
     */
    public static void              setDouble (PreparedStatement ps, int idx, double v) 
        throws SQLException
    {
        if (Double.isNaN (v))
            ps.setNull (idx, Types.FLOAT);
        else
            ps.setDouble (idx, v);
    }
    
    /**
     *  Converts NaN to NULL
     */
    public static void              updateDouble (ResultSet rs, int idx, double v) 
        throws SQLException
    {
        if (Double.isNaN (v))
            rs.updateNull (idx);
        else
            rs.updateDouble (idx, v);
    }
    
    public static int               queryInt (PreparedStatement ps) throws SQLException {
        ResultSet               rs = ps.executeQuery ();
        
        try {
            if (!rs.next ())
                throw new SQLException ("No rows returned");
            
            int                 ret = rs.getInt (1);
            
            if (rs.next ())
                throw new SQLException ("Multiple rows returned");
            
            return (ret);
        } finally {
            close (rs);
        }
    }
    
    public static void              rollbackNoExceptions (Connection conn) {
        if (conn != null)
            try {
                conn.rollback ();
            } catch (Throwable x) {
                Common.LOGGER.log (Level.SEVERE, "Error while rolling back a transaction", x);
            }
    }
    
    public static void              close (Connection conn) {
        if (conn != null)
            try {
                conn.close ();
            } catch (Throwable x) {
                Common.LOGGER.log (Level.SEVERE, "Error while closing a connection", x);
            }
    }
    
    public static void              close (Statement stmt) {
        if (stmt != null)
            try {
                stmt.close ();
            } catch (Throwable x) {
                Common.LOGGER.log (Level.SEVERE, "Error while closing a statement", x);
            }
    }
    
    public static void              close (ResultSet rs) {
        if (rs != null)
            try {
                rs.close ();
            } catch (Throwable x) {
                Common.LOGGER.log (Level.SEVERE, "Error while closing a result set", x);
            }
    }
    
    public static void              truncateTable (Connection conn, String tname) 
        throws SQLException
    {
        exec (conn, "TRUNCATE TABLE \"" + tname + "\"");
    }
    
    public static void              deleteTable (Connection conn, String tname) 
        throws SQLException
    {
        exec (conn, "DELETE FROM \"" + tname + "\"");
    }
    
    public static void              prepare (Connection conn, String tname, TableOp op)
        throws SQLException
    {
        switch (op) {
            case APPEND:        break;
            case DELETE:        deleteTable (conn, tname);      break;
            case TRUNCATE:      truncateTable (conn, tname);    break;
        }  
    }
    
    public static void             exec (Connection conn, String sql) 
        throws SQLException
    {
        Statement           stmt = conn.createStatement ();

        try {
            stmt.execute (sql);        
            stmt.close ();
            stmt = null;
        } finally {
            close (stmt);
        }
    }

}
