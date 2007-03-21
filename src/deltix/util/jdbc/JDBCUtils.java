package deltix.util.jdbc;

import java.sql.*;
import java.util.logging.*;
import java.util.*;
import java.io.*;

import deltix.util.*;

/**
 *
 */
public class JDBCUtils {
    static ResourceBundle           RB = 
        ResourceBundle.getBundle ("deltix.util.jdbc.ui");
    
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
    
    /**
     *  Converts null to SQL NULL
     */
    public static void              setString (PreparedStatement ps, int idx, String v) 
        throws SQLException
    {
        if (v == null)
            ps.setNull (idx, Types.VARCHAR);
        else
            ps.setString (idx, v);
    }
    
    public static int               queryInt (PreparedStatement ps) throws SQLException {
        ResultSet               rs = ps.executeQuery ();
        
        try {
            if (!rs.next ())
                throw new NoRowsReturnedException ();
                        
            int                 ret = rs.getInt (1);
            
            if (rs.wasNull ())
                throw new NullValueException ();
            
            if (rs.next ())
                throw new MultipleRowsReturnedException ();
            
            return (ret);
        } finally {
            close (rs);
        }
    }
    
    public static PreparedStatement prepareStatement (Connection conn, String query, Object ... params)
        throws SQLException
    {
        PreparedStatement       ps = conn.prepareStatement (query);
        boolean                 ok = false;
        
        try {           
            for (int ii = 0; ii < params.length; ii++)
                ps.setObject (ii + 1, params [ii]);
        
            ok = true;
            return (ps);
        } finally {
            if (!ok)
                close (ps);
        }
    }
    
    public static int               queryInt (Connection conn, String query, Object ... params)
        throws SQLException
    {
        PreparedStatement       ps = prepareStatement (conn, query, params);

        try {           
            return (queryInt (ps));
        } finally {
            close (ps);
        }
    }
    
    public static String            queryString (PreparedStatement ps) throws SQLException {
        ResultSet               rs = ps.executeQuery ();
        
        try {
            if (!rs.next ())
                throw new NoRowsReturnedException ();
            
            String              ret = rs.getString (1);
            
            if (rs.next ())
                throw new MultipleRowsReturnedException ();
            
            return (ret);
        } finally {
            close (rs);
        }
    }
    
    public static String            queryString (Connection conn, String query, Object ... params)
        throws SQLException
    {
        PreparedStatement       ps = prepareStatement (conn, query, params);

        try {           
            return (queryString (ps));
        } finally {
            close (ps);
        }
    }
        
    public static List <String>     queryStrings (PreparedStatement ps)
        throws SQLException
    {
        ResultSet               rs = ps.executeQuery ();
        ArrayList <String>      ret = new ArrayList <String> ();
        try {           
            while (rs.next ())
                ret.add (rs.getString (1));

            return (ret);
        } finally {
            close (rs);
        }
    }
    
    public static List <String>     queryStrings (Connection conn, String query, Object ... params)
        throws SQLException
    {
        PreparedStatement       ps = prepareStatement (conn, query, params);

        try {           
            return (queryStrings (ps));
        } finally {
            close (ps);
        }
    }

    public static void              rollbackNoExceptions (Connection conn) {
        if (conn != null)
            try {
                conn.rollback ();
            } catch (Throwable x) {
                Util.LOGGER.log (Level.SEVERE, "Error while rolling back a transaction", x);
            }
    }
    
    public static void              close (Connection conn) {
        if (conn != null)
            try {
                conn.close ();
            } catch (Throwable x) {
                Util.LOGGER.log (Level.SEVERE, "Error while closing a connection", x);
            }
    }
    
    public static void              close (Statement stmt) {
        if (stmt != null)
            try {
                stmt.close ();
            } catch (Throwable x) {
                Util.LOGGER.log (Level.SEVERE, "Error while closing a statement", x);
            }
    }
    
    public static void              close (ResultSet rs) {
        if (rs != null)
            try {
                rs.close ();
            } catch (Throwable x) {
                Util.LOGGER.log (Level.SEVERE, "Error while closing a result set", x);
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
    /*
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
    */
    public static void             exec (Connection conn, String sql, Object ... params) 
        throws SQLException
    {
        PreparedStatement           ps = prepareStatement (conn, sql, params);

        try {
            ps.execute ();        
            ps.close ();
            ps = null;
        } finally {
            close (ps);
        }
    }
    
    public static void				exec (Connection conn, List <String> sqlList)
        throws SQLException
    {
        Statement	stmt = conn.createStatement ();

        try {
            for (String sql : sqlList)
                stmt.execute (sql);
            
            stmt.close ();
            stmt = null;
        } finally {
            close (stmt);
        }
    }
    
    public static void              formatResultSet (
        StringBuffer                    out,
        ResultSet                       rs,
        int                             maxLineWidth, 
        boolean                         printHeaders,
        String                          left,
        String                          columnSeparator,
        String                          right,
        char                            headerUnderscore,
        String                          clip,
        Justification []                columnJustification
    ) 
        throws SQLException
    {
        if (left == null)
            left = "";
        
        if (right == null)
            right = "";
        
        if (columnSeparator == null)
            columnSeparator = "|";
        
        ResultSetMetaData   rsmd = rs.getMetaData ();
        int                 numColumns = rsmd.getColumnCount ();
        List <Object []>    rows = new ArrayList <Object []> ();
        String []           headers = null;
        int []              widths = new int [numColumns];
        
        if (printHeaders) {
            headers = new String [numColumns];
            
            for (int ii = 0; ii < numColumns; ii++) {
                String      label = rsmd.getColumnLabel (ii + 1);
                headers [ii] = label;
                widths [ii] = label.length ();
            }
        }
        
        while (rs.next ()) {
            Object []       row = new Object [numColumns];
            
            for (int ii = 0; ii < numColumns; ii++) {
                String      value = rs.getString (ii + 1);
                row [ii] = value;
                widths [ii] = value.length ();
            }
            
            rows.add (row);
        }
        
        int                 formatOverhead = 
            left.length () + right.length () +
            columnSeparator.length () * (numColumns - 1);
        
        int                 availWidth = maxLineWidth - formatOverhead;        
        int                 avgWidth = availWidth / numColumns;        
        int                 totalWidth = 0;
        int                 totalWidthOfColsLEAvg = 0;
        
        for (int w : widths) {
            totalWidth += w;
            
            if (w <= avgWidth)
                totalWidthOfColsLEAvg += w;
        }
        
        /*
         *  If we exceed the width quota, shrink columns that are 
         *  larger than average
         */
        if (totalWidth > availWidth) {
            double          totalWidthOfColsGrAvg = totalWidth - totalWidthOfColsLEAvg;
            double          widthAvailForLargeCols = availWidth - totalWidthOfColsLEAvg;
            
            double          k = widthAvailForLargeCols / totalWidthOfColsGrAvg;
            
            if (k >= 1)
                throw new RuntimeException ("logic error");
            
            for (int ii = 0; ii < numColumns; ii++) {
                int         w = widths [ii];
                
                if (w > avgWidth)
                    w = (int) (w * k);
                
                /*
                 *  Even maxLineWidth will be overridden to show at least one char per column.
                 */
                if (w == 0)     
                    w = 1;
                
                widths [ii] = w;
            }
        }
        
        int                 lineWidth = formatOverhead;
        
        for (int w : widths)
            lineWidth += w;
        
        if (printHeaders) {
            out.append (left);
            
            for (int ii = 0; ii < numColumns; ii++) {
                if (ii > 0)
                    out.append (columnSeparator);
                
                String      header = headers [ii];
                
                Util.format (out, header, Justification.CENTER, widths [ii],  clip);
            }
            
            out.append (right);
            out.append ("\n");
            
            for (int ii = 0; ii < lineWidth; ii++)                 
                out.append (headerUnderscore);     
            
            out.append ("\n");
        }
        
        for (Object [] row : rows) {
            out.append (left);
            
            for (int ii = 0; ii < numColumns; ii++) {
                if (ii > 0)
                    out.append (columnSeparator);
                
                Object              cell = row [ii];
                Justification       j =
                    columnJustification == null || columnJustification.length <= ii ?
                        Justification.RIGHT :
                        columnJustification [ii];
                
                Util.format (out, cell, j, widths [ii],  clip);
            }
            
            out.append (right);
            out.append ("\n");
        }
    }
    
}
